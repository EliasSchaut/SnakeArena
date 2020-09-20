package snakes;

import board.*;

import java.awt.*;


/**
 * an example snakes.Snake for testing
 */
public class MySnake extends Snake {

    private int direction;
    private int counter;

    public MySnake() {
        this.NAME = "Lila Schlange"; // set your favorite name
        this.COLOR = new Color(80, 0, 80); // set your favorite color

        counter = 0;
        direction = UP;
    }

    public MySnake(String path) {
        this();
        this.PATH = path;
    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {
        // lovely intelligence code here
        if (counter == 2) {
            direction = (direction + 1) % 4;
            counter = 0;
        }
        ++counter;


        return direction; // or LEFT, or DOWN, or UP
    }
}
