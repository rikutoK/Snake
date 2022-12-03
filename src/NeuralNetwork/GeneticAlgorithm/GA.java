package NeuralNetwork.GeneticAlgorithm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import NeuralNetwork.NN;

public class GA {
    public static final int POPULATION_SIZE = 10;
    public static final double MUTATION_RATE = 0.01;
    public static final double CROSSOVER_RATE = 0.7;

    public static final int MAX_GENERATION = 1000;

    public static final int ELITISM = 1; //number of elitist individuals to keep

    private NN neuralNetwork;

    private Population currPopulation;
    private Population newPopulation;

    private Individual elitist;

    public GA(NN neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        this.elitist = new Individual();

        currPopulation = Population.initPopulation(POPULATION_SIZE);
        newPopulation = new Population(POPULATION_SIZE);

        for(int i = 0; i < MAX_GENERATION; i++) {
            //playing and calculating fitness
            for(Individual individual : currPopulation.getPopulation()) {
                neuralNetwork.setWeights(individual);
                individual.setFitness(neuralNetwork.play()); //returns the score which is the fitness of the individual
            }

            System.out.println("Generation " + (i+1) + ": " + currPopulation.averageFitness());


            //elitism
            Individual elitist = currPopulation.getElitist();
            if(this.elitist.getFitness() < elitist.getFitness()) {
                this.elitist = elitist.clone();
            }

            for(int j = 0; j < ELITISM; j++) {
                newPopulation.set(j, elitist);

                // Population temp = currPopulation.removeIndividual(elitist);
                // elitist = temp.getElitist();
            }


            //selecion, crossover
            for(int j = ELITISM; j < newPopulation.size(); j++) {
                Individual parent1 = currPopulation.selection();
                Individual parent2 = currPopulation.selection();

                Individual child = Population.crossover(parent1, parent2);

                newPopulation.set(j, child);
            }

            //mutation
            newPopulation.mutation();

            currPopulation = newPopulation;
            newPopulation = new Population(POPULATION_SIZE);   
        }

        System.out.println("Elitist: " + this.elitist.getFitness());

        //writting the best to text file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./src//NeuralNetwork//weight.txt")));

            double[] chromosome = this.elitist.getChromosome();
            for(int i = 0; i < chromosome.length; i++) {
                if(i != chromosome.length - 1) {
                    writer.write(chromosome[i] + "\n");
                }
                else {
                    writer.write(chromosome[i] + "");
                }
            }

            writer.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
