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
    public static int SIZE_X;
    public static int SIZE_Y;
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

    private final Random rand = new Random();

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
        BoardLogic.SIZE_X = Integer.parseInt(cfgMap.get("SIZE_X"));
        BoardLogic.SIZE_Y = Integer.parseInt(cfgMap.get("SIZE_Y"));
        BoardLogic.MAX_APPLES_ON_BOARD = Integer.parseInt(cfgMap.get("MAX_APPLES_ON_BOARD"));
        BoardLogic.OFFSET = Integer.parseInt(cfgMap.get("OFFSET"));
        BoardLogic.START_LENGTH = Integer.parseInt(cfgMap.get("START_LENGTH"));
        BoardLogic.CALC_TIME = Integer.parseInt(cfgMap.get("CALC_TIME"));
        // -------------------------------------------------


        // create an empty board with empty fields ---------
        fields = new Field[BoardLogic.SIZE_X][BoardLogic.SIZE_Y];
        for (int i = 0; i < BoardLogic.SIZE_X; i++) {
            for (int j = 0; j < BoardLogic.SIZE_Y; j++) {
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

                // Then check if this random field an the num of START_LENGTH left fields are free.
                isFree = true;
                for (int i = 0; i < BoardLogic.START_LENGTH; i++) {
                    if (((random.getPosX() + BoardLogic.START_LENGTH) > BoardLogic.SIZE_X) || !fields[random.getPosX() + i][random.getPosY()].isFree()) {

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
        // or no more apples are available
        if ((snakes.size() <= 1) && (game.end(game)) || apples.size() == 0) {
            game.pausePlay();
            System.out.println("The game has ended!");
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

            // check if we are able to place new apples before the snake moves
            setApples();

            // ---------------------------------------------------
            // Call think() method of ith snake and give her CALC_TIME to calculate
            // ---------------------------------------------------
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(() -> "Ready");

            try {
                direction = this.snakes.get(i).think(new BoardInfo(i, fields.clone(), new ArrayList<>(snakesLocation),
                        new ArrayList<>(apples), new ArrayList<>(barriers)));
                future.get(BoardLogic.CALC_TIME, TimeUnit.MILLISECONDS);

            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println("Snake " + snakes.get(i).NAME + " calculated too long!\n");
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


            // check if snake is allowed to move in this direction
            // and if true, set new value of newX or newY; else kill snake --
            if ((direction == Snake.LEFT) && (newX > 0)) {
                --newX;

            } else if ((direction == Snake.UP) && (newY > 0)) {
                --newY;

            } else if ((direction == Snake.RIGHT) && (newX < (SIZE_X - 1))) {
                ++newX;

            } else if ((direction == Snake.DOWN) && (newY < (SIZE_Y - 1))) {
                ++newY;

            } else {
                System.out.println("Snake " + this.snakes.get(i).NAME + " returned no correct direction " +
                        "or drove into a border");
                killSnake(i);
                i--;
                continue;
            }
            // --------------------------------------------------------------


            // check if snake run in another snake/barrier/outside or in an apple --
            boolean ate = false;
            if (!this.fields[newX][newY].isFree()) {

                // check edge case if a snake runs in its own tail
                if ((newX != snakesLocation.get(i).getFirst().getPosX())
                        || (newY != snakesLocation.get(i).getFirst().getPosY())) {
                    killSnake(i);
                    i--;
                    continue;
                }


            } else if (this.fields[newX][newY].getState() == FieldState.Apple) {
                if (!removeApple(newX, newY, FieldState.Snake)) {
                    System.out.println("Snake " + this.snakes.get(i).NAME
                            + " tried to eat an apple that doesn't exist at " + newX + ", " + newY + "!");
                }
                ate = true;
            }
            // ---------------------------------------------------------------------


            // set Fields
            int oldX = this.snakesLocation.get(i).getFirst().getPosX();
            int oldY = this.snakesLocation.get(i).getFirst().getPosY();

            this.fields[newX][newY].setState(FieldState.Snake);


            // move snake
            this.snakesLocation.get(i).addLast(new Field(newX, newY));


            // dont remove last part of body, if snake ate an apple
            if (!ate) {
                this.snakesLocation.get(i).removeFirst();
                this.fields[oldX][oldY].setState(FieldState.Empty);
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

        // check if all existing apples are still valid
        // do it via an auxiliary list to avoid concurrent modification exception
        Set<Field> validAppleFields = getValidAppleFields(true);
        List<Field> removedApples = new ArrayList<>();
        for (Field appleField : apples) {
            if (!validAppleFields.contains(appleField)) {
                removedApples.add(appleField);
            }
        }
        for (Field appleField : removedApples) {
            removeApple(appleField.getPosX(), appleField.getPosY(), FieldState.Empty);
        }
    }


    /**
     * Remove an apple on the board and place another
     *
     * @param x x-Coordinate of apple
     * @param y y-Coordinate of apple
     * @param replacementState the state the field should have after removing
     * @return if an apple was removed
     */
    protected boolean removeApple(int x, int y, FieldState replacementState) {
        boolean removed = apples.removeIf(appleField -> (appleField.getPosX() == x) && (appleField.getPosY() == y));
        if (removed) {
            this.fields[x][y].setState(replacementState);
        }
        return removed;
    }


    /**
     * Place apples on the board until MAX_APPLES_ON_BOARD value is reached
     */
    protected void setApples() {
        // only check further if there are apples missing
        if (apples.size() < MAX_APPLES_ON_BOARD) {

            // get all valid fields
            List<Field> validFields = new ArrayList<>(getValidAppleFields(false));

            // add apples until value of MAX_APPLES_ON_BOARD is reached
            while (apples.size() < MAX_APPLES_ON_BOARD && validFields.size() > 0) {

                // get random valid field
                Field appleField = validFields.get(rand.nextInt(validFields.size()));
                validFields.remove(appleField);

                // set field state of random field to value apple and add apple to apple-list
                appleField.setState(FieldState.Apple);
                apples.add(appleField);
            }
        }
    }


    /**
     * Generate a set of all valid apple fields.
     * A field is valid if every snake head can reach it.
     *
     * @param includeApples if apples should be considered a valid Field
     * @return a set containing all fields where an apple could be reached by a snake
     */
    protected Set<Field> getValidAppleFields(boolean includeApples) {

        // collect all snake heads as start points and save them in checkFields
        Deque<Field> checkFields = new ArrayDeque<>();
        for (LinkedList<Field> snake : snakesLocation) {
            checkFields.add(snake.getLast());
        }

        // all valid fields will saved in this list
        Set<Field> validFields = new HashSet<>();

        // iterate through checkFields until it's empty.
        while (!checkFields.isEmpty()) {

            // remove last element of checkFields at start of every iteration.
            // this field is now called iteration field
            Field field = checkFields.removeLast();

            // checks which directions can be reached from the iteration field.
            // when a direction can be reached, it will added to checkFields.
            for (int i = 0; i < 4; i++) {
                Field tempField = null;
                boolean validDir = false;

                // checks for all four directions whether you would bump to the edge of the board
                // if true, they will stored in tempField
                switch (i) {
                    // UP
                    case 0 -> {
                        if (field.getPosY() > 0) {
                            validDir = true;
                            tempField = getFields()[field.getPosX()][field.getPosY() - 1];
                        }
                    }
                    // DOWN
                    case 1 -> {
                        if (field.getPosY() < SIZE_Y - 1) {
                            validDir = true;
                            tempField = getFields()[field.getPosX()][field.getPosY() + 1];
                        }
                    }
                    // LEFT
                    case 2 -> {
                        if (field.getPosX() > 0) {
                            validDir = true;
                            tempField = getFields()[field.getPosX() - 1][field.getPosY()];
                        }
                    }
                    // RIGHT
                    case 3 -> {
                        if (field.getPosX() < SIZE_X - 1) {
                            validDir = true;
                            tempField = getFields()[field.getPosX() + 1][field.getPosY()];
                        }
                    }
                }

                // If a field from tempField is not already in validFields
                // and either the field is empty or (includeApples must be true) there is an apple on the field,
                // then the field is added to validFields and checkFields
                if (validDir  && !validFields.contains(tempField)
                        && (tempField.getState() == FieldState.Empty
                        || includeApples && tempField.getState() == FieldState.Apple)) {
                    validFields.add(tempField);
                    checkFields.add(tempField);
                }
            }
        }

        // visualization for debugging!
        /*int[][] debug = new int[SIZE_X][SIZE_Y];
        for (Field f : validFields) {
            debug[f.getPosX()][f.getPosY()] = 1;
        }
        for (int y = 0; y < SIZE_Y; y++) {
            for (int x = 0; x < SIZE_X; x++) {
                System.out.print(debug[x][y] + "\t");
            }
            System.out.println();
        }
        System.out.println();*/

        return validFields;
    }

    /**
     * Returns a random Field on the board
     *
     * @return a random Field on the board
     */
    protected Field getRandomField() {
        int x = rand.nextInt(SIZE_X);
        int y = rand.nextInt(SIZE_Y);

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
