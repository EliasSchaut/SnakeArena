package io;

/**
 * The Main-Class
 */
public class Game {

    // step-interval time in milliseconds
    private final int WAIT_TIME = 200;

    // the window representing the game
    private Window window;

    // game will loop until this is false
    public boolean isRunning = true;

    // game will stop until this is false
    private boolean isPaused = true;

    // This game
    private Game game;

    // no one can create a new game (╬▔皿▔)╯
    private Game() {}

    // --- Main Method --------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {

        // Create game.Game Object
        Game game = new Game();
        game.game = game;

        // Set Up Graphics & Layout
        game.window = new Window(game, "SnakeArena");

        // --- game.Game Loop ---
        while (game.isRunning) {

            if (!game.isPaused) {
                // render Graphics
                game.window.update();
            }

            // wait for 200 ms
            // set this to whatever speed you like
            // a higher number means a slower game
            Thread.sleep(game.WAIT_TIME);
        }
    }
    // ------------------------------------------------------------------------------

    public void pausePlay() {
        game.isPaused = !isPaused;
    }
}
