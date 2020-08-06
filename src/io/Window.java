package io;

import board.Board;
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

    private final JPanel boardPanel;

    public Window(Game game, String title) {
        super(title);

        // Create game.Window frame
        this.setSize(Board.SCALE * Board.SCALE + 300,Board.SCALE * Board.SCALE + 40);
        this.setLayout(new BorderLayout());

        // add snakes to board -----
        Snake mySnake = new MySnake();
        Snake mySnake2 = new MySnake();
        Snake mySnake3 = new MySnake();
        DebugSnake debugSnake = new DebugSnake();

        Snake[] mySnakes;
        if (game.DEBUG) {
            mySnakes = new Snake[]{mySnake, mySnake2, mySnake3, debugSnake};
        } else {
            mySnakes = new Snake[]{mySnake, mySnake2, mySnake3};
        }
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
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void update(){
        this.boardPanel.repaint();
    }
}

