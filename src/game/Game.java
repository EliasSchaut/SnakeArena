package game;

import io.Window;
import java.util.Map;

/**
 * The Game-Class
 * Here is the big game loop. Every loop every snake move in a direction and the board repaints.
 * The game loop pause or replays if something called the pausePlay() method
 * The game will loop until something called the end() method
 */
public class Game {

    // game will loop until this is false
    private boolean isRunning = true;

    // game will pause until this is false
    private boolean isPaused = true;

    // if true, the game stops, if one snake remains, else it will never stop
    private boolean stop_game = true;

    // the window of the game
    private Window window;

    // singleton so no one else can create a new game (╬▔皿▔)╯
    private Game() {}


    // --- Main Method --------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {

        // Create Game
        Game game = new Game();

        // ------------------------------
        // import config and set variables
        // ------------------------------
        final Config cfg = new Config();
        final Map<String, String> cfgMap = cfg.getAll();
        game.isPaused = Boolean.parseBoolean(cfgMap.get("START_PAUSED"));
        game.stop_game = Boolean.parseBoolean(cfgMap.get("STOP_TIME"));
        // ------------------------------

        // Set Up Graphics & Layout
        game.window = new Window(game, cfgMap);


        // ------------------------------
        // Game loop
        // ------------------------------
        while (game.isRunning) {

            // skip, if game is paused
            if (!game.isPaused) {

                // render Graphics and make snake movements
                game.window.update();
            }

            // wait for WAIT_TIME in ms
            Thread.sleep(game.window.getWaitTime());
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
     * show or hide the helper window
     */
    public void showHelperWindow() {
        window.displayHelper();
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
