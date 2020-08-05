import java.awt.*;

public class DebugSnake extends Snake{

    public int direction = RIGHT;

    public DebugSnake() {
        this.NAME = "DebugSnake";                      // everybody can set his favorite name
        this.COLOR = new Color(0, 80, 0); // everybody can set his favorite color

    }


    @Override
    public int think(Board board) {
        return direction;
    }
}
