package snakes;

import java.awt.*;
import board.*;

public abstract class Snake {

    // Markers for the direction
    public static final int UP 		= 0;
    public static final int RIGHT 	= 1;
    public static final int DOWN 	= 2;
    public static final int LEFT 	= 3;

    public String NAME;
    public Color COLOR;


    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    public abstract int think(BoardInfo board);

}
