package snakes.examples;

import board.BoardInfo;
import snakes.Snake;

import java.awt.*;

/**
 * This Snake rotates in a circle the whole time.
 * This snake appears 'CircleSnake' times an board.
 * The value behind 'CircleSnake' can set in the property file (snake_arena.properties).
 */
public class CircleSnake extends Snake {

    private int direction;
    private int counter;

    public CircleSnake() {
        this.NAME = "CircleSnake";
        this.COLOR = new Color(80, 0, 80);

        counter = 0;
        direction = DOWN;
    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {
        if (counter == 2) {
            direction = (direction + 1) % 4;
            counter = 0;
        }
        ++counter;

        return direction;
    }

}
