package snakes.examples;

import board.BoardInfo;
import snakes.Snake;

import java.awt.*;

public class BarrierSnake extends Snake {

    public BarrierSnake() {
        this.NAME = "BarrierSnake";         // set your favorite name
        this.COLOR = new Color(0, 0, 0, 255); // set your favorite color

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


        return LEFT; // or LEFT, or DOWN, or UP
    }

}
