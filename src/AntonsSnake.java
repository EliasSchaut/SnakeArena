import java.awt.*;

public class AntonsSnake extends Snake{

    public AntonsSnake() {
        this.NAME = "Cpt_Tony";                      // everybody can set his favorite name
        this.COLOR = new Color(2, 146, 255, 136); // everybody can set his favorite color

    }

    @Override
    public int think(Board board) {

        return RIGHT;
    }
}
