package snakes.contest.snakeue;

import board.*;
import snakes.Snake;

import java.awt.*;
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
        this.NAME = "Snakeü :)"; // set your favorite name
        this.COLOR = new Color(5, 155, 0); // set your favorite color

    }

    /**
     * Main function for every intelligence of the snake
     *
     * @param board the whole board with every information necessary
     * @return direction in which the snake should move
     */
    @Override 
    public int think(BoardInfo board) {
    	
        int returning = 0; // initializing returning direction variable
        
        this.NAME = "Snakeü :)";
        int prevred = this.COLOR.getRed();
        try {this.COLOR = new Color(prevred - 10, 155, 0);} // takes previous red colour - 10
        catch(IllegalArgumentException e) {this.COLOR = new Color(0, 155, 0);} // in case red is negative
        
        if (stage1(board)) returning = stage1Path(board); // getting easy apple
        if (stage1(board) && board.isNextStepFree(returning)) return returning; // just in case!
        
        else {
        	try {this.COLOR = new Color(prevred + 20, 155, 0);} // if panicking, getting red
        	catch(IllegalArgumentException e) {this.COLOR = new Color(255, 155, 0);}
        	this.NAME = "Snakeü :O";
        }
        
        return panicMode(board);	// panic-mode: trying to survive by going in direction with longest distance to barrier

    }

    /**
     * Gives direction for the easiest way to an apple. If closest one doesn't work, does same for farthest.
     * Checks, whether apple is a trap!
     */
    public static int stage1Path(BoardInfo board) {
        Field minApple = board.getNearestApple();
        Field maxApple = altApple(board);
        
        int appleID = 0;

        int posX = board.getOwnHead().getPosX();
        int posY = board.getOwnHead().getPosY();

        int numXFields[] = {posX - minApple.getPosX(), posX - maxApple.getPosX()};
        int numYFields[] = {minApple.getPosY() - posY, maxApple.getPosY() - posY};
        
        int[] borders = {
                0,
                0,
                0,
                0
            };
        
        int direction = RIGHT;
        
		borders[0] = findBorderUp(board);
        borders[1] = findBorderRight(board);
        borders[2] = findBorderDown(board);
        borders[3] = findBorderLeft(board);

        if (testCustomPathX(board, posX, minApple.getPosX(), posY) && testCustomPathY(board, posY, minApple.getPosY(), minApple.getPosX())) {

        	appleID = 0;
        	if (numXFields[appleID] < 0) direction = RIGHT;
        	else if (numXFields[appleID] > 0) direction = LEFT;
        	else if (numYFields[appleID] > 0) direction = DOWN;
        	else if (numYFields[appleID] < 0) direction = UP;
        }
        
        else if(testCustomPathX(board, posX, maxApple.getPosX(), posY) && testCustomPathY(board, posY, maxApple.getPosY(), maxApple.getPosX())) {
            
        	appleID = 1;
        	if (numXFields[appleID] < 0) direction = RIGHT;
        	else if (numXFields[appleID] > 0) direction = LEFT;
        	else if (numYFields[appleID] > 0) direction = DOWN;
        	else if (numYFields[appleID] < 0) direction = UP;
            
        }
        
        
        int saveIndex = (direction+1)%4;
        
        for(int i = 0; i < 4; i++) if(borders[i] <= 1) saveIndex = i;
        
        int nextDir = altIndex(direction, saveIndex, borders);
        
        if(numXFields[appleID] == 0 || numYFields[appleID] == 0) {
        	
        	if(isAppleTrap(board, direction, borders)) {
        		
        		if(testCustomPathX(board, posX, maxApple.getPosX(), posY) && testCustomPathY(board, posY, maxApple.getPosY(), maxApple.getPosX())) {
                    
        			appleID = 1;
                	if (numXFields[appleID] < 0) direction = RIGHT;
                	else if (numXFields[appleID] > 0) direction = LEFT;
                	else if (numXFields[appleID] > 0) direction = DOWN;
                	else if (numXFields[appleID] < 0) direction = UP;
                    
                }
        		else {return nextDir;}
        		
        	}
        	
        }
        
        return direction;
        
    }

    /**
     * Checks the easiest way to an apple.
     */
    public static boolean stage1(BoardInfo board) {
        Field minApple = board.getNearestApple();
        Field maxApple = altApple(board);

        int posX = board.getOwnHead().getPosX();
        int posY = board.getOwnHead().getPosY();

        if (testCustomPathX(board, posX, minApple.getPosX(), posY) && testCustomPathY(board, posY, minApple.getPosY(), minApple.getPosX())) {
            return true;
        } else {
        	if (testCustomPathX(board, posX, maxApple.getPosX(), posY) && testCustomPathY(board, posY, maxApple.getPosY(), maxApple.getPosX())) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    /**
     * Checks whether certain path in x-direction consists of only free squares.
     */
    public static boolean testCustomPathX(BoardInfo board, int startX, int endX, int posY) {
        try {
            Field[][] fields = board.getFields();
            if (startX < endX) {
                for (int i = 1; i <= endX - startX; i++) {
                    if (!fields[startX + i][posY].isFree()) return false;
                }
            } else if (startX != endX) {
                for (int i = 1; i <= startX - endX; i++) {
                    if (!fields[startX - i][posY].isFree()) return false;
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

    }

    /**
     * Checks whether certain path in y-direction consists of only free squares.
     */
    public static boolean testCustomPathY(BoardInfo board, int startY, int endY, int posX) {
        try {
            Field[][] fields = board.getFields();
            if (startY < endY) {
                for (int i = 1; i <= endY - startY; i++) {
                    if (!fields[posX][startY + i].isFree()) return false;
                }
            } else if (startY != endY) {
                for (int i = 1; i <= startY - endY; i++) {
                    if (!fields[posX][startY - i].isFree()) return false;
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Checks whether certain path in x-direction consists of only borders.
     */
    public static boolean onlyBorderPathX(BoardInfo board, int startX, int endX, int posY) {
    	
    	try {
            Field[][] fields = board.getFields();
            if (startX < endX) {
                for (int i = 1; i <= endX - startX; i++) {
                    if (fields[startX + i][posY].isFree()) return false;
                }
            } else if (startX != endX) {
                for (int i = 1; i <= startX - endX; i++) {
                    if (fields[startX - i][posY].isFree()) return false;
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    	
    }
    
    /**
     * Checks whether certain path in y-direction consists of only borders.
     */
    public static boolean onlyBorderPathY(BoardInfo board, int startY, int endY, int posX) {
        try {
            Field[][] fields = board.getFields();
            if (startY < endY) {
                for (int i = 1; i <= endY - startY; i++) {
                    if (fields[posX][startY + i].isFree()) return false;
                }
            } else if (startY != endY) {
                for (int i = 1; i <= startY - endY; i++) {
                    if (fields[posX][startY - i].isFree()) return false;
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
    
    /**
     * Going crazy!!! Gives direction with longest distance to barrier. Tests for traps!
     */
    public static int panicMode(BoardInfo board) {

        int posX = board.getOwnHead().getPosX();
        int posY = board.getOwnHead().getPosY();


        int[] borders = {
            0,
            0,
            0,
            0
        };

        borders[0] = findBorderUp(board);
        borders[1] = findBorderRight(board);
        borders[2] = findBorderDown(board);
        borders[3] = findBorderLeft(board);

        int max = 0;
        int maxIndex = 0;
        for (int i = 0; i < borders.length; i++) {

            if (borders[i] > max) {
                max = borders[i];
                maxIndex = i;
            }
        }
        
        if(!isGoalDirTrap(board, maxIndex, posX, posY, borders)) {return maxIndex;}

        return anyStep(board);

    }
    
    /**
     * Gives direction, which is free, no matter where.
     */
    public static int anyStep(BoardInfo board) {
    	
    	int direction = 0;
    	int counter = 0;
    	
    	while (!board.isNextStepFree(direction) && counter < 3) {
    		
            direction = (direction + 1)%4;
            counter++;
            
        }
    	
    	return direction;
    	
    }

    /**
     * Gives distance to border in positive x-direction.
     */
    public static int findBorderRight(BoardInfo board) {
        int posX = board.getOwnHead().getPosX();
        int posY = board.getOwnHead().getPosY();
        Field[][] fields = board.getFields();

        int steps = 1;

        while (true) {
            if (board.getSIZE_X() == posX + steps) return steps - 1;
            if (fields[posX + steps][posY].isFree()) {
                steps++;

            } else {
                return steps - 1;
            }
        }
    }

    /**
     * Gives distance to border in negative x-direction.
     */
    public static int findBorderLeft(BoardInfo board) {
        int posX = board.getOwnHead().getPosX();
        int posY = board.getOwnHead().getPosY();
        Field[][] fields = board.getFields();

        int steps = 1;

        while (true) {
            if (posX - steps == -1) return steps - 1;
            if (fields[posX - steps][posY].isFree()) {
                steps++;

            } else {
                return steps - 1;
            }
        }
    }

    /**
     * Gives distance to border in negative y-direction.
     */
    public static int findBorderUp(BoardInfo board) {
        int posX = board.getOwnHead().getPosX();
        int posY = board.getOwnHead().getPosY();
        Field[][] fields = board.getFields();

        int steps = 1;

        while (true) {
            if (posY - steps == -1) return steps - 1;
            if (fields[posX][posY - steps].isFree()) {
                steps++;

            } else {
                return steps - 1;
            }
        }
    }

    /**
     * Gives distance to border in positive y-direction.
     */
    public static int findBorderDown(BoardInfo board) {
        int posX = board.getOwnHead().getPosX();
        int posY = board.getOwnHead().getPosY();
        Field[][] fields = board.getFields();

        int steps = 1;

        while (true) {
            if (board.getSIZE_Y() == posY + steps) return steps - 1;
            if (fields[posX][posY + steps].isFree()) {
                steps++;

            } else {
                return steps - 1;
            }
        }
    }

    /**
     * Checks, whether goal in a certain direction is a trap!
     */
    public static boolean isGoalDirTrap(BoardInfo board, int direction, int posX, int posY, int[] borders) {
        if(direction == 0) {
            if(!isGoalATrap(board, posX, posY - borders[direction], borders[direction])) return false;
        } else if(direction == 2) {
            if(!isGoalATrap(board, posX, posY + borders[direction], borders[direction])) return false;
        } else if(direction == 1) {
            if(!isGoalATrap(board, posX + borders[direction], posY, borders[direction])) return false;
        } else if(direction == 3) {
            if(!isGoalATrap(board, posX - borders[direction], posY, borders[direction])) return false;
        }
        return true;
    }
    
    /**
     * Checks, whether position is a trap!
     */
    public static boolean isGoalATrap(BoardInfo board, int x, int y, int distance) {
        Field[][] fields = board.getFields();
        int counter = 0;
        if(board.getSIZE_X() > x + 1) {
            if(fields[x + 1][y].isFree()) counter++;
        }
        if(x - 1 >= 0) {
            if(fields[x - 1][y].isFree()) counter++;
        }
        if(board.getSIZE_Y() > y + 1) {
            if(fields[x][y + 1].isFree()) counter++;
        }
        if(y - 1 >= 0) {
            if(fields[x][y - 1].isFree()) counter++;
        }
        
        if(counter < 2 && distance > 1) {
        	
        	return true;
        	
        }
        
        else if(counter == 1 && distance <= 1) {
        	
        	return false;
        	
        }
        
        else if(counter == 0 && distance <= 1){
        	
        	return true;
        	
        }
        
        else {return false;}
        
    }
    
    /**
     * Gives an alternative direction, if the best is a trap.
     */
    public static int altIndex(int maxIndex, int minIndex, int[] borders) {
    	
    	int nextDir = (maxIndex + 1) % 4;
        if(nextDir == minIndex || borders[nextDir] == borders[minIndex]) nextDir = (nextDir + 1) % 4;
        if(nextDir == minIndex || borders[nextDir] == borders[minIndex]) nextDir = (nextDir + 1) % 4;
        if(nextDir == minIndex || borders[nextDir] == borders[minIndex]) nextDir = (nextDir + 1) % 4;
    	
    	return nextDir;
    	
    }
    
    /**
     * Checks if apple is a trap.
     */
    public static boolean isAppleTrap(BoardInfo board, int direction, int[] borders) {
    	
    	Field minApple = board.getNearestApple();
    	int posX = board.getOwnHead().getPosX();
        int posY = board.getOwnHead().getPosY();
    	
    	try {
	        if(isGoalDirTrap(board, direction, posX, posY, borders)) {
	        	
	        	if(direction==0) {
	        			
	        		if(!onlyBorderPathY(board, minApple.getPosY(), posY - borders[direction], posX+1)
	        				&& !onlyBorderPathY(board, minApple.getPosY(), posY - borders[direction], posX-1)
	        				&& minApple.getPosY() != posY - borders[direction]) {return false;}
	        		
	        	}
	        	if(direction==2) {
        			
	        		if(!onlyBorderPathY(board, minApple.getPosY(), posY + borders[direction], posX+1)
	        				&& !onlyBorderPathY(board, minApple.getPosY(), posY + borders[direction], posX-1)
	        				&& minApple.getPosY() != posY + borders[direction]) {return false;}
	        		
	        	}
	        	
	        	if(direction==1) {
        			
	        		if(!onlyBorderPathX(board, minApple.getPosX(), posX + borders[direction], posY+1)
	        				&& !onlyBorderPathX(board, minApple.getPosX(), posX + borders[direction], posY-1)
	        				&& minApple.getPosX() != posX + borders[direction]) {return false;}
	        		
	        	}
	        	if(direction==3) {
        			
	        		if(!onlyBorderPathX(board, minApple.getPosX(), posX - borders[direction], posY+1)
	        				&& !onlyBorderPathX(board, minApple.getPosX(), posX - borders[direction], posY-1)
	        				&& minApple.getPosX() != posX - borders[direction]) {return false;}
	        		
	        	}
	        	return true;
	        	
	        }
	        else {return false;}
        } catch(ArrayIndexOutOfBoundsException e) {
        	
        	return true;
        	
        }
    	
    }
    
    /**
     * Finds the apple farthest away.
     */
    public static Field altApple(BoardInfo board) {
    	
    	List<Field> apples = board.getApples();
        int maxReach = 0; // set max distance
        Field maxApple = null;
        int curReach;

        // iterate through all apples
        for (Field apple: apples) {

            // calculate number of fields to reach this apple
            curReach = Math.abs(board.getOwnHead().getPosX() - apple.getPosX()) +
                    Math.abs(board.getOwnHead().getPosY() - apple.getPosY());

            // if curReach is smaller than every reach bevor, set this as nearest apple
            if (curReach > maxReach) {
                maxApple = apple;
                maxReach = curReach;
            }
        }

        return maxApple;
    	
    }
    
}