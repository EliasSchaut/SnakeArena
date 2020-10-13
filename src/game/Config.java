package game;

import java.util.*;

/**
 * Read the snake_arena.properties file, get all config values and store it.
 * Other classes can get a single value with a given key with the get() method.
 * Other classes can get all configs as HashMap with the getAll() method
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
            System.err.println("Config file not found or cannot read!\n\n");
            e.printStackTrace();
        }
    }

    /**
     * Get a single value with a given key with this method.
     * If key was not found in the config file, an error occurs
     *
     * @param key the key of value which is needed
     * @return the value of the given key
     */
    public String get(String key) {

        try {
            return this.configFile.getProperty(key);

        } catch (Exception e) {
            System.err.println("Error in config file!\nKey not found or other Error\n\n");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get all configs as HashMap as (string, string).
     * The first string is the name of the config and the second string is the value
     *
     * @return all configs as HashMap.
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
