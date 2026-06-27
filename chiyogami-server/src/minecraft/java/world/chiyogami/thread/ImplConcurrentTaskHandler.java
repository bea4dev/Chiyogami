package world.chiyogami.thread;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;

import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class ImplConcurrentTaskHandler extends ConcurrentTaskHandler {
    
    public ImplConcurrentTaskHandler() {super();}
    
    @Override
    protected <T> T runConcurrentTaskForWorldImpl(World targetWorld, Supplier<T> supplier) {
        // Main thread
        if (Bukkit.isMainThread()) {
            return supplier.get();
        }
        
        WorldTask<T> worldTask = new WorldTask<>(supplier);
        
        WorldThread currentWorldThread = WorldThreadPool.getWorldThreadFromCurrentThread();
        WorldThread targetWorldThread = ((CraftWorld) targetWorld).getHandle().worldThread;
        
        // Async thread
        if (currentWorldThread == null) {
            try {
                targetWorldThread.LOCK.lock();
                if (targetWorldThread.isProcessingWorldTick()) {
                    targetWorldThread.addWorldTask(worldTask);
                } else {
                    worldTask.complete();
                }
            } finally {
                targetWorldThread.LOCK.unlock();
            }
            
            try {
                return worldTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                throw new IllegalStateException("Failed to wait for world task.");
            }
        }
        
        // World thread
        if (currentWorldThread == targetWorldThread) {
            worldTask.complete();
            try {
                return worldTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                throw new IllegalStateException("Failed to wait for world task.");
            }
        } else {
            return currentWorldThread.runWorldTaskThreadSafely(targetWorldThread, worldTask, "Internal error.");
        }
    }
    
}
