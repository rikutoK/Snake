package Main;

import NeuralNetwork.NN;
import NeuralNetwork.GeneticAlgorithm.GA;

public class Main {
    public static void main(String[] args) {
        Snake snake = new Snake(true);
        NN neuralNetwork = new NN(snake);
        new GA(neuralNetwork);
    }
}
