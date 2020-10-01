package board;

import snakes.*;
import game.Game;

import java.util.*;
import java.util.List;

/**
 * This class represents the Arena with the snakes
 */
public class BoardLogic {

    public static int SCALE;
    public static int MAX_X;
    public static int MAX_Y;
    public static int MAX_APPLES_ON_BOARD;
    public static int OFFSET;

    private final Field[][] fields;
    private final List<Field> apples = new ArrayList<>();
    private final List<LinkedList<Field>> snakesLocation = new ArrayList<>();
    private final List<Snake> snakes = new ArrayList<>();
    private final List<Field> barriers = new ArrayList<>();
    private final List<String> deadSnakesInfo = new ArrayList<>();

    private final BoardPaint bPaint;
    private final Game game;


    /**
     * Create Board with the start positions of the snakes
     */
    public BoardLogic(Game game, List<Snake> snakes,
                      int SCALE, int MAX_X, int MAX_Y, int OFFSET, int MAX_APPLES_ON_BOARD) {
        this.bPaint = new BoardPaint(this);
        this.game = game;
        BoardLogic.SCALE = SCALE;
        BoardLogic.MAX_X = MAX_X;
        BoardLogic.MAX_Y = MAX_Y;
        BoardLogic.MAX_APPLES_ON_BOARD = MAX_APPLES_ON_BOARD;
        BoardLogic.OFFSET = OFFSET;

        // create an empty board with empty fields
        fields = new Field[BoardLogic.MAX_X][BoardLogic.MAX_Y];
        for (int i = 0; i < BoardLogic.MAX_X; i++) {
            for (int j = 0; j < BoardLogic.MAX_Y; j++) {
                fields[i][j] = new Field(i, j);

            }
        }

        // set all startpoints of all snakes in board:
        for (Snake snake : snakes) {
            Field random;

            do {
                random = getRandomField();

            } while (
                !(
                ((random.getPosX() + 2) < BoardLogic.MAX_X)
                && fields[random.getPosX()    ][random.getPosY()].isFree()
                && fields[random.getPosX() + 1][random.getPosY()].isFree()
                && fields[random.getPosX() + 2][random.getPosY()].isFree())
            );

            fields[random.getPosX()    ][random.getPosY()].setFree(false);
            fields[random.getPosX() + 1][random.getPosY()].setFree(false);
            fields[random.getPosX() + 2][random.getPosY()].setFree(false);

            var snakeLocation = new LinkedList<Field>();
            snakeLocation.addLast(new Field(random.getPosX()    , random.getPosY()));
            snakeLocation.addLast(new Field(random.getPosX() + 1, random.getPosY()));
            snakeLocation.addLast(new Field(random.getPosX() + 2, random.getPosY()));

            this.snakes.add(snake);
            this.snakesLocation.add(snakeLocation);
        }

        // set the apples
        this.setApples();
    }

    public void update() {
        if (snakes.size() <= 1) {
            if(!game.end(game)) {
                moveSnakes();
            }

        } else {
            moveSnakes();

        }
    }


    /**
     * Moves the snakes over the board.
     */
    protected void moveSnakes() {
        int direction;
        int newX;
        int newY;

        for (int i = 0; i < this.snakes.size(); i++) {

            // ---------------------------------------------------
            // Call ith snake
            // ---------------------------------------------------
            direction = this.snakes.get(i).think(new BoardInfo(i, fields, snakesLocation, apples, barriers));
            // ---------------------------------------------------

            newX = this.snakesLocation.get(i).getLast().getPosX();
            newY = this.snakesLocation.get(i).getLast().getPosY();

            // check if snake is allowed to move in this direction
            if ((direction == Snake.LEFT) && (newX > 0)) {
                --newX;

            } else if ((direction == Snake.UP) && (newY > 0)) {
                --newY;

            } else if ((direction == Snake.RIGHT) && (newX < (MAX_X - 1))) {
                ++newX;

            } else if ((direction == Snake.DOWN) && (newY < (MAX_Y - 1))) {
                ++newY;

            } else {
                System.out.println("snakes.Snake " + this.snakes.get(i).NAME + " returns no correct direction " +
                        "or drive in a border");
                killSnake(i);
                i--;
                continue;
            }

            // check if snake run in another snake/barrier or in an apple TODO: define better field status
            boolean ate = false;
            if (!this.fields[newX][newY].isFree()) {
                killSnake(i);
                i--;
                continue;

            } else if (this.fields[newX][newY].isApple()) {
                removeApple(newX, newY);
                ate = true;
            }


            // set Fields -> TODO: make better
            int oldX = this.snakesLocation.get(i).getFirst().getPosX();
            int oldY = this.snakesLocation.get(i).getFirst().getPosY();

            this.fields[newX][newY].setFree(false);
            this.fields[oldX][oldY].setFree(true);

            // move snake
            this.snakesLocation.get(i).addLast(new Field(newX, newY));

            if (!ate) {
                this.snakesLocation.get(i).removeFirst();
            }
        }
    }


    /**
     * Use epic kame hame ha power to kill a snake and turn it to a barrier
     *
     * @param snakeIndex snake to be killed
     */
    protected void killSnake(int snakeIndex) {
        this.barriers.addAll(this.snakesLocation.get(snakeIndex));
        this.deadSnakesInfo.add(snakes.get(snakeIndex).NAME + " (" + snakesLocation.get(snakeIndex).size() + ")");

        this.snakesLocation.remove(snakeIndex);
        this.snakes.remove(snakeIndex);

    }


    /**
     * remove an apple on the board and place another
     *
     * @param x x-Coordinate of apple
     * @param y y-Coordinate of apple
     * @return if an apple was removed
     */
    protected boolean removeApple(int x, int y) {
        for (Field appleField : apples) {
            if ((appleField.getPosX() == x) && (appleField.getPosY() == y)) {

                // no .setFree here, because a snake will be on this field TODO false
                //fields[appleField.getPosX()][appleField.getPosY()].setFree(false);
                fields[appleField.getPosX()][appleField.getPosY()].setApple(false);
                apples.remove(appleField);
                this.setApples();

                return true;
            }
        }

        return false;
    }


    /**
     * place apples on the board until MAX_APPLES_ON_BOARD value is reached
     */
    protected void setApples() {
        while (apples.size() < MAX_APPLES_ON_BOARD) {
            Field appleField;

            do {
                appleField = getRandomField();

            } while (!fields[appleField.getPosX()][appleField.getPosY()].isFree());

            //fields[appleField.getPosX()][appleField.getPosY()].setFree(false);
            fields[appleField.getPosX()][appleField.getPosY()].setApple(true);
            apples.add(appleField);
        }
    }


    /**
     * Returns a random Field on the board
     *
     * @return a random Field on the board
     */
    protected Field getRandomField() {
        int x = (int) (Math.random() * MAX_X);
        int y = (int) (Math.random() * MAX_Y);

        return new Field(x, y);
    }



    // ---------------------------------------------------
    // Getter-Methods
    // ---------------------------------------------------
    protected Field[][] getFields() {
        return fields;
    }

    protected List<Field> getApples() {
        return apples;
    }

    protected List<LinkedList<Field>> getSnakesLocation() {
        return snakesLocation;
    }

    protected List<Snake> getSnakes() {
        return snakes;
    }

    protected List<Field> getBarrier() {
        return barriers;
    }

    protected List<String> getDeadSnakesInfo() {
        return deadSnakesInfo;
    }

    public BoardPaint getBPaint() {
        return bPaint;
    }
    // ---------------------------------------------------
}

