package snakes.contest.eSnake;

import board.*;
import snakes.Snake;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MySnake extends Snake {

    private String fullName = "\u202ESNAKE";
    private boolean nameGrowing = false;

    private float hue = 0.f;

    private final Random RANDOM = new Random();

    public MySnake() {
        NAME = fullName;
        COLOR = new Color(0, 0, 0, 0);
    }

    @Override
    public int think(BoardInfo board) {
        if (nameGrowing) {
            NAME = NAME + fullName.charAt(NAME.length());
            if (NAME.length() >= fullName.length()) {
                nameGrowing = false;
            }
        } else if (NAME.length() > 1) {
            NAME = NAME.substring(0, NAME.length() - 1);
        } else {
            nameGrowing = true;
        }

        COLOR = Color.getHSBColor(hue += 0.05f, 1.f, 1.f);

        int headX = board.getOwnHead().getPosX();
        int headY = board.getOwnHead().getPosY();

        boolean[] dirsValid = new boolean[4];
        int[] floodings = {
            flood(board, headX, headY - 1),
            flood(board, headX + 1, headY),
            flood(board, headX, headY + 1),
            flood(board, headX - 1, headY)
        };
        int maxFloodings = 0;
        for (int i = 0; i < 4; i++) {
            // System.out.print(floodings[i] + "\t");
            if (maxFloodings < floodings[i]) {
                maxFloodings = floodings[i];
            }
        }

        for (int i = 0; i < 4; i++) {
            if (floodings[i] == maxFloodings) {
                dirsValid[i] = true;
            }
        }
        // System.out.println();

        int[] hValues = {
            distanceToApple(board, headX, headY - 1),
            distanceToApple(board, headX + 1, headY),
            distanceToApple(board, headX, headY + 1),
            distanceToApple(board, headX - 1, headY)
        };
        int dir = -1;
        int minVal = Integer.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            // System.out.print(hValues[i] + "\t");
            if (dirsValid[i] && board.isNextStepFree(i) && (hValues[i] < minVal || hValues[i] == minVal && RANDOM.nextBoolean())) {
                dir = i;
                minVal = hValues[i];
            }
        }
        // System.out.println();

        // We lost!
        if (dir == -1) {
            NAME = "DEAD SNAKE";
            // System.exit(-1); // Better not do this
        }

        return dir;
    }


    // Helper functions

    private int distance(Field f, int x, int y) {
        return Math.abs(f.getPosX() - x) + Math.abs(f.getPosY() - y);
    }

    private int distanceToApple(BoardInfo board, int x, int y) {
        int dist = Integer.MAX_VALUE;
        Field[][] fields = board.getFields();
        for (Field f : board.getApples()) {
            int newDist = distance(f, x, y);
            if ((f.getPosY() < board.getSIZE_Y()-1 && fields[f.getPosX()][f.getPosY()+1].isFree() ? 1:0)
                    + (f.getPosY() > 0 && fields[f.getPosX()][f.getPosY()-1].isFree() ? 1:0)
                    + (f.getPosX() < board.getSIZE_X()-1 && fields[f.getPosX()+1][f.getPosY()].isFree() ? 1:0)
                    + (f.getPosX() > 0 && fields[f.getPosX()-1][f.getPosY()].isFree() ? 1:0) >= (newDist <= 1 ? 1 : 2)) {
                dist = Math.min(dist, newDist);
            }
        }
        return dist;
    }

    private Set<Integer> floodSet = new HashSet<>();
    private int flood(BoardInfo board, int x, int y) {
        if (x < 0 || x >= board.getSIZE_X() || y < 0 || y >= board.getSIZE_Y()) {
            return 0;
        }
        floodSet.clear();
        return floodInternal(board, x, y);
    }
    private int floodInternal(BoardInfo board, int x, int y) {
        if (board.getFields()[x][y].isFree() && !floodSet.contains(fuseCords(board, x, y))) {
            floodSet.add(fuseCords(board, x, y));
            int flooded = 1;
            if (y > 0) {
                flooded += floodInternal(board, x, y - 1);
            }
            if (y < board.getSIZE_Y() - 1) {
                flooded += floodInternal(board, x, y + 1);
            }
            if (x < board.getSIZE_X() - 1) {
                flooded += floodInternal(board, x + 1, y);
            }
            if (x > 0) {
                flooded += floodInternal(board, x - 1, y);
            }
            return flooded;
        }
        return 0;
    }

    // Fuses two coordinates into one unique int value
    private int fuseCords(BoardInfo board, int x, int y) {
        return board.getSIZE_Y() * x + y;
    }

}
