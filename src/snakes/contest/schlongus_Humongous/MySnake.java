package snakes.contest.schlongus_Humongous;

import board.BoardInfo;
import board.Field;
import snakes.Snake;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the snake which has to be filled with lovely intelligence code
 * to become the longest snake of all other snakes.
 *
 * This snake appears 'MySnake' times an board.
 * The value behind 'MySnake' can set in the property file (snake_arena.properties).
 */
public class MySnake extends Snake {

    public MySnake() {
        this.NAME = "Schlongus Humongous";         // set your favorite name
        this.COLOR = new Color(147, 48, 205); // set your favorite color

    }
    int headX, headY, appleX, appleY, boardX, boardY;
    Node[] nodes;
    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {
        // Get important data
        appleX = board.getNearestApple().getPosX();
        appleY = board.getNearestApple().getPosY();
        boardX = board.getSIZE_X();
        boardY = board.getSIZE_Y();
        headX = board.getOwnHead().getPosX();
        headY = board.getOwnHead().getPosY();

        // Find path to closest Apple
        Node nextNode = pathFinder(board);
        // Get the direction of the next node
        int direction = getDirection(nextNode);

        // Direction is 10 when there is no path to the current apple
        if (direction != 10) {
            return direction;
        }

        // If the program ends up here there is no path to the first apple
        // Get the second apple and update appleX and appleY
        Field newApple = getSecondApple(board);
        appleX = newApple.getPosX();
        appleY = newApple.getPosY();

        // Get the new path to the second apple
        nextNode = pathFinder(board);
        direction = getDirection(nextNode);

        // If there is no path to any apple then the snake enters panic mode and just hopes to survive
        // or else it returns the direction to the second apple
        if (direction == 10) {
            return panicMode(board);
        } else {
            return direction;
        }

    }

    /**
     * pathFinder is a function to set up the nodes which are needed to find the shortest path
     * @param board -> The information needed to set up the nodes
     * @return the node the snake should occupy next
     */
    private Node pathFinder(BoardInfo board) {

        Field[][] feld = board.getFields();
        nodes = new Node[boardX * boardY];
        List<Node> open = new LinkedList<>();
        List<Node> closed = new LinkedList<>();


        for (int y = 0; y < boardY; y++) {
            for (int x = 0; x < boardX; x++) {
                int pos = y*boardX + x;

                if(x == headX && y == headY) {
                    nodes[pos] = new Node("START",x,y);
                    open.add(nodes[pos]);
                    // System.out.print("O");
                    continue;
                }

                if(x == appleX && y == appleY) {
                    nodes[pos] = new Node("END",x,y);
                    // System.out.print("E");
                    continue;
                }
                if (!feld[x][y].isFree()){
                    nodes[pos] = new Node("OBSTACLE",x,y);
                    // System.out.print("#");
                } else {
                    nodes[pos] = new Node("FREE",x,y);
                    // System.out.print("_");
                }
            }
            //System.out.println();
        }
        // hcostDisplay(board, nodes);
        //System.out.println();
        Node nextNode = astar(open,closed);
        return nextNode;
    }

    /**
     * Implementation of the A-Star Pathfinding-Algorithm
     * @param open is a List of Nodes to be checked (Usually only the start node is included at first)
     * @param closed is a list of already checked nodes (Usually empty at start)
     * @return the next field where the snake should go to (following the shortest path)
     */
    private Node astar(List<Node> open, List<Node> closed) {

        boolean finished = false;
        while(!finished) {
            // If open is empty there is no path to the END-Node
            if (open.isEmpty()){
                return null;
            }

            Node current = getCurrentNode(open);
            open.remove(current);
            closed.add(current);

            // If the current node is the END-Node the goal is reached
            if(current.type == "END") {
                finished = true;
                continue;
            }

            Node[] neighbour = getNeighbour(current);

            for (Node n : neighbour) {

                if (n.type == "OBSTACLE" || closed.contains(n)) {
                    continue;
                }

                if (!open.contains(n)) {
                    n.parent = current;

                    if (!open.contains(n)) {
                        open.add(n);
                    }
                }
            }
        }

        // Search for nextNode
        boolean notFound = true;
        Node curr = nodes[appleY * boardX + appleX];
        Node nextNode = null;
        while (notFound) {
            Node parent = curr.parent;
            if (parent.type == "START") {
                nextNode = curr;
                notFound = false;
                continue;
            }
            curr = parent;
        }
        return nextNode;
    }


    /**
     * Snake enters Panic-Mode. It just tries to survive
     * @param board -> necessary board information
     * @return  direction the snake should go
     */
    private int panicMode(BoardInfo board) {
        for (int i = 0; i < 4; i++) {
            if (board.isNextStepFree(i)){
                return i;
            }

        }
        //If completely trapped just go right
        return RIGHT;
    }

    /**
     * Gets the node from open with the lowest hcost
     * @param open -> the List of Nodes
     * @return the node with the lowest hcost
     */
    private Node getCurrentNode(List<Node> open) {
        int lowestHcost = Integer.MAX_VALUE;
        Node bestNode = null;
        for (Node n : open) {
            if (n.hcost <= lowestHcost){
                lowestHcost = n.hcost;
                bestNode = n;
            }
        }
        return bestNode;
    }

    /**
     * Gets all the neighbours of current node
     * @param current
     * @return all neighbours
     */
    private Node[] getNeighbour(Node current) {
        List<Node> neighbour = new LinkedList<>();
        int x = current.x;
        int y = current.y;
        if (y != 0){
            neighbour.add(nodes[(y-1) * boardX + x]);
        }
        if (y != (boardY - 1)) {
            neighbour.add(nodes[(y+1) * boardX + x]);
        }
        if (x != 0) {
            neighbour.add(nodes[y * boardY + x - 1]);
        }
        if (x != (boardX - 1)) {
            neighbour.add(nodes[y * boardY + x + 1]);
        }
        return neighbour.toArray(new Node[0]);
    }

    /**
     * Takes in the next node and computes the direction
     * @param nextNode -> the node the snake should conquer next round
     * @return the direction
     */
    private int getDirection(Node nextNode) {
        if (nextNode == null) {
            return 10;
        }
        int nodeX = nextNode.x;
        int nodeY = nextNode.y;
        int xOffset = headX - nodeX;
        int yOffset = headY - nodeY;
        switch (xOffset){
            case 1: return LEFT;
            case -1: return RIGHT;
        }
        switch (yOffset){
            case 1: return UP;
            case -1: return DOWN;
        }
        System.out.println("Direction not found");
        return RIGHT;

    }

    /**
     * Get the second apple
     * @param board -> necessary board information
     * @return the field of the second apple
     */
    private Field getSecondApple(BoardInfo board) {
        List<Field> apples = board.getApples();
        for (Field f : apples) {
            if (f.getPosX() == appleX && f.getPosY() == appleY) {
                continue;
            }
            return f;
        }
        return null;
    }


    private class Node {
        int hcost;
        int x,y;
        public String type;
        Node parent = null;

        public Node(String t, int x, int y) {
            type = t;
            this.x = x;
            this.y = y;
            hcost = calcHCost();

        }

        private int calcHCost() {
            return ((Math.abs(appleY - y)) * (Math.abs(appleY - y))) + ((Math.abs(appleX - x)) * (Math.abs(appleX - x)));
        }

    }


}
