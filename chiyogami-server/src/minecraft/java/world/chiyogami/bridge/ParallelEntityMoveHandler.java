package world.chiyogami.bridge;

import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Function;

public class ParallelEntityMoveHandler {
    
    private final Player player;
    
    public Function<Map.Entry<BoundingBox, Vector>, Boolean> tryToMoveFunction = null;
    
    public ParallelEntityMoveHandler(Player player){this.player = player;}
    
    public boolean tryToMoveBoundingBox(BoundingBox boundingBox, Vector movement) {
        return tryToMoveFunction.apply(new AbstractMap.SimpleEntry<>(boundingBox, movement));
    }
    
}

