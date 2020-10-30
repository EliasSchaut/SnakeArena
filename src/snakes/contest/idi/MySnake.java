package snakes.contest.idi;

import board.*;
import snakes.Snake;

import java.awt.*;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.Arrays;

/**
 * This is the snake which has to be filled with lovely intelligence code
 * to become the longest snake of all other snakes.
 *
 * This snake appears 'MySnake' times an board.
 * The value behind 'MySnake' can set in the property file (snake_arena.properties).
 */

class Graph {
    private BoardInfo board;
    private int customGrid[][];  
    private int maxX, maxY;
    private int visited[][];


    private final static int UP = 0;
    private final static int RIGHT = 1;
    private final static int DOWN = 2;
    private final static int LEFT = 3;

    public final static int EMPTY = 0;
    public final static int APPLE = 1;
    public final static int OTHERSNAKE = 2;
    public final static int BARRIER = 3;
    public final static int MYSNAKE = 5;
    public final static int OTHERHEAD = 6;
    public final static int MYHEAD = 7;
    public final static int GOODAPPLE = 8; // Apples where we are the closest
    public final static int BADAPPLE = 9;  // Apples where someone else is closer

    public final static int NOTMINE = -2;  // Same as BADAPPLE
    public final static int MINE = -1;     // Same as GOODAPPLE

    public final static int INFINITY = 1000000;


    public Graph(BoardInfo board) {
        setEverything(board);
    }

    private boolean inside(int x, int y) {
        return (x >= 0 && x < this.maxX && y >= 0 && y < this.maxY);
    }

    private boolean isFree(int x, int y) {
        return (customGrid[x][y] < 2 || customGrid[x][y] > 7);
    }

    public void bfsCalcDistances(int x, int y) {
        for (int[] row: this.visited)
            Arrays.fill(row, -1);       // Fill with -1 because UP == 0

        Queue<Field> queue = new LinkedList<>();

        BiConsumer<Field, Integer> addToQueue = (field, pParentNo) -> {
            int posX = field.getPosX(), posY = field.getPosY();
            if (inside(posX, posY) && isFree(posX, posY) && this.visited[posX][posY] == -1) {
                visited[posX][posY] = pParentNo;
                queue.add(field);
            }
        };
        
        addToQueue.accept(new Field(x, y), 0);

        while (!queue.isEmpty()) {
            Field field = queue.remove();
            int curX = field.getPosX(), curY = field.getPosY();
            
            int parentNo = visited[curX][curY] + 1;
            addToQueue.accept(new Field(curX+1, curY), parentNo);
            addToQueue.accept(new Field(curX-1, curY), parentNo);
            addToQueue.accept(new Field(curX, curY+1), parentNo);
            addToQueue.accept(new Field(curX, curY-1), parentNo);
        }
    }

    public int bfsGetDirection(int x, int y) {
        int[][] localVisited = new int[this.maxX][this.maxY];
        for (int[] row: localVisited)
            Arrays.fill(row, -1);       // Fill with -1 because UP == 0

        Queue<Field> queue = new LinkedList<>();

        BiConsumer<Field, Integer> addToQueue = (field, pParentNo) -> {
            int posX = field.getPosX(), posY = field.getPosY();
            if (inside(posX, posY) && isFree(posX, posY) && localVisited[posX][posY] == -1) {
                localVisited[posX][posY] = pParentNo;
                queue.add(field);
            }
        };

        localVisited[x][y] = -2;
        {
            double left = countReachable(x-1, y, LEFT);
            double right = countReachable(x+1, y, RIGHT);
            double up = countReachable(x, y-1, UP);
            double down = countReachable(x, y+1, DOWN);

            List<Field> mySnake = this.board.getOwnSnake();
            double limit = 0.2 + 0.005*mySnake.size();

            if (left >= limit) addToQueue.accept(new Field(x-1, y), LEFT);
            if (right >= limit) addToQueue.accept(new Field(x+1, y), RIGHT);
            if (up >= limit) addToQueue.accept(new Field(x, y-1), UP);
            if (down >= limit) addToQueue.accept(new Field(x, y+1), DOWN);

            if (queue.isEmpty()) {
                if (left >= right && left >= up && left >= down) return LEFT;
                else if (right >= up && right >= down) return RIGHT;
                else if (up >= down) return UP;
                else return DOWN;
            }
        }

        while (!queue.isEmpty()) {
            Field field = queue.remove();
            int curX = field.getPosX(), curY = field.getPosY();
            int parentNo = localVisited[curX][curY];
            if (this.customGrid[curX][curY] == GOODAPPLE) return parentNo;
            
            addToQueue.accept(new Field(curX+1, curY), parentNo);
            addToQueue.accept(new Field(curX-1, curY), parentNo);
            addToQueue.accept(new Field(curX, curY+1), parentNo);
            addToQueue.accept(new Field(curX, curY-1), parentNo);
        }

        if (inside(x+1, y) && isFree(x+1, y)) return RIGHT;
        if (inside(x-1, y) && isFree(x-1, y)) return LEFT;
        if (inside(x, y+1) && isFree(x, y+1)) return DOWN;
        return UP;
    }

    private double countReachable(int x, int y, int dir) {
        if (!inside(x, y) || !isFree(x, y)) return 0;
        bfsCalcDistances(x, y);
        int total = 0, reachable = 0;
        for (int i=0; i<this.maxX; i++) {
            for (int j=0; j<this.maxY; j++) {
                if (isFree(i, j)) total++;
                if (this.visited[i][j] != -1) reachable++;
            }
        }
        double ans = 0;
        if (dir == RIGHT && (!inside(x+1, y) || !isFree(x+1, y))) ans = 0.01;
        if (dir == LEFT && (!inside(x-1, y) || !isFree(x-1, y))) ans = 0.01;
        if (dir == UP && (!inside(x, y-1) || !isFree(x, y-1))) ans = 0.01;
        if (dir == DOWN && (!inside(x, y+1) || !isFree(x, y+1))) ans = 0.01;
        return (double) reachable / total - ans;
    }

    private int getHeadDistance(Field field) {
        int x = field.getPosX(), y = field.getPosY();
        int ans = INFINITY;
        
        if (inside(x-1, y) && this.visited[x-1][y] != -1)
            ans = Math.min(ans, this.visited[x-1][y]);
        if (inside(x+1, y) && this.visited[x+1][y] != -1)
            ans = Math.min(ans, this.visited[x+1][y]);
        if (inside(x, y-1) && this.visited[x][y-1] != -1)
            ans = Math.min(ans, this.visited[x][y-1]);
        if (inside(x, y+1) && this.visited[x][y+1] != -1)
            ans = Math.min(ans, this.visited[x][y+1]);

        return ans+1;
    }

    public boolean checkApple(Field apple) {
        int posX = apple.getPosX(), posY = apple.getPosY();
        bfsCalcDistances(posX, posY);

        List<Field> otherHeads = this.board.getOtherHeads();
        int myDistance = getHeadDistance(this.board.getOwnHead());

        for (Field otherHead : otherHeads) {
            if (getHeadDistance(otherHead) < myDistance) {
                this.customGrid[posX][posY] = BADAPPLE;
                return false;
            }
        }

        this.customGrid[posX][posY] = GOODAPPLE;
        return true;
    }

    public boolean ohNo() {
        List<Field> apples = this.board.getApples();
        List<Field> otherHeads = this.board.getOtherHeads();
        int myDistance = INFINITY;
        int otherDistance = INFINITY;

        for (Field apple : apples) {
            int posX = apple.getPosX(), posY = apple.getPosY();
            bfsCalcDistances(posX, posY);
            for (Field otherHead : otherHeads) {
                otherDistance = Math.min(otherDistance, getHeadDistance(otherHead));
            }
            myDistance = Math.min(myDistance, getHeadDistance(this.board.getOwnHead()));
        }
        return otherDistance < myDistance;
    }

    public void setBestFields() {
        Field head = this.board.getOwnHead();
        int headX = head.getPosX(), headY = head.getPosY();

        int appleClosestToHere[][] = new int[this.maxX][this.maxY];
        int mxFieldValue = 0;
        List<Field> otherHeads = this.board.getOtherHeads();
        for (int appleX=0; appleX < this.maxX; appleX++) {
            for (int appleY=0; appleY < this.maxY; appleY++) {
                bfsCalcDistances(appleX, appleY);

                int othersDistance = INFINITY;
                for (Field otherHead : otherHeads)
                    othersDistance = Math.min(othersDistance, getHeadDistance(otherHead));

                for (int x=Math.max(0, headX-5); x<Math.min(this.maxX, headX+6); x++) {
                    for (int y=Math.max(0, headY-5); y<Math.min(this.maxY, headY+6); y++) {
                        if (this.visited[x][y] > 0 && this.visited[x][y] <= othersDistance) {
                            appleClosestToHere[x][y]++;
                            mxFieldValue = Math.max(mxFieldValue, appleClosestToHere[x][y]);
                        }
                    }
                }
            }
        }
        // All fields where the probabilty to have a good apple spawn is maximal are 
        //transformed into good apples
        for (int x=0; x<this.maxX; x++) {
            for (int y=0; y<this.maxY; y++) {
                if (appleClosestToHere[x][y] == mxFieldValue) {
                    this.customGrid[x][y] = GOODAPPLE;
                }
            }
        }
    }

    public int killYourself(Field head) {
        int x = head.getPosX(), y = head.getPosY();
        if (!inside(x+1, y) || !isFree(x+1, y)) return RIGHT;
        if (!inside(x-1, y) || !isFree(x-1, y)) return LEFT;
        if (!inside(x, y+1) || !isFree(x, y+1)) return DOWN;
        return UP;
    }

    private void setEverything(BoardInfo board) {
        this.board = board;
        Field[][] grid = this.board.getFields();
        this.maxX = this.board.getSIZE_X();
        this.maxY = this.board.getSIZE_Y();
        this.visited = new int[this.maxX][this.maxY];

        this.customGrid = new int[this.maxX][this.maxY];
        for (int x=0; x<this.maxX; x++) {
            for (int y=0; y<this.maxY; y++) {
                this.customGrid[x][y] = grid[x][y].getState().get();
            }
        }
        List<Field> ownSnake = this.board.getOwnSnake();
        ownSnake.forEach((part) -> {
            this.customGrid[part.getPosX()][part.getPosY()] = MYSNAKE;
        });
        Field head = this.board.getOwnHead();
        this.customGrid[head.getPosX()][head.getPosY()] = MYHEAD;

        List<Field> otherHeads = this.board.getOtherHeads();
        otherHeads.forEach((part) -> {
            this.customGrid[part.getPosX()][part.getPosY()] = OTHERHEAD; 
        });
    }
}

public class MySnake extends Snake {
    private int maxOtherLength;
    private Color[] colors;
    private int turn;

    public MySnake() {
        this.NAME = ".IDI";         // set your favorite name
        this.COLOR = new Color(0, 0, 0); // set your favorite color
        this.maxOtherLength = 3;
        this.colors = new Color[6];

        this.colors[0] = new Color(250, 0, 0);
        this.colors[1] = new Color(250, 250, 0);
        this.colors[2] = new Color(50, 250, 0);
        this.colors[3] = new Color(0, 250, 250);
        this.colors[4] = new Color(150, 0, 250);
        this.colors[5] = new Color(250, 0, 200);
        this.turn = 0;
    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {
        this.COLOR = this.colors[turn%colors.length];
        this.turn++;
        // STUPID code here
        Field head = board.getOwnHead();
        Graph graph = new Graph(board);

        List<LinkedList<Field>> otherSnakes = board.getOtherSnakes();
        for (LinkedList<Field> otherSnake : otherSnakes) {
            this.maxOtherLength = Math.max(maxOtherLength, otherSnake.size());
        }
        if (otherSnakes.size() == 1 && board.getOwnSnake().size() > maxOtherLength) {
            return graph.killYourself(head);
        } else if (otherSnakes.size() == 1 && board.getOwnSnake().size() == maxOtherLength
                && otherSnakes.get(0).size() == maxOtherLength) {
            if (graph.ohNo()) return graph.killYourself(head);
        }

        boolean noGoodApple = true;
        List<Field> apples = board.getApples();
        for (int i=0; i<apples.size(); i++) {
            Field apple = apples.get(i);
            if (graph.checkApple(apple)) noGoodApple = false;
        }

        if (noGoodApple) {
            graph.setBestFields();
        }

        int dir = graph.bfsGetDirection(head.getPosX(), head.getPosY());
        return dir;
    }
}
