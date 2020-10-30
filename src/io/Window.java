package io;

import board.BoardLogic;
import game.Game;
import snakes.MySnake;
import snakes.examples.*;
import snakes.Snake;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * The window class represents the full window on the screen.
 * It renders all appearances.
 */
public class Window extends JFrame {

    // helper window we own
    private final HelperWindow helperWindow;

    private final JPanel boardPanel;
    private final BoardLogic boardLogic;


    /**
     * The Constructor.
     * It will set all relevant values from the property file, create BoardLogic and BoardPaint and create a window
     *
     * @param game the game
     * @param cfgMap all configs
     */
    public Window(Game game, Map<String, String> cfgMap) {

        // set needed config values
        super(cfgMap.get("WINDOW_TITLE"));
        final int SCALE = Integer.parseInt(cfgMap.get("SCALE"));
        final int MAX_X = Integer.parseInt(cfgMap.get("SIZE_X"));
        final int MAX_Y = Integer.parseInt(cfgMap.get("SIZE_Y"));
        final int OFFSET = Integer.parseInt(cfgMap.get("OFFSET"));

        // Create Window frame
        this.setSize(SCALE * MAX_X + 500 + (2 * OFFSET),SCALE * (MAX_Y + 2) + (2 * OFFSET));
        this.setLayout(new BorderLayout());

        // collect all snakes -----
        List<Snake> snakes = collectSnakes(cfgMap);

        // enable KeyboardSnake, if DEBUG in config is true
        KeyboardSnake kSnake = new KeyboardSnake();
        if (Boolean.parseBoolean(cfgMap.get("DEBUG"))) {
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

        // create helper window
        helperWindow = new HelperWindow(this, game, cfgMap, listener);
    }


    /**
     * Make a game step.
     * boardLogic.update() updates the logic of the game. It calls every think-method of every snake an move the snakes
     * boardPanel.repaint() will paint the board with the new values from boardLogic
     */
    public void update() {
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


        // contest snakes -----------------------------------------------------------

        // Python
        num = Integer.parseInt(cfgMap.get("Python"));
        for (int i = 0; i < num; i++) {
            snakes.add(new snakes.contest.pyhton.MySnake());
        }

        // Snakeue
        num = Integer.parseInt(cfgMap.get("Snakeue"));
        for (int i = 0; i < num; i++) {
            snakes.add(new snakes.contest.snakeue.MySnake());
        }

        // fiesserIk0ri4n
        num = Integer.parseInt(cfgMap.get("fiesserIk0ri4n"));
        for (int i = 0; i < num; i++) {
            snakes.add(new snakes.contest.fiesserIk0ri4n.MySnake());
        }

        // DevilOfParadise
        num = Integer.parseInt(cfgMap.get("DevilOfParadise"));
        for (int i = 0; i < num; i++) {
            snakes.add(new snakes.contest.devilOfParadise.MySnake());
        }

        // SnakeBy
        num = Integer.parseInt(cfgMap.get("SnakeBy"));
        for (int i = 0; i < num; i++) {
            snakes.add(new snakes.contest.snakeBy.MySnake());
        }

        // .IDI
        num = Integer.parseInt(cfgMap.get("IDI"));
        for (int i = 0; i < num; i++) {
            snakes.add(new snakes.contest.idi.MySnake());
        }

        // ESnake
        num = Integer.parseInt(cfgMap.get("ESnake"));
        for (int i = 0; i < num; i++) {
            snakes.add(new snakes.contest.eSnake.MySnake());
        }

        // Chrysopelea_Tobias
        num = Integer.parseInt(cfgMap.get("Chrysopelea_Tobias"));
        for (int i = 0; i < num; i++) {
            snakes.add(new snakes.contest.chrysopelea_Tobias.MySnake());
        }

        // Schlongus_Humongous
        num = Integer.parseInt(cfgMap.get("Schlongus_Humongous"));
        for (int i = 0; i < num; i++) {
            snakes.add(new snakes.contest.schlongus_Humongous.MySnake());
        }

        // Norbert
        num = Integer.parseInt(cfgMap.get("Norbert"));
        for (int i = 0; i < num; i++) {
            snakes.add(new snakes.contest.norbert.MySnake());
        }
        // --------------------------------------------------------------------------

        Collections.shuffle(snakes);

        return snakes;
    }

    // ---------------------------------------------------
    // Exposing necessary HelperWindow functionality
    // ---------------------------------------------------
    public void displayHelper() {
        helperWindow.displayHide();
    }

    public int getWaitTime() {
        return helperWindow.getWaitTime();
    }
    // ---------------------------------------------------

}

