package board;

public enum FieldState {
    Empty (0),
    Apple (1),
    Snake (2),
    Barrier (3);

    private final int status;

    FieldState(int status) {
        this.status = status;
    }

    public int get() {
        return status;
    }
}
