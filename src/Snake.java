import java.awt.*;

public class Snake {

    // Markers for the direction
    public static final int LEFT 	= 1;
    public static final int UP 		= 2;
    public static final int RIGHT 	= 3;
    public static final int DOWN 	= 4;


    public String NAME;
    public Color COLOR;


    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    public int think(Board board) {

        return LEFT;
    }
}
