package io;

import game.Game;

import javax.swing.*;
import java.util.Map;

/**
 * Class representing the help window that displays next to the main window.
 * Enables the user to change the game speed at runtime and pause via the mouse.
 */
public class HelperWindow extends JFrame {

    // window reference
    private final Window window;

    // gui components
    private final JSlider waitTimeSlider;
    private final JLabel waitTimeLabel;
    private final JButton pauseButton;

    // the time between game moves in ms
    private int waitTime;

    // the width of the window
    private final int width = 250;

    public HelperWindow(Window window, Game game, Map<String, String> cfgMap, InputListener listener) {
        super("Snake Arena Helper");

        this.window = window;

        // no automatic layout managing
        this.setLayout(null);

        // initializing wait time from file
        waitTime = Math.max(0, Math.min(1000, Integer.parseInt(cfgMap.get("WAIT_TIME"))));

        // creating GUI items
        waitTimeLabel = new JLabel("WAIT TIME: " + Integer.toString(waitTime), JLabel.CENTER);

        waitTimeSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int) Math.sqrt(waitTime * 1000.0));
        waitTimeSlider.addChangeListener(e -> {
            // this formula allows for finer control in the lower range
            waitTime = waitTimeSlider.getValue() * waitTimeSlider.getValue() / 1000;
            waitTimeLabel.setText("WAIT TIME: " + Integer.toString(waitTime));
        });

        pauseButton = new JButton("PAUSE");
        pauseButton.addActionListener(e -> {
            game.pausePlay();
        });
        // ----------

        // set visibility and stuff
        this.setSize(width, 150);
        display();
        this.setResizable(false);
        this.setFocusable(true);
        this.addKeyListener(listener);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // making sure to respect the insets
        final int actualWidth = width - this.getInsets().left - this.getInsets().right;
        int y = 0;
        waitTimeSlider.setBounds(0, y, actualWidth, 20);
        y += 20;
        waitTimeLabel.setBounds(0, y, actualWidth, 20);
        y += 40;
        pauseButton.setBounds(10, y, actualWidth - 20, 30);

        // adding components to window
        this.add(waitTimeSlider);
        this.add(waitTimeLabel);
        this.add(pauseButton);
    }

    /**
     * Hides the helper window or shows and places it next to the main window
     */
    public void display() {
        if (!this.isVisible()) {
            this.setLocation(window.getX() + window.getWidth(), window.getY());
            this.setVisible(true);
        } else {
            this.setVisible(false);
        }
    }

    // getter
    public int getWaitTime() {
        return waitTime;
    }

}
