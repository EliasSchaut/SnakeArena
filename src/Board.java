import java.util.ArrayList;

/**
 * This class represents the Arena with the snakes
 *
 * TODO: integrate Snake and IO
 */
public class Board {

    private static final int MAX_X = 20;
    private static final int MAX_Y = 20;
    private static final int MAX_APPLES_ON_BOARD = 2;


    private final Field[][] fields = new Field[MAX_X][MAX_Y];
    private final ArrayList<Field> apples = new ArrayList<>();


    /**
     * Create Board
     */
    public Board() {

        // declare Board
        for (int i = 0; i < MAX_X; i++) {
            for (int j = 0; j < MAX_Y; j++) {
                fields[i][j] = new Field(i, j);

            }
        }
    }


    /**
     * remove an apple on the board and place another
     *
     * @param x x-Coordinate of apple
     * @param y y-Coordinate of apple
     * @return if an apple was removed
     */
    private boolean removeApple(int x, int y) {
        for (Field appleField : apples) {
            if ((appleField.getPosX() == x) && (appleField.getPosY() == y)) {
                fields[appleField.getPosX()][appleField.getPosY()].setApple(false);
                apples.remove(appleField);
                setApple();

                return true;
            }
        }

        return false;
    }


    /**
     * place an apple on the board
     *
     * @return if an apple was set on board
     */
    private boolean setApple() {
        if (apples.size() < MAX_APPLES_ON_BOARD) {
            Field appleField;

            do {
                appleField = getRandomField();

            } while (!fields[appleField.getPosX()][appleField.getPosY()].isFree());

            fields[appleField.getPosX()][appleField.getPosY()].setFree(false);
            fields[appleField.getPosX()][appleField.getPosY()].setApple(true);
            apples.add(appleField);
            return true;

        } else {
            return false;

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

