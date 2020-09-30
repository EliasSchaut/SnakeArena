package snakes.examples;

import board.BoardInfo;
import snakes.Snake;

import java.awt.*;

public class CircleSnake extends Snake {

    private int direction;
    private int counter;

    public CircleSnake() {
        this.NAME = "CircleSnake";               // set your favorite name
        this.COLOR = new Color(80, 0, 80); // set your favorite color

        counter = 0;
        direction = UP;
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
