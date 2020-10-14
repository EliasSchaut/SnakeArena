package snakes.examples;

import board.BoardInfo;
import board.Field;
import snakes.Snake;

import java.awt.*;

/**
 * An example for a snake with less intelligence.
 * This snake will always go to the nearest apple. If something blocks the way,
 * it will turn around until its path is free or its dead.
 *
 * This snake appears 'EasySnake' times an board.
 * The value behind 'EasySnake' can set in the property file (snake_arena.properties).
 */
public class EasySnake extends Snake {

    public EasySnake() {
        this.NAME = "EasySnake";
        this.COLOR = new Color(0, 0, 80);

    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {
        Field minApple = board.getNearestApple();
        int posX = board.getOwnHead().getPosX();
        int posY = board.getOwnHead().getPosY();

        int numXFields = posX - minApple.getPosX();
        int numYFields = posY - minApple.getPosY();

        int direction;

        if (numXFields != 0) {
            direction = (numXFields > 0) ? LEFT : RIGHT;

        } else {
            direction = (numYFields > 0) ? UP : DOWN;
        }

        int counter = 0;
        while ((counter < 3) && !board.isNextStepFree(direction)) {
            direction = (direction + 1) % 4;
            counter++;
        }

        return direction;
    }
}
