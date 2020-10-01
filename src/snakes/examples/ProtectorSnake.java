package snakes.examples;

import board.BoardInfo;
import board.Field;
import snakes.Snake;

import java.awt.*;

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
