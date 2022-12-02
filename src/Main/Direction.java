package Main;

public enum Direction {
    UP(0),
    DOWN(1),
    LEFT(2),
    RIGHT(3);

    private int val;

    private Direction(int val) {
        this.val = val;
    }

    public int value() {
        return val;
    }

    public static Direction getDirection(int val) {
        switch (val) {
            case 0:
                return UP;
            case 1:
                return DOWN;
            case 2:
                return LEFT;
            case 3:
                return RIGHT;
            default:
                return null;
        }
    }
}