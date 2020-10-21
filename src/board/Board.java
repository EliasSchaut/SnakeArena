package board;

import snakes.*;
import io.Game;

import javax.swing.JPanel;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents the Arena with the snakes
 */
public class Board extends JPanel {

    public static final int SCALE = 20;
    protected static final int MAX_X = 20;
    protected static final int MAX_Y = 20;
    protected static final int MAX_APPLES_ON_BOARD = 2;

    private final Field[][] fields = new Field[MAX_X][MAX_Y];
    private final List<Position> apples = new ArrayList<>();
    private final List<LinkedList<Position>> snakesLocation = new ArrayList<>();
    private final List<Snake> snakes = new ArrayList<>();
    private final List<Position> barrier = new ArrayList<>();

    private Game game;
    private int startCounter;

    /**
     * Create Board.Board
     */
    public Board(Game game, Snake[] snakes) {
        this.game = game;

        initFields();

        // set all starpoints of all snakes:
        for (int i = 0; i < snakes.length; i++) {
            Position random;

            do {
                random = getRandomField();

            } while (!(((random.getX() + 2) < MAX_X) && fields[random.getX() + 0][random.getY()].isFree()
                    && fields[random.getX() + 1][random.getY()].isFree()
                    && fields[random.getX() + 2][random.getY()].isFree()));

            fields[random.getX() + 0][random.getY()].setFree(false);
            fields[random.getX() + 1][random.getY()].setFree(false);
            fields[random.getX() + 2][random.getY()].setFree(false);

            var snake = new LinkedList<Position>();
            snake.addLast(new Position(random.getX() + 0, random.getY()));
            snake.addLast(new Position(random.getX() + 1, random.getY()));
            snake.addLast(new Position(random.getX() + 2, random.getY()));

            this.snakes.add(snakes[i]);
            this.snakesLocation.add(snake);
            startCounter = 2;
        }
    }

    /**
     * Deserialize Board from String
     * 
     * @param lines the lines read from stdin
     */
    public Board(List<String> lines) {
        initFields();
        var new_snakes = new HashMap<Integer, LinkedList<Position>>();
        for (int i = 1; i < lines.size(); i++) {
            var cells = lines.get(i).trim().split(" ");
            for (int j = 0; j < cells.length; j++) {
                if (cells[j].equals("O")) {
                    this.fields[i][j].setApple(true);
                } else if (!cells[j].equals("#")) {
                    int id = Integer.parseInt(cells[j]);
                    int aid = Math.abs(id) - 1;
                    if (!new_snakes.containsKey(aid)) {
                        new_snakes.put(aid, new LinkedList<Position>());
                    }
                    var list = new_snakes.get(aid);
                    this.fields[i][j].setFree(false);
                    if (id < 0) {
                        list.addLast(new Position(i, j));
                    } else {
                        list.addFirst(new Position(i, j));
                    }
                }
            }
        }
        new_snakes.keySet().stream().forEachOrdered(id -> this.snakesLocation.add(new_snakes.get(id)));
    }

    /**
     * Serialize Board to String
     */
    public String serialize() {
        var builder = new StringBuilder();
        var snakes = new int[MAX_X][MAX_Y];
        for (int i = 0; i < this.snakes.size(); i++) {
            for (var pos : this.snakesLocation.get(i)) {
                snakes[pos.getX()][pos.getY()] = i + 1;
            }
            var pos = snakesLocation.get(i).getLast();
            snakes[pos.getX()][pos.getY()] = -i - 1;
        }

        for (int i = 0; i < MAX_X; i++) {
            for (int j = 0; j < MAX_Y; j++) {
                if (this.fields[i][j].isApple()) {
                    builder.append("O ");
                } else if (snakes[i][j] != 0) {
                    builder.append(snakes[i][j] + " ");
                } else {
                    builder.append("# ");
                }
            }
            builder.append('\n');
        }

        return builder.toString();
    }


    private void initFields() {
        // set all Fields an Board
        for (int i = 0; i < MAX_X; i++) {
            for (int j = 0; j < MAX_Y; j++) {
                fields[i][j] = new Field();

            }
        }
    }
    /**
     * paints everything
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        this.paintNames(g2d);
        this.paintBoard(g2d);
        this.setApple(g2d);
        this.paintBarrier(g2d);

        // Prevent the first two unwant starts
        if (startCounter > 0) {
            this.paintSnakes(g2d);
            startCounter--;
            return;

        }

        // check if game is finish
        if (snakes.size() <= 1) {
            game.isRunning = false;
            this.paintSnakes(g2d);
            return;

        } else {
            this.moveSnakes(g2d);
            this.paintSnakes(g2d);
        }

    }

    /**
     * Draws the grid board
     *
     * @param g2d
     */
    private void paintBoard(Graphics2D g2d) {

        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                g2d.drawRect(x * MAX_X, y * MAX_Y, SCALE, SCALE);
            }
        }
        g2d.drawRect(MAX_X, MAX_Y, SCALE, SCALE);
    }

    /**
     * Draw names of living snakes on the right side of the board
     *
     * @param g2d
     */
    private void paintNames(Graphics2D g2d) {
        for (int i = 0; i < this.snakes.size(); i++) {
            g2d.setColor(snakes.get(i).COLOR);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 14f));
            g2d.drawString(snakes.get(i).NAME, SCALE * SCALE + 100, (50 * i) + 100);

        }
    }

    /**
     * Draw an apple on board
     *
     * @param g2d
     * @param x   x-coordinate of the to drawn apple
     * @param y   y-coordinate of the to drawn apple
     */
    private void paintApple(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.red);

        g2d.fillOval(x * MAX_X, y * MAX_Y, SCALE, SCALE);
    }

    /**
     * Draws the current snake positions
     *
     * @param g2d
     */
    private void paintSnakes(Graphics2D g2d) {

        for (int i = 0; i < this.snakesLocation.size(); i++) {
            g2d.setColor(this.snakes.get(i).COLOR);

            for (int j = 0; j < this.snakesLocation.get(i).size(); j++) {
                g2d.fillOval(this.snakesLocation.get(i).get(j).getX() * MAX_X,
                        this.snakesLocation.get(i).get(j).getY() * MAX_Y, SCALE, SCALE);
            }
        }
    }

    /**
     * Paint dead snakes as barrier forever (╯▔皿▔)╯
     *
     * @param g2d
     */
    private void paintBarrier(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);

        for (var barrier : this.barrier) {
            g2d.fillRect(barrier.getX() * MAX_X, barrier.getY() * MAX_Y, SCALE, SCALE);
        }
    }

    private int executeSnake(String path, String boardinfo, int timeout) {
        String line;
        OutputStream stdin = null;
        InputStream stderr = null;
        InputStream stdout = null;
        int result = -1;
        try {

            // launch EXE and grab stdin/stdout and stderr
            Process process = Runtime.getRuntime().exec(path);
            stdin = process.getOutputStream();
            stderr = process.getErrorStream();
            stdout = process.getInputStream();

            // "write" the parms into stdin
            stdin.write(boardinfo.getBytes());
            stdin.close();

            if(!process.waitFor(timeout, TimeUnit.MILLISECONDS)) {
                System.out.println("Process " + path + " took to long to execute");
                process.destroy(); // consider using destroyForcibly instead
                return result;
            }

            // clean up if any output in stdout
            BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(stdout));
            while ((line = brCleanUp.readLine()) != null) {
                result = Integer.parseInt(line);
            }
            brCleanUp.close();

            // clean up if any output in stderr
            brCleanUp = new BufferedReader(new InputStreamReader(stderr));
            while ((line = brCleanUp.readLine()) != null) {
                System.out.println("[Stderr] " + line);
            }
            brCleanUp.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return result;
    }

    /**
     * Moves the snakes over the board
     *
     * @param g2d
     */
    private void moveSnakes(Graphics2D g2d) {
        int direction;
        int newX;
        int newY;
        String board = serialize();

        for (int i = 0; i < this.snakes.size(); i++) {
            if (game.CLI_SERVER) {
                var snake =this.snakes.get(i);
                direction = executeSnake(snake.PATH, new BoardInfo(this, i).serialize(board), game.WAIT_TIME);
            } else {
                direction = this.snakes.get(i).think(new BoardInfo(this, i));
            }
            newX = this.snakesLocation.get(i).getLast().getX();
            newY = this.snakesLocation.get(i).getLast().getY();

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
                System.out.println("snakes.Snake " + this.snakes.get(i).NAME + " did not return a correct direction "
                        + "or drove into a border");
                killSnake(g2d, i);
                i--;
                continue;
            }

            // check if snake run in another snake/barrier or in an apple
            boolean ate = false;
            if (!this.fields[newX][newY].isFree()) {

                if (this.fields[newX][newY].isApple()) {
                    removeApple(g2d, newX, newY);
                    ate = true;

                } else {
                    killSnake(g2d, i);
                    i--;
                    continue;

                }
            }

            // set Fields -> TODO: make better
            int oldX = this.snakesLocation.get(i).getFirst().getX();
            int oldY = this.snakesLocation.get(i).getFirst().getY();

            this.fields[newX][newY].setFree(false);
            this.fields[oldX][oldY].setFree(true);

            // move snake
            this.snakesLocation.get(i).addLast(new Position(newX, newY));

            if (!ate) {
                this.snakesLocation.get(i).removeFirst();
            }
        }
    }

    /**
     * Use epic kame hame ha power to kill a snake and turn it to a barrier
     *
     * @param g2d
     * @param snakeIndex snake to be killed
     */
    private void killSnake(Graphics2D g2d, int snakeIndex) {
        barrier.addAll(this.snakesLocation.get(snakeIndex));

        this.paintBarrier(g2d);

        this.snakesLocation.remove(snakeIndex);
        this.snakes.remove(snakeIndex);

    }

    /**
     * remove an apple on the board and place another
     *
     * @param g2d
     * @param x   x-Coordinate of apple
     * @param y   y-Coordinate of apple
     * @return if an apple was removed
     */
    private boolean removeApple(Graphics2D g2d, int x, int y) {
        for (var appleField : apples) {
            if ((appleField.getX() == x) && (appleField.getY() == y)) {

                // no .setFree here, because a snake will be on this field
                fields[appleField.getX()][appleField.getY()].setApple(false);
                apples.remove(appleField);

                this.setApple(g2d);

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
            Position appleField;

            do {
                appleField = getRandomField();

            } while (!fields[appleField.getX()][appleField.getY()].isFree());

            fields[appleField.getX()][appleField.getY()].setFree(false);
            fields[appleField.getX()][appleField.getY()].setApple(true);
            apples.add(appleField);
        }

        // paint apples
        for (var apple : apples) {
            this.paintApple(g2d, apple.getX(), apple.getY());
        }
    }

    /**
     * Returns a random Board.Field on the Board.Board
     *
     * @return a random Board.Field on the Board.Board
     */
    protected Position getRandomField() {
        int x = (int) (Math.random() * MAX_X);
        int y = (int) (Math.random() * MAX_Y);

        return new Position(x, y);
    }

    protected Field[][] getFields() {
        return fields;
    }

    protected List<Position> getApples() {
        return apples;
    }

    protected List<LinkedList<Position>> getSnakesLocation() {
        return snakesLocation;
    }

    protected List<Snake> getSnakes() {
        return snakes;
    }

    protected List<Position> getBarrier() {
        return barrier;
    }
}
