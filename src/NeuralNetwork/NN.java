package NeuralNetwork;

import Main.Constants;
import Main.Snake;

public class NN {
    public static final int FIRST_LAYER = Constants.GRID_LENGTH * Constants.GRID_WIDTH; //entire game board
    public static final int SECOND_LAYER = 16;
    public static final int THIRD_LAYER = 16;
    public static final int OUTPUT_LAYER = 3; //left, straight, right

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

    public int start() {
        snake.setRunning(true);

        while(snake.isRunning()) {
            getInput();

            calcLayers();
        }

        int score = snake.getScore();

        snake.reset();

        return score; //score
    }

    public void getInput() {
        int[][] grid = snake.generateGrid();

        for(int i = 0; i < Constants.GRID_WIDTH; i++) {
            for(int j = 0; j < Constants.GRID_LENGTH; j++) {
                first_layer[i + j] = grid[i][j];
            }
        }
    }

    public void calcLayers() {
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
    public void sigmoid(double[] layer) {
        for(int i = 0; i < layer.length; i++) {
            layer[i] = 1/(1 + Math.pow(Math.E, -layer[i])); // 1 / (1 + e^-x)
        }
    }


}
