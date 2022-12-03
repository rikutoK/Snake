package NeuralNetwork.GeneticAlgorithm;

import NeuralNetwork.NN;

public class Individual {
    //weigt first-second, second-third, third-output
    public static final int WEIGHT_LENGTH = (NN.FIRST_LAYER * NN.SECOND_LAYER) + (NN.SECOND_LAYER * NN.THIRD_LAYER) + (NN.THIRD_LAYER * NN.OUTPUT_LAYER);
    //bias second, third output
    public static final int BIAS_LENGTH = NN.SECOND_LAYER + NN.THIRD_LAYER + NN.OUTPUT_LAYER;
    public static final int LENGTH = WEIGHT_LENGTH + BIAS_LENGTH;

    double[] chromosome;
    int fitness; //score of the game

    public Individual() {
        chromosome = new double[LENGTH];
        fitness = Integer.MIN_VALUE;

        for(int i = 0; i < WEIGHT_LENGTH; i++) {
            chromosome[i] = Math.random(); //weight from 0 to 1
        }

        for(int i = WEIGHT_LENGTH; i < LENGTH; i++) {
            chromosome[i] = Math.random() * 2 - 1; //bias from -1 to 1
        }
    }

    public Individual(double[] chromosome, int fitness) {
        this.chromosome = chromosome;
        this.fitness = fitness;
    }
 
    public double get(int index) {
        return chromosome[index];
    }

    public void set(int index, double gene) {
        chromosome[index] = gene;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public double[] getChromosome() {
        return chromosome;
    }


    public Individual clone() {
        return new Individual(chromosome.clone(), fitness);
    }

    @Override
    public String toString() {
        String output = "";

        for(double d : chromosome) {
            output += d + ", ";
        }

        return output.substring(0, output.length() - 2);
    }
}
