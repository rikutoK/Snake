package Main;

import java.awt.Color;

public class Constants {
    //GUI Components
    public static final int GRID_SIZE = 20;
    public static final int GRID_WIDTH = 30; //number of grids
    public static final int GRID_LENGTH = 30; //number of grids

    public static final int SCREEN_WIDTH = GRID_SIZE * GRID_WIDTH;
    public static final int SCREEN_LENGTH = GRID_SIZE * GRID_LENGTH;

    //Game
    public static final int EMPTY = 0;
    public static final int SNAKE_HEAD = 1;
    public static final int SNAKE_BODY = 2;
    public static final int FOOD = 3;

    public static final Color EMPTY_COLOR = Color.BLACK;
    public static final Color SNAKE_HEAD_COLOR = Color.GREEN;
    public static final Color SNAKE_BODY_COLOR = new Color(144, 238, 144);
    public static final Color FOOD_COLOR = Color.RED;

    public static final int DELAY = 70;
}

enum Turn {
    LEFT(-1),
    STRAIGHT(0),
    RIGHT(1);

    private int val;

    private Turn(int val) {
        this.val = val;
    }

    public static Turn getDirection(int val) {
        switch (val) {
            case -1:
                return LEFT;
            case 0:
                return STRAIGHT;
            case 1:
                return RIGHT;
            default:
                return null;
        }
    }
}