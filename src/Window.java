import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The Window Class represents the full window on the screen.
 * It renders all appearances.
 */
public class Window extends JFrame {

    private static final long serialVersionUID = -4535052202864274946L;

    private JPanel boardPanel;

    private Game game;

    public Window(Game game, String title) {
        super(title);
        this.game = game;

        // Create Window frame
        this.setSize(500,500);
        this.setLayout(new BorderLayout());

        // Test -----
        Snake mySnake = new MySnake();
        Snake[] mySnakes = {mySnake, mySnake};
        // ----------

        // create Arena (BoardLayout)
        this.boardPanel = new Board(game, mySnakes);

        // Adds Arena Panel to Window frame
        this.add(this.boardPanel, BorderLayout.CENTER);

        // set visibility and stuff
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void update(){
        this.boardPanel.repaint();
    }
}

