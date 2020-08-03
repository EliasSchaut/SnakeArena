import java.awt.*;


/**
 * an example Snake for testing
 */
public class MySnake extends Snake {

    public MySnake() {
        this.NAME = "MySnake";                      // everybody can set his favorite name
        this.COLOR = new Color(80, 0, 80); // everybody can set his favorite color

    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    public int think(Board board) {
        // lovely intelligence code here

        return RIGHT; // or LEFT, or DOWN, or UP
    }
}
