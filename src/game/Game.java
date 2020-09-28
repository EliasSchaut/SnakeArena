package game;

import io.Window;

/**
 * The Main-Class
 */
public class Game {

    // This game
    private Game game;

    // game will loop until this is false
    private boolean isRunning = true;

    // game will stop until this is false
    private boolean isPaused = true;

    private Window window;

    // no one else can create a new game (╬▔皿▔)╯
    private Game() {}



    // --- Main Method --------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {

        // Create Game
        Game game = new Game();
        game.game = game;


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

        try {
            WAIT_TIME = Integer.parseInt(cfg.get("WAIT_TIME"));
            DEBUG = Boolean.parseBoolean(cfg.get("debug"));
            WINDOW_TITLE = cfg.get("WINDOW_TITLE");
            game.isPaused = Boolean.parseBoolean(cfg.get("start_paused"));
            SCALE = Integer.parseInt(cfg.get("SCALE"));
            MAX_X = Integer.parseInt(cfg.get("MAX_X"));
            MAX_Y = Integer.parseInt(cfg.get("MAX_Y"));
            MAX_APPLES_ON_BOARD = Integer.parseInt(cfg.get("MAX_APPLES_ON_BOARD"));

        } catch (Exception e) {
            System.out.println("Error in config file!\n\n");
            e.printStackTrace();
        }
        // ------------------------------


        // Set Up Graphics & Layout
        game.window = new Window(game, WINDOW_TITLE, DEBUG, SCALE, MAX_X, MAX_Y, MAX_APPLES_ON_BOARD);


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
    public void end() {
        isRunning = false;
    }
}
