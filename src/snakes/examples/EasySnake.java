package snakes.examples;

import board.BoardInfo;
import board.BoardLogic;
import board.Field;
import snakes.Snake;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EasySnake extends Snake {

    public EasySnake() {
        this.NAME = "EasySnake";                   // everybody can set his favorite name
        this.COLOR = new Color(0, 0, 80); // everybody can set his favorite color

    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {
        Field minApple = getNearestApple(board);
        int posX = board.getOwnHead().getPosX();
        int posY = board.getOwnHead().getPosY();

        int numXFields = posX - minApple.getPosX();
        int numYFields = posY - minApple.getPosY();

        if (numXFields != 0) {
            if (numXFields > 0) {
                return LEFT;

            } else {
                return RIGHT;
            }

        } else {
            if (numYFields > 0) {
                return LEFT;

            } else {
                return RIGHT;
            }
        }
    }


    /**
     * Find nearest Apple from SnakeHead
     *
     * @param board the whole board with every information necessary
     * @return nearest apple field
     */
    private Field getNearestApple(BoardInfo board) {
        List<Field> apples = new ArrayList<>();
        apples = board.getApples();

        int minReach = BoardLogic.MAX_X * BoardLogic.MAX_Y;
        Field minApple = null;

        for (Field apple: apples) {
            int curReach = Math.abs(board.getOwnHead().getPosX() - apple.getPosX()) +
                    Math.abs(board.getOwnHead().getPosY() - apple.getPosY());

            if (curReach < minReach) {
                minApple = apple;
            }
        }

        return minApple;
    }
}
