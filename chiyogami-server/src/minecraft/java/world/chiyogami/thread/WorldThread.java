package world.chiyogami.thread;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import world.chiyogami.log.ChiyogamiLogger;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class WorldThread implements Runnable {
    
    public static final ReentrantLock TASK_SCHEDULING_LOCK = new ReentrantLock(true);
    
    
    private final Level level;
    
    private Runnable worldTickRunnable;
    
    private final List<WorldTask<?>> worldTasks = new ArrayList<>();
    
    private long tick = 0;
    
    private WorldThread waitFor = null;
    
    private boolean isProcessingWorldTick = false;
    
    public final ReentrantLock LOCK = new ReentrantLock(true);
    
    private Thread currentThread = Thread.currentThread();
    
    private final List<WorldThreadRunnable> scheduledRunnableList = new ArrayList<>();
    
    private final Map<WorldThreadRunnable, Long> scheduledDelayRunnableListMap = new HashMap<>();
    
    private final Map<WorldThreadRunnable, Long> scheduledTimerRunnableListMap = new HashMap<>();
    
    private final ReentrantLock SCHEDULER_LOCK = new ReentrantLock(true);
    
    public AtomicInteger safeLockCount = new AtomicInteger();
    
    public WorldThread(Level level) {
        this.level = level;
    }
    
    public Level getLevel() {return level;}
    
    public Runnable getWorldTickRunnable() {return worldTickRunnable;}
    
    public void setWorldTickRunnable(Runnable worldTickRunnable) {this.worldTickRunnable = worldTickRunnable;}
    
    public boolean isProcessingWorldTick() {return isProcessingWorldTick;}
    
    public Thread getCurrentThread() {return currentThread;}
    
    public void setCurrentThread(Thread currentThread) {this.currentThread = currentThread;}
    
    public void addWorldTask(WorldTask<?> worldTask) {worldTasks.add(worldTask);}
    
    public void resetAllPluginWorldTasks() {
        scheduledRunnableList.clear();
        scheduledDelayRunnableListMap.clear();
        scheduledTimerRunnableListMap.clear();
    }
    
    public void scheduleWorldThreadRunnable(WorldThreadRunnable runnable) {
        try {
            SCHEDULER_LOCK.lock();
            if (runnable.getDelay() == 0) {
                if (runnable.getPeriod() == 0) {
                    scheduledRunnableList.add(runnable);
                } else {
                    scheduledTimerRunnableListMap.put(runnable, tick);
                }
            } else {
                scheduledDelayRunnableListMap.put(runnable, runnable.getDelay() + tick);
            }
        } finally {
            SCHEDULER_LOCK.unlock();
        }
    }
    
    
    @Override
    public void run() {
        tick++;
        
        currentThread = Thread.currentThread();
        WorldThreadPool.addCurrentThreadToWorldThreadHashMap(this);
        
        try {
            LOCK.lock();
            isProcessingWorldTick = true;
        } finally {
            LOCK.unlock();
        }
        
        //Scheduler
        try {
            SCHEDULER_LOCK.lock();
            for (Map.Entry<WorldThreadRunnable, Long> entry : scheduledDelayRunnableListMap.entrySet()) {
                WorldThreadRunnable runnable = entry.getKey();
                long tick = entry.getValue();
                
                if (this.tick == tick && !runnable.isCanceled()) {
                    if (runnable.getPeriod() == 0) {
                        scheduledRunnableList.add(runnable);
                    } else {
                        scheduledTimerRunnableListMap.put(runnable, tick);
                    }
                }
            }
            
            for (WorldThreadRunnable runnable : scheduledRunnableList) {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            for (Map.Entry<WorldThreadRunnable, Long> entry : scheduledTimerRunnableListMap.entrySet()) {
                WorldThreadRunnable runnable = entry.getKey();
                long tick = entry.getValue();
                
                if ((this.tick - tick) % runnable.getPeriod() == 0) {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            
            scheduledDelayRunnableListMap.keySet().removeIf(WorldThreadRunnable::isCanceled);
            scheduledTimerRunnableListMap.keySet().removeIf(WorldThreadRunnable::isCanceled);
            scheduledRunnableList.clear();
        } finally {
            SCHEDULER_LOCK.unlock();
        }
        
        worldTickRunnable.run();
        
        try {
            LOCK.lock();
            isProcessingWorldTick = false;
            
            if (worldTasks.size() == 0) return;
            
            for (WorldTask<?> worldTask : worldTasks) {
                worldTask.complete();
            }
            worldTasks.clear();
        } finally {
            LOCK.unlock();
        }
    }
    
    public WorldThread getWaitFor() {return waitFor;}
    
    public void catchIllegalThread(String reason){
        Thread currentThread = Thread.currentThread();
        
        if(currentThread == MinecraftServer.getServer().serverThread) return;
        if(currentThread == this.currentThread) return;
        
        throw new IllegalStateException("Chiyogami > Unauthorized concurrent execution to worlds."
            + System.lineSeparator() + "             World : " + level.getWorld().getName() + "  Reason : " + reason
            + System.lineSeparator() + "This operation is unacceptable because it can cause serious damage to this server."
            + System.lineSeparator() + "It has been rejected by Chiyogami, it is not a bug.");
    }
    
    public <T> T runWorldTaskThreadSafely(WorldThread targetWorldThread, WorldTask<T> worldTask, String reason) {
        this.catchIllegalThread(reason);
        if (targetWorldThread == null) {
            throw new IllegalStateException("Failed to wait for world task.");
        }
        
        ChiyogamiLogger.info("Try wait " + level.getWorld().getName() + " -> " + targetWorldThread.level.getWorld().getName());

        if (level != targetWorldThread.level) {
            try {
                TASK_SCHEDULING_LOCK.lock();
                targetWorldThread.LOCK.lock();

                //Checks to see if threads waiting to exit are not recursively waiting for themselves.
                WorldThread waitForThread = targetWorldThread;
                do {
                    waitForThread = waitForThread.waitFor;
                } while (waitForThread != null && waitForThread != this);

                if (waitForThread == null) {
                    if (targetWorldThread.isProcessingWorldTick) {
                        if (this.safeLockCount.get() != 0) {
                            throw new IllegalStateException("""
                                The current thread has already acquired a lock with WorldThreadSafeLock.
                                In this state, it cannot safely wait for another world's task completion.
                                This operation cannot be performed because it may cause a deadlock.""");
                        }

                        ChiyogamiLogger.info("Wait for " + level.getWorld().getName() + " -> " + targetWorldThread.level.getWorld().getName());
                        targetWorldThread.worldTasks.add(worldTask);
                        this.waitFor = targetWorldThread;
                    } else {
                        worldTask.complete();
                    }
                } else {
                    worldTask.complete();
                }
            } finally {
                targetWorldThread.LOCK.unlock();
                TASK_SCHEDULING_LOCK.unlock();
            }
        } else {
            worldTask.complete();
        }
        
        try {
            T result = worldTask.get();
            this.waitFor = null;
            return result;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to wait for world task.");
        }
    }
    
}
