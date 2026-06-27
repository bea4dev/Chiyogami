package world.chiyogami.bridge;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelBridge {
    
    public static boolean parallelWorldBridge = false;
    
    private static final Map<Player, ParallelEntityMoveHandler> parallelEntityMoveHandlerMap = new ConcurrentHashMap<>();
    
    public static ParallelEntityMoveHandler getParallelEntityMoveHandler(Player player){
        if(!parallelWorldBridge) return null;
        return parallelEntityMoveHandlerMap.computeIfAbsent(player, ParallelEntityMoveHandler::new);
    }
    
    public static void removeParallelEntityMoveHandler(Player player){parallelEntityMoveHandlerMap.remove(player);}
    
}
