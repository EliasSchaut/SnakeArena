package board;

import io.Game;
import snakes.Snake;

import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedList;

/**
 * The full needed information about the board for snake with index snakeIndex
 */
public class BoardInfo {

    private final Board board;
    private final int snakeIndex;

    /**
     * the Board-Class create this Board-Inforamtion for a spezific snake with the index snakeIndex
     *
     * @param board the board full of mistery
     * @param snakeIndex the index of the snake, which will need this information now
     */
    protected BoardInfo(Board board, int snakeIndex) {
        this.board = board;
        this.snakeIndex = snakeIndex;

    }

    /**
     * Deserialize BoardInfo from String
     * @param lines the lines read from stdin
     */
    public BoardInfo(List<String> lines) {
        this.snakeIndex = Integer.parseInt(lines.get(0));
        List<String> new_lines = lines.stream().skip(1).collect(Collectors.toList());
        this.board = new Board(new_lines);
    }

    /**
     * Serialize BoardInfo to String
     */
    public String serialize() {
        return serialize(board.serialize());
    }

    /**
     * Serialize BoardInfo to String
     */
    public String serialize(String board) {
        var builder = new StringBuilder();
        builder.append(snakeIndex);
        builder.append('\n');
        builder.append(board);
        return builder.toString();
    }


    /**
     * returns own head
     *
     * @return own head
     */
    public Position getOwnHead() {
        return board.getSnakesLocation().get(snakeIndex).getLast();
    }


    /**
     * returns own snake position
     *
     * @return own snake position
     */
    public List<Position> getOwnSnake() {
        return board.getSnakesLocation().get(snakeIndex);
    }


    /**
     * Returns head of other snakes
     *
     * @return head of other snakes
     */
    public List<Position> getOtherHeads() {
        LinkedList<Position> heads = new LinkedList<>();

        for (int i = 0; i < this.board.getSnakesLocation().size(); i++) {
            if (i != snakeIndex) {
                heads.add(this.board.getSnakesLocation().get(i).getLast());

            }
        }

        return heads;
    }


    /**
     * Returns position of all other snakes
     *
     * @return position of all other snakes
     */
    public List<LinkedList<Position>> getOtherSnakes() {
        LinkedList<LinkedList<Position>> snakes = new LinkedList<>();

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
    public List<Position> getApples() {
        return board.getApples();
    }


    /**
     * Returns all fields with barriers
     *
     * @return all fields with barriers
     */
    public List<Position> getBarrier() {
        return board.getBarrier();
    }


    /**
     * Returns if the next step in the given direction of the own snake will be free
     *
     * @param direction a snake direction
     * @return true, if the next step in the given direction of the own snake will be free, else false
     */
    public boolean isNextStepFree(int direction) {
        Position head = getOwnHead();
        Field[][] board = this.board.getFields();

        if (direction == Snake.UP) {
            return (head.getY() > 0) && (board[head.getX()][head.getY()].isFree());

        } else if (direction == Snake.RIGHT) {
            return (head.getX() < (Board.MAX_X - 1)) && (board[head.getX()][head.getY()].isFree());

        } else if (direction == Snake.DOWN) {
            return (head.getY() < (Board.MAX_Y - 1)) && (board[head.getX()][head.getY()].isFree());

        } else if (direction == Snake.LEFT) {
            return (head.getX() > 0) && (board[head.getX()][head.getY()].isFree());

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
        return Board.MAX_X;
    }


    /**
     * Returns the maximum of the y-coordinates
     *
     * @return the maximum of the y-coordinates
     */
    public int getMAX_Y() {
        return Board.MAX_Y;
    }


    /**
     * Returns the maximum of apples on board
     *
     * @return the maximum of apples on board
     */
    public int getMAX_APPLES_ON_BOARD() {
        return Board.MAX_APPLES_ON_BOARD;
    }
}
