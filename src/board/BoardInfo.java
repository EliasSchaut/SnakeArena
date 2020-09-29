package board;

import snakes.Snake;

import java.util.List;
import java.util.LinkedList;

/**
 * The full needed information about the board for snake with index snakeIndex
 */
public class BoardInfo {

    private final BoardLogic boardLogic;
    private final int snakeIndex;

    /**
     * the Board-Class create this Board-Inforamtion for a spezific snake with the index snakeIndex
     *
     * @param boardLogic the board full of mistery
     * @param snakeIndex the index of the snake, which will need this information now
     */
    protected BoardInfo(BoardLogic boardLogic, int snakeIndex) {
        this.boardLogic = boardLogic;
        this.snakeIndex = snakeIndex;

    }


    /**
     * returns own head
     *
     * @return own head
     */
    public Field getOwnHead() {
        return boardLogic.getSnakesLocation().get(snakeIndex).getLast();
    }


    /**
     * returns own snake position
     *
     * @return own snake position
     */
    public List<Field> getOwnSnake() {
        return boardLogic.getSnakesLocation().get(snakeIndex);
    }


    /**
     * Returns head of other snakes
     *
     * @return head of other snakes
     */
    public List<Field> getOtherHeads() {
        LinkedList<Field> heads = new LinkedList<>();

        for (int i = 0; i < this.boardLogic.getSnakesLocation().size(); i++) {
            if (i != snakeIndex) {
                heads.add(this.boardLogic.getSnakesLocation().get(i).getLast());

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

        for (int i = 0; i < boardLogic.getSnakesLocation().size(); i++) {
            if (i != snakeIndex) {
                snakes.add(boardLogic.getSnakesLocation().get(i));
            }
        }

        return snakes;
    }


    /**
     * Return apple positions
     *
     * @return apple positions
     */
    public List<Field> getApples() {
        return boardLogic.getApples();
    }


    /**
     * Returns all fields with barriers
     *
     * @return all fields with barriers
     */
    public List<Field> getBarrier() {
        return boardLogic.getBarrier();
    }


    /**
     * Returns if the next step in the given direction of the own snake will be free
     *
     * @param direction a snake direction
     * @return true, if the next step in the given direction of the own snake will be free, else false
     */
    public boolean isNextStepFree(int direction) {
        Field head = getOwnHead();
        Field[][] board = this.boardLogic.getFields();

        if (direction == Snake.UP) {
            return (head.getPosY() > 0) && (board[head.getPosX()][head.getPosY()].isFree());

        } else if (direction == Snake.RIGHT) {
            return (head.getPosX() < (BoardLogic.MAX_X - 1)) && (board[head.getPosX()][head.getPosY()].isFree());

        } else if (direction == Snake.DOWN) {
            return (head.getPosY() < (BoardLogic.MAX_Y - 1)) && (board[head.getPosX()][head.getPosY()].isFree());

        } else if (direction == Snake.LEFT) {
            return (head.getPosX() > 0) && (board[head.getPosX()][head.getPosY()].isFree());

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
