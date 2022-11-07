package Main;

public class Main {
    public static void main(String[] args) {
        int[][] grid = new int[Constants.GRID_WIDTH][Constants.GRID_LENGTH];
        grid[0][0] = Constants.FOOD;
        grid[15][15] = Constants.SNAKE_HEAD;
        grid[14][15] = Constants.SNAKE_BODY;
        grid[13][15] = Constants.SNAKE_BODY;
        
        new GUI(grid);
    }
}
