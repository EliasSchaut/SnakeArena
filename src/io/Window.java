package io;

import board.BoardLogic;
import game.Game;
import snakes.MySnake;
import snakes.examples.*;
import snakes.Snake;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * The window class represents the full window on the screen.
 * It renders all appearances.
 */
public class Window extends JFrame {

    private final JPanel boardPanel;
    private final BoardLogic boardLogic;

    public Window(Game game, Map<String, String> cfgMap) {
        // set needed config values
        super(cfgMap.get("WINDOW_TITLE"));
        final int SCALE = Integer.parseInt(cfgMap.get("SCALE"));
        final int MAX_X = Integer.parseInt(cfgMap.get("MAX_X"));
        final int MAX_Y = Integer.parseInt(cfgMap.get("MAX_Y"));
        final int OFFSET = Integer.parseInt(cfgMap.get("OFFSET"));

        // Create Window frame
        this.setSize(SCALE * MAX_X + 500 + (2 * OFFSET),SCALE * (MAX_Y + 2) + (2 * OFFSET));
        this.setLayout(new BorderLayout());

        // collect all snakes -----
        List<Snake> snakes = collectSnakes(cfgMap);

        // enable KeyboardSnake, if debug in config is true
        KeyboardSnake kSnake = new KeyboardSnake();
        if (Boolean.parseBoolean(cfgMap.get("debug"))) {
            snakes.add(kSnake);
        }
        // ----------

        // create Arena (BoardLayout)
        this.boardLogic = new BoardLogic(game, snakes, cfgMap);
        this.boardPanel = boardLogic.getBPaint();

        // Adds Arena Panel to window frame
        this.add(this.boardPanel, BorderLayout.CENTER);

        // register key listener
        InputListener listener = new InputListener(game, kSnake);
        this.addKeyListener(listener);
        this.setFocusable(true);

        // set visibility and stuff
        this.setVisible(true);
        this.setResizable(Boolean.parseBoolean(cfgMap.get("RESIZEABLE")));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    /**
     * Make a game step.
     * boardLogic.update() updates the logic of the game. It calls every think-method of every snake an move the snakes
     * boardPanel.repaint() will paint the board with the new values from boardLogic
     */
    public void update(){
        this.boardLogic.update();
        this.boardPanel.repaint();
    }


    /**
     * collect all snakes (except KeyboardSnake) and return them as list.
     *
     * @param cfgMap the config values
     * @return a list with all snakes in game (except KeyboardSnake)
     */
    private List<Snake> collectSnakes(Map<String, String> cfgMap) {
        List<Snake> snakes = new ArrayList<>();
        int num;

        // MySnake
        num = Integer.parseInt(cfgMap.get("MySnake"));
        for (int i = 0; i < num; i++) {
            snakes.add(new MySnake());
        }

        // BarrierSnake
        num = Integer.parseInt(cfgMap.get("BarrierSnake"));
        for (int i = 0; i < num; i++) {
            snakes.add(new BarrierSnake());
        }

        // CircleSnake
        num = Integer.parseInt(cfgMap.get("CircleSnake"));
        for (int i = 0; i < num; i++) {
            snakes.add(new CircleSnake());
        }

        // EasySnake
        num = Integer.parseInt(cfgMap.get("EasySnake"));
        for (int i = 0; i < num; i++) {
            snakes.add(new EasySnake());
        }

        // ProtectorSnake
        num = Integer.parseInt(cfgMap.get("ProtectorSnake"));
        for (int i = 0; i < num; i++) {
            snakes.add(new ProtectorSnake());
        }

        return snakes;
    }
}

