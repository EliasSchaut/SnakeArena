package game;

import java.util.*;

/**
 * Read the properties file. Other classes can read the values with the get-method.
 */
public class Config {
    private final Properties configFile;

    /**
     * The Constructor!
     * Reads the config file and safe it in var configFile
     */
    public Config() {
        configFile = new Properties();
        try {
            String cfg_location = "snake_arena.properties";
            configFile.load(this.getClass().getClassLoader().getResourceAsStream(cfg_location));

        } catch (Exception e) {
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
            e.printStackTrace();
        }

        return null;
    }
}
