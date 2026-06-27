package world.chiyogami.config;


import io.papermc.paper.plugin.manager.PaperEventManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.SimplePluginManager;
import world.chiyogami.log.ChiyogamiLogger;
import world.chiyogami.thread.WorldThreadPool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ChiyogamiConfig {
    
    private static final int VERSION = 2;
    
    private static final List<ConfigComponent<?>> configComponentList = new ArrayList<>();
    
    private static final ConfigComponent<Integer> CONFIG_VERSION = new ConfigComponent<>("config-version", VERSION, VERSION);
    private static final ConfigComponent<Integer> MAX_WORLD_THREADS = new ConfigComponent<>("max-world-threads", 0, 0);
    private static final ConfigComponent<Boolean> SHOW_CHIYOGAMI_LOG = new ConfigComponent<>("show-detail-log", true, 0);
    private static final ConfigComponent<Boolean> SYNCHRONIZED_EVENT = new ConfigComponent<>("synchronized-event", true, 1);
    
    
    private static void setDefaultValues(YamlConfiguration yml){
        int configVer = 0;
        if(yml.contains(CONFIG_VERSION.path)) configVer = yml.getInt(CONFIG_VERSION.path);
        
        for(ConfigComponent<?> configComponent : configComponentList){
            String path = configComponent.path;
            Object defaultValue = configComponent.defaultValue;
            int componentVer = configComponent.version;
            
            if(configVer < componentVer || !yml.contains(path)){
                yml.set(path, defaultValue);
            }
        }
    }
    
    
    private static void loadValues(YamlConfiguration yml){
        for(ConfigComponent<?> configComponent : configComponentList){
            String path = configComponent.path;
            if(yml.contains(path)){
                try {
                    configComponent.setValue(yml.get(path));
                }catch (Exception e){e.printStackTrace();}
            }
        }
    }
    
    
    private static class ConfigComponent<T>{
        
        private final String path;
        
        private final T defaultValue;
        
        private final int version;
        
        private T value = null;
        
        private ConfigComponent(String path, T defaultValue, int version){
            this.path = path;
            this.defaultValue = defaultValue;
            this.version = version;
            configComponentList.add(this);
        }
        
        private void setValue(Object value) {this.value = (T)value;}
        
        private T getValue() {
            if(value != null) return value;
            return defaultValue;
        }
    }
    
    
    
    
    public static void load(){
        try {
            
            File file = new File("chiyogami.yml");
            
            YamlConfiguration yml;
            if (file.exists()) {
                yml = YamlConfiguration.loadConfiguration(file);
            } else {
                yml = new YamlConfiguration();
            }
            setDefaultValues(yml);
            yml.save(file);
            
            loadValues(yml);
            
            //WorkMode.MULTI_THREAD_TICK = MAX_WORLD_THREADS.getValue() != 1;
            WorldThreadPool.setMaxPoolSize(MAX_WORLD_THREADS.getValue());
            
            ChiyogamiLogger.isShowLogs = SHOW_CHIYOGAMI_LOG.getValue();
            
            PaperEventManager.synchronizedEvent = SYNCHRONIZED_EVENT.getValue();
            
            //ParallelWorldBridge.parallelWorldBridge = PARALLEL_WORLD_BRIDGE.getValue();
            
        }catch (Exception e){e.printStackTrace();}
    }
    
    public static int getMaxWorldThreads() {return MAX_WORLD_THREADS.getValue();}
    
    public static boolean isShowLogs() {return SHOW_CHIYOGAMI_LOG.getValue();}
    
    public static boolean isSynchronizedEvent(){return SYNCHRONIZED_EVENT.getValue();}
}

