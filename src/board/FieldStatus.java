package board;

public enum FieldStatus {
    FREE                (0),
    BLOCKED             (1),
    APPLE               (2),
    YOUR_SNAKE          (3),
    YOUR_SNAKE_HEAD     (4),
    OTHER_SNAKE         (5),
    OTHER_SNAKE_HEAD    (6);

    FieldStatus(int i) {}
}
