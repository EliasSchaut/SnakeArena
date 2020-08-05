/**
 * The Main-Class
 */
public class Game {

    private final int WAIT_TIME = 200;

    // the window representing the game
    private Window window;

    // game will loop until this is false
    public boolean isRunning = true;

    private boolean isPaused = true;

    // This game
    private Game game;

    // --- Main Method --------------------------------------------------------------
    public static void main(String[] args) throws InterruptedException {

        // Create Game Object
        Game game = new Game();
        game.game = game;

        // Set Up Graphics & Layout
        game.window = new Window(game, "SnakeArena");

        // --- Game Loop ---
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
