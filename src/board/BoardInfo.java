package board;

import snakes.Snake;

import java.util.List;
import java.util.LinkedList;

/**
 * The full needed information about the board for snake with index snakeIndex
 */
public class BoardInfo {

    private final int snakeIndex;
    private final Field[][] fields;
    private final List<Field> apples;
    private final List<LinkedList<Field>> snakesLocation;
    private final List<Field> barriers;


    /**
     * the Board-Class create this Board-Inforamtion for a spezific snake with the index snakeIndex
     *
     * @param snakeIndex the index of the snake, which will need this information now
     */
    protected BoardInfo(int snakeIndex, Field[][] fields, List<LinkedList<Field>> snakesLocation,
                        List<Field> apples, List<Field> barriers) {
        this.snakeIndex = snakeIndex;
        this.fields = fields;
        this.snakesLocation = snakesLocation;
        this.apples = apples;
        this.barriers = barriers;

    }


    /**
     * returns own head as field
     *
     * @return own head as field
     */
    public Field getOwnHead() {
        return snakesLocation.get(snakeIndex).getLast();
    }


    /**
     * returns own snake position as list
     *
     * @return own snake position as list
     */
    public List<Field> getOwnSnake() {
        return snakesLocation.get(snakeIndex);
    }


    /**
     * Returns head of other snakes as list
     *
     * @return head of other snakes as list
     */
    public List<Field> getOtherHeads() {
        LinkedList<Field> heads = new LinkedList<>();

        for (int i = 0; i < snakesLocation.size(); i++) {
            if (i != snakeIndex) {
                heads.add(snakesLocation.get(i).getLast());

            }
        }

        return heads;
    }


    /**
     * Returns position of all other snakes
     *
     * @return position of all other snakes
     */
    public List<LinkedList<Field>> getOtherSnakes() {
        LinkedList<LinkedList<Field>> snakes = new LinkedList<>();

        for (int i = 0; i < snakesLocation.size(); i++) {
            if (i != snakeIndex) {
                snakes.add(snakesLocation.get(i));
            }
        }

        return snakes;
    }


    /**
     * Return apple positions as list
     *
     * @return apple positions as list
     */
    public List<Field> getApples() {
        return apples;
    }


    /**
     * Find nearest Apple from SnakeHead
     *
     * @return nearest apple field
     */
    public Field getNearestApple() {
        List<Field> apples = getApples();

        int minReach = BoardLogic.MAX_X + BoardLogic.MAX_Y;
        int curReach;
        Field minApple = null;
        for (Field apple: apples) {
            curReach = Math.abs(getOwnHead().getPosX() - apple.getPosX()) +
                    Math.abs(getOwnHead().getPosY() - apple.getPosY());

            if (curReach < minReach) {
                minApple = apple;
                minReach = curReach;
            }
        }

        return minApple;
    }


    /**
     * Returns all fields with barriers as list
     *
     * @return all fields with barriers as list
     */
    public List<Field> getBarrier() {
        return barriers;
    }


    /**
     * Returns if the next step in the given direction of the own snake is free
     *
     * @param direction a snake direction
     * @return true, if the next step in the given direction of the own snake is free, else false
     */
    public boolean isNextStepFree(int direction) {
        Field head = getOwnHead();
        Field[][] board = fields;

        if (direction == Snake.UP) {
            return (head.getPosY() > 0) && (board[head.getPosX()][head.getPosY() - 1].isFree());

        } else if (direction == Snake.RIGHT) {
            return (head.getPosX() < (BoardLogic.MAX_X - 1)) && (board[head.getPosX() + 1][head.getPosY()].isFree());

        } else if (direction == Snake.DOWN) {
            return (head.getPosY() < (BoardLogic.MAX_Y - 1)) && (board[head.getPosX()][head.getPosY() + 1].isFree());

        } else if (direction == Snake.LEFT) {
            return (head.getPosX() > 0) && (board[head.getPosX() - 1][head.getPosY()].isFree());

        } else {
            return false;
        }
    }


    /**
     * Returns the maximum of the x-coordinates
     *
     * @return the maximum of the x-coordinates
     */
    public int getMAX_X() {
        return BoardLogic.MAX_X;
    }


    /**
     * Returns the maximum of the y-coordinates
     *
     * @return the maximum of the y-coordinates
     */
    public int getMAX_Y() {
        return BoardLogic.MAX_Y;
    }


    /**
     * Returns the maximum of apples on board
     *
     * @return the maximum of apples on board
     */
    public int getMAX_APPLES_ON_BOARD() {
        return BoardLogic.MAX_APPLES_ON_BOARD;
    }
}
