package board;

import snakes.Snake;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class BoardPaint extends JPanel {

    private final BoardLogic boardLogic;


    public BoardPaint(BoardLogic boardLogic) {
        this.boardLogic = boardLogic;
    }


    /**
     * paints everything
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        this.paintNames(g2d, boardLogic.getSnakes(), boardLogic.getSnakesLocation(), boardLogic.getDeadSnakesInfo());
        this.paintBoard(g2d);
        this.paintApples(g2d, boardLogic.getApples()); // paint Apple
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
                g2d.drawRect(x * BoardLogic.MAX_X, y * BoardLogic.MAX_Y, BoardLogic.SCALE, BoardLogic.SCALE);
            }
        }
        g2d.drawRect(BoardLogic.MAX_X, BoardLogic.MAX_Y, BoardLogic.SCALE, BoardLogic.SCALE);
    }


    /**
     * Draw names of living and dead snakes on the right side of the board
     *
     * @param g2d the Graphics2D, which is needed for painting
     * @param snakes the snakes in game
     * @param snakesLocation the location of snakes on board
     * @param deadSnakesInfo the names and lenght of dead snakes
     */
    private void paintNames(Graphics2D g2d, List<Snake> snakes, List<LinkedList<Field>> snakesLocation, List<String> deadSnakesInfo) {
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 14f));

        // Living Snakes
        g2d.setColor(Color.BLACK);
        g2d.drawString("Living Snakes:", BoardLogic.SCALE * BoardLogic.SCALE + 100,50);

        for (int i = 0; i < snakes.size(); i++) {
            g2d.setColor(snakes.get(i).COLOR);
            g2d.drawString(snakes.get(i).NAME + " (" + snakesLocation.get(i).size() + ")",
                    BoardLogic.SCALE * BoardLogic.SCALE + 100,(50 * (i + 1)) + 50);

        }

        // Dead Snakes
        g2d.setColor(Color.BLACK);
        g2d.drawString("Dead Snakes:", BoardLogic.SCALE * BoardLogic.SCALE + 300,50);

        g2d.setColor(Color.DARK_GRAY);
        for (int i = 0; i < deadSnakesInfo.size(); i++) {
            g2d.drawString(deadSnakesInfo.get(i), BoardLogic.SCALE * BoardLogic.SCALE + 300,(50 * (i + 1)) + 50);
        }

    }


    /**
     * Draw the apples on board
     *
     * @param g2d the Graphics2D, which is needed for painting
     * @param apples list of all apples, which have to paint
     */
    private void paintApples(Graphics2D g2d, List<Field> apples) {
        g2d.setColor(Color.red);
        for (Field apple: apples) {
            g2d.fillOval(apple.getPosX() * BoardLogic.MAX_X, apple.getPosY() * BoardLogic.MAX_Y, BoardLogic.SCALE, BoardLogic.SCALE);
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

        for (int i = 0; i < snakesLocation.size(); i++) {
            g2d.setColor(snakes.get(i).COLOR);

            for (int j = 0; j < snakesLocation.get(i).size(); j++) {
                g2d.fillOval(snakesLocation.get(i).get(j).getPosX() * BoardLogic.MAX_X,
                        snakesLocation.get(i).get(j).getPosY() * BoardLogic.MAX_Y, BoardLogic.SCALE, BoardLogic.SCALE);
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
        g2d.setColor(Color.DARK_GRAY);

        for (Field barrier: barriers) {
            g2d.fillRect(barrier.getPosX() * BoardLogic.MAX_X,
                    barrier.getPosY() * BoardLogic.MAX_Y, BoardLogic.SCALE, BoardLogic.SCALE);
        }
    }
}
