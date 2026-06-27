package world.chiyogami.thread;

abstract class WorldThreadLockHandler {
    
    public static WorldThreadLockHandler INSTANCE;
    
    
    protected WorldThreadLockHandler() {INSTANCE = this;}
    
    public abstract void lock(WorldThreadSafeLock worldThreadSafeLock);
    
    public abstract void unlock(WorldThreadSafeLock worldThreadSafeLock);
    
}
