package NeuralNetwork;

import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.Timer;

import Main.Constants;
import Main.Snake;
import NeuralNetwork.GeneticAlgorithm.Individual;
import Main.Constants.Direction;


public class NN {
    public static final int FIRST_LAYER = Constants.GRID_LENGTH * Constants.GRID_WIDTH; //entire game board
    public static final int SECOND_LAYER = 20;
    public static final int THIRD_LAYER = 10;
    public static final int OUTPUT_LAYER = 3; //turn left, straight, right

    //neurons
    public double[] first_layer;
    public double[] second_layer;
    public double[] third_layer;
    public double[] output_layer;

    public double[] second_layer_bias;
    public double[] third_layer_bias;
    public double[] output_layer_bias;

    //weight
    public double[][] first_to_second; 
    public double[][] second_to_third;
    public double[][] third_to_output;

    private Snake snake;

    public NN(Snake snake) {
        this.snake = snake;

        first_layer = new double[FIRST_LAYER];
        second_layer = new double[SECOND_LAYER];
        third_layer = new double[THIRD_LAYER];
        output_layer = new double[OUTPUT_LAYER];

        second_layer_bias = new double[SECOND_LAYER];
        third_layer_bias = new double[THIRD_LAYER];
        output_layer_bias = new double[OUTPUT_LAYER];

        first_to_second = new double[SECOND_LAYER][FIRST_LAYER];
        second_to_third = new double[THIRD_LAYER][SECOND_LAYER];
        third_to_output = new double[OUTPUT_LAYER][THIRD_LAYER];
    }

    public int play() {
        snake.setRunning(true);

        int count = 0; // count for how long the snake survived

        while(snake.isRunning()) {
            getInput();

            calcLayers();

            snake.setNextDirection(output());

            // try {
            //     Thread.sleep(60);
            // } 
            // catch (InterruptedException e) {
            //     e.printStackTrace();
            // }

            count++;
        }

        // try {
        //     Thread.sleep(100);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        int score = snake.getScore();

        snake.reset();

        return score * 100 + count; //score
    }

    private void getInput() {
        int[][] grid = snake.generateGrid();

        for(int i = 0; i < Constants.GRID_WIDTH; i++) {
            for(int j = 0; j < Constants.GRID_LENGTH; j++) {
                first_layer[i + j] = grid[i][j];
            }
        }
    }

    private void calcLayers() {
        //calculating second layer
        for(int i = 0; i < SECOND_LAYER; i++) {
            second_layer[i] = 0;

            for(int j = 0; j < FIRST_LAYER; j++) {
                second_layer[i] += first_layer[j] * first_to_second[i][j];
            }

            second_layer[i] += second_layer_bias[i];
        }
        sigmoid(second_layer);

        //third layer
        for(int i = 0; i < THIRD_LAYER; i++) {
            third_layer[i] = 0;

            for(int j = 0; j < SECOND_LAYER; j++) {
                third_layer[i] += second_layer[j] * second_to_third[i][j];
            }

            third_layer[i] += third_layer_bias[i];
        }
        sigmoid(third_layer);

        //output layer
        for(int i = 0; i < OUTPUT_LAYER; i++) {
            output_layer[i] = 0;

            for(int j = 0; j < THIRD_LAYER; j++) {
                output_layer[i] += third_layer[j] * third_to_output[i][j];
            }

            output_layer[i] += output_layer_bias[i];
        }
        sigmoid(output_layer);
    }

    //activation function
    private void sigmoid(double[] layer) {
        for(int i = 0; i < layer.length; i++) {
            // layer[i] = 1/(1 + Math.pow(Math.E, -layer[i])); // 1 / (1 + e^-x)
            layer[i] = Math.max(0, layer[i]);
        }
    }

    private Direction output() {
        //converting the output to probability
        double sum = 0;
        for(int i = 0; i < OUTPUT_LAYER; i++) {
            sum += output_layer[i];
        }

        for(int i = 0; i < OUTPUT_LAYER; i++) {
            output_layer[i] /= sum; 
        }


        int index = -1;

        double probability = Math.random();
        sum = 0;

        while(probability > sum) {
            index++;
            sum += output_layer[index];
        }


        switch (index) {
            case 0: //left
                switch (snake.getCurrDirection()) {
                    case UP:
                        return Direction.LEFT;

                    case DOWN:
                        return Direction.RIGHT;

                    case LEFT:
                        return Direction.DOWN;

                    case RIGHT:
                        return Direction.UP;
                }

                break;
            
            case 1: //straight
                return snake.getCurrDirection();
            
            case 2: //right
                switch (snake.getCurrDirection()) {
                    case UP:
                        return Direction.RIGHT;

                    case DOWN:
                        return Direction.LEFT;

                    case LEFT:
                        return Direction.UP;

                    case RIGHT:
                        return Direction.RIGHT;
                }
                
                break;
        }

        return null;
    }

    public void setWeights(Individual individual) {
        int index = 0;

        //weights
        for(int i = 0; i < SECOND_LAYER; i++) {
            for(int j = 0; j < FIRST_LAYER; j++) {
                first_to_second[i][j] = individual.get(index);
                index++;
            }
        }

        for(int i = 0; i < THIRD_LAYER; i++) {
            for(int j = 0; j < SECOND_LAYER; j++) {
                second_to_third[i][j] = individual.get(index);
                index++;
            }
        }

        for(int i = 0; i < OUTPUT_LAYER; i++) {
            for(int j = 0; j < THIRD_LAYER; j++) {
                third_to_output[i][j] = individual.get(index);
                index++;
            }
        }

        //bias
        for(int i = 0; i < SECOND_LAYER; i++) {
            second_layer_bias[i] = individual.get(index);
            index++;
        }

        for(int i = 0; i < THIRD_LAYER; i++) {
            third_layer_bias[i] = individual.get(index);
            index++;
        }

        for(int i = 0; i < OUTPUT_LAYER; i++) {
            output_layer_bias[i] = individual.get(index);
            index++;
        }
    }

    public void read(File file) {
        try {
            Scanner reader = new Scanner(file);

            int index = 0;

        //weights
        for(int i = 0; i < SECOND_LAYER; i++) {
            for(int j = 0; j < FIRST_LAYER; j++) {
                first_to_second[i][j] = reader.nextDouble();
                index++;
            }
        }

        for(int i = 0; i < THIRD_LAYER; i++) {
            for(int j = 0; j < SECOND_LAYER; j++) {
                second_to_third[i][j] = reader.nextDouble();
                index++;
            }
        }

        for(int i = 0; i < OUTPUT_LAYER; i++) {
            for(int j = 0; j < THIRD_LAYER; j++) {
                third_to_output[i][j] = reader.nextDouble();
                index++;
            }
        }

        //bias
        for(int i = 0; i < SECOND_LAYER; i++) {
            second_layer_bias[i] = reader.nextDouble();
            index++;
        }

        for(int i = 0; i < THIRD_LAYER; i++) {
            third_layer_bias[i] = reader.nextDouble();
            index++;
        }

        for(int i = 0; i < OUTPUT_LAYER; i++) {
            output_layer_bias[i] = reader.nextDouble();
            index++;
        }
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
