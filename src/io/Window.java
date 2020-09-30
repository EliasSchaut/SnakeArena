package io;

import board.BoardLogic;
import game.Game;
import snakes.DebugSnake;
import snakes.MySnake;
import snakes.Snake;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The game.Window Class represents the full window on the screen.
 * It renders all appearances.
 */
public class Window extends JFrame {

    private final JPanel boardPanel;
    private final BoardLogic boardLogic;

    public Window(Game game, String title, boolean RESIZEABLE, boolean debug, int SCALE, int MAX_X, int MAX_Y, int MAX_APPLES_ON_BOARD) {
        super(title);

        // Create Window frame
        this.setSize(SCALE * MAX_X + 500,SCALE * MAX_Y + 40);
        this.setLayout(new BorderLayout());


        // collect all snakes -----
        List<Snake> snakes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            snakes.add(new MySnake());
        }

        DebugSnake dSnake = new DebugSnake();
        if (debug) {
            snakes.add(dSnake);
        }
        // ----------


        // create Arena (BoardLayout)
        this.boardLogic = new BoardLogic(game, snakes, SCALE, MAX_X, MAX_Y, MAX_APPLES_ON_BOARD);
        this.boardPanel = boardLogic.getBPaint();

        // Adds Arena Panel to window frame
        this.add(this.boardPanel, BorderLayout.CENTER);

        // register key listener
        InputListener listener = new InputListener(game, dSnake);
        this.addKeyListener(listener);
        this.setFocusable(true);

        // set visibility and stuff
        this.setVisible(true);
        this.setResizable(RESIZEABLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void update(){
        this.boardLogic.update();
        this.boardPanel.repaint();
    }
}

