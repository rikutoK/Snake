package Main;

import java.io.File;

import NeuralNetwork.NN;
import NeuralNetwork.GeneticAlgorithm.GA;

public class Main {
    public static void main(String[] args) {
        Snake snake = new Snake(false);
        NN neuralNetwork = new NN(snake);

        new GA(neuralNetwork);

        // neuralNetwork.read(new File("./src/NeuralNetwork/weight.txt"));
        // neuralNetwork.play();
    }
}
