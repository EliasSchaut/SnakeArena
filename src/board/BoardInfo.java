package board;

import java.util.LinkedList;

public class BoardInfo {

    private final Board board;
    private final int snakeIndex;

    protected BoardInfo(Board board, int snakeIndex) {
        this.board = board;
        this.snakeIndex = snakeIndex;

    }

    /**
     * Return Board as 2-dim Array
     *
     * @return Board as 2-dim Array
     */
    public int[][] getBoard() {
        int[][] board = new int[Board.MAX_X][Board.MAX_Y];

        for (int i = 0; i < Board.MAX_X; i++) {
            for (int j = 0; j < Board.MAX_Y; j++) {

                



            }
        }

        return board;
    }


    /**
     * returns own head
     *
     * @return own head
     */
    public Field getOwnHead() {
        return board.getSnakesLocation().get(snakeIndex).getLast();
    }


    /**
     * returns own snake position
     *
     * @return own snake position
     */
    public LinkedList<Field> getOwnSnake() {
        return board.getSnakesLocation().get(snakeIndex);
    }


    /**
     * Returns head of other snakes
     *
     * @return head of other snakes
     */
    public LinkedList<Field> getOtherHeads() {
        LinkedList<Field> heads = new LinkedList<>();

        for (int i = 0; i < board.getSnakesLocation().size(); i++) {
            if (i != snakeIndex) {
                heads.add(board.getSnakesLocation().get(i).getLast());

            }
        }

        return heads;
    }


    /**
     * Returns position of all other snakes
     *
     * @return position of all other snakes
     */
    public LinkedList<LinkedList<Field>> getOtherSnakes() {
        LinkedList<LinkedList<Field>> snakes = new LinkedList<>();

        for (int i = 0; i < board.getSnakesLocation().size(); i++) {
            if (i != snakeIndex) {
                snakes.add(board.getSnakesLocation().get(i));
            }
        }

        return snakes;
    }


    /**
     * Return apple positions
     *
     * @return apple positions
     */
    public LinkedList<Field> getApples() {
        return board.getApples();
    }


    public LinkedList<Field> getBarrier() {
        return board.getBarrier();
    }
}
