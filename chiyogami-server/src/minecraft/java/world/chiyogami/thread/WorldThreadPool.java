package world.chiyogami.thread;

import ca.spottedleaf.moonrise.common.util.TickThread;
import net.minecraft.server.level.ServerLevel;
import world.chiyogami.config.ChiyogamiConfig;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class WorldThreadPool {
    
    private static ExecutorService executorService = null;
    public static Set<Thread> allThreads = ConcurrentHashMap.newKeySet();
    
    private static int maxPoolSize = 0;
    
    private static int previousPoolSize = 0;
    
    public static void setMaxPoolSize(int poolSize) {WorldThreadPool.maxPoolSize = poolSize;}
    
    public static Map<Thread, WorldThread> worldThreadHashMap = new HashMap<>();
    
    private static final ReentrantLock WORLD_THREAD_HASH_MAP_LOCK = new ReentrantLock(true);
    
    public static void addCurrentThreadToWorldThreadHashMap(WorldThread worldThread) {
        //Copy on write
        try {
            WORLD_THREAD_HASH_MAP_LOCK.lock();
            Thread currentThread = Thread.currentThread();
            Map<Thread, WorldThread> copy = new HashMap<>(worldThreadHashMap);
            copy.put(currentThread, worldThread);
            worldThreadHashMap = copy;
        } finally {
            WORLD_THREAD_HASH_MAP_LOCK.unlock();
        }
    }

    public static void init() {
        int poolSize = ChiyogamiConfig.getMaxWorldThreads();
        if (poolSize == 0) {
            poolSize = 4;
        }

        AtomicInteger threadCount = new AtomicInteger(0);

        executorService = Executors.newFixedThreadPool(poolSize, runnable -> {
            Thread thread = new TickThread(runnable, "WorldTickThread[" + threadCount.getAndIncrement() + "]");
            allThreads.add(thread);
            return thread;
        });
        // create all thread
        Callable<Void> callable = () -> null;
        try {
            executorService.invokeAll(IntStream.range(0, poolSize).mapToObj(i -> callable).toList());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static boolean isWorldThread() {
        return worldThreadHashMap.containsKey(Thread.currentThread());
    }
    
    public static WorldThread getWorldThreadFromCurrentThread() {return worldThreadHashMap.get(Thread.currentThread());}
    
    
    public static void shutdown(){if(executorService != null) executorService.shutdown();}
    
    public static void doTick(Collection<ServerLevel> serverLevels){
        Set<Future<?>> futures = new HashSet<>();
        for (ServerLevel serverLevel : serverLevels) {
            Future<?> future = executorService.submit(serverLevel.worldThread);
            futures.add(future);
        }
        
        for(Future<?> future : futures){
            try {
                future.get();
            } catch (Exception e){e.printStackTrace();}
        }
    }
    
}

