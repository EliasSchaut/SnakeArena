package snakes.contest.devilOfParadise;

import board.*;
import snakes.Snake;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * This is the snake which has to be filled with lovely intelligence code
 * to become the longest snake of all other snakes.
 *
 * This snake appears 'MySnake' times an board.
 * The value behind 'MySnake' can set in the property file (snake_arena.properties).
 */
public class MySnake extends Snake {

    public MySnake() {
        this.NAME = "DevilOfParadise";         // set your favorite name
        this.COLOR = new Color(0, 0, 255); // set your favorite color
        this.additionalBoardInformation = new AdditionalBoardInformation();
    }
    AdditionalBoardInformation additionalBoardInformation;
    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override
    public int think(BoardInfo board) {
        //  Performs all the necessary calculations.
        additionalBoardInformation.setBoardInfo(board);

        //  Returns the best direction for this snake.
        return additionalBoardInformation.chooseBestDirection();
//        PathFinding pathFinder = new PathFinding(board);
//        List<PathField> path = pathFinder.findPath(board.getOwnHead(), board.getNearestApple());
//        // System.out.println(path.size());
//
//        int moveDirection = pathFinder.getMoveDirection(board.getOwnHead(), path.get(0));
//
//        return moveDirection; // or LEFT, or DOWN, or UP
    }

    public static class AdditionalBoardInformation {

        // Proper Constructor implementation necessary.
        public AdditionalBoardInformation() {

            //Object that are both:
            var evaluateFlooding = new EvaluateFlooding(100f);

            // Thinking Objects
            thinkingObjects = new ArrayList<>();
            thinkingObjects.add(new BlockedFieldAnalysis());
            thinkingObjects.add(new NextToBoarderAnalysis());
            thinkingObjects.add(evaluateFlooding);
            thinkingObjects.add(new SnakeMovementAnalysis());
            thinkingObjects.add(new PathfinderAnalysis());

            //  Evaluating Objects
            evaluatingObjects = new ArrayList<>();

            evaluatingObjectsWithoutPathfinder = new ArrayList<>();
            evaluatingObjectsWithoutPathfinder.add(new EvaluateNextToBoarder(0.2f));
            evaluatingObjectsWithoutPathfinder.add(evaluateFlooding);
            evaluatingObjectsWithoutPathfinder.add(new SnakeMovementEvaluation(0.5f, 0f));

            var sum = new EvaluateSum(evaluatingObjectsWithoutPathfinder);

            evaluatingObjects.add(new EvaluatePathfinder(sum));

            //  This creates the final evaluate object for the direction.
            evaluateHead = new EvaluateHead(evaluatingObjects);
        }

        //  The board passed by the game. The new board information is passed each round.
        public BoardInfo boardInfo;

        //  This list contains all objects which should perform their Logic in a certain way.
        public List<ThinkingObject> thinkingObjects;

        //This Objects are used to evaluate the final direction.
        public List<EvaluatingObject> evaluatingObjects;
        public List<EvaluatingObject> evaluatingObjectsWithoutPathfinder;

        //  Here go the EvaluatingObjects.
        EvaluateHead evaluateHead;

        //  HashMaps that are used by further ThinkingObjects and/or in the chooseBestDirectionMethod.
        Set<Field> barrierBlocking = new HashSet<>();
        HashMap<Field, Integer> fieldClearedInXRounds = new HashMap<>();
        HashMap<Field, Float> nextToBorderAnalysis = new HashMap<>();
        HashMap<Field, HashMap<Field, Integer>> snakeMovement = new HashMap<>();

        HashMap<Field, List<Field>> ownAppleMap = new HashMap<>();
        HashMap<Field, HashMap<Field, List<Field>>> otherSnakesAppleMap = new HashMap<>();

        //  Some example values, how they might look.
    //    public float weightBestApple = 2.0f;
    //    public float weightNearestApple = 5.0f;

        public int chooseBestDirection() {
            var boardInfo = this.boardInfo;
            var head = boardInfo.getOwnHead();

            Set<EvaluateField> possibleFields = new HashSet<>();
            if (boardInfo.isNextStepFree(UP)) {
                possibleFields.add(new EvaluateField(head.getPosX(), head.getPosY() - 1, UP, this));
            }
            if (boardInfo.isNextStepFree(DOWN)) {
                possibleFields.add(new EvaluateField(head.getPosX(), head.getPosY() + 1, DOWN, this));
            }
            if (boardInfo.isNextStepFree(RIGHT)) {
                possibleFields.add(new EvaluateField(head.getPosX() + 1, head.getPosY(), RIGHT, this));
            }
            if (boardInfo.isNextStepFree(LEFT)) {
                possibleFields.add(new EvaluateField(head.getPosX() - 1, head.getPosY(), LEFT, this));
            }

            var min = possibleFields.stream().max(EvaluateField::compareTo);

            System.out.println("-----------------");
            if (min.isPresent()) {
                var field = min.get();
                for (EvaluateField f :possibleFields) {
                    if (f == field) {
                        System.out.println(" ->" + f.toString());
                    } else {
                        System.out.println("   " + f.toString());
                    }
                }
                System.out.println();
                return field.getDirection();
            }

            //Returns anything if there is no direction
            return UP;
        }

        //  This method performs all the necessary work on the thinking board information.
        public void setBoardInfo(BoardInfo boardInfo) {
            this.boardInfo = boardInfo;
            for (ThinkingObject t : thinkingObjects) {
                t.thinkWithBoard(this);
            }
        }


        public HashMap<Field, Integer> getFieldClearedInXRounds() {
            return fieldClearedInXRounds;
        }

        public Set<Field> getBarrierBlocking() {
            return barrierBlocking;
        }

        public void setFieldClearedInXRounds(HashMap<Field, Integer> fieldClearedInXRounds) {
            this.fieldClearedInXRounds = fieldClearedInXRounds;
        }

        public HashMap<Field, Float> getNextToBorderAnalysis() {
            return nextToBorderAnalysis;
        }

        public void setNextToBorderAnalysis(HashMap<Field, Float> nextToBorderAnalysis) {
            this.nextToBorderAnalysis = nextToBorderAnalysis;
        }

        public HashMap<Field, HashMap<Field, Integer>> getSnakeMovement() {
            return snakeMovement;
        }

        public void setSnakeMovement(HashMap<Field, HashMap<Field, Integer>> snakeMovement) {
            this.snakeMovement = snakeMovement;
        }

        public HashMap<Field, List<Field>> getOwnAppleMap() {
            return ownAppleMap;
        }

        public void setOwnAppleMap(HashMap<Field, List<Field>> ownAppleMap) {
            this.ownAppleMap = ownAppleMap;
        }

        public HashMap<Field, HashMap<Field, List<Field>>> getOtherSnakesAppleMap() {
            return otherSnakesAppleMap;
        }

        public void setOtherSnakesAppleMap(HashMap<Field, HashMap<Field, List<Field>>> otherSnakesAppleMap) {
            this.otherSnakesAppleMap = otherSnakesAppleMap;
        }
    }

    public static class EvaluateField implements Comparable<EvaluateField> {

        private final int direction;
        private final int posX;
        private final int posY;
        private final float evaluation;

        public EvaluateField(int posX, int posY, int direction, AdditionalBoardInformation additionalBoardInformation) {
            this.posX = posX;
            this.posY = posY;
            this.evaluation = additionalBoardInformation
                    .evaluateHead
                    .returnEvaluationFor(additionalBoardInformation.boardInfo.getFields()[posX][posY], additionalBoardInformation, 0);
            this.direction = direction;
        }

        public int getDirection() {
            return direction;
        }

        public int getPosX() {
            return posX;
        }

        public int getPosY() {
            return posY;
        }

        public float getEvaluation() {
            return evaluation;
        }

        @Override
        public int compareTo(EvaluateField o) {
            return Float.compare(this.getEvaluation(), o.getEvaluation());
        }

        @Override
        public String toString() {
            return " ("+ posX + "|" + posY + ")\t" + evaluation;
        }
    }

    //  This EvaluationObject is finally responsible to evaluate the next step.
    //  It uses all the objects added in the Initializer.
    public static class EvaluateHead implements EvaluatingObject {
        public EvaluateHead(List<EvaluatingObject> evaluatingObjects) { this.uses = evaluatingObjects; }

        private List<EvaluatingObject> uses;

        @Override
        public float returnEvaluationFor(Field field, AdditionalBoardInformation additionalBoardInformation, int inXRounds) {
            return uses.stream()
                    .map(evaluatingObject -> evaluatingObject.returnEvaluationFor(field, additionalBoardInformation, inXRounds))
                    .reduce(0.0f, (aFloat, aFloat2) -> (aFloat.floatValue() + aFloat2.floatValue()));
        }
    }

    //  This interface is used to evaluate a field in a specific way.
    public static interface EvaluatingObject {
        float returnEvaluationFor(Field field, AdditionalBoardInformation additionalBoardInformation, int inXRounds);
    }

    public static interface ThinkingObject {
        void thinkWithBoard(AdditionalBoardInformation additionalBoardInformation);
    }

    //  This class has to do stuff during thinking phase.
    //  This class evalates a field, and calculates how many fields can be entered by this.
    //  Returns the value of free places (places - snake lenght) or (-Float.MaxValue+AvailableSpace) if the snake would be to long, to enter this field.
    //  Each occupied snake place inside hals the value.
    public static class EvaluateFlooding implements EvaluatingObject, ThinkingObject {
        public EvaluateFlooding(float weight) {
            this.weight = weight;
        }

        @Override
        public void thinkWithBoard(AdditionalBoardInformation additionalBoardInformation) {
            sharedFields = new HashMap<>();
        }

        private float weight;
        private HashMap<Set<Field>, Float> sharedFields = new HashMap<>();

        @Override
        public float returnEvaluationFor(Field field, AdditionalBoardInformation additionalBoardInformation, int inXRounds) {
            for (Set<Field> shared : sharedFields.keySet()) {
                if (shared.contains(field)) {
                    return sharedFields.get(shared);
                }
            }
            var finalSet = new HashSet<Field>();
            var info = new AdditionalFloodingInformation();
            floodingField(field, additionalBoardInformation, finalSet, info);
            var result = calculateResult(finalSet, info, additionalBoardInformation.boardInfo.getOwnSnake().size());
            sharedFields.put(finalSet, result);
    //        System.out.println(field.getPosX() + "|" + field.getPosY() + ": " + result);
            return result;
        }

        //  Snake Fields are counted and have to be removed afterwards!
        private void floodingField(Field field, AdditionalBoardInformation additionalBoardInformation, Set<Field> set, AdditionalFloodingInformation additionalFloodingInformation) {
            set.add(field);
            var neighbours = GeneralMethods.getNeighbourFields(field, additionalBoardInformation.boardInfo);
            var filter = neighbours.stream().filter((f) -> {
                return !additionalBoardInformation.getBarrierBlocking().contains(f) && !set.contains(f);
            }).collect(Collectors.toList());

            for (Field f:filter) {
                if (f.getState() == FieldState.Barrier || f.getState() == FieldState.Outside) {
                    continue;
                }

                set.add(f);
                if (f.getState() == FieldState.Apple) {
                    additionalFloodingInformation.applesCount++;
                    floodingField(f, additionalBoardInformation, set, additionalFloodingInformation);
                } else if (f.getState() == FieldState.Empty) {
                    floodingField(f, additionalBoardInformation, set, additionalFloodingInformation);
                } else if (f.getState() == FieldState.Snake){
                    additionalFloodingInformation.snakeBodyCount++;
                }
            }
        }

        private float calculateResult(Set<Field> fields, AdditionalFloodingInformation additionalFloodingInformation, int snakeLength) {
            var size = (fields.size() - additionalFloodingInformation.snakeBodyCount);
            System.out.println("S:" + size);
            if (size > snakeLength) {
                return 0;//((float) additionalFloodingInformation.applesCount * 3.0f + (float) additionalFloodingInformation.snakeBodyCount) * weight;
            } else {
                return ((float) size - (float) additionalFloodingInformation.applesCount - (float) additionalFloodingInformation.snakeBodyCount) * weight;
            }
        }

        private class AdditionalFloodingInformation {
            int applesCount = 0;
            int snakeBodyCount = 0;
        }
    }

    public static class EvaluateNextToBoarder implements EvaluatingObject {

        public EvaluateNextToBoarder(float weightNoBorder) {
            this.noBoarder = weightNoBorder;
        }

        public EvaluateNextToBoarder() {
            this(10f);
        }

        private float noBoarder;

        @Override
        public float returnEvaluationFor(Field field, AdditionalBoardInformation additionalBoardInformation, int inXRounds) {
            return additionalBoardInformation.getNextToBorderAnalysis().getOrDefault(field, noBoarder);
        }
    }

    public static class EvaluatePathfinder implements EvaluatingObject {

        public EvaluatePathfinder(EvaluatingObject evaluatingObject) {
            this.evaluatingObject = evaluatingObject;
        }

        private EvaluatingObject evaluatingObject;

        @Override
        public float returnEvaluationFor(Field field, AdditionalBoardInformation additionalBoardInformation, int inXRounds) {
            PathFinder pathFinding = new PathFinder(additionalBoardInformation, evaluatingObject);

            var listSorted = additionalBoardInformation.getOwnAppleMap().entrySet().stream().sorted((a, b) ->{
                return  Integer.compare(a.getValue().size(), b.getValue().size());
            }).collect(Collectors.toList());

            A: for (Map.Entry<Field, List<Field>> apple:listSorted) {
                for (HashMap<Field, List<Field>> snakes:additionalBoardInformation.getOtherSnakesAppleMap().values()) {
                    if (!snakes.containsKey(apple.getKey()) || apple.getValue().size() > snakes.get(apple.getKey()).size()) {
                        continue A;
                    }
                }
                var p = pathFinding.findPath(additionalBoardInformation.boardInfo.getOwnHead(), apple.getKey());
                if (p != null) {
                    var first = p.stream().findFirst();
                    if (first.isPresent() && GeneralMethods.getRealField(first.get(), additionalBoardInformation.boardInfo) == field) {
                        return Float.MAX_VALUE - p.stream().count();
                    }
                }
            }

            var min = additionalBoardInformation.getOwnAppleMap().entrySet().stream().min(Comparator.comparingInt(e -> e.getValue().size()));

            if (min.isPresent()) {
                var p = pathFinding.findPath(additionalBoardInformation.boardInfo.getOwnHead(), min.get().getKey());
                if (p != null) {

                    var first = p.stream().findFirst();
                    if (first.isPresent() && GeneralMethods.getRealField(first.get(), additionalBoardInformation.boardInfo) == field) {
                        return Float.MAX_VALUE - p.stream().count();
                    }
                }
            }
            var p = pathFinding.findPath(additionalBoardInformation.boardInfo.getOwnHead(), additionalBoardInformation.boardInfo.getNearestApple());
            if (p == null) {
                return 0;
            }
            var first = p.stream().findFirst();
            if(first.isPresent() && GeneralMethods.getRealField(first.get(), additionalBoardInformation.boardInfo) == field) {
                return Float.MAX_VALUE - p.stream().count();
            }
            return 0;
        }
    }

    public static class EvaluateSum implements EvaluatingObject {
        public EvaluateSum(List<EvaluatingObject> evaluatingObjects) { this.uses = evaluatingObjects; }
        private List<EvaluatingObject> uses;

        @Override
        public float returnEvaluationFor(Field field, AdditionalBoardInformation additionalBoardInformation, int inXRounds) {
            return uses.stream()
                    .map(evaluatingObject -> evaluatingObject.returnEvaluationFor(field, additionalBoardInformation, inXRounds))
                    .reduce(0.0f, (aFloat, aFloat2) -> (aFloat.floatValue() + aFloat2.floatValue()));
        }
    }

    public static class EvaluluateEnterField implements EvaluatingObject {

        //  additionalRoundsExpected deals with delay when collecting an apple.
        //  maxRounds gives the number of rounds when there is the possibility that this might not happen, e.g. snake dies.
        public EvaluluateEnterField(int additionalRoundsExpected, int maxRounds) {
            this.additionalRoundsExpected = additionalRoundsExpected;
            this.maxRounds = maxRounds;
        }
        private int additionalRoundsExpected;
        private int maxRounds;

        @Override
        public float returnEvaluationFor(Field field, AdditionalBoardInformation additionalBoardInformation, int inXRounds) {
            if (additionalBoardInformation.getBarrierBlocking().contains(field)) {
                return Float.POSITIVE_INFINITY;
            }
            var freeInXRounds = additionalBoardInformation.getFieldClearedInXRounds().getOrDefault(field, 0);
            if (freeInXRounds <= inXRounds && freeInXRounds < maxRounds) {
                return 0;
            }
            return Float.POSITIVE_INFINITY;
        }
    }

    //  Returns a value, how likely it is that a different snake reaches that place before.
    public static class SnakeMovementEvaluation implements EvaluatingObject {

        public SnakeMovementEvaluation() {
            this(1f, 10f);
        }

        public SnakeMovementEvaluation(float weight, float preferFreePlaces) {
            this.weight = weight;
            this.preferFreePlaces = preferFreePlaces;
        }

        private float weight;
        private float preferFreePlaces;

        @Override
        public float returnEvaluationFor(Field field, AdditionalBoardInformation additionalBoardInformation, int inXRounds) {
            var allSnakes = additionalBoardInformation
                    .getSnakeMovement()
                    .values()
                    .stream()
                    .map(fieldIntegerHashMap -> fieldIntegerHashMap.getOrDefault(field, 0)).collect(Collectors.toSet());

           var min = allSnakes.stream().min(Integer::compareTo);
           if (!min.isPresent()) {
               return preferFreePlaces;
           }
           if (min.get() < inXRounds) {
               return preferFreePlaces;
           } else {
               return (float) allSnakes.stream().reduce(0, new BinaryOperator<Integer>() {
                   @Override
                   public Integer apply(Integer integer, Integer integer2) {
                       return integer + integer2 > inXRounds ? 0 : integer2;
                   }
               }) * weight;
           }
        }
    }

    public static class KeySnakeApple {
        Field apple;
        Field snakeHead;

        public KeySnakeApple(Field apple, Field snakeHead) {
            this.apple = apple;
            this.snakeHead = snakeHead;
        }
    }

    public static class PathAnalysis implements ThinkingObject {

        @Override
        public void thinkWithBoard(AdditionalBoardInformation additionalBoardInformation) {
            var boardInfo = additionalBoardInformation.boardInfo;
            var apples = boardInfo.getApples();
            var snakeHeads = boardInfo.getOtherHeads();

            // var snakePaths = additionalBoardInformation.getSnakePaths();
            // snakePaths = new HashMap<>();

            PathFinder pathFinder = new PathFinder(additionalBoardInformation);

            for (Field snakeHead : snakeHeads) {
                for (Field apple : apples) {
                    // snakePaths.put(new KeySnakeApple(apple, snakeHead), pathFinder.findPath(apple, snakeHead));
                }
            }

        }
    }

    public static class PathField extends Field {

        private int gCost;
        private int hCost;
        private PathField parent;
        private Field field;

        // initialize basic PathField from Field
        public PathField(Field field) {
            super(field.getPosX(), field.getPosY());
            this.gCost = 0;
            this.hCost = 0;
            this.field = field;
        }

        public int getFCost() {
            return this.gCost + this.hCost;
        }

        public int getGCost() {
            return this.gCost;
        }

        public void setGCost(int gCost) {
            this.gCost = gCost;
        }

        public int getHCost() {
            return this.hCost;
        }

        public void setHCost(int hCost) {
            this.hCost = hCost;
        }

        public PathField getParent() {
            return this.parent;
        }

        public void setParent(PathField parent) {
            this.parent = parent;
        }

        public Field getField() {
            return this.field;
        }
    }

    public static class PathFinder {

        private Map<Field, PathField> pathFieldMap;
        private Optional<EvaluatingObject> evaluatingObject;
        private AdditionalBoardInformation board;

        public PathFinder(AdditionalBoardInformation board) {
            this.board = board;
            this.evaluatingObject = Optional.empty();
        }

        public PathFinder(AdditionalBoardInformation board, EvaluatingObject evaluatingObject) {
            this.board = board;
            this.evaluatingObject = Optional.of(evaluatingObject);
        }

        private void createNewPathFieldMap() {
            this.pathFieldMap = new HashMap<>();
        }

        /**
         * Implementation of the A* path finding algorithm
         *
         * @param start
         * @param target
         * @return the shortest path found as a list of fields; null if no path found
         */
        public List<PathField> findPath(Field start, Field target) {
            this.createNewPathFieldMap();
            PathField startField = new PathField(start);
            PathField targetField = new PathField(target);
            this.pathFieldMap.put(start, startField);
            this.pathFieldMap.put(target, targetField);

            List<PathField> openSet = new ArrayList<>();
            Set<PathField> closedSet = new HashSet<>();
            openSet.add(startField);

            while(openSet.size() > 0) {
                PathField currentField = openSet.get(0);
                for (PathField openField : openSet) {
                    if (openField.getFCost() < currentField.getFCost()
                        || openField.getFCost() == currentField.getFCost() && openField.getHCost() < currentField.getHCost()) {
                        currentField = openField;
                    }
                }
                openSet.remove(currentField);
                closedSet.add(currentField);
                // System.out.println("currentField position: " + String.valueOf(currentField.getPosX()) + "|" + String.valueOf(currentField.getPosY()));

                if (currentField == targetField) {
                    return retracePath(startField, targetField);
                }

                // only exclude *static* non-walkable fields -> use getBarrierBlocking
                Set<Field> neighbourFields = GeneralMethods.getNonBarrierBlockingNeighbours(currentField, this.board);
                for (Field neighbourField : neighbourFields)  {
                    if (!this.pathFieldMap.containsKey(neighbourField)) {
                        this.pathFieldMap.put(neighbourField, new PathField(neighbourField));
                    }
                    PathField neighbour = this.pathFieldMap.get(neighbourField);
                    if (closedSet.contains(neighbour)) {
                        continue;
                    }
                    int newCostToNeighbour = currentField.getGCost() + GeneralMethods.getManhattanDistance(currentField, neighbour);

                    if (evaluatingObject.isPresent()) {
                        newCostToNeighbour += evaluatingObject.get().returnEvaluationFor(neighbourField, board, 0);
                    }

                    if (newCostToNeighbour < neighbour.getGCost() || !openSet.contains(neighbour)) {
                        neighbour.setGCost(newCostToNeighbour);
                        neighbour.setHCost(GeneralMethods.getManhattanDistance(neighbour, targetField));
                        neighbour.setParent(currentField);
                        // check free in X rounds
                        var retracedPath = this.retracePath(startField, neighbour);


                        Boolean neighbourIsClearedInXRounds = true;
                        if (neighbourField.getState() == FieldState.Snake && board.getFieldClearedInXRounds().containsKey(neighbourField)) {
                            neighbourIsClearedInXRounds = retracedPath.size() > board.getFieldClearedInXRounds().get(neighbourField);
    //                         System.out.println("(" + neighbour.getPosX() + "|" + neighbour.getPosY() + ")");
                            // System.out.println(retracedPath.size());
                            // System.out.println(board.getFieldClearedInXRounds().get(neighbourField));
                            // System.out.println("---");
                        }
                        if (!openSet.contains(neighbour) && neighbourIsClearedInXRounds) {
                            openSet.add(neighbour);
                        }
                    }
                }
            }
            return null;
        }

        private List<PathField> retracePath(PathField startField, PathField targetField) {
            List<PathField> path = new ArrayList<>();
            PathField currentField = targetField;
            // System.out.println("target field position: " + String.valueOf(targetField.getPosX()) + "|" + String.valueOf(targetField.getPosY()));

            while (currentField != startField) {
                path.add(currentField);
                currentField = currentField.getParent();
            }
            Collections.reverse(path);

            return path;
        }

    }

    public static class BlockedFieldAnalysis implements ThinkingObject {
        @Override
        public void thinkWithBoard(AdditionalBoardInformation additionalBoardInformation) {
            var boardInfo = additionalBoardInformation.boardInfo;
            var barrier = boardInfo.getBarrier();
            var snakes = boardInfo.getOtherSnakes();
            var ownSnake = boardInfo.getOwnSnake();


            //  Analysis only barriers:
            var newBarriers = barrier.stream().filter((f) -> !additionalBoardInformation.getBarrierBlocking().contains(f)).collect(Collectors.toList());
    //        additionalBoardInformation.getBarrierBlocking().addAll(barrier);

            for(Field f : newBarriers) {
                additionalBoardInformation.getBarrierBlocking().add(GeneralMethods.getRealField(f, additionalBoardInformation.boardInfo));
                var possibleNeighbours = GeneralMethods.getNeighbourFields(f, additionalBoardInformation.boardInfo);
                var filterNeighbours = possibleNeighbours.stream().filter(field -> ((field.isFree() || (field.getState() == FieldState.Snake)) && !additionalBoardInformation.getBarrierBlocking().contains(field))).collect(Collectors.toList());
                for (Field neighbour:filterNeighbours) {
                    testCanEnterField(neighbour, additionalBoardInformation);
                }
            }

            //  Analysis with snakes:

            var length = 1;
            for (Field f:ownSnake) {
                additionalBoardInformation.getFieldClearedInXRounds().put(GeneralMethods.getRealField(f, additionalBoardInformation.boardInfo), length);
                length++;
            }
            for(LinkedList<Field> s:snakes) {
                length = 1;
                for (Field f:s) {
                    additionalBoardInformation.getFieldClearedInXRounds().put(GeneralMethods.getRealField(f, additionalBoardInformation.boardInfo), length);
                    length++;
                }
            }

    //        for (LinkedList<Field> s:snakes) {
    //            System.out.println("......");
    //            for (Field e:s) {
    //                System.out.println("(" + e.getPosX() + "|" + e.getPosY() + ") :" + additionalBoardInformation.getFieldClearedInXRounds().get(e));
    //            }
    //        }

    //        for (LinkedList<Field> s:snakes) {
    //            for (Field f:s) {
    //                testSnakeNeighbour(f, additionalBoardInformation, additionalBoardInformation.getFieldClearedInXRounds().get(f));
    //            }
    //        }
    //
    //        for (Field f:ownSnake) {
    //            testSnakeNeighbour(f, additionalBoardInformation, additionalBoardInformation.getFieldClearedInXRounds().get(f));
    //        }

            //DEBUG


        }

        private void testCanEnterField(Field f, AdditionalBoardInformation additionalBoardInformation) {
            var possibleNeighbours = GeneralMethods.getNeighbourFields(f, additionalBoardInformation.boardInfo);
            var filterNeighbours = possibleNeighbours.stream().filter(field -> ((field.isFree() || (field.getState() == FieldState.Snake)) && !additionalBoardInformation.getBarrierBlocking().contains(field))).collect(Collectors.toList());
    //        System.out.println(f.getPosX() + "|" + f.getPosY() + ":" +filterNeighbours.size());

            if (filterNeighbours.size() < 2) {
                additionalBoardInformation.getBarrierBlocking().add(f);
                for (Field neighbour:filterNeighbours) {
                    testCanEnterField(neighbour, additionalBoardInformation);
                }
                return;
            }
        }
    //    //  Tests whether it makes sense to enter a field in X rounds.
    //    private void testSnakeNeighbour(Field f, AdditionalBoardInformation additionalBoardInformation, int maxNeighbour) {
    //        var ownMaxValue = additionalBoardInformation.getFieldClearedInXRounds().getOrDefault(f, 0);
    //        // all possible directions to enter this field.
    //        var possibleNeighbours = GeneralMethods.getNeighbourFields(f, additionalBoardInformation.boardInfo);
    //        // The neighbours that are free to enter.
    //        var filterNeighbours = possibleNeighbours.stream().filter(field -> (field.isFree() && !additionalBoardInformation.getBarrierBlocking().contains(field))).collect(Collectors.toList());
    //
    //        var maxRoundsToLeave = filterNeighbours.stream().mapToInt((field) -> {
    //            return additionalBoardInformation.getFieldClearedInXRounds().getOrDefault(field, 0);
    //        }).reduce(ownMaxValue, (i, field) -> {
    //            return Integer.min(i, field);
    //        });
    //        maxRoundsToLeave--;
    //
    //        if (!additionalBoardInformation.getFieldClearedInXRounds().containsKey(f) && maxRoundsToLeave > 0) {
    //            additionalBoardInformation.getFieldClearedInXRounds().put(f, maxRoundsToLeave);
    //            for (Field field : filterNeighbours) {
    //                testSnakeNeighbour(field, additionalBoardInformation);
    //            }
    //        }
    //    }

    //    private void testNeighbourFields(Field f, AdditionalBoardInformation additionalBoardInformation) {
    //        var boardInfo = additionalBoardInformation.boardInfo;
    //        var barrierFields = additionalBoardInformation.getBarrierBlocking();
    //        //method: Test neighbours
    //        for (Field n:GeneralMethods.getNeighbourFields(f, boardInfo)) {
    //            var borderCount = GeneralMethods.getNeighbourFields(n, boardInfo).stream().filter(field -> {
    //                return (field.isFree() || field.getState() == FieldState.Snake) && !barrierFields.contains(field);
    //            }).collect(Collectors.toList()).size();
    //
    //            if (borderCount < 2) {
    //                barrierFields.add(n);
    //                if (!barrierFields.contains(n)) {
    //                    testNeighbourFields(n, additionalBoardInformation);
    //                }
    //                break;
    //            }
    //        }
    //    }
    //
    //    private int testSnakeNeighbour(Field f, AdditionalBoardInformation additionalBoardInformation, int freeInXRounds) {
    //        var boardInfo = additionalBoardInformation.boardInfo;
    //        var barrierFields = additionalBoardInformation.getBarrierBlocking();
    //        var freeInXRoundsMap = additionalBoardInformation.getFieldClearedInXRounds();
    //
    //        for (Field n:GeneralMethods.getNeighbourFields(f, boardInfo)) {
    //            var borderCount = GeneralMethods.getNeighbourFields(n, boardInfo).stream().filter(field -> {
    //                var free = freeInXRoundsMap.getOrDefault(n, 0);
    //                return (field.isFree()) && !barrierFields.contains(field) && (free <= freeInXRounds);
    //            }).collect(Collectors.toList()).size();
    //        }
    //        return 0;
    //    }

    }

    public static class NextToBoarderAnalysis implements ThinkingObject {

        public NextToBoarderAnalysis(float weight) {
            this.weight = weight;
        }

        public NextToBoarderAnalysis() {
            this(100f);
        }

        private float weight;

        @Override
        public void thinkWithBoard(AdditionalBoardInformation additionalBoardInformation) {
            additionalBoardInformation.setNextToBorderAnalysis(new HashMap<>());

            for (Field f:additionalBoardInformation.getBarrierBlocking()) {
                addToBorder(GeneralMethods.getRealField(f, additionalBoardInformation.boardInfo), additionalBoardInformation, weight);
            }
            for (Field f: additionalBoardInformation.boardInfo.getOwnSnake()) {
                addToBorder(GeneralMethods.getRealField(f, additionalBoardInformation.boardInfo), additionalBoardInformation, weight/2f);
            }
            for (LinkedList<Field> s:additionalBoardInformation.boardInfo.getOtherSnakes()) {
                for (Field f:s) {
                    addToBorder(GeneralMethods.getRealField(f, additionalBoardInformation.boardInfo), additionalBoardInformation, weight);
                }
            }


    //        System.out.println("-------------");
    //        for (Map.Entry<Field, Float> e:additionalBoardInformation.getNextToBorderAnalysis().entrySet()) {
    //            System.out.println(e.getKey().getPosX() + "|" + e.getKey().getPosY() + ": " + e.getValue());
    //        }
        }

        private void addToBorder(Field f, AdditionalBoardInformation additionalBoardInformation, float weight) {
            for (Field neighbour:GeneralMethods.getNeighbourFields(f, additionalBoardInformation.boardInfo)) {
                if (additionalBoardInformation.getNextToBorderAnalysis().containsKey(neighbour)) {
                    continue;
                }

                var neighbours = GeneralMethods.getNeighbourFields(neighbour, additionalBoardInformation.boardInfo);
                var neighboursFreeToEnter = neighbours.stream().filter((field -> (!additionalBoardInformation.getBarrierBlocking().contains(field)))).collect(Collectors.toList());
                if (neighboursFreeToEnter.size() > 2) {
                    additionalBoardInformation.getNextToBorderAnalysis().put(neighbour, 0f);

    //                    //All fields that might be able to enter should be tested for its corner.
    //                    for (Field neighbour2:GeneralMethods.getNeighbourFields(neighbour, additionalBoardInformation.boardInfo)) {
    //                        if (additionalBoardInformation.getNextToBorderAnalysis().containsKey(neighbour2)) {
    //                            continue;
    //                        }
    //
    //                        var neighbours3 = GeneralMethods.getNeighbourFields(neighbour2, additionalBoardInformation.boardInfo);
    //                        var neighboursFreeToEnter2 = neighbours3.stream().filter((field -> (!additionalBoardInformation.getBarrierBlocking().contains(field) && field.isFree()))).collect(Collectors.toList());
    //                        if (neighboursFreeToEnter2.size() > 2) {
    //                            additionalBoardInformation.getNextToBorderAnalysis().put(neighbour2, Float.MAX_VALUE/2);
    //                        }
    //                    }
                } else {
                    additionalBoardInformation.getNextToBorderAnalysis().put(neighbour, weight);
                }
            }
        }
    }

    public static class PathfinderAnalysis implements ThinkingObject {

        @Override
        public void thinkWithBoard(AdditionalBoardInformation additionalBoardInformation) {
            var pathFinder = new PathFinder(additionalBoardInformation);
            additionalBoardInformation.setOtherSnakesAppleMap(findApplesOtherSnakes(pathFinder, additionalBoardInformation));

            var head = GeneralMethods.getRealField(additionalBoardInformation.boardInfo.getOwnHead(), additionalBoardInformation.boardInfo);
            additionalBoardInformation.setOwnAppleMap(findAllApplesForSnake(head, pathFinder, additionalBoardInformation));
        }

        private HashMap<Field,HashMap<Field, List<Field>>> findApplesOtherSnakes(PathFinder pathFinding, AdditionalBoardInformation additionalBoardInformation) {
            var map = new HashMap<Field, HashMap<Field, List<Field>>>();
            for (Field s:additionalBoardInformation.boardInfo.getOtherSnakes().stream().map(LinkedList::getLast).collect(Collectors.toList())) {
                map.put(s, findAllApplesForSnake(s, pathFinding, additionalBoardInformation));
            }
            return map;
        }

        private HashMap<Field, List<Field>> findAllApplesForSnake(Field head, PathFinder pathFinding, AdditionalBoardInformation additionalBoardInformation) {
            var map = new HashMap<Field, List<Field>>();
            for (Field apple:additionalBoardInformation.boardInfo.getApples()) {
                findWay(head, apple, pathFinding, additionalBoardInformation).ifPresent(p-> map.put(apple, p));
            }
            return map;
        }

        private Optional<List<Field>> findWay(Field from, Field to, PathFinder pathFinding, AdditionalBoardInformation additionalBoardInformation) {
            var path = Optional.ofNullable(pathFinding.findPath(from, to));
            if (path.isPresent()) {
                var p = path.get();
                var list = p.stream().map(f -> {
                    var v = f;
                    return GeneralMethods.getRealField(f, additionalBoardInformation.boardInfo);
                }).collect(Collectors.toList());
                return Optional.ofNullable(list);
            } else {
                return Optional.empty();
            }
        }
    }

    public static class SnakeMovementAnalysis implements ThinkingObject {
        @Override
        public void thinkWithBoard(AdditionalBoardInformation additionalBoardInformation) {
            var otherSnakes = additionalBoardInformation.boardInfo.getOtherSnakes();
            additionalBoardInformation.getSnakeMovement().clear();
            for(int x = 0; x < otherSnakes.size(); x++) {
                var head = GeneralMethods.getRealField(otherSnakes.get(x).getLast(), additionalBoardInformation.boardInfo);
                var reached = new HashMap<Field, Integer>();
                reachInXRounds(reached, new HashSet<>(), 1, additionalBoardInformation);
                additionalBoardInformation.getSnakeMovement().put(head, reached);
            }
        }

        private void reachInXRounds(HashMap<Field, Integer> reached, Set<Field> reachThisRound, int actualRound, AdditionalBoardInformation additionalBoardInformation) {
            if (reachThisRound.size() == 0) {
                return;
            }
            var newFields = new HashSet<Field>();
            var keySet = reached.keySet();
            for (Field f:reachThisRound) {
                if (keySet.contains(f)) {
                    continue;
                } else {
                    reached.put(f, actualRound);
                }
                switch (f.getState()) {
                    case Barrier:
                    case Outside:
                        continue;
                    case Snake:
                        if (actualRound > additionalBoardInformation.getFieldClearedInXRounds().get(f)) {
                            continue;
                        }
                    case Empty:
                    case Apple:
                        newFields.addAll(GeneralMethods.getNeighbourFields(f, additionalBoardInformation.boardInfo));
                }
            }
            reachInXRounds(reached, newFields, actualRound++, additionalBoardInformation);
        }
    }

    public static class GeneralMethods {

        //  This methods returns all possible neighbours of this field.
        public static Set<Field> getNeighbourFields(Field field, BoardInfo boardInfo) {
            Set<Field> set = new HashSet<>();
            if (field.getPosX() + 1 < boardInfo.getSIZE_X()) {
                set.add(boardInfo.getFields()[field.getPosX() + 1][field.getPosY()]);
            }
            if (field.getPosY() + 1 < boardInfo.getSIZE_Y()) {
                set.add(boardInfo.getFields()[field.getPosX()][field.getPosY() + 1]);
            }
            if (field.getPosX() > 0) {
                set.add(boardInfo.getFields()[field.getPosX() - 1][field.getPosY()]);
            }
            if (field.getPosY() > 0) {
                set.add(boardInfo.getFields()[field.getPosX()][field.getPosY() - 1]);
            }
            return set;
        }

        //  This method returns all non-barrier-blocking neighbours.
        public static Set<Field> getNonBarrierBlockingNeighbours(Field field, AdditionalBoardInformation board) {
            var set = getNeighbourFields(field, board.boardInfo);
            return set.stream().filter(f -> !board.getBarrierBlocking().contains(f)).collect(Collectors.toSet());
        }

        // This method returns the so-called "manhattan distance" between two fields
        public static int getManhattanDistance(Field fieldA, Field fieldB) {
            return Math.abs(fieldA.getPosX() - fieldB.getPosX()) + Math.abs(fieldA.getPosY() - fieldB.getPosY());
        }

        // This method returns the snake direction given head & neighbour field
        public static int getMoveDirection(Field head, Field neighbour) {
            int deltaX = neighbour.getPosX() - head.getPosX();
            int deltaY = neighbour.getPosY() - head.getPosY();

            int moveDirection = 0;
            if (deltaX == 1) {
                moveDirection = RIGHT;
            } else if (deltaX == -1) {
                moveDirection = LEFT;
            } else if (deltaY == 1) {
                moveDirection = DOWN;
            } else if (deltaY == -1) {
                moveDirection = UP;
            }

            return moveDirection;
        }

        public static Field getRealField(Field field, BoardInfo boardInfo) {
            return boardInfo.getFields()[field.getPosX()][field.getPosY()];
        }
    }
}
