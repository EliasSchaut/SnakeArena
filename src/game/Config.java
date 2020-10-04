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

        cfgMap.put("debug", this.get("debug"));
        cfgMap.put("start_paused", this.get("start_paused"));
        cfgMap.put("stop_game", this.get("stop_game"));
        cfgMap.put("WINDOW_TITLE", this.get("WINDOW_TITLE"));
        cfgMap.put("WAIT_TIME", this.get("WAIT_TIME"));
        cfgMap.put("SCALE", this.get("SCALE"));
        cfgMap.put("MAX_X", this.get("MAX_X"));
        cfgMap.put("MAX_Y", this.get("MAX_Y"));
        cfgMap.put("OFFSET", this.get("OFFSET"));
        cfgMap.put("MAX_APPLES_ON_BOARD", this.get("MAX_APPLES_ON_BOARD"));
        cfgMap.put("RESIZEABLE", this.get("RESIZEABLE"));
        cfgMap.put("MySnake", this.get("MySnake"));
        cfgMap.put("BarrierSnake", this.get("BarrierSnake"));
        cfgMap.put("CircleSnake", this.get("CircleSnake"));
        cfgMap.put("EasySnake", this.get("EasySnake"));
        cfgMap.put("ProtectorSnake", this.get("ProtectorSnake"));

        return cfgMap;
    }
}
