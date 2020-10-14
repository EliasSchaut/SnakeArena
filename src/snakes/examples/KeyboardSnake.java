package snakes.examples;

import snakes.Snake;

import board.*;
import java.awt.*;

/**
 * Only for Debugging or Testing. This snake is controllable with the arrows on the keyboard.
 * This snake will be in game, if 'DEBUG' is true in property file.
 *
 * It can be only one KeyboardSnake at the same time on board.
 */
public class KeyboardSnake extends Snake {

    public int direction = RIGHT;

    public KeyboardSnake() {
        this.NAME = "KeyboardSnake";                  // everybody can set his favorite name
        this.COLOR = new Color(0, 80, 0); // everybody can set his favorite color

    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {

        return direction;
    }
}
