package snakes.contest.chrysopelea_Tobias;

import board.*;
import snakes.Snake;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * This is the snake which has to be filled with lovely intelligence code
 * to become the longest snake of all other snakes.
 *
 * This snake appears 'MySnake' times an board.
 * The value behind 'MySnake' can set in the property file (snake_arena.properties).
 */
public class MySnake extends Snake {


    public MySnake() {
        this.NAME = "Chrysopelea Tobias";
        this.COLOR = new Color(80,86,48);

    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {
        return new SnakeAi(board).getNextStep();
    }

    private static class SnakeAi{
        private static final float MIN_FLOOD_PERCENTAGE = 0.5f;
        private static final int MAX_CLOSER_SNAKES = 2;
        private static final float LOOKAHEAD_PERCENTAGE = 0.8f;

        private final int FIELD_SIZE;
        private final int MIN_FLOOD;
        private final int MIN_FLOOD_LOOKAHEAD;
        private final int NUM_APPLES;


        private final Random RANDOM = new Random();

        private final BoardInfo boardInfo;

        private final AiField[][] boardMatrix;

        /**
         * Initialize class with given information.
         *
         * @param board {@link BoardInfo} which will contain all necessary information
         */
        private SnakeAi(BoardInfo board) {
            boardInfo = board;
            FIELD_SIZE = boardInfo.getSIZE_X() * boardInfo.getSIZE_Y();

            int blockedSpaces = board.getBarrier().size();
            for (List<Field> s : board.getOtherSnakes()) {
                blockedSpaces += s.size();
            }
            blockedSpaces += board.getOwnSnake().size();
            MIN_FLOOD = (int) ((FIELD_SIZE - blockedSpaces) * MIN_FLOOD_PERCENTAGE);
            MIN_FLOOD_LOOKAHEAD = (int) ((FIELD_SIZE - blockedSpaces) * LOOKAHEAD_PERCENTAGE) - 1;

            NUM_APPLES = boardInfo.getApples().size();

            boardMatrix = new AiField[board.getSIZE_X()][board.getSIZE_Y()];
            for (int x = 0; x < board.getSIZE_X(); x++) {
                for (int y = 0; y < board.getSIZE_Y(); y++) {
                    boardMatrix[x][y] = new AiField(boardInfo.getFields()[x][y], NUM_APPLES);
                }
            }
            initDistanceMatrix();
        }

        /**
         * Use the AI to calculate the next step which will yield the best results
         *
         * @return the direction to the next step
         */
        public int getNextStep() {
            int x = boardInfo.getOwnHead().getPosX();
            int y = boardInfo.getOwnHead().getPosY();

            int apple = determineApple();
            Movement[] distance;
            if (apple >= 0) {
                distance = getMovementsAround(x, y, apple);
            } else {
                distance = getDistanceToCenter(x, y);
            }
            int[] flood = calcFlood(x, y);
            int[] lookAhead = lookahead(x, y);

            Arrays.sort(distance);
            for (Movement m : distance) {
                if (flood[m.direction] > MIN_FLOOD && lookAhead[m.direction] >= MIN_FLOOD_LOOKAHEAD) {
                    return m.direction;
                }
            }

            int[] value = new int[]{-1, -1, -1, -1};
            for (int i = 0; i < 4; i++) {
                if (flood[i] > 0) {
                    value[i] = flood[i] + lookAhead[i];
                }
            }

            int bestValue = 0;
            int bestValueInd = -1;
            for (int i = 0; i < 4; i++) {
                if (bestValue < value[i]) {
                    bestValueInd = i;
                    bestValue = value[i];
                }
            }

            return bestValueInd;
        }

        /**
         * Determine which apple is the most likely to be reachable before all other snakes.
         *
         * @return the index of the apple which should be gone after or -1 if none is realistic
         */
        private int determineApple() {
            int myDistance = Integer.MAX_VALUE;
            int tmp;
            int minDistanceInd = 0;
            if (boardInfo.getOtherSnakes().isEmpty()) {
                for (int i = 0; i < NUM_APPLES; i++) {
                    tmp = getClosestDistance(boardInfo.getOwnHead(), i);
                    if (tmp < myDistance) {
                        myDistance = tmp;
                        minDistanceInd = i;
                    }
                }
                return minDistanceInd;
            }

            int[] snakesCloser = new int[NUM_APPLES];
            for (int i = 0; i < NUM_APPLES; i++) {
                myDistance = getClosestDistance(boardInfo.getOwnHead(), i);
                for (LinkedList<Field> snake : boardInfo.getOtherSnakes()) {
                    tmp = getClosestDistance(snake.getLast(), i);
                    if (tmp < myDistance) {
                        snakesCloser[i]++;
                    }
                }
            }
            int minCloser = MAX_CLOSER_SNAKES + 1;
            int minAppleInd = -1;
            for (int i = 0; i < snakesCloser.length; i++) {
                if (snakesCloser[i] < minCloser) {
                    minCloser = snakesCloser[i];
                    minAppleInd = i;
                }
            }

            return minAppleInd;
        }

        private int[] lookahead(int startX, int startY) {
            int currentX;
            int currentY;
            int[] ret = new int[4];
            FieldState previous;
            for (int direction = 0; direction < 4; direction++) {
                currentX = getXfromDirection(startX, direction);
                currentY = getYfromDirection(startY, direction);
                if (!validCoords(currentX, currentY) || !boardMatrix[currentX][currentY].isFree()) {
                    continue;
                }

                previous = boardMatrix[currentX][currentY].state;
                boardMatrix[currentX][currentY].state = FieldState.Snake;

                int[] flood = calcFlood(currentX, currentY);
                for (int j : flood) {
                    ret[direction] += Math.max(0, j);
                }

                boardMatrix[currentX][currentY].state = previous;
            }
            return ret;
        }


        // ---------------------------------------------------
        // Pathfinding logic
        // ---------------------------------------------------


        /**
         * Get the closest distance from the snakeHead to the apple.
         *
         * @param snakeHead position of the snakeHead
         * @param apple     index of the specified apple
         * @return the closest distance from the head to the apple
         */
        private int getClosestDistance(Field snakeHead, int apple) {
            int[] dis = getDistancesAround(snakeHead.getPosX(), snakeHead.getPosY(), apple);
            int min = Integer.MAX_VALUE;
            for (int i : dis) {
                if (i < min) min = i;
            }
            return min + 1;
        }

        /**
         * Get an array of {@link Movement}s for all possible steps from the current position.
         * The {@link Movement#distance} will represents the distance to the specified apple.
         * See {@link SnakeAi}
         *
         * @param x     the current x-position on the board
         * @param y     the current y-position on the board
         * @param apple index of the specified apple
         * @return an array with all Movements possible
         */
        private Movement[] getMovementsAround(int x, int y, int apple) {
            return new Movement[]{
                    new Movement(0, getDistance(x, y - 1, apple)),
                    new Movement(1, getDistance(x + 1, y, apple)),
                    new Movement(2, getDistance(x, y + 1, apple)),
                    new Movement(3, getDistance(x - 1, y, apple))
            };
        }

        /**
         * Get an array of the distances to the apple for all possible steps from the current position.
         * See {@link SnakeAi}
         *
         * @param x     the current x-position on the board
         * @param y     the current y-position on the board
         * @param apple index of the specified apple
         * @return an array with the distances for all possible steps
         */
        private int[] getDistancesAround(int x, int y, int apple) {
            return new int[]{
                    getDistance(x, y - 1, apple),
                    getDistance(x + 1, y, apple),
                    getDistance(x, y + 1, apple),
                    getDistance(x - 1, y, apple)
            };
        }

        /**
         * Get the distance to the apple at the given position.
         *
         * @param x     the x-position on the board.
         * @param y     the y-position on the board.
         * @param apple index of the wanted apple
         * @return the distance to the specific apple from the given position
         */
        private int getDistance(int x, int y, int apple) {
            if (validCoords(x, y)) {
                return boardMatrix[x][y].distance[apple];
            }
            return Integer.MAX_VALUE;
        }


        /**
         * Helper function for pathfinding
         */
        private void initDistanceMatrix() {
            List<Field> apples = boardInfo.getApples();
            for (int i = 0; i < apples.size(); i++) {
                boardMatrix[apples.get(i).getPosX()][apples.get(i).getPosY()].distance[i] = 0;
                expandMatrix(apples.get(i).getPosX(), apples.get(i).getPosY(), 0, i);
            }
        }

        /**
         * Helper function for pathfinding
         */
        private void expandMatrix(AiField pos, int distance, int apple) {
            expandMatrix(pos.x, pos.y, distance, apple);
        }

        /**
         * Helper function for pathfinding
         */
        private void expandMatrix(int x, int y, int distance, int apple) {
            if (validCoords(x, y - 1)) {
                if (boardMatrix[x][y - 1].checkDistance(distance, apple)) {
                    expandMatrix(boardMatrix[x][y - 1], boardMatrix[x][y - 1].distance[apple], apple);
                }
            }
            if (validCoords(x + 1, y)) {
                if (boardMatrix[x + 1][y].checkDistance(distance, apple)) {
                    expandMatrix(boardMatrix[x + 1][y], boardMatrix[x + 1][y].distance[apple], apple);
                }
            }
            if (validCoords(x, y + 1)) {
                if (boardMatrix[x][y + 1].checkDistance(distance, apple)) {
                    expandMatrix(boardMatrix[x][y + 1], boardMatrix[x][y + 1].distance[apple], apple);
                }
            }
            if (validCoords(x - 1, y)) {
                if (boardMatrix[x - 1][y].checkDistance(distance, apple)) {
                    expandMatrix(boardMatrix[x - 1][y], boardMatrix[x - 1][y].distance[apple], apple);
                }
            }
        }


        // ---------------------------------------------------
        // Flooding logic
        // ---------------------------------------------------


        /**
         * Get an array of the number reachable fields for all possible steps from the current position.
         * See {@link SnakeAi#startFlood(int, int)}
         * See {@link SnakeAi}
         *
         * @param x the x-position on the field
         * @param y the y-position on the field
         * @return an array with number of reachable fields for all possible steps
         */
        private int[] calcFlood(int x, int y) {
            return new int[]{
                    startFlood(x, y - 1),
                    startFlood(x + 1, y),
                    startFlood(x, y + 1),
                    startFlood(x - 1, y),
            };
        }

        /**
         * Calculate all reachable fields from this position.
         *
         * @param x the x-position on the board
         * @param y the y-position on the board
         * @return the number of reachable fields or -1 if the starting field is unreachable
         */
        private int startFlood(int x, int y) {
            if (!validCoords(x, y)) return -1;
            if (!boardMatrix[x][y].isFree()) return -1;

            boolean[] checked = new boolean[FIELD_SIZE];
            checked[getUniqueIntFromCoords(x, y)] = true;
            return flood(checked, x, y);
        }

        /**
         * Helper function to calculate all reachable fields from a position.
         */
        private int flood(boolean[] checked, int startX, int startY) {
            int count = 1;
            int tmp;

            if (startY > 0) {
                tmp = getUniqueIntFromCoords(startX, startY - 1);
                if (!checked[tmp] && boardMatrix[startX][startY - 1].isFree()) {
                    checked[tmp] = true;
                    count += flood(checked, startX, startY - 1);
                }
            }
            if (startX < boardInfo.getSIZE_X() - 1) {
                tmp = getUniqueIntFromCoords(startX + 1, startY);
                if (!checked[tmp] && boardMatrix[startX + 1][startY].isFree()) {
                    checked[tmp] = true;
                    count += flood(checked, startX + 1, startY);
                }
            }
            if (startY < boardInfo.getSIZE_Y() - 1) {
                tmp = getUniqueIntFromCoords(startX, startY + 1);
                if (!checked[tmp] && boardMatrix[startX][startY + 1].isFree()) {
                    checked[tmp] = true;
                    count += flood(checked, startX, startY + 1);
                }
            }
            if (startX > 0) {
                tmp = getUniqueIntFromCoords(startX - 1, startY);
                if (!checked[tmp] && boardMatrix[startX - 1][startY].isFree()) {
                    checked[tmp] = true;
                    count += flood(checked, startX - 1, startY);
                }
            }

            return count;
        }


        // ---------------------------------------------------
        // Helper functions
        // ---------------------------------------------------


        /**
         * Get an array of {@link Movement}s for all possible steps from the current position.
         * The {@link Movement#distance} will represents the distance to the center of the board.
         * See {@link SnakeAi}
         *
         * @param x the current x-position on the board
         * @param y the current y-position on the board
         * @return an array with all Movements possible
         */
        private Movement[] getDistanceToCenter(int x, int y) {
            int dX = boardInfo.getSIZE_X() / 2 - x;
            int dY = boardInfo.getSIZE_Y() / 2 - y;
            if (dX == 0) dX = RANDOM.nextBoolean() ? 1 : -1;
            if (dY == 0) dY = RANDOM.nextBoolean() ? 1 : -1;
            return new Movement[]{
                    new Movement(0, dY < 0 ? -dX : Integer.MAX_VALUE),
                    new Movement(1, dX > 0 ? dX : Integer.MAX_VALUE),
                    new Movement(2, dY > 0 ? dY : Integer.MAX_VALUE),
                    new Movement(3, dX < 0 ? -dX : Integer.MAX_VALUE),
            };
        }

        /**
         * Check whether the coordinates are still within bounds.
         *
         * @param x the x-position on the board
         * @param y the y-position on the board
         * @return if the coordinates are valid
         */
        private boolean validCoords(int x, int y) {
            if (y < 0 || x < 0) return false;
            if (x > boardInfo.getSIZE_X() - 1) return false;
            return y <= boardInfo.getSIZE_Y() - 1;
        }

        /**
         * Generate a unique number for every position on the board.
         *
         * @param x the x-position on the board
         * @param y the y-position on the board
         * @return the unique number for this position
         */
        private int getUniqueIntFromCoords(int x, int y) {
            return x * boardInfo.getSIZE_X() + y;
        }

        private int getXfromDirection(int startX, int direction) {
            if (direction == 1) {
                return startX + 1;
            } else if (direction == 3) {
                return startX - 1;
            }
            return startX;
        }

        private int getYfromDirection(int startY, int direction) {
            if (direction == 0) {
                return startY - 1;
            } else if (direction == 2) {
                return startY + 1;
            }
            return startY;
        }


// ---------------------------------------------------
// Helper classes
// ---------------------------------------------------


        /**
         * Represents one possible step for the snake. It has a direction and the distance it will then have to the next apple.
         */
        private static class Movement implements Comparable<Movement> {
            int direction;
            int distance;

            Movement(int dir, int dis) {
                direction = dir;
                distance = dis;
            }

            @Override
            public int compareTo(Movement o) {
                return distance - o.distance;
            }
        }


        /**
         * Represents one field on the board for pathfinding. It has a corresponding {@link Field} on the board.
         */
        private static class AiField {
            /**
             * corresponding attributes of {@link Field} on the board
             */
            final int x;
            final int y;
            FieldState state;
            /**
             * Array with distances to all apples on the field
             */
            int[] distance;

            /**
             * Representation of a field on the board.
             *
             * @param field     the corresponding field with the information
             * @param maxApples the number of apples on the board
             */
            AiField(Field field, int maxApples) {
                x = field.getPosX();
                y = field.getPosY();
                state = field.getState();
                distance = new int[maxApples];
                for (int i = 0; i < maxApples; i++) {
                    distance[i] = Integer.MAX_VALUE;
                }
            }

            /**
             * Check if the field is empty and if the field was ever reached from a closer one.
             *
             * @param currDistance distance to the apple of the field it is reached from
             * @param apple        index of the current apple
             * @return if the field is free and the new distance is the closest path to it
             */
            boolean checkDistance(int currDistance, int apple) {
                if (!isFree()) {
                    return false;
                }
                if ((currDistance + 1) < distance[apple]) {
                    distance[apple] = currDistance + 1;
                    return true;
                }
                return false;
            }

            /**
             * Returns true, if a field is empty or has an apple on it; else false.
             * So if this is true a snake can get on this field without dying, else not.
             *
             * @return true, if a field is empty or has an apple on it; else false.
             */
            public boolean isFree() {
                return (this.state == FieldState.Empty) || (this.state == FieldState.Apple);
            }

        }
    }
}
