package io;

import snakes.DebugSnake;
import snakes.Snake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Listen the Keyboard and do stuff maybe, if something is typed
 * Important for DebugSnake and PausePlay
 */
public class InputListener implements KeyListener {

    private final Game game;
    private final DebugSnake debugSnake;

    // --- Constructor -----------------------------------------------------

    protected InputListener(Game game, DebugSnake debugSnake) {
        this.debugSnake = debugSnake;
        this.game = game;

    }

    // --- Methods ----------------------------------------------

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {

        // System.out.println("Key: "+KeyEvent.getKeyText(e.getKeyCode())+" Code: "+e.getKeyCode());

        switch (e.getKeyCode()) {
            // left
            case 37 -> debugSnake.direction = Snake.LEFT;
            // up
            case 38 -> debugSnake.direction = Snake.UP;
            // right
            case 39 -> debugSnake.direction = Snake.RIGHT;
            // down
            case 40 -> debugSnake.direction = Snake.DOWN;
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
