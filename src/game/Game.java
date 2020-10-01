package game;

import io.Window;

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

        int WAIT_TIME = 0;
        boolean DEBUG = false;
        String WINDOW_TITLE = "";
        int SCALE = 0;
        int MAX_X = 0;
        int MAX_Y = 0;
        int MAX_APPLES_ON_BOARD = 0;
        boolean RESIZEABLE = false;
        int OFFSET = 0;

        try {
            WAIT_TIME = Integer.parseInt(cfg.get("WAIT_TIME"));
            DEBUG = Boolean.parseBoolean(cfg.get("debug"));
            WINDOW_TITLE = cfg.get("WINDOW_TITLE");
            game.isPaused = Boolean.parseBoolean(cfg.get("start_paused"));
            SCALE = Integer.parseInt(cfg.get("SCALE"));
            MAX_X = Integer.parseInt(cfg.get("MAX_X"));
            MAX_Y = Integer.parseInt(cfg.get("MAX_Y"));
            MAX_APPLES_ON_BOARD = Integer.parseInt(cfg.get("MAX_APPLES_ON_BOARD"));
            RESIZEABLE = Boolean.parseBoolean(cfg.get("RESIZEABLE"));
            OFFSET = Integer.parseInt(cfg.get("OFFSET"));
            game.stop_game = Boolean.parseBoolean(cfg.get("stop_game"));


        } catch (Exception e) {
            System.out.println("Wrong type format in config file!\n\n");
            e.printStackTrace();
        }
        // ------------------------------


        // Set Up Graphics & Layout
        game.window = new Window(game, WINDOW_TITLE, RESIZEABLE, DEBUG, SCALE, MAX_X, MAX_Y, OFFSET, MAX_APPLES_ON_BOARD);


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
