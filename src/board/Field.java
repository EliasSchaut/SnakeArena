package board;


/**
 * Represents one field of the board.
 */
public class Field {
    private boolean apple;
    private boolean isFree;


    /**
     * Initialize Board.Field
     *
     * @param x the x-Coordinate of the Board.Board
     * @param y the y-Coordinate of the Board.Board
     */
    public Field() {
        this.apple = false;
        this.isFree = true;
    }


    protected boolean isApple() {
        return apple;
    }

    protected void setApple(boolean apple) {
        this.apple = apple;
    }

    protected boolean isFree() {
        return isFree;
    }

    protected void setFree(boolean free) {
        isFree = free;
    }
}
