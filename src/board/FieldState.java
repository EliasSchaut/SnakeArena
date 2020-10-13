package board;

public enum FieldState {
    // A Field is empty an has nothing in it; A Snake can enter this field without dying
    Empty (0),

    // A Field has only an apple in it; A Snake can enter this field without dying
    Apple (1),

    // A Field has a part of a SnakeBody in it; A Snake will die if it enters this Field.
    Snake (2),

    // A Field has a part of a Barrier (or dead Snake) in it; A Snake will die if it enters this Field.
    Barrier (3),

    // A Field is outside of the boardA Snake will die if it enters this Field.
    Outside (4);

    private final int status;


    /**
     * The Constructor.
     * It set the number of the status.
     *
     * @param status set status with this given value
     */
    FieldState(int status) {
        this.status = status;
    }

    /**
     * Get number of status
     *
     * @return number of status
     */
    public int get() {
        return status;
    }
}
