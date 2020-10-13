package board;

import snakes.Snake;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is responsible for painting everything in the window.
 * The paint() method repaints everything with the values from boardLogic
 */
public class BoardPaint extends JPanel {

    private final BoardLogic boardLogic;


    /**
     * The Constructor.
     * It will set the boardLogic behind the painting
     *
     * @param boardLogic set global variable with this value
     */
    public BoardPaint(BoardLogic boardLogic) {
        this.boardLogic = boardLogic;
    }


    /**
     * This method is in other words an update method that repaint everything with the values from boardLogic
     *
     * This method paints the following:
     * The names of dead and living snakes with their current length; the board grid; the apples on board;
     * the snakes on board; the barriers on board
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        this.paintNames(g2d, boardLogic.getSnakes(), boardLogic.getSnakesLocation(), boardLogic.getDeadSnakesInfo());
        this.paintBoard(g2d);
        this.paintApples(g2d, boardLogic.getApples());
        this.paintBarrier(g2d, boardLogic.getBarrier());
        this.paintSnakes(g2d, boardLogic.getSnakes(), boardLogic.getSnakesLocation());

    }


    /**
     * Draws the grid board
     *
     * @param g2d the Graphics2D, which is needed for painting
     */
    private void paintBoard(Graphics2D g2d) {
        for (int x = 0; x < BoardLogic.MAX_X; x++) {
            for (int y = 0; y < BoardLogic.MAX_Y; y++) {
                g2d.drawRect(x * BoardLogic.SCALE + BoardLogic.OFFSET, y * BoardLogic.SCALE + BoardLogic.OFFSET
                        , BoardLogic.SCALE, BoardLogic.SCALE);
            }
        }
    }


    /**
     * Draw names of living and dead snakes on the right side of the board
     *
     * @param g2d the Graphics2D, which is needed for painting
     * @param snakes the snakes in game
     * @param snakesLocation the location of snakes on board
     * @param deadSnakesInfo the names and lenght of dead snakes
     */
    private void paintNames(Graphics2D g2d, List<Snake> snakes,
                            List<LinkedList<Field>> snakesLocation, List<String> deadSnakesInfo) {

        // set font
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 14f));


        // ----------------------------------
        // Draw names of living snakes
        // ----------------------------------
        // Draw header
        g2d.setColor(Color.BLACK);
        g2d.drawString("Living Snakes:", BoardLogic.SCALE * BoardLogic.MAX_X + 100 + BoardLogic.OFFSET,
                50 + BoardLogic.OFFSET);

        // Draw living snakes with current length
        for (int i = 0; i < snakes.size(); i++) {
            g2d.setColor(snakes.get(i).COLOR);
            g2d.drawString(snakes.get(i).NAME + " (" + snakesLocation.get(i).size() + ")",
                    BoardLogic.SCALE * BoardLogic.MAX_X + 100 + BoardLogic.OFFSET,
                    (25 * (i + 1)) + 75 + BoardLogic.OFFSET);

        }
        // ----------------------------------


        // ----------------------------------
        // Dead Snakes
        // ----------------------------------
        // Draw header
        g2d.setColor(Color.BLACK);
        g2d.drawString("Dead Snakes:", BoardLogic.SCALE * BoardLogic.MAX_X + 300 + BoardLogic.OFFSET,
                50 + BoardLogic.OFFSET);

        // Draw dead snakes with current length
        g2d.setColor(Color.DARK_GRAY);
        for (int i = 0; i < deadSnakesInfo.size(); i++) {
            g2d.drawString(deadSnakesInfo.get(i), BoardLogic.SCALE * BoardLogic.MAX_X + 300 + BoardLogic.OFFSET,
                    (25 * (i + 1)) + 75 + BoardLogic.OFFSET);
        }
        // ----------------------------------
    }


    /**
     * Draw the apples on board
     *
     * @param g2d the Graphics2D, which is needed for painting
     * @param apples list of all apples, which have to paint
     */
    private void paintApples(Graphics2D g2d, List<Field> apples) {
        // set color
        g2d.setColor(Color.red);

        // paint apples
        for (Field apple: apples) {
            g2d.fillOval(apple.getPosX() * BoardLogic.SCALE + BoardLogic.OFFSET,
                    apple.getPosY() * BoardLogic.SCALE + BoardLogic.OFFSET, BoardLogic.SCALE, BoardLogic.SCALE);
        }

    }


    /**
     * Draws the current snakes positions
     *
     * @param g2d the Graphics2D, which is needed for painting
     * @param snakes the snakes in game
     * @param snakesLocation the location of snakes on board
     */
    private void paintSnakes(Graphics2D g2d, List<Snake> snakes, List<LinkedList<Field>> snakesLocation) {
        // iterate all snakes
        for (int i = 0; i < snakesLocation.size(); i++) {

            // set color of the snake
            g2d.setColor(snakes.get(i).COLOR);

            // iterate the whole body of a single snake with index i
            for (int j = 0; j < snakesLocation.get(i).size(); j++) {
                g2d.fillOval(snakesLocation.get(i).get(j).getPosX() * BoardLogic.SCALE + BoardLogic.OFFSET,
                        snakesLocation.get(i).get(j).getPosY() * BoardLogic.SCALE + BoardLogic.OFFSET,
                        BoardLogic.SCALE, BoardLogic.SCALE);
            }
        }
    }


    /**
     * Paint dead snakes as barrier forever (╯▔皿▔)╯
     *
     * @param g2d the Graphics2D, which is needed for painting
     * @param barriers a list of barrier fields
     */
    private void paintBarrier(Graphics2D g2d, List<Field> barriers) {
        // set color
        g2d.setColor(Color.DARK_GRAY);

        // paint barriers
        for (Field barrier: barriers) {
            g2d.fillRect(barrier.getPosX() * BoardLogic.SCALE + BoardLogic.OFFSET,
                    barrier.getPosY() * BoardLogic.SCALE + BoardLogic.OFFSET, BoardLogic.SCALE, BoardLogic.SCALE);
        }
    }
}
