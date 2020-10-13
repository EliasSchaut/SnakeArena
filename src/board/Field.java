package board;

/**
 * Represents one field of the board.
 * It has a x- and y-postion and a FieldState
 */
public class Field {

    private final int posX;
    private final int posY;
    private FieldState state;


    /**
     * Initialize Field with given postions and an empty FieldState
     *
     * @param x the x-postion of the Board
     * @param y the y-postion of the Board
     */
    public Field(int x, int y) {
        this.posX = x;
        this.posY = y;
        state = FieldState.Empty;
    }


    /**
     * Get x-postion
     *
     * @return x-postion
     */
    public int getPosX() {
        return posX;
    }


    /**
     * Get y-postion
     *
     * @return y-postion
     */
    public int getPosY() {
        return posY;
    }


    /**
     * Returns true, if a field is empty or has an apple on it; else false.
     * So if this is true a snake can get on this field without dying, else not.
     *
     * @return true, if a field is empty or has an apple on it; else false.
     */
    public boolean isFree() {
        return (this.state == FieldState.Empty) || (this.state == FieldState.Apple);
    }


    /**
     * Get FieldState
     *
     * @return FieldState
     */
    public FieldState getState() {
        return this.state;
    }


    /**
     * Set FieldState
     *
     * @param status the status that needs to be set
     */
    protected void setState(FieldState status) {
        this.state = status;
    }
}
