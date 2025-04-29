package world.chiyogami.thread;

import org.bukkit.Bukkit;

public class ImplWorldThreadLockHandler extends WorldThreadLockHandler {
    
    public ImplWorldThreadLockHandler() {super();}
    
    @Override
    public void lock(WorldThreadSafeLock worldThreadSafeLock) {
        //Main thread
        if (Bukkit.isMainThread()) {
            return;
        }
        
        WorldThread worldThread = WorldThreadPool.getWorldThreadFromCurrentThread();
        
        //Async thread
        if (worldThread == null) {
            return;
        }
        
        //World thread
        worldThread.safeLockCount.addAndGet(1);
    }
    
    @Override
    public void unlock(WorldThreadSafeLock worldThreadSafeLock) {
        //Main thread
        if (Bukkit.isMainThread()) {
            return;
        }
        
        WorldThread worldThread = WorldThreadPool.getWorldThreadFromCurrentThread();
        
        //Async thread
        if (worldThread == null) {
            return;
        }
        
        //World thread
        int lockNest = worldThread.safeLockCount.addAndGet(-1);
        
        if (lockNest < 0) {
            int old = worldThread.safeLockCount.getAndSet(0);
            throw new IllegalStateException("Lock and unlock counts do not match. \n" +
                "Excessive unlocking or an internal error may have occurred.\n" +
                "LockCount : " + old);
        }
    }
    
}
