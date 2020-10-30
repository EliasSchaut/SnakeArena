package snakes.contest.norbert;

import java.lang.Math;
import board.*;
import snakes.Snake;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Dijkstra {

    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {

        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();
        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeigh = adjacencyPair.getValue();

                if (!settledNodes.contains(adjacentNode)) {
                    CalculateMinimumDistance(adjacentNode, edgeWeigh, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static void CalculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }
}
class Graph {

    private Set<Node> nodes = new HashSet<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Node getNodeAt(int x, int y) {
        Iterator<Node> it = this.nodes.iterator();

        while(it.hasNext()) {
            Node current = it.next();
            if (current.getPosX() == x && current.getPosY() == y) {
                return current;
            }
        }
        return new Node(-1, -1);
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }
}

class Node {

    private int posX;

    private int posY;

    private LinkedList<Node> shortestPath = new LinkedList<>();

    private Integer distance = Integer.MAX_VALUE;

    private Map<Node, Integer> adjacentNodes = new HashMap<>();

    public Node(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public Integer getPosX() {
        return posX;
    }
    public Integer getPosY() {
        return posY;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(LinkedList<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

}


/**
 * This is the snake which has to be filled with lovely intelligence code
 * to become the longest snake of all other snakes.
 *
 * This snake appears 'MySnake' times an board.
 * The value behind 'MySnake' can set in the property file (snake_arena.properties).
 */
public class MySnake extends Snake {

    public MySnake() {
        this.NAME = "Norbert";         // set your favorite name
        this.COLOR = new Color(212, 116, 121); // set your favorite color
    }





    public int getIndexOfLargest( int[] array )
    {
        if ( array == null || array.length == 0 ) return -1; // null or empty

        int largest = 0;
        for ( int i = 1; i < array.length; i++ )
        {
            if ( array[i] > array[largest] ) largest = i;
        }
        return largest; // position of the first largest found
    }

    public int getIndexOfSmallest( int[] array )
    {
        if ( array == null || array.length == 0 ) return -1; // null or empty

        int smallest = 0;
        for ( int i = 1; i < array.length; i++ )
        {
            if ( array[i] < array[smallest] ) smallest = i;
        }
        return smallest;
    }

    public FieldState getFieldState(BoardInfo board, int posX, int posY) {
        try {
            return board.getFields()[posX][posY].getState();
        }
        catch (Exception e) {
            return FieldState.Outside;
        }

    }
    // Liefert groeße eines Raumes mit Startposition posX, posY
    public int determineRoomSize(BoardInfo board, int posX, int posY) {

        HashSet<int[]> mySet = new HashSet<int[]>();

        return freeFields(board, posX, posY, mySet);
    }

    public boolean existsInSet(HashSet<int[]> set, int posX, int posY) {

        Iterator<int[]> iterator = set.iterator();
        while (iterator.hasNext()) {
            int[] current = iterator.next();
            if (current[0] == posX && current[1] == posY) {
                return true;
            }
        }
        return false;
    }
    // Rekusive Funktion
    public int freeFields(BoardInfo board, int posX, int posY, HashSet<int[]> alreadyCounted) {
        int[] countedField = {posX, posY};

        if (!existsInSet(alreadyCounted, posX, posY) && isEmpty(board, posX, posY)) {
            alreadyCounted.add(countedField);
            return 1 + freeFields(board, posX+1, posY, alreadyCounted) + freeFields(board, posX-1, posY, alreadyCounted)+ freeFields(board, posX, posY+1, alreadyCounted)+ freeFields(board, posX, posY-1, alreadyCounted);
        }
        else {
            return 0;
        }
    }
    public boolean isEmpty(BoardInfo board, int posX, int posY) {
        return getFieldState(board, posX, posY).get() <= 1;
    }

    public boolean isClose(int aPosX, int aPosY, int bPosX, int bPosY, int distance) {
        return Math.abs(aPosX-bPosX) <= distance && Math.abs(aPosY-bPosY) <= distance;
    }

    public Graph getGraph(BoardInfo board, Field headPosition) {
        Graph graph = new Graph();
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if (isEmpty(board, x, y)) {
                    Node currentNode = new Node(x, y);
                    graph.addNode(currentNode);
                }
            }
        }

        Node headNode = new Node(headPosition.getPosX(), headPosition.getPosY());
        graph.addNode(headNode);

        List<Field> otherHeads = board.getOtherHeads();

        Iterator<Node> it = graph.getNodes().iterator();

        int snakeDistance = 1;
        int snakeWeight = 20;

        while (it.hasNext()) {
            Node current = it.next();

            if (isEmpty(board, current.getPosX()-1, current.getPosY())) {
                int distance = 1;
                for (Field head : otherHeads) {
                    if (isClose(head.getPosX(), head.getPosY(), current.getPosX()-1, current.getPosY(), snakeDistance)) {
                        distance += snakeWeight;
                    }
                }
                current.addDestination(graph.getNodeAt(current.getPosX()-1, current.getPosY()), distance);
            }
            if (isEmpty(board, current.getPosX()+1, current.getPosY())) {
                int distance = 1;
                for (Field head : otherHeads) {
                    if (isClose(head.getPosX(), head.getPosY(), current.getPosX()+1, current.getPosY(), snakeDistance)) {
                        distance += snakeWeight;
                    }
                }
                current.addDestination(graph.getNodeAt(current.getPosX()+1, current.getPosY()), distance);
            }
            if (isEmpty(board, current.getPosX(), current.getPosY()-1)) {
                int distance = 1;
                for (Field head : otherHeads) {
                    if (isClose(head.getPosX(), head.getPosY(), current.getPosX(), current.getPosY()-1, snakeDistance)) {
                        distance += snakeWeight;
                    }
                }
                current.addDestination(graph.getNodeAt(current.getPosX(), current.getPosY()-1), distance);
            }
            if (isEmpty(board, current.getPosX(), current.getPosY()+1)) {
                int distance = 1;
                for (Field head : otherHeads) {
                    if (isClose(head.getPosX(), head.getPosY(), current.getPosX(), current.getPosY()+1, snakeDistance)) {
                        distance += snakeWeight;
                    }
                }
                current.addDestination(graph.getNodeAt(current.getPosX(), current.getPosY()+1), distance);
            }
        }

        if (isEmpty(board, headPosition.getPosX()-1, headPosition.getPosY())) {
            headNode.addDestination(graph.getNodeAt(headPosition.getPosX()-1, headPosition.getPosY()), 1);
        }
        if (isEmpty(board, headPosition.getPosX()+1, headPosition.getPosY())) {
            headNode.addDestination(graph.getNodeAt(headPosition.getPosX()+1, headPosition.getPosY()), 1);
        }
        if (isEmpty(board, headPosition.getPosX(), headPosition.getPosY()-1)) {
            headNode.addDestination(graph.getNodeAt(headPosition.getPosX(), headPosition.getPosY()-1), 1);
        }
        if (isEmpty(board, headPosition.getPosX(), headPosition.getPosY()+1)) {
            headNode.addDestination(graph.getNodeAt(headPosition.getPosX(), headPosition.getPosY()+1), 1);
        }
        return graph;
    }

    private int[][] directions = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};

    private int getDirectionIndex(int xDelta, int yDelta) {
        for (int i = 0; i < 4; i++) {
            if (directions[i][0] == xDelta && directions[i][1] == yDelta) {
                return i;
            }
        }
        return -1;
    }

    private int suicideDirection(BoardInfo board) {
        for (int i=0; i<4; i++) {
            if (!board.isNextStepFree(i)) {
                return i;
            }
        }
        return -1;
    }


    private int trulyNearestAppleDirection(BoardInfo board, List<Field> apples, Field headPosition, Graph graph) {

        Dijkstra.calculateShortestPathFromSource(graph, graph.getNodeAt(headPosition.getPosX(), headPosition.getPosY()));

        int[] appleDistances = new int[2];

        for (int i=0; i < apples.size(); i++) {
            Field currentApple = apples.get(i);

            Node currentNode = graph.getNodeAt(currentApple.getPosX(), currentApple.getPosY());

            appleDistances[i] = currentNode.getDistance();
        }

        Field shortestAppleField = apples.get(getIndexOfSmallest(appleDistances));

        Node shortestAppleNode = graph.getNodeAt(shortestAppleField.getPosX(), shortestAppleField.getPosY());

        List<Node> path = shortestAppleNode.getShortestPath();

        path.add(shortestAppleNode);


        if (path.size() < 2) {
            return -1;
        } else {
            return getDirectionIndex(path.get(1).getPosX()-headPosition.getPosX(), path.get(1).getPosY()-headPosition.getPosY());
        }
        };

    private int longestSnakeSize = 0;



    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {

        // lovely intelligence code here
        Field nearestApple = board.getNearestApple();

        Field headPosition = board.getOwnHead();

        Graph fieldGraph = getGraph(board, headPosition);

        List<LinkedList<Field>> otherSnakes = board.getOtherSnakes();

        List<Field> mySnake = board.getOwnSnake();

        for (LinkedList<Field> otherSnake : board.getOtherSnakes()) {
            if (this.longestSnakeSize < otherSnake.size()) {
                this.longestSnakeSize = otherSnake.size();
            }
        }

        if (otherSnakes.size() == 1 && mySnake.size() - 1 > this.longestSnakeSize) {
            return suicideDirection(board);
        }

        ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            if (board.isNextStepFree(i)) {
                possibleMoves.add(i);
            }
        }
        if (possibleMoves.size() == 2) {

            int room0Size = determineRoomSize(board, headPosition.getPosX()+directions[possibleMoves.get(0)][0], headPosition.getPosY()+directions[possibleMoves.get(0)][1]);
            int room1Size = determineRoomSize(board, headPosition.getPosX()+directions[possibleMoves.get(1)][0], headPosition.getPosY()+directions[possibleMoves.get(1)][1]);

            if (room0Size > room1Size) {
                return possibleMoves.get(0);
            }
            else if (room0Size < room1Size) {
                return possibleMoves.get(1);
            }

        } else if (possibleMoves.size() == 3) {

            int[] roomSizesList = new int[3];

            roomSizesList[0] = determineRoomSize(board, headPosition.getPosX()+directions[possibleMoves.get(0)][0], headPosition.getPosY()+directions[possibleMoves.get(0)][1]);
            roomSizesList[1] = determineRoomSize(board, headPosition.getPosX()+directions[possibleMoves.get(1)][0], headPosition.getPosY()+directions[possibleMoves.get(1)][1]);
            roomSizesList[2] = determineRoomSize(board, headPosition.getPosX()+directions[possibleMoves.get(2)][0], headPosition.getPosY()+directions[possibleMoves.get(2)][1]);

            if (!(roomSizesList[0] == roomSizesList[1] && roomSizesList[1] == roomSizesList[2])) {
                return possibleMoves.get(getIndexOfLargest(roomSizesList));
            }
        }

        int appleDir = trulyNearestAppleDirection(board, board.getApples(), headPosition, fieldGraph);

        if (appleDir != -1) {
            return appleDir;
        }
        else {
            for (int i = 0; i < 4; i++) {
                if (board.isNextStepFree(i)) {
                    return i;
                }
            }
        }
        return UP;
    }

}