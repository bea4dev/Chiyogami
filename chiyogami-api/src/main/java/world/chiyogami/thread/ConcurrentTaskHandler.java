package world.chiyogami.thread;

import org.bukkit.World;
import java.util.function.Supplier;

/**
 * Task utility class for safe operation on worlds from other threads.
 */
public abstract class ConcurrentTaskHandler {
    
    protected static ConcurrentTaskHandler INSTANCE = null;
    
    /**
     * Performs tasks to the target world in a thread-safe and non-delayed manner.
     * <p>
     * When accessing from a thread other than the main thread. Typically, a latency of 50 ms or less is incurred.
     * <p>
     * <b>This method only guarantees thread-safety for operations on worlds,
     * not for global variables or methods (e.g. Bukkit.createInventory();).</b>
     *
     * @param targetWorld The world in which the task is to be performed.
     * @param task Supplier task.
     * @return Result of supplier.
     */
    public static <T> T runConcurrentTaskForWorld(World targetWorld, Supplier<T> task) {
        return INSTANCE.runConcurrentTaskForWorldImpl(targetWorld, task);
    }
    
    /**
     * Performs tasks to the target world in a thread-safe and non-delayed manner.
     * <p>
     * When accessing from a thread other than the main thread. Typically, a latency of 50 ms or less is incurred.
     * <p>
     * <b>This method only guarantees thread-safety for operations on worlds,
     * not for global variables or methods (e.g. Bukkit.createInventory();).</b>
     *
     * @param targetWorld The world in which the task is to be performed.
     * @param runnable Runnable task.
     */
    public static void runConcurrentTaskForWorld(World targetWorld, Runnable runnable) {
        INSTANCE.runConcurrentTaskForWorldImpl(targetWorld, () -> {
            runnable.run();
            return null;
        });
    }
    
    
    protected ConcurrentTaskHandler(){INSTANCE = this;}
    
    protected abstract <T> T runConcurrentTaskForWorldImpl(World targetWorld, Supplier<T> supplier);
    
}
