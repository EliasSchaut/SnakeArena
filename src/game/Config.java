package game;

import java.util.*;

/**
 * Read the properties file. Other classes can read the values with the get-method.
 */
public class Config {
    private final Properties configFile;

    /**
     * The Constructor!
     * Reads the config file and safe it
     */
    public Config() {
        configFile = new Properties();
        try {
            String cfg_location = "snake_arena.properties";
            configFile.load(this.getClass().getClassLoader().getResourceAsStream(cfg_location));

        } catch (Exception e) {
            System.out.println("Config file not found or cannot read!\n\n");
            e.printStackTrace();
        }
    }

    /**
     * Other classes can get the config values with this method
     *
     * @param key the key of value which is needed
     * @return the value of the given key
     */
    public String get(String key) {

        try {
            return this.configFile.getProperty(key);

        } catch (Exception e) {
            System.out.println("Error in config file!\nKey not found or other Error\n\n");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get all config values
     *
     * @return all config values as (string,string) map
     */
    public Map<String, String> getAll() {
        Map<String, String> cfgMap = new HashMap<>();

        Set<Object> keys = this.configFile.keySet();
        for (Object key : keys) {
            cfgMap.put(String.valueOf(key), this.get(String.valueOf(key)));
        }

        return cfgMap;
    }
}
