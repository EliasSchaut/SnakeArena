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


    /**
     * the Constructor.
     * It will set all relevant values from the property file, create a helper window and set all relevant stuff on it
     *
     * @param window the window of the game
     * @param game the game
     * @param cfgMap the configs
     * @param listener the keyListener
     */
    public HelperWindow(Window window, Game game, Map<String, String> cfgMap, InputListener listener) {
        super(cfgMap.get("HELPER_TITLE"));
        this.window = window;

        // get config values ----------------------------
        int width = Integer.parseInt(cfgMap.get("HELPER_WIDTH"));
        int height = Integer.parseInt(cfgMap.get("HELPER_HEIGHT"));
        int minWait = Integer.parseInt(cfgMap.get("HELPER_MIN_WAIT_SLIDER"));
        int maxWait = Integer.parseInt(cfgMap.get("HELPER_MAX_WAIT_SLIDER"));
        this.waitTime = Integer.parseInt(cfgMap.get("WAIT_TIME"));
        boolean resizable = Boolean.parseBoolean(cfgMap.get("HELPER_RESIZEABLE"));
        boolean focusable = Boolean.parseBoolean(cfgMap.get("HELPER_FOCUSABLE"));
        // -------------------------------------------------

        // no automatic layout managing
        this.setLayout(null);

        // creating GUI items -------------

        // create wait time slider
        int sliderStart = ((int) Math.sqrt((maxWait - minWait) * (waitTime - minWait))) + minWait;
        waitTimeLabel = new JLabel("WAIT TIME: " + waitTime, JLabel.CENTER);
        waitTimeSlider = new JSlider(JSlider.HORIZONTAL, minWait, maxWait, sliderStart);
        waitTimeSlider.addChangeListener(e -> {

            // this formula allows for finer control in the lower range
            int sliderValue = ((int) Math.pow(waitTimeSlider.getValue() - minWait, 2)) / (maxWait - minWait) + minWait;
            waitTimeLabel.setText("WAIT TIME: " + sliderValue);
            this.waitTime = sliderValue;
        });

        // create pause/play button with action listener
        pauseButton = new JButton();
        setButtonLaben(game);
        pauseButton.addActionListener(e -> {
            game.pausePlay();
            setButtonLaben(game);

        });
        // ---------------------------------

        // set visibility and stuff
        this.setSize(width, height);
        displayHide();
        this.setResizable(resizable);
        this.setFocusable(focusable);
        this.addKeyListener(listener);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // making sure to respect the insets
        final int actualWidth = width - this.getInsets().left - this.getInsets().right;
        int y = 10;
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
    public void displayHide() {
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


    // set the label of the pause/play button
    private void setButtonLaben(Game game) {
        pauseButton.setText("PLAY/PAUSE");

        /* use this if you can call this method after pressing P with GOOD!! code!
        if (game.isPaused()) {
            pauseButton.setText("PLAY");
        } else {
            pauseButton.setText("PAUSE");
        }*/
    }

}
