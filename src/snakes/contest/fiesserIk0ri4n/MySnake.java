package snakes.contest.fiesserIk0ri4n;

import board.BoardInfo;
import board.BoardLogic;
import board.Field;
import board.FieldState;
import snakes.Snake;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;
import java.util.stream.Collectors;


/**
 * This is the snake which has to be filled with lovely intelligence code
 * to become the longest snake of all other snakes.
 * <p>
 * This snake appears 'MySnake' times an board.
 * The value behind 'MySnake' can set in the property file (snake_arena.properties).
 * <p>
 * Internally MySnake uses another Snake implementation as its mind, which can be swapped for different behaviour.
 */
public class MySnake extends Snake {

    /**
     * Returns a list of adjacent points in a certain walking distance,
     * that lie inside the given boundaries.
     *
     * @param distance     the maximum walking distance in fields.
     * @param currentPoint the x- and y-value of the current point.
     * @param maxX         the maximum x value.
     * @param maxY         the maximum y value.
     * @return a list of adjacent points.
     */
    public static List<Point> getAdjacentPoints(int distance, Point currentPoint,
                                                BiFunction<Integer, Integer, Point> pointProvider, int maxX, int maxY) {
        return getAdjacentPoints(distance, currentPoint, pointProvider, 0, 0, maxX, maxY);
    }

    /**
     * Returns a list of adjacent points in a certain walking distance,
     * that lie inside the given boundaries.
     *
     * @param distance     the maximum walking distance in fields.
     * @param currentPoint the x- and y-value of the current point.
     * @param minX         the minimum x value.
     * @param minY         the minimum y value.
     * @param maxX         the maximum x value.
     * @param maxY         the maximum y value.
     * @return a list of adjacent points.
     */
    public static List<Point> getAdjacentPoints(int distance, Point currentPoint,
                                                BiFunction<Integer, Integer, Point> pointProvider,
                                                int minX, int minY, int maxX, int maxY) {
        return getAdjacentPoints(distance, currentPoint, MySnake::getDistance, pointProvider, minX, minY, maxX, maxY);
    }

    /**
     * Returns a list of adjacent points in a certain walking distance,
     * that lie inside the given boundaries.
     *
     * @param distance           the maximum walking distance in fields.
     * @param currentPoint       the x- and y-value of the current point.
     * @param distanceCalculator a function calculating the distance between two points.
     * @param minX               the minimum x value.
     * @param minY               the minimum y value.
     * @param maxX               the maximum x value.
     * @param maxY               the maximum y value.
     * @return a list of adjacent points.
     */
    public static List<Point> getAdjacentPoints(int distance, Point currentPoint, ToIntBiFunction<Point, Point> distanceCalculator,
                                                BiFunction<Integer, Integer, Point> pointProvider, int minX, int minY, int maxX, int maxY) {
        List<Point> points = new ArrayList<>();

        Point newPoint;
        int currentDistance;
        for (int x = Math.max((-distance + currentPoint.getX()), minX); x <= Math.min(distance + currentPoint.getX(), maxX - 1); x++) {
            for (int y = Math.max((-distance + currentPoint.getY()), minY); y <= Math.min(distance + currentPoint.getY(), maxY - 1); y++) {
                newPoint = pointProvider.apply(x, y);
                currentDistance = distanceCalculator.applyAsInt(currentPoint, newPoint);
                if (0 < currentDistance && currentDistance <= distance) {
                    points.add(newPoint);
                }
            }
        }

        return points;
    }

    /**
     * Returns the walking distance between to fields.
     *
     * @param point1 a field.
     * @param point2 another field.
     * @return the distance between point1 and point2.
     */
    public static int getDistance(Field point1, Field point2) {
        return getDistance(point1.getPosX(), point1.getPosY(), point2.getPosX(), point2.getPosY());
    }

    /**
     * Returns the walking distance between to points.
     *
     * @param point1 a field.
     * @param point2 another field.
     * @return the distance between point1 and point2.
     */
    public static int getDistance(Point point1, Point point2) {
        return getDistance(point1.getX(), point1.getY(), point2.getX(), point2.getY());
    }

    /**
     * Returns the walking distance between to points.
     *
     * @param x1 the x-value of the first point.
     * @param y1 the y-value of the first point.
     * @param x2 the x-value of the second point.
     * @param y2 the y-value of the second point.
     * @return the distance between point 1 and 2.
     */
    public static int getDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private final Snake snakeMind;

    public MySnake() {
        this.NAME = "fiesserIk0ri4n";         // set your favorite name
        this.COLOR = new Color(54, 152, 32); // set your favorite color

        snakeMind = new TacticalSnake();
    }

    /**
     * Main function for every intelligence of the snake. This snake simply calls the think method of the used mind.
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {
        return snakeMind.think(board);
    }

    public class TacticalSnake extends Snake {
        private BoardHelper helper;
        private AStarGrid grid;
        private boolean initialised;

        private int lastDirection = RIGHT;

        public boolean InitialiseSnake() {
            helper = new BoardHelper(BoardLogic.SIZE_X, BoardLogic.SIZE_Y);
            grid = new AStarGrid(helper, BoardLogic.SIZE_X, BoardLogic.SIZE_Y);
            return true;
        }

        /**
         * Main function for every intelligence of the snake.
         * <p>
         * This snake uses an A* implementation for finding a path to the closest apple (by way distance).
         *
         * @param board the whole board with every information necessary
         * @return direction in which the snake should move
         */
        @Override
        public int think(BoardInfo board) {
            if (!initialised) {
                initialised = InitialiseSnake();
            }

            helper.update(board);

            return calculateMove(board);
        }

        private int calculateMove(BoardInfo board) {
            Field start = board.getOwnHead();
            List<Field> goal = board.getApples();

            Point startingPoint = helper.getPoint(start.getPosX(), start.getPosY());
            List<Point> goalPoints = goal.stream()
                    .map((Field field) -> helper.getPoint(field.getPosX(), field.getPosY()))
                    .collect(Collectors.toList());

            List<List<Point>> ownPaths = new ArrayList<>(2);
            ownPaths.add(grid.findPath(startingPoint, goalPoints.get(0)));
            ownPaths.add(grid.findPath(startingPoint, goalPoints.get(1)));

            int path0length = ownPaths.get(0).size();
            int path1length = ownPaths.get(1).size();
            int smallestPath;
            if (path0length == 0 && path1length == 0) {
                smallestPath = Integer.MAX_VALUE;
            } else {
                smallestPath = Math.min(path0length, path1length);
            }

            List<Field> otherLivingHeads = helper.getOtherSnakes().stream()
                    .filter(snakeInfo -> snakeInfo.state == SnakeState.Alive)
                    .map(snakeInfo -> snakeInfo.body.get(snakeInfo.body.size() - 1))
                    .collect(Collectors.toList());

            List<Point>[][] otherSnakePaths = new List[otherLivingHeads.size()][2];
            Field currentHead;
            for (int i = 0; i < otherLivingHeads.size(); i++) {
                currentHead = otherLivingHeads.get(i);
                otherSnakePaths[i][0] = grid.findPath(helper.getPoint(currentHead.getPosX(), currentHead.getPosY()), goalPoints.get(0));
                otherSnakePaths[i][1] = grid.findPath(helper.getPoint(currentHead.getPosX(), currentHead.getPosY()), goalPoints.get(1));
            }

            if (helper.getLivingSnakeCount() == 2) {
                List<SnakeInfo> longestSnakes = helper.getLongestSnakes();

                if (longestSnakes.stream().anyMatch(SnakeInfo::isMySnake)) {
                    if (longestSnakes.size() == 1) {
                        return suicide();
                    } else {
                        SnakeInfo otherLivingSnake = helper.getOtherSnakes().stream()
                                .filter(snakeInfo -> snakeInfo.state == SnakeState.Alive)
                                .collect(Collectors.toList())
                                .get(0);

                        int enemyPath0length = otherSnakePaths[0][0].size();
                        int enemyPath1length = otherSnakePaths[0][1].size();
                        int smallestEnemyPath;
                        if (enemyPath0length == 0 && enemyPath1length == 0) {
                            smallestEnemyPath = Integer.MAX_VALUE;
                        } else {
                            smallestEnemyPath = Math.min(enemyPath0length, enemyPath1length);
                        }

                        if (longestSnakes.stream().anyMatch(snakeInfo -> !snakeInfo.isMySnake()
                                && snakeInfo.state == SnakeState.Alive)) {

                            if (smallestEnemyPath < smallestPath) {
                                return suicide();
                            }
                        } else {
                            if (smallestPath > smallestEnemyPath && otherLivingSnake.body.size() == board.getOwnSnake().size() - 1) {
                                return suicide();
                            }
                        }
                    }
                }
            }

            int smallestEnemyPath0 = Integer.MAX_VALUE;
            int smallestEnemyPath1 = Integer.MAX_VALUE;
            for (List<Point>[] path : otherSnakePaths) {
                if (path[0].size() < smallestEnemyPath0 && path[0].size() != 0) {
                    smallestEnemyPath0 = path[0].size();
                }
                if (path[1].size() < smallestEnemyPath1 && path[1].size() != 0) {
                    smallestEnemyPath1 = path[1].size();
                }
            }

            List<Point> path = null;
            if (path0length != 0 && path0length <= smallestEnemyPath0) {
                path = ownPaths.get(0);
            }
            if (path1length != 0 && path1length <= smallestEnemyPath1) {
                if (path == null || path1length < path0length) {
                    path = ownPaths.get(1);
                }
            }

            if (path != null) {
                return getDirection(startingPoint, path.get(0));
            }

            return getDirection(startingPoint, findPointWithGreatestControl(board, startingPoint));
        }

        private Point findPointWithGreatestControl(BoardInfo boardInfo, Point point) {
            List<Point> points = MySnake.getAdjacentPoints(1, point, helper::getPoint, BoardLogic.SIZE_X, BoardLogic.SIZE_Y);
            int maxControl = 0;
            int bestPointIndex = 0;
            int bestPointDistance = Integer.MAX_VALUE;

            Point currentPoint;
            int currentDistance;
            int control;
            for (int i = 0; i < points.size(); i++) {
                currentPoint = points.get(i);
                if(!helper.isPointFree(currentPoint)){
                    continue;
                }

                currentDistance = MySnake.getDistance(10, 10, currentPoint.getX(), currentPoint.getY());
                control = getFieldControl(boardInfo, currentPoint);
                if(control > maxControl || (control == maxControl && currentDistance < bestPointDistance)) {
                    bestPointIndex = i;
                    maxControl = control;
                    bestPointDistance = currentDistance;
                }
            }
            return points.get(bestPointIndex);
        }

        private int getDirection(Point start, Point nextPoint) {
            if (nextPoint.getX() != start.getX()) {
                lastDirection = nextPoint.getX() > start.getX() ? RIGHT : LEFT;
            } else {
                lastDirection = nextPoint.getY() > start.getY() ? DOWN : UP;
            }
            return lastDirection;
        }

        private int suicide() {
            switch (lastDirection) {
                case UP:
                    return DOWN;
                case DOWN:
                    return UP;
                case RIGHT:
                    return LEFT;
                default:
                    return RIGHT;
            }
        }

        private int getFieldControl(BoardInfo boardInfo, Point head) {
            int control = 1;
            int[][] board = new int[BoardLogic.SIZE_X][BoardLogic.SIZE_Y];

            List<Point> myPoints = new ArrayList<>();
            myPoints.add(head);
            List<Point> otherPoints = new ArrayList<>();

            for (Field barrier : boardInfo.getBarrier()) {
                board[barrier.getPosX()][barrier.getPosY()] = -1;
            }
            List<Field> ownSnake = boardInfo.getOwnSnake();
            Field body;
            for (int i = 0, ownSnakeSize = ownSnake.size(); i < ownSnakeSize; i++) {
                body = ownSnake.get(i);
                board[body.getPosX()][body.getPosY()] = -1;
            }
            for (SnakeInfo snake : helper.getOtherSnakes().stream()
                    .filter(snakeInfo -> snakeInfo.state == SnakeState.Alive).collect(Collectors.toList())) {
                for (int i = 0, snakeSize = snake.body.size(); i < snakeSize; i++) {
                    body = snake.body.get(i);
                    if (i == snakeSize - 1) {
                        board[body.getPosX()][body.getPosY()] = 2;
                        otherPoints.add(helper.getPoint(body.getPosX(), body.getPosY()));
                    } else {
                        board[body.getPosX()][body.getPosY()] = -1;
                    }
                }
            }

            while (!(myPoints.isEmpty() && otherPoints.isEmpty())) {
                if (!myPoints.isEmpty()) {
                    for (int i = myPoints.size(); i > 0; i--) {
                        Point currentPoint = myPoints.get(i - 1);
                        List<Point> points = MySnake.getAdjacentPoints(1, currentPoint, helper::getPoint, BoardLogic.SIZE_X, BoardLogic.SIZE_Y);

                        for (Point point : points) {
                            if (board[point.getX()][point.getY()] == 0) {
                                board[point.getX()][point.getY()] = 1;
                                myPoints.add(point);
                                control++;
                            }
                        }
                        myPoints.remove(currentPoint);
                    }
                }

                if (!otherPoints.isEmpty()) {
                    for (int i = otherPoints.size(); i > 0; i--) {
                        Point currentPoint = otherPoints.get(i - 1);
                        List<Point> points = MySnake.getAdjacentPoints(1, currentPoint, helper::getPoint, BoardLogic.SIZE_X, BoardLogic.SIZE_Y);

                        for (Point point : points) {
                            if (board[point.getX()][point.getY()] == 0) {
                                board[point.getX()][point.getY()] = 1;
                                otherPoints.add(point);
                            }
                        }
                        otherPoints.remove(currentPoint);
                    }
                }
            }

            return control;
        }

    }
    public class AStarGrid {
        private final int MAX_X;
        private final int MAX_Y;
        private final BoardHelper helper;

        private Node[][] gridNodes;
        private LinkedList<Node> openNodes;
        private Point startingPoint;
        private Point goalPoint;

        /**
         * Creates an {@link AStarGrid} using a {@link BoardHelper}, start and goal points and boundary values.
         *
         * @param helper a {@link BoardHelper}.
         * @param maxX   the maximum x-value.
         * @param maxY   the maximum y-value.
         */
        public AStarGrid(BoardHelper helper, int maxX, int maxY) {
            MAX_X = maxX;
            MAX_Y = maxY;

            this.helper = helper;
        }

        public void reset(Point startingPoint, Point goalPoint) {
            gridNodes = new Node[MAX_X][MAX_Y];
            openNodes = new LinkedList<>();

            this.startingPoint = startingPoint;
            this.goalPoint = goalPoint;
        }

        /**
         * Returns the shortest path to a goal point.
         *
         * @return a list of points.
         */
        public List<Point> findPath(Point startingPoint, Point goalPoint) {
            reset(startingPoint, goalPoint);

            Node startNode = new Node(startingPoint, startingPoint, goalPoint);
            startNode.travelCost = 0;
            openNodes.add(startNode);

            LinkedList<Point> path = new LinkedList<>();
            Node goalNode = search(startNode);

            if(goalNode == null) {
                return path;
            }

            Point currentPoint;
            Node currentNode = goalNode;

            do {
                currentPoint = currentNode.position;
                path.add(0, currentPoint);
                currentNode = currentNode.parent;
            } while (!currentNode.equals(startNode));

            return path;
        }

        private Node search(Node currentNode) {
            if(currentNode == null){
                return null;
            }
            if (currentNode.heuristicDistance == 0) {
                return currentNode;
            }

            List<Node> nodes = getOrAddWalkableNeighbours(currentNode);

            updateCostAndParents(currentNode, nodes);

            openNodes.remove(currentNode);
            openNodes.sort(Node::compareTo);

            if (openNodes.size() == 0) {
                return null;
            }
            return search(openNodes.getFirst());
        }

        private List<Node> getOrAddWalkableNeighbours(Node currentNode) {
            List<Point> points = MySnake.getAdjacentPoints(1, currentNode.position, helper::getPoint, MAX_X, MAX_Y);

            return getOrAddWalkableNodesOn(points);
        }

        private List<Node> getOrAddWalkableNodesOn(List<Point> points) {
            List<Node> nodes = new ArrayList<>(points.size());

            Node node;
            for (Point point : points) {
                if (helper.isPointFree(point)) {
                    node = gridNodes[point.getX()][point.getY()];
                    if (node == null) {
                        node = new Node(point, startingPoint, goalPoint);
                        gridNodes[point.getX()][point.getY()] = node;
                        openNodes.add(node);
                    }

                    nodes.add(node);
                }
            }

            return nodes;
        }

        private void updateCostAndParents(Node currentNode, List<Node> nodes) {
            int travelCost;
            for (Node node : nodes) {
                travelCost = currentNode.travelCost + 1;
                if (travelCost < node.travelCost) {
                    node.travelCost = travelCost;
                    node.parent = currentNode;
                }
            }
        }

        /**
         * A node for the A*-algorithm.
         */
        private class Node implements Comparable<Node> {
            public Node parent;
            public Point position;

            public int travelCost;
            public int heuristicDistance;

            public Node(Point position, Point startingPoint, Point goalPosition) {
                this.position = position;

                travelCost = Integer.MAX_VALUE;

                heuristicDistance = MySnake.getDistance(position, goalPosition);
            }

            public int getTotalHeuristicDistance() {
                return travelCost + heuristicDistance;
            }

            /**
             * Compares this object with the specified object for order.  Returns a
             * negative integer, zero, or a positive integer as this object is less
             * than, equal to, or greater than the specified object.
             *
             * <p>The implementor must ensure
             * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
             * for all {@code x} and {@code y}.  (This
             * implies that {@code x.compareTo(y)} must throw an exception iff
             * {@code y.compareTo(x)} throws an exception.)
             *
             * <p>The implementor must also ensure that the relation is transitive:
             * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
             * {@code x.compareTo(z) > 0}.
             *
             * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
             * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
             * all {@code z}.
             *
             * <p>It is strongly recommended, but <i>not</i> strictly required that
             * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
             * class that implements the {@code Comparable} interface and violates
             * this condition should clearly indicate this fact.  The recommended
             * language is "Note: this class has a natural ordering that is
             * inconsistent with equals."
             *
             * <p>In the foregoing description, the notation
             * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
             * <i>signum</i> function, which is defined to return one of {@code -1},
             * {@code 0}, or {@code 1} according to whether the value of
             * <i>expression</i> is negative, zero, or positive, respectively.
             *
             * @param o the object to be compared.
             * @return a negative integer, zero, or a positive integer as this object
             * is less than, equal to, or greater than the specified object.
             * @throws NullPointerException if the specified object is null
             * @throws ClassCastException   if the specified object's type prevents it
             *                              from being compared to this object.
             */
            @Override
            public int compareTo(Node o) {
                return getTotalHeuristicDistance() - o.getTotalHeuristicDistance();
            }
        }
    }
    public class BoardHelper {
        private final Point[][] points;
        private Field[][] boardState;

        private boolean initialised;
        private SnakeInfo mySnake;
        private List<SnakeInfo> otherSnakes;
        private int livingSnakeCount;

        /**
         * Construct an empty {@link BoardHelper}.
         */
        public BoardHelper(int maxX, int maxY) {
            points = new Point[maxX][maxY];

            for (int x = 0; x < maxX; x++) {
                for (int y = 0; y < maxY; y++) {
                    points[x][y] = new Point(x, y);
                }
            }
        }

        public boolean Initialise(BoardInfo info) {
            mySnake = new SnakeInfo(-1,true);
            mySnake.body = info.getOwnSnake();

            List<LinkedList<Field>> snakes = info.getOtherSnakes();

            livingSnakeCount = snakes.size() + 1;
            otherSnakes = new ArrayList<>(snakes.size());

            SnakeInfo currentSnake;
            for (int i = 0, snakesSize = snakes.size(); i < snakesSize; i++) {
                LinkedList<Field> snake = snakes.get(i);
                currentSnake = new SnakeInfo(i);
                currentSnake.body = snake;
                otherSnakes.add(currentSnake);
            }
            return true;
        }

        /**
         * Update this helper with current board information.
         *
         * @param info this rounds board information
         */
        public void update(BoardInfo info) {
            this.boardState = info.getFields();

            if (!initialised) {
                initialised = Initialise(info);
            } else {
                mySnake.body = info.getOwnSnake();

                List<LinkedList<Field>> snakes = info.getOtherSnakes();
                for (int i = 0; i < snakes.size(); i++) {
                    otherSnakes.get(i).body = snakes.get(i);
                }
            }

            updateSnakeStates();
        }

        private void updateSnakeStates() {
            for (SnakeInfo snake : otherSnakes) {
                List<Field> body = snake.body;
                Field test = body.get(0);

                if (boardState[test.getPosX()][test.getPosY()].getState() == FieldState.Barrier
                        && snake.state == SnakeState.Alive) {
                    snake.state = SnakeState.Dead;
                    livingSnakeCount--;
                }
            }
        }

        /**
         * Returns whether a position on the board is free.
         *
         * @param position a point on the board
         * @return true, if the field on the point is free
         */
        public boolean isPointFree(Point position) {
            return boardState[position.getX()][position.getY()].isFree();
        }

        /**
         * Returns the at the given coordinates.
         *
         * @param x the x-coordinate
         * @param y the y-coordinate
         * @return the point at the position
         */
        public Point getPoint(int x, int y) {
            return points[x][y];
        }

        public List<SnakeInfo> getLongestSnakes() {
            List<SnakeInfo> longestSnakes = new ArrayList<>(otherSnakes.size() + 1);

            longestSnakes.add(mySnake);
            int length = mySnake.getLength();

            for (SnakeInfo snake : otherSnakes) {
                if (snake.getLength() > length) {
                    longestSnakes.clear();
                    longestSnakes.add(snake);
                    length = snake.getLength();
                } else if (snake.getLength() == length) {
                    longestSnakes.add(snake);
                }
            }

            return longestSnakes;
        }

        public List<SnakeInfo> getOtherSnakes() {
            return otherSnakes;
        }

        public int getLivingSnakeCount() {
            return livingSnakeCount;
        }
    }
    public class Point {
        private final int x;
        private final int y;

        /**
         * Construct a new point from the coordinates.
         *
         * @param x the x-coordinate
         * @param y the y-coordinate
         */
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Returns the x-coordinate of this point.
         *
         * @return the x-coordinate
         */
        public int getX() {
            return x;
        }

        /**
         * Returns the y-coordinate of this point.
         *
         * @return the y-coordinate
         */
        public int getY() {
            return y;
        }
    }
    public class SnakeInfo {
        private final boolean mySnake;
        private final int snakeIndex;
        public SnakeState state;
        public List<Field> body;

        public SnakeInfo(int index) {
            this(index, false);
        }

        public SnakeInfo(int index, boolean isMySnake) {
            snakeIndex = index;
            mySnake = isMySnake;
            state = SnakeState.Alive;
        }

        public int getLength() {
            return body.size();
        }

        public boolean isMySnake() {
            return mySnake;
        }

        public int getSnakeIndex() {
            return snakeIndex;
        }
    }
    public enum SnakeState {
        Alive,
        Dead
    }
}
