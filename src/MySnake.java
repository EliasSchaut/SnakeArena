import java.awt.*;


/**
 * an example Snake for testing
 */
public class MySnake extends Snake {

    private int direction;
    private int counter;

    public MySnake() {
        this.NAME = "MySnake";                      // everybody can set his favorite name
        this.COLOR = new Color(80, 0, 80); // everybody can set his favorite color

        counter = 0;
        direction = LEFT;
    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    public int think(Board board) {
        // lovely intelligence code here
        if (counter == 2) {
            direction = (direction + 1) % 4;
            counter = 0;
        }
        ++counter;

        return direction; // or LEFT, or DOWN, or UP
    }
}
