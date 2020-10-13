package io;

import game.Game;
import snakes.examples.KeyboardSnake;
import snakes.Snake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Listen the keyboard and do stuff, if something is typed
 * Important for KeyboardSnake and PausePlay
 */
public class InputListener implements KeyListener {

    private final Game game;
    private final KeyboardSnake keyboardSnake;


    /**
     * The Constructor.
     * It sets game and keyboardSnake
     *
     * @param game the Game
     * @param keyboardSnake the KeyboardSnake
     */
    protected InputListener(Game game, KeyboardSnake keyboardSnake) {
        this.keyboardSnake = keyboardSnake;
        this.game = game;

    }


    /**
     * Reacts, if a key was typed.
     *
     * @param e the specific, which was typed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }


    /**
     * Reacts, if a key was pressed.
     *
     * @param e the specific, which was pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {

        // System.out.println("Key: "+KeyEvent.getKeyText(e.getKeyCode())+" Code: "+e.getKeyCode());

        switch (e.getKeyCode()) {
            // left
            case 37 -> keyboardSnake.direction = Snake.LEFT;
            // up
            case 38 -> keyboardSnake.direction = Snake.UP;
            // right
            case 39 -> keyboardSnake.direction = Snake.RIGHT;
            // down
            case 40 -> keyboardSnake.direction = Snake.DOWN;
            // pausePlay
            case 80 -> game.pausePlay();
            default -> System.out.println("Unknown Key: " + KeyEvent.getKeyText(e.getKeyCode()) + " Code: " + e.getKeyCode());
        }
    }


    /**
     * Reacts, if a key was released.
     *
     * @param e the specific, which was released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }
}
