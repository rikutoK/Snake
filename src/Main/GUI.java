package Main;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JPanel {
    private JFrame frame;

    private int[][] grid;

    public GUI(int[][] grid) {
        this.grid = grid;

        //set up for GUI
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_LENGTH));
        this.setBackground(Constants.EMPTY_COLOR);

        frame = new JFrame();
        frame.add(this);
        frame.setTitle("Snake");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setBackground(Constants.EMPTY_COLOR);
        for(int i = 0; i < Constants.GRID_LENGTH; i++) {
            for(int j = 0; j < Constants.GRID_WIDTH; j++) {
                switch (grid[i][j]) {
                    case Constants.SNAKE_HEAD:
                        drawSquare(i, j, Constants.SNAKE_HEAD_COLOR, g);
                        break;
                    case Constants.SNAKE_BODY:
                        drawSquare(i, j, Constants.SNAKE_BODY_COLOR, g);
                        break;
                    case Constants.FOOD:
                        drawSquare(i, j, Constants.FOOD_COLOR, g);
                        break;
                }
            }
        }
    }

    private void drawSquare(int x, int y, Color c, Graphics g) {
        g.setColor(c);
        g.fillRect(x * Constants.GRID_SIZE, y * Constants.GRID_SIZE, Constants.GRID_SIZE, Constants.GRID_SIZE);
    }
}
