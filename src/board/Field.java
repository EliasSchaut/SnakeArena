package board;

/**
 * Represents one field of the board.
 */
public class Field {

    private final int posX;
    private final int posY;
    private FieldState state;

    /**
     * Initialize Board.Field
     *
     * @param x the x-Coordinate of the Board
     * @param y the y-Coordinate of the Board
     */
    public Field(int x, int y) {
        this.posX = x;
        this.posY = y;
        state = FieldState.Empty;
    }


    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isFree() {
        return (this.state == FieldState.Empty) || (this.state == FieldState.Apple);
    }

    public FieldState getState() {
        return this.state;
    }

    protected void setState(FieldState status) {
        this.state = status;
    }
}
