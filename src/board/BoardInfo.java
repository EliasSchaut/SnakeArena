package board;

import snakes.Snake;

import java.util.List;
import java.util.LinkedList;

/**
 * The methods in this class give the full needed information about the board
 * from the perspective of the snake with the snakeIndex given in constructor.
 */
public class BoardInfo {

    private final int snakeIndex;
    private final Field[][] fields;
    private final List<Field> apples;
    private final List<LinkedList<Field>> snakesLocation;
    private final List<Field> barriers;


    /**
     * The Constructor.
     * It sets all important information values from BoardLogic
     *
     * @param snakeIndex the index of the snake, which will need this information now
     * @param fields the whole board as 2-dim array
     * @param snakesLocation the body locations of all snakes
     * @param apples the positions of all apples
     * @param barriers the positions of all barriers
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
     * Get the whole board as 2-dim array filled with fields
     *
     * @return the whole board as 2-dim array filled with fields
     */
    public Field[][] getFields() {
        return fields;
    }


    /**
     * returns own snake-head as field
     *
     * @return own snake-head as field
     */
    public Field getOwnHead() {
        return snakesLocation.get(snakeIndex).getLast();
    }


    /**
     * returns own snake-position as list
     *
     * @return own snake-position as list
     */
    public List<Field> getOwnSnake() {
        return snakesLocation.get(snakeIndex);
    }


    /**
     * Returns head of other snakes as list (without own head)
     *
     * @return head of other snakes as list (without own head)
     */
    public List<Field> getOtherHeads() {
        List<Field> heads = new LinkedList<>();

        for (int i = 0; i < snakesLocation.size(); i++) {
            if (i != snakeIndex) {
                heads.add(snakesLocation.get(i).getLast());

            }
        }

        return heads;
    }


    /**
     * Returns position of all other snakes (without own snake)
     *
     * @return position of all other snakes (without own snake)
     */
    public List<LinkedList<Field>> getOtherSnakes() {
        List<LinkedList<Field>> snakes = new LinkedList<>();

        for (int i = 0; i < snakesLocation.size(); i++) {
            if (i != snakeIndex) {
                snakes.add(snakesLocation.get(i));
            }
        }

        return snakes;
    }


    /**
     * Return all apple positions as list
     *
     * @return all apple positions as list
     */
    public List<Field> getApples() {
        return apples;
    }


    /**
     * Find nearest apple from own SnakeHead
     *
     * @return nearest apple field
     */
    public Field getNearestApple() {
        List<Field> apples = getApples();
        int minReach = BoardLogic.MAX_X + BoardLogic.MAX_Y; // set max distance
        Field minApple = null;
        int curReach;

        // iterate through all apples
        for (Field apple: apples) {

            // calculate number of fields to reach this apple
            curReach = Math.abs(getOwnHead().getPosX() - apple.getPosX()) +
                    Math.abs(getOwnHead().getPosY() - apple.getPosY());

            // if curReach is smaller than every reach bevor, set this as nearest apple
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
     * Returns if the next field in the given direction of the own snake head is free.
     * Free means, that the field has status empty or apple.
     *
     * @param direction a snake direction
     * @return true, if the next field in the given direction of the own snake is free, else false
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
     * Returns the state of the field in the given direction from the own snake head
     *
     * @param direction a snake direction
     * @return a field state
     */
    public FieldState getNextStepState(int direction) {
        Field head = getOwnHead();
        Field[][] board = fields;
        FieldState state;

        if (direction == Snake.UP) {
            state = (head.getPosY() > 0) ? board[head.getPosX()][head.getPosY() - 1].getState() : FieldState.Outside;

        } else if (direction == Snake.RIGHT) {
            state = (head.getPosX() < (BoardLogic.MAX_X - 1)) ?
                    board[head.getPosX() + 1][head.getPosY()].getState() : FieldState.Outside;

        } else if (direction == Snake.DOWN) {
            state = (head.getPosY() < (BoardLogic.MAX_Y - 1)) ?
                    board[head.getPosX()][head.getPosY() + 1].getState() : FieldState.Outside;

        } else if (direction == Snake.LEFT) {
            state = (head.getPosX() > 0) ? board[head.getPosX() - 1][head.getPosY()].getState() : FieldState.Outside;

        } else {
            state = FieldState.Outside;
        }

        return state;
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
