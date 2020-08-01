/**
 * Represents one field of the board.
 */
public class Field {
    private final int posX;
    private final int posY;

    private boolean apple;

    /**
     * Initialize Field
     *
     * @param x the x-Coordinate of the Board
     * @param y the y-Coordinate of the Board
     * @param apple is there an apple on field or not
     */
    public Field(int x, int y, boolean apple) {
        this.posX = x;
        this.posY = y;
        this.apple = apple;
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
}
