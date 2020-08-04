import java.awt.*;

public abstract class Snake {

    // Markers for the direction
    public static final int LEFT 	= 0;
    public static final int UP 		= 1;
    public static final int RIGHT 	= 2;
    public static final int DOWN 	= 3;


    public String NAME;
    public Color COLOR;


    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    public abstract int think(Board board);

}
