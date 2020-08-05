package io;

import board.Board;
import snakes.AntonsSnake;
import snakes.DebugSnake;
import snakes.MySnake;
import snakes.Snake;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The game.Window Class represents the full window on the screen.
 * It renders all appearances.
 */
public class Window extends JFrame {

    private static final long serialVersionUID = -4535052202864274946L;

    private final JPanel boardPanel;

    public Window(Game game, String title) {
        super(title);

        // Create game.Window frame
        this.setSize(500,500);
        this.setLayout(new BorderLayout());

        // Test -----
        Snake mySnake = new MySnake();
        Snake mySnake2 = new MySnake();
        Snake mySnake3 = new MySnake();
        DebugSnake debugSnake = new DebugSnake();
        Snake anton = new AntonsSnake();
        Snake[] mySnakes = {mySnake, mySnake2, mySnake3, debugSnake, anton};
        // ----------

        // create Arena (BoardLayout)
        this.boardPanel = new Board(game, mySnakes);

        // Adds Arena Panel to game.Window frame
        this.add(this.boardPanel, BorderLayout.CENTER);

        // register key listener
        InputListener listener = new InputListener(game, debugSnake);
        this.addKeyListener(listener);
        this.setFocusable(true);

        // set visibility and stuff
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void update(){
        this.boardPanel.repaint();
    }
}

