package world.chiyogami.thread;

import net.minecraft.server.MinecraftServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import world.chiyogami.chiyogamilib.ChiyogamiLib;
import world.chiyogami.chiyogamilib.ServerThreadProvider;

public class ChiyogamiServerThreadProvider {
    public static void init() {
        try {
            var providerField = ChiyogamiLib.class.getDeclaredField("serverThreadProvider");
            providerField.setAccessible(true);
            providerField.set(null, new ServerThreadProvider() {
                @Override
                public Thread getMainThread() {
                    return MinecraftServer.getServer().serverThread;
                }

                @Override
                public Thread getWorldThread(World world) {
                    return ((CraftWorld) world).getHandle().thread;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
