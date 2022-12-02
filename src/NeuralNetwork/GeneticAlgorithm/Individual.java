package NeuralNetwork.GeneticAlgorithm;

import NeuralNetwork.NN;

public class Individual {
    //weigt first-second, second-third, third-output
    public static final int WEIGHT_LENGTH = (NN.FIRST_LAYER * NN.SECOND_LAYER) + (NN.SECOND_LAYER * NN.THIRD_LAYER) + (NN.THIRD_LAYER * NN.OUTPUT_LAYER);
    //bias second, third output
    public static final int BIAS_LENGTH = NN.SECOND_LAYER + NN.THIRD_LAYER + NN.OUTPUT_LAYER;
    public static final int LENGTH = WEIGHT_LENGTH + BIAS_LENGTH;

    double[] chromosome;

    public Individual() {
        chromosome = new double[LENGTH];

        for(int i = 0; i < WEIGHT_LENGTH; i++) {
            chromosome[i] = Math.random(); //weight from 0 to 1
        }

        for(int i = WEIGHT_LENGTH; i < LENGTH; i++) {
            chromosome[i] = Math.random() * 2 - 1; //bias from -1 to 1
        }
    }

    public double get(int index) {
        return chromosome[index];
    }
}
