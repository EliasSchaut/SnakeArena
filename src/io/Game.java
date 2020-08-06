package io;

/**
 * The Main-Class
 */
public class Game {

    // step-interval time in milliseconds
    private final int WAIT_TIME = 200;

    // if true, debugSnake will sporn on board
    protected final boolean DEBUG = true;

    // title of window
    private final String WINDOW_TITLE = "SnakeArena";

    // the window representing the game
    private Window window;

    // game will loop until this is false
    public boolean isRunning = true;

    // game will stop until this is false
    private boolean isPaused = true;

    // This game
    private Game game;

    // no one else can create a new game (╬▔皿▔)╯
    private Game() {}

    // --- Main Method --------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {

        // Create game.Game Object
        Game game = new Game();
        game.game = game;

        // Set Up Graphics & Layout
        game.window = new Window(game, game.WINDOW_TITLE);

        // --- game.Game Loop ---
        while (game.isRunning) {

            if (!game.isPaused) {
                // render Graphics
                game.window.update();
            }

            // wait for WAIT_TIME in ms
            Thread.sleep(game.WAIT_TIME);
        }
    }
    // ------------------------------------------------------------------------------

    /**
     * pause or resume the game
     */
    public void pausePlay() {
        game.isPaused = !isPaused;
    }
}
