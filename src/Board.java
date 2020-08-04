import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedList;

/**
 * This class represents the Arena with the snakes
 *
 * TODO: Snake move
 */
public class Board extends JPanel {

    private static final int MAX_X = 20;
    private static final int MAX_Y = 20;
    private static final int MAX_APPLES_ON_BOARD = 2;

    private final Field[][] fields = new Field[MAX_X][MAX_Y];
    private final LinkedList<Field> apples = new LinkedList<>();
    private LinkedList<LinkedList<Field>> snakesLocation = new LinkedList<>();
    private LinkedList<Snake> snakes = new LinkedList<>();

    private Game game;


    /**
     * Create Board
     */
    public Board(Game game, Snake[] snakes) {
        this.game = game;

        // declare Board
        for (int i = 0; i < MAX_X; i++) {
            for (int j = 0; j < MAX_Y; j++) {
                fields[i][j] = new Field(i, j);

            }
        }

        // snakes beginn:
        for (int i = 0; i < snakes.length; i++) {
            Field random;

            do {
                random = getRandomField();

            } while (
                    !(
                    ((random.getPosX() + 2) < MAX_X)
                    && fields[random.getPosX() + 0][random.getPosY()].isFree()
                    && fields[random.getPosX() + 1][random.getPosY()].isFree()
                    && fields[random.getPosX() + 2][random.getPosY()].isFree())
            );

            fields[random.getPosX() + 0][random.getPosY()].setFree(false);
            fields[random.getPosX() + 1][random.getPosY()].setFree(false);
            fields[random.getPosX() + 2][random.getPosY()].setFree(false);

            LinkedList<Field> snake = new LinkedList<>();
            snake.addLast(new Field(random.getPosX() + 0, random.getPosY()));
            snake.addLast(new Field(random.getPosX() + 1, random.getPosY()));
            snake.addLast(new Field(random.getPosX() + 2, random.getPosY()));

            this.snakes.add(snakes[i]);
            this.snakesLocation.add(snake);
        }
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

        this.paintBoard(g2d);
        this.setApple(g2d);

        this.moveSnakes();

        this.paintSnakes(g2d);
    }


    /**
     * Draws the grid board
     *
     * @param g
     */
    private void paintBoard(Graphics2D g2d) {

        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                g2d.drawRect(x * MAX_X, y * MAX_Y, MAX_X, MAX_Y);
            }
        }
        g2d.drawRect(MAX_X, MAX_Y, MAX_X, MAX_Y);
    }

    /**
     * Draw an apple
     *
     * @param g2d
     * @param x
     * @param y
     */
    private void paintApple(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.red);

        g2d.fillOval(x * MAX_X, y * MAX_Y, MAX_X, MAX_Y);
    }


    /**
     * Draws the current snake position
     *
     * @param g2d
     */
    private void paintSnakes(Graphics2D g2d) {

        for (int i = 0; i < this.snakesLocation.size(); i++) {
            g2d.setColor(this.snakes.get(i).COLOR);

            for (int j = 0; j < this.snakesLocation.get(i).size(); j++) {
                g2d.fillOval(this.snakesLocation.get(i).get(j).getPosX() * MAX_X,
                        this.snakesLocation.get(i).get(j).getPosY() * MAX_Y, MAX_X, MAX_Y);
            }
        }
    }


    /**
     * Moves the snakes over the board
     */
    private void moveSnakes() {
        int direction;
        int newX;
        int newY;

        for (int i = 0; i < this.snakes.size(); i++) {
            direction = this.snakes.get(i).think(this);
            newX = this.snakesLocation.get(i).getLast().getPosX();
            newY = this.snakesLocation.get(i).getLast().getPosY();

            if ((direction == Snake.LEFT) && (newX > 0)) {
                newX = --newX;

            } else if ((direction == Snake.UP) && (newY > 0)) {
                newY = --newY;

            } else if ((direction == Snake.RIGHT) && (newX < (MAX_X - 1))) {
                newX = ++newX;

            } else if ((direction == Snake.DOWN) && (newY < (MAX_Y - 1))) {
                newY = ++newY;

            } else {
                System.out.println("Snake " + this.snakes.get(i).NAME + " returns no correct direction " +
                        "or drive in a border");
                // TODO: kill snake

            }

            this.snakesLocation.get(i).addLast(new Field(newX, newY));
            this.snakesLocation.get(i).removeFirst();

        }
    }


    /**
     * remove an apple on the board and place another
     *
     * @param g2d
     * @param x x-Coordinate of apple
     * @param y y-Coordinate of apple
     * @return if an apple was removed
     */
    private boolean removeApple(Graphics2D g2d, int x, int y) {
        for (Field appleField : apples) {
            if ((appleField.getPosX() == x) && (appleField.getPosY() == y)) {

                // no .setFree here, because a snake will be on this field
                fields[appleField.getPosX()][appleField.getPosY()].setApple(false);
                apples.remove(appleField);

                // no this.setApple here, because setApple will create new Apples automatic,
                // if the list apples is les then MAX_APPLES_ON_BOARD

                return true;
            }
        }

        return false;
    }


    /**
     * place an apple on the board
     *
     * @param g2d
     */
    private void setApple(Graphics2D g2d) {
        while (apples.size() < MAX_APPLES_ON_BOARD) {
            Field appleField;

            do {
                appleField = getRandomField();

            } while (!fields[appleField.getPosX()][appleField.getPosY()].isFree());

            fields[appleField.getPosX()][appleField.getPosY()].setFree(false);
            fields[appleField.getPosX()][appleField.getPosY()].setApple(true);
            apples.add(appleField);
        }


        // paint apples
        for (int i = 0; i < apples.size(); i++) {
            this.paintApple(g2d, apples.get(i).getPosX(), apples.get(i).getPosY());
        }
    }


    /**
     * Returns a random Field on the Board
     *
     * @return a random Field on the Board
     */
    private Field getRandomField() {
        int x = (int) (Math.random() * MAX_X);
        int y = (int) (Math.random() * MAX_Y);

        return new Field(x, y);
    }
}

