package snakes.examples;

import board.BoardInfo;
import snakes.Snake;

import java.awt.*;

/**
 * This snake goes to the nearest apple and circle around of it
 */
public class ProtectorSnake extends Snake {

    public ProtectorSnake() {
        this.NAME = "ProtectorSnake";
        this.COLOR = new Color(80, 0, 0);

    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {
        // TODO

        return UP;
    }
}
