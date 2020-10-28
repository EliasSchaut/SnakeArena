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
            case KeyEvent.VK_UP, KeyEvent.VK_W -> keyboardSnake.direction = Snake.UP;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> keyboardSnake.direction = Snake.RIGHT;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> keyboardSnake.direction = Snake.DOWN;
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> keyboardSnake.direction = Snake.LEFT;
            case KeyEvent.VK_P -> game.pausePlay();
            case KeyEvent.VK_SPACE -> game.showHelperWindow();
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
