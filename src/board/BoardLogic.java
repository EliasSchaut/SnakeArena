package board;

import snakes.*;
import game.Game;

import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class represents the logic of the arena with the snakes.
 * The update() method will move all snakes in the direction of their return value of the think-method
 */
public class BoardLogic {

    public static int SCALE;
    public static int MAX_X;
    public static int MAX_Y;
    public static int MAX_APPLES_ON_BOARD;
    public static int OFFSET;
    public static int START_LENGTH;
    public static int CALC_TIME;

    private final Field[][] fields;
    private final List<Field> apples = new ArrayList<>();
    private final List<LinkedList<Field>> snakesLocation = new ArrayList<>();
    private final List<Snake> snakes = new ArrayList<>();
    private final List<Field> barriers = new ArrayList<>();
    private final List<String> deadSnakesInfo = new ArrayList<>();

    private final BoardPaint bPaint;
    private final Game game;


    /**
     * The Constructor.
     * First of all it will set all important values from the configs.
     * Then it will create the board.
     * At least it set the start positions of the snakes and apples.
     *
     * @param game The Game
     * @param snakes All snakes in game
     * @param cfgMap All configs
     */
    public BoardLogic(Game game, List<Snake> snakes, Map<String, String> cfgMap) {

        // set important values ----------------------------
        this.bPaint = new BoardPaint(this);
        this.game = game;
        BoardLogic.SCALE = Integer.parseInt(cfgMap.get("SCALE"));
        BoardLogic.MAX_X = Integer.parseInt(cfgMap.get("MAX_X"));
        BoardLogic.MAX_Y = Integer.parseInt(cfgMap.get("MAX_Y"));
        BoardLogic.MAX_APPLES_ON_BOARD = Integer.parseInt(cfgMap.get("MAX_APPLES_ON_BOARD"));
        BoardLogic.OFFSET = Integer.parseInt(cfgMap.get("OFFSET"));
        BoardLogic.START_LENGTH = Integer.parseInt(cfgMap.get("START_LENGTH"));
        BoardLogic.CALC_TIME = Integer.parseInt(cfgMap.get("calc_time"));
        // -------------------------------------------------


        // create an empty board with empty fields ---------
        fields = new Field[BoardLogic.MAX_X][BoardLogic.MAX_Y];
        for (int i = 0; i < BoardLogic.MAX_X; i++) {
            for (int j = 0; j < BoardLogic.MAX_Y; j++) {
                fields[i][j] = new Field(i, j);

            }
        }
        // -------------------------------------------------


        // set all start points of all snakes in board ------
        for (Snake snake : snakes) {
            Field random;

            boolean isFree;
            do {
                // First get Field on random position
                random = getRandomField();

                // Then check if this random field an the num of BoardLogic.START_LENGTH left fields are free.
                isFree = true;
                for (int i = 0; i < BoardLogic.START_LENGTH; i++) {
                    if (((random.getPosX() + 2) >= BoardLogic.MAX_X) || !fields[random.getPosX() + i][random.getPosY()].isFree()) {

                        isFree = false;
                        break;
                    }
                }

            // If the fields are free, take this fields for this snake; else restart
            } while (!isFree);

            // give the selected Fields the snake status
            for (int i = 0; i < BoardLogic.START_LENGTH; i++) {
                fields[random.getPosX() + i][random.getPosY()].setState(FieldState.Snake);
            }

            // save selected Fields to Body of Snake
            var snakeLocation = new LinkedList<Field>();
            for (int i = 0; i < BoardLogic.START_LENGTH; i++) {
                snakeLocation.addLast(new Field(random.getPosX() + i, random.getPosY()));
            }

            // add everything together
            this.snakes.add(snake);
            this.snakesLocation.add(snakeLocation);
        }
        // -------------------------------------------------


        // set the apples
        this.setApples();
    }


    /**
     * Update the whole logic.
     * Move all snakes in the direction of their return value of the think-method and save/set the new values
     */
    public void update() {
        // end game if only one snake remains and config value stop_game is true
        if ((snakes.size() <= 1) && (game.end(game))) {
            return;
        }

        // move all snakes
        moveSnakes();
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
            // Call think() method of ith snake and give her CALC_TIME to calculate
            // ---------------------------------------------------
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(() -> "Ready");

            try {
                direction = this.snakes.get(i).think(new BoardInfo(i, fields.clone(), snakesLocation, apples, barriers));
                future.get(BoardLogic.CALC_TIME, TimeUnit.MILLISECONDS);

            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println("Snake " + snakes.get(i).NAME + " calculate to long!\n");
                direction = Snake.RIGHT;

            } catch (Exception e) {
                System.out.println("An error occurred in Snake " + snakes.get(i).NAME + "!");
                e.printStackTrace();
                direction = Snake.RIGHT;

            }

            executor.shutdownNow();
            // ---------------------------------------------------


            newX = this.snakesLocation.get(i).getLast().getPosX();
            newY = this.snakesLocation.get(i).getLast().getPosY();


            // check if snake is allowed to move in this direction --
            if ((direction == Snake.LEFT) && (newX > 0)) {
                --newX;

            } else if ((direction == Snake.UP) && (newY > 0)) {
                --newY;

            } else if ((direction == Snake.RIGHT) && (newX < (MAX_X - 1))) {
                ++newX;

            } else if ((direction == Snake.DOWN) && (newY < (MAX_Y - 1))) {
                ++newY;

            } else {
                System.out.println("Snake " + this.snakes.get(i).NAME + " returns no correct direction " +
                        "or drive in a border");
                killSnake(i);
                i--;
                continue;
            }
            // ---------------------------------------------------


            // check if snake run in another snake/barrier/outside or in an apple --
            boolean ate = false;
            if (!this.fields[newX][newY].isFree()) {
                killSnake(i);
                i--;
                continue;

            } else if (this.fields[newX][newY].getState() == FieldState.Apple) {
                removeApple(newX, newY);
                ate = true;
            }
            // ---------------------------------------------------------------------


            // set Fields
            int oldX = this.snakesLocation.get(i).getFirst().getPosX();
            int oldY = this.snakesLocation.get(i).getFirst().getPosY();

            this.fields[newX][newY].setState(FieldState.Snake);
            this.fields[oldX][oldY].setState(FieldState.Empty);


            // move snake
            this.snakesLocation.get(i).addLast(new Field(newX, newY));


            // dont remove last part of body, if snake ate an apple
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
        // add snake body to barrier list and set field status of fields to value barrier
        this.barriers.addAll(this.snakesLocation.get(snakeIndex));
        for (Field snakeLoc : snakesLocation.get(snakeIndex)) {
            this.fields[snakeLoc.getPosX()][snakeLoc.getPosY()].setState(FieldState.Barrier);
        }

        // add dead snake info
        this.deadSnakesInfo.add(snakes.get(snakeIndex).NAME + " (" + snakesLocation.get(snakeIndex).size() + ")");

        // remove snake from all living snake lists
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

            // find apple with given coordinates and remove it from the list
            if ((appleField.getPosX() == x) && (appleField.getPosY() == y)) {
                fields[appleField.getPosX()][appleField.getPosY()].setState(FieldState.Snake);
                apples.remove(appleField);

                // add new apples
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

        // add apples until value of MAX_APPLES_ON_BOARD is reached
        while (apples.size() < MAX_APPLES_ON_BOARD) {

            // get random field with field status empty
            Field appleField;
            do {
                appleField = getRandomField();

            } while (!(fields[appleField.getPosX()][appleField.getPosY()].getState() == FieldState.Empty));

            // set field state of random field to value apple and add apple to apple-list
            fields[appleField.getPosX()][appleField.getPosY()].setState(FieldState.Apple);
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

