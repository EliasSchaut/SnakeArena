package board;
/**
 * The Position represents a simple tuple of coordinates
 */
public class Position {
    private int posX;
    private int posY;

    /**
     * Initialize Position
     *
     * @param x the x-Coordinate of the Board.Board
     * @param y the y-Coordinate of the Board.Board
    */
    public Position(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    int getX() {
        return this.posX;
    }

    int getY() {
        return this.posY;
    }
}

