package io;

import snakes.DebugSnake;
import snakes.Snake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class InputListener implements KeyListener {

    private final Game game;
    private final DebugSnake debugSnake;

    // --- Constructor -----------------------------------------------------

    protected InputListener(Game game, DebugSnake debugSnake) {
        this.debugSnake = debugSnake;
        this.game = game;

        System.out.println("Heyy Listen, Link, Link, Link, Link, wake up, Link, Link!!");
    }

    // --- Methods ----------------------------------------------

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        // System.out.println("Key: "+KeyEvent.getKeyText(e.getKeyCode())+" Code: "+e.getKeyCode());

        switch(e.getKeyCode()) {

            case 37:	// left
                debugSnake.direction = Snake.LEFT;
                break;
            case 38:	// up
                debugSnake.direction = Snake.UP;
                break;
            case 39:	// right
                debugSnake.direction = Snake.RIGHT;
                break;
            case 40:	// down
                debugSnake.direction = Snake.DOWN;
                break;
            case 80:
                game.pausePlay();
            default:
                System.out.println("Unknown Key: "+KeyEvent.getKeyText(e.getKeyCode())+" Code: "+e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }
}
