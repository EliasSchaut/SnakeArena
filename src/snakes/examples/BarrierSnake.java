package snakes.examples;

import board.BoardInfo;
import snakes.Snake;

import java.awt.*;

/**
 * This Snake turns immediately into barrier
 * This snake appears 'BarrierSnake' times an board.
 * The value behind 'BarrierSnake' can set in the property file (snake_arena.properties).
 */
public class BarrierSnake extends Snake {

    public BarrierSnake() {
        this.NAME = "BarrierSnake";
        this.COLOR = new Color(0, 0, 0, 255);

    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {

        return LEFT; // or LEFT, or DOWN, or UP
    }

}
