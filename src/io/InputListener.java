package io;

import game.Game;
import snakes.examples.KeyboardSnake;
import snakes.Snake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Listen the keyboard and do stuff, if something is typed
 * Important for DebugSnake and PausePlay
 */
public class InputListener implements KeyListener {

    private final Game game;
    private final KeyboardSnake keyboardSnake;


    protected InputListener(Game game, KeyboardSnake keyboardSnake) {
        this.keyboardSnake = keyboardSnake;
        this.game = game;

    }


    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

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

    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }
}
