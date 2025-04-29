package world.chiyogami.log;


import net.minecraft.server.MinecraftServer;

public class ChiyogamiLogger {
    
    public static boolean isShowLogs = true;
    
    public static void info(String string){
        if(!isShowLogs) return;
        string = "Chiyogami : INFO > " + string;
        MinecraftServer.LOGGER.info(string);
    }
    
}

