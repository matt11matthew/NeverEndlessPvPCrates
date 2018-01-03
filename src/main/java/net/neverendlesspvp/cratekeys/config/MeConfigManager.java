package net.neverendlesspvp.cratekeys.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthew E on 12/13/2017.
 */
public class MeConfigManager {
    private Map<String, MeConfig> meConfigMap;
    private Map<Class, String> nameClassMap;
    private JavaPlugin plugin;
    private static MeConfigManager instance;

    public MeConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
        this.meConfigMap = new HashMap<>();
        this.nameClassMap = new HashMap<>();
    }

    /**
     * Getter for property 'instance'.
     *
     * @return Value for property 'instance'.
     */
    public static MeConfigManager getInstance() {
        return instance;
    }

    public void load(MeConfig... meConfigs) {
        Arrays.asList(meConfigs).forEach(this::load);
    }

    public <T extends MeConfig> T getMeConfig(Class<T> meConfigClass) {
        if (nameClassMap.containsKey(meConfigClass)) {
            String name = nameClassMap.get(meConfigClass);
            if (this.meConfigMap.containsKey(name)) {
                return (T) this.meConfigMap.get(name);
            }
        }
        return null;
    }


    private void load(MeConfig meConfig) {
        if (!this.meConfigMap.containsKey(meConfig.getName())) {
            this.meConfigMap.put(meConfig.getName(), meConfig);
        }
        Class<? extends MeConfig> aClass = meConfig.getClass();
        if (!this.nameClassMap.containsKey(aClass)) {
            this.nameClassMap.put(aClass, meConfig.getName());
        }
    }

    /**
     * Getter for property 'plugin'.
     *
     * @return Value for property 'plugin'.
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Getter for property 'meConfigMap'.
     *
     * @return Value for property 'meConfigMap'.
     */
    public Map<String, MeConfig> getMeConfigMap() {
        return meConfigMap;
    }

    public void unload() {
        reload();
    }

    public <T extends MeConfig> Class<T> getMeClass(String name) {
        for (Class aClass : this.nameClassMap.keySet()) {
            if (nameClassMap.get(aClass).equalsIgnoreCase(name)) {
                return aClass;
            }
        }
        return null;
    }

    public void reload() {
        this.meConfigMap.values().forEach(MeConfig::reload);
    }


}
