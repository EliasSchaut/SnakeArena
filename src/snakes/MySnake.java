package snakes;

import board.*;
import java.awt.*;


/**
 * an example snake for testing
 */
public class MySnake extends Snake {

    public MySnake() {
        this.NAME = "YOUR_KUHL_SNAKE_NAME";         // set your favorite name
        this.COLOR = new Color(80, 0, 80); // set your favorite color

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


        return RIGHT; // or LEFT, or DOWN, or UP
    }
}
