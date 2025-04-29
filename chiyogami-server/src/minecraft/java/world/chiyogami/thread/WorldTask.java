package world.chiyogami.thread;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class WorldTask<T> extends CompletableFuture<T> {
    
    private final Supplier<T> task;
    
    public WorldTask(Supplier<T> task) {
        this.task = task;
    }
    
    public void complete(){super.complete(task.get());}
    
}
