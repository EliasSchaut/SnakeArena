package game;

import io.Window;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The Main-Class
 */
public class Game {

    // game will loop until this is false
    private boolean isRunning = true;

    // game will pause until this is false
    private boolean isPaused = true;

    // if true, the game stops, if one snake remains
    private boolean stop_game = true;

    // the window of the game
    private Window window;

    // no one else can create a new game (╬▔皿▔)╯
    private Game() {}


    // --- Main Method --------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {

        // Create Game
        Game game = new Game();

        // ------------------------------
        // import config
        // ------------------------------
        final Config cfg = new Config();
        final Map<String, String> cfgMap = cfg.getAll();
        final int WAIT_TIME = Integer.parseInt(cfgMap.get("WAIT_TIME"));
        game.isPaused = Boolean.parseBoolean(cfgMap.get("start_paused"));
        game.stop_game = Boolean.parseBoolean(cfgMap.get("stop_game"));
        // ------------------------------


        // Set Up Graphics & Layout
        game.window = new Window(game, cfgMap);


        // ------------------------------
        // Game loop
        // ------------------------------
        while (game.isRunning) {

            // skip, if game is paused
            if (!game.isPaused) {

                // render Graphics
                game.window.update();
            }

            // wait for WAIT_TIME in ms
            Thread.sleep(WAIT_TIME);
        }
        // ------------------------------

    }
    // ------------------------------------------------------------------------------

    /**
     * pause or resume the game
     */
    public void pausePlay() {
        isPaused = !isPaused;
    }

    /**
     * end game
     */
    public boolean end(Game game) {
        if (game.stop_game) {
            isRunning = false;
        }

        return game.stop_game;
    }
}
