package board;

/**
 * Represents one field of the board.
 */
public class Field {
    private final int posX;
    private final int posY;

    private boolean apple;
    private boolean isFree;


    /**
     * Initialize Board.Field
     *
     * @param x the x-Coordinate of the Board.Board
     * @param y the y-Coordinate of the Board.Board
     */
    public Field(int x, int y) {
        this.posX = x;
        this.posY = y;
        this.apple = false;
        this.isFree = true;
    }


    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isApple() {
        return apple;
    }

    public void setApple(boolean apple) {
        this.apple = apple;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }
}
