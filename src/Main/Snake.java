package Main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Snake extends JPanel implements KeyListener, ActionListener {
    private JFrame frame;

    private ArrayList<Coordinate> snake; //0 --> head, rest --> body
    private Coordinate food;
    private int score;
    private Direction currDirection; //keep track of which way the snake is going
    private Direction nextDirection; //keep track of legal turns possible

    private boolean playing; //whether the player is playing or not

    private Timer timer;
    private boolean running;


    public Snake(boolean playing) {
        this.playing = playing;

        initSnake();
        generateFood();

        //set up for GUI
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_LENGTH));
        this.setBackground(Constants.EMPTY_COLOR);
        this.setFocusable(true);
        this.addKeyListener(this);

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

        drawCircle(food, Constants.FOOD_COLOR, g); //food

        for(int i = 1; i < snake.size(); i++) { //snake body
            drawSquare(snake.get(i), Constants.SNAKE_BODY_COLOR, g);
        }

        drawSquare(snake.get(0), Constants.SNAKE_HEAD_COLOR, g); //snake head
    }

    private void drawSquare(Coordinate c, Color color, Graphics g) {
        g.setColor(color);
        g.fillRect(c.getX() * Constants.GRID_SIZE, c.getY() * Constants.GRID_SIZE, Constants.GRID_SIZE, Constants.GRID_SIZE);
    }

    private void drawCircle(Coordinate c, Color color, Graphics g) {
        g.setColor(color);
        g.fillOval(c.getX() * Constants.GRID_SIZE, c.getY() * Constants.GRID_SIZE, Constants.GRID_SIZE, Constants.GRID_SIZE);
    }

    private void initSnake() {
        score = 0;
        currDirection = Direction.RIGHT;
        nextDirection = Direction.RIGHT;

        snake = new ArrayList<>();

        snake.add(new Coordinate(Constants.GRID_WIDTH / 2, Constants.GRID_LENGTH / 2));
        snake.add(new Coordinate(Constants.GRID_WIDTH / 2 - 1, Constants.GRID_LENGTH / 2));
        snake.add(new Coordinate(Constants.GRID_WIDTH / 2 - 2, Constants.GRID_LENGTH / 2));
    }

    private void generateFood() {
        int x = 0;
        int y = 0;

        boolean overlap = true;
        while(overlap) {
            overlap = false;

            x = (int) Math.floor(Math.random() * Constants.GRID_WIDTH);
            y = (int) Math.floor(Math.random() * Constants.GRID_LENGTH);

            for(Coordinate c : snake) {
                if(c.equals(x, y)) {
                    overlap = true;
                    break;
                }
            }
        }

        food = new Coordinate(x, y);
    }

    private void move() {
        for(int i = snake.size() - 1; i > 0; i--) { //moves all the body by 1
            snake.get(i).setCoordinate(snake.get(i - 1));
        }

        switch(nextDirection) { //head
            case UP:
                snake.get(0).decrementY();
                break;
            case DOWN:
                snake.get(0).incrementY();
                break;
            case LEFT:
                snake.get(0).decrementX();
                break;
            case RIGHT:
                snake.get(0).incrementX();
                break;
        }

        currDirection = nextDirection;
    }

    private boolean checkCollision() {
        for(int i = 1; i < snake.size(); i++) { //collision against it self
            if(snake.get(0).equals(snake.get(i))) {
                return true;
            }
        }

        if(snake.get(0).getX() < 0) { //collision against the wall
            return true;
        }
        if(snake.get(0).getX() >= Constants.GRID_WIDTH) {
            return true;
        }
        if(snake.get(0).getY() < 0) {
            return true;
        }
        if(snake.get(0).getY() >= Constants.GRID_LENGTH) {
            return true;
        }

        return false;
    }

    private boolean checkFood() {
        return snake.get(0).equals(food);
    }

    public void start() {
        running = true;
        timer = new Timer(Constants.DELAY, this);
        timer.start();
    }

    public void update() {
        if(running) {
            move(); //moves one square

            if(checkCollision()) {
                running = false;
            }
            if(checkFood()) {
                snake.add(new Coordinate(snake.get(snake.size() - 1))); //increase snake by 1
                score++;
                generateFood();
            }
            repaint();
        }
    }

    public int[][] generateGrid() {
        int[][] grid = new int[Constants.GRID_WIDTH][Constants.GRID_LENGTH];

        grid[food.getX()][food.getY()] = Constants.FOOD; //food

        grid[snake.get(0).getX()][snake.get(0).getY()] = Constants.SNAKE_HEAD; //head

        for(int i = 1; i < snake.size(); i++) { //body
            grid[snake.get(i).getX()][snake.get(i).getY()] = Constants.SNAKE_BODY;
        }

        return grid;
    }
    
    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public Direction getCurrDirection() {
        return currDirection;
    }

    public void setNextDirection(Direction nextDirection) {
        this.nextDirection = nextDirection;

        update();
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        initSnake();
        generateFood();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ENTER: //to start the game if manually played
                if(playing) {
                    start();
                }
                break;
            case KeyEvent.VK_UP:
                if(currDirection != Direction.DOWN) {
                    nextDirection = Direction.UP;
                }
                break;
            case KeyEvent.VK_DOWN:
                if(currDirection != Direction.UP) {
                    nextDirection = Direction.DOWN;
                }
                break;
            case KeyEvent.VK_LEFT:
                if(currDirection != Direction.RIGHT) {
                    nextDirection = Direction.LEFT;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(currDirection != Direction.LEFT) {
                    nextDirection = Direction.RIGHT;
                }
                break;
        }        
        
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
    }
}
