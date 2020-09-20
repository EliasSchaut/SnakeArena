package io;

import board.*;
import java.util.*;

/**
 * The Main-Class
 */
public class Game {

    // step-interval time in milliseconds
    private final int WAIT_TIME = 200;

    // if true, debugSnake will sporn on board
    protected final boolean DEBUG = false;

    // if true, program is compiled as cli server
    public boolean CLI_SERVER = true;

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
        if (args.length > 0 && args[0].trim().equals("client")) {
            Scanner scanner = new Scanner(System.in);
            var lines = new ArrayList<String>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
            var boardinfo = new BoardInfo(lines);
            System.out.println( new snakes.MySnake().think(boardinfo));
            return;
        }

        // Create game.Game Object
        Game game = new Game();
        if (args.length > 0 && args[0].trim().equals("cli-server")) {
            game.CLI_SERVER = true;
        }
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
