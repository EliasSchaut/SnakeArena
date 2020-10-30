package snakes.contest.snakeBy;

import board.*;
import snakes.Snake;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MySnake extends Snake {

    private int usedDirection;
    private BoardInfo boardInUse;

    public MySnake() {
        this.NAME = "SnakeBy";
        this.COLOR = new Color(213, 205, 13);

        usedDirection = RIGHT;
    }

    @Override
    public int think(BoardInfo board) {

        // -- COLOR :3
        int cInt = (int)Math.floor(Math.random()*125);
        this.COLOR = new Color(50+cInt, 13, 75+cInt);

        // -- SETUP [ UP , RIGHT , DOWN , LEFT ]
        boardInUse = board;
        Field ownHead = board.getOwnHead();
        List<Field> otherHeads = board.getOtherHeads();
        double[] MainList = { 1, 1, 1, 1 };
        double[] WallList = { 1, 1, 1, 1 };
        double[] NextRoundList = { 1, 1, 1, 1 };
        double[] AppleList = { 1, 1, 1, 1 };
        double[] TargetList = { 1, 1, 1, 1 };
        double[] ActingList = { 1, 1, 1, 1};
        double NextRoundWeightAsExponent = 3;
        double AppleWeight = 125;
        double OtherTargetWeight = 3;
        int whileBreakValue = 5;

        // -- SET WALL-LIST AND NEXT-ROUND-LIST
        for(int counter = 0; counter < 4; counter++) {
            Field newOwnHead;
            newOwnHead = getFieldByDirection(counter, ownHead);

            if(!board.isNextStepFree(counter)) { WallList[counter] = 0; NextRoundList[counter] = 0; }
            else {
                int freeCounter = countAroundFree(newOwnHead);
                if(freeCounter == 3) { freeCounter = 2; }
                NextRoundList[counter] = Math.pow(freeCounter, NextRoundWeightAsExponent);
            }
        }

        // -- SET APPLE-LIST
        boolean foundATarget = false;
        double LengthToTarget = 1000;
        for(Field appleField : board.getApples()) {
            int xDifOwn = appleField.getPosX()-ownHead.getPosX();
            int yDifOwn = appleField.getPosY()-ownHead.getPosY();
            double ownDifLength = Math.sqrt(xDifOwn*xDifOwn+yDifOwn*yDifOwn);
            double otherDifLength = ownDifLength+1;
            for(Field otherHead : otherHeads) {
                int xDifTemp = appleField.getPosX()-otherHead.getPosX();
                int yDifTemp = appleField.getPosY()-otherHead.getPosY();
                double DifLengthTemp = Math.sqrt(xDifTemp*xDifTemp+yDifTemp*yDifTemp);
                if(DifLengthTemp < otherDifLength) { otherDifLength = DifLengthTemp; }
            }
            if(ownDifLength < otherDifLength && ownDifLength < LengthToTarget) {
                double[] AppleReset = { 1, 1, 1, 1 };
                AppleList = AppleReset;
                if(xDifOwn != 0) { if(xDifOwn > 0) { AppleList[1] = AppleWeight; } else { AppleList[3] = AppleWeight; } }
                if(yDifOwn != 0) { if(yDifOwn > 0) { AppleList[2] = AppleWeight; } else { AppleList[0] = AppleWeight; } }
                foundATarget = true;
                LengthToTarget = ownDifLength;
            }
        }

        // -- SET MIDDLE AS TARGET IF APPLE IS NO TARGET
        if(!foundATarget) {
            double xDifOwn = ((double)board.getSIZE_X()/2) - (double)ownHead.getPosX();
            double yDifOwn = ((double)board.getSIZE_Y()/2) - (double)ownHead.getPosY();
            if(xDifOwn != 0) { if(xDifOwn > 0) { TargetList[1] = OtherTargetWeight; } else { TargetList[3] = OtherTargetWeight; } }
            if(yDifOwn != 0) { if(yDifOwn > 0) { TargetList[2] = OtherTargetWeight; } else { TargetList[0] = OtherTargetWeight; } }
        }

        // -- CHANGE ACTING IN SOME CASES:
        if(!board.isNextStepFree(usedDirection)) {
            int freeCounterLeft = 0;
            int freeCounterRight = 0;
            int checkDirection;
            // -- LEFT
            checkDirection = mapInt(usedDirection-1);
            if(WallList[checkDirection] > 0) {
                freeCounterLeft = countFreeInDirection(checkDirection, ownHead, whileBreakValue);
            }
            // -- RIGHT
            checkDirection = mapInt(usedDirection+1);
            if(WallList[checkDirection] > 0) {
                freeCounterRight = countFreeInDirection(checkDirection, ownHead, whileBreakValue);
            }
            if(freeCounterRight < freeCounterLeft) { ActingList[mapInt(usedDirection+1)] = 0; }
            else if(freeCounterRight > freeCounterLeft) { ActingList[mapInt(usedDirection-1)] = 0;  }
        }

        // -- COMBINE LISTS TO MAIN-LIST AND USE "BEST" OPTION
        int bestOptionIndex = 0;
        for(int counter = 0; counter < 4; counter++) {
            MainList[counter] = WallList[counter] * NextRoundList[counter] * AppleList[counter] * TargetList[counter];
            MainList[counter] *= ActingList[counter];
            if(MainList[counter] > MainList[bestOptionIndex]) { bestOptionIndex = counter; }
            else if(MainList[counter] == MainList[bestOptionIndex]) {
                if(Math.random() < 0.5) { bestOptionIndex = counter; }
            }
        }
        if(isEverythingZero(MainList) || !board.isNextStepFree(bestOptionIndex)) {
            for(int counter = 0; counter < 4; counter++) {
                if(WallList[counter] > 0) {
                    bestOptionIndex = counter;
                }
            }
        }

        usedDirection = bestOptionIndex;
        return bestOptionIndex;
    }

    Boolean isFieldFree(Field field) {

        List<Field> barrierPositions = boardInUse.getBarrier();
        List<Field> ownSnake = boardInUse.getOwnSnake();
        List<LinkedList<Field>> otherSnakes = boardInUse.getOtherSnakes();
        List<Field> otherHeads = boardInUse.getOtherHeads();
        Field ownHead = boardInUse.getOwnHead();

        for (Field bF : barrierPositions) {
            if(fieldToString(bF).equals(fieldToString(field))) { return false; }
        }
        for (Field oF : ownSnake) {
            if(fieldToString(oF).equals(fieldToString(field))) { return false; }
        }
        for (List<Field> sFL : otherSnakes) {
            for (Field sF : sFL) {
                if(fieldToString(sF).equals(fieldToString(field))) { return false; }
            }
        }
        for (Field oF : otherHeads) {
            if(fieldToString(oF).equals(fieldToString(field))) { return false; }
        }
        if(fieldToString(ownHead).equals(fieldToString(field))) { return false; }
        if(field.getPosX() >= boardInUse.getSIZE_X() || field.getPosY() >= boardInUse.getSIZE_Y()) { return false; }
        else if(field.getPosX() < 0 || field.getPosY() < 0) { return false; }

        return true;
    }

    int countAroundFree(Field field) {
        Field upField = new Field(field.getPosX(), field.getPosY()-1);
        Field rightField = new Field(field.getPosX()+1, field.getPosY());
        Field downField = new Field(field.getPosX(), field.getPosY()+1);
        Field leftField = new Field(field.getPosX()-1, field.getPosY());
        int freeCounter = 0;
        if(isFieldFree(upField)) { freeCounter++; }
        if(isFieldFree(rightField)) { freeCounter++; }
        if(isFieldFree(downField)) { freeCounter++; }
        if(isFieldFree(leftField)) { freeCounter++; }
        return freeCounter;
    }

    int countAroundFreeWithExtraWalls(Field field, List<Field> wallFields) {
        Field upField = new Field(field.getPosX(), field.getPosY()-1);
        Field rightField = new Field(field.getPosX()+1, field.getPosY());
        Field downField = new Field(field.getPosX(), field.getPosY()+1);
        Field leftField = new Field(field.getPosX()-1, field.getPosY());
        Boolean upCheck = true; Boolean rightCheck = true; Boolean downCheck = true; Boolean leftCheck = true;
        for(Field wallField : wallFields) {
            if(fieldToString(upField).equals(fieldToString(wallField))) { upCheck = false; }
            else if(fieldToString(rightField).equals(fieldToString(wallField))) { rightCheck = false; }
            else if(fieldToString(downField).equals(fieldToString(wallField))) { downCheck = false; }
            else if(fieldToString(leftField).equals(fieldToString(wallField))) { leftCheck = false; }
        }
        int freeCounter = 0;
        if(isFieldFree(upField) && upCheck) { freeCounter++; }
        if(isFieldFree(rightField) && rightCheck) { freeCounter++; }
        if(isFieldFree(downField) && downCheck) { freeCounter++; }
        if(isFieldFree(leftField) && leftCheck) { freeCounter++; }
        return freeCounter;
    }

    String fieldToString(Field field) {
        String s;
        s = ((Integer)field.getPosX()).toString() + "|" + ((Integer)field.getPosY()).toString();
        s = s.trim();
        return s;
    }

    int mapInt(int i) {
        while (i > 3) { i -= 4; }
        while (i < 0) { i += 4; }
        return i;
    }

    Field getFieldByDirection(int direction, Field refField) {
        Field field;
        if(direction == 0) { field = new Field(refField.getPosX(), refField.getPosY()-1); }
        else if(direction == 1) { field = new Field(refField.getPosX()+1, refField.getPosY()); }
        else if(direction == 2) { field = new Field(refField.getPosX(), refField.getPosY()+1); }
        else { field = new Field(refField.getPosX()-1, refField.getPosY()); }
        return field;
    }

    int countFreeInDirection(int checkDirection, Field ownHead, int whileBreakValue) {
        int countFree = 0;

        List<Field> fieldsToCheck = new ArrayList<>();
        List<Field> fieldsChecked = new ArrayList<>();
        List<Field> newFieldsToCheck = new ArrayList<>();
        fieldsToCheck.add(getFieldByDirection(checkDirection, ownHead));
        int whileBreaker = 0;
        while (whileBreaker < whileBreakValue && fieldsToCheck.size() > 0) {
            for(Field checkField : fieldsToCheck) {
                if(isFieldFree(checkField)) { countFree++; }
                if(countAroundFreeWithExtraWalls(checkField, fieldsChecked) > 0) {
                    newFieldsToCheck.add(getFieldByDirection(0, checkField));
                    newFieldsToCheck.add(getFieldByDirection(1, checkField));
                    newFieldsToCheck.add(getFieldByDirection(2, checkField));
                    newFieldsToCheck.add(getFieldByDirection(3, checkField));
                }
                fieldsChecked.add(checkField);
            }
            fieldsToCheck = newFieldsToCheck;
            newFieldsToCheck = new ArrayList<>();
            whileBreaker++;
        }

        return countFree;
    }

    Boolean isEverythingZero(double[] list) {
        for(double d : list) {
            if(d > 0) { return false; }
        }
        return true;
    }
}
