package NeuralNetwork.GeneticAlgorithm;

public class Population {
    private Individual[] population;

    public Population(int size) {
        population = new Individual[size];
    }

    public static Population initPopulation(int size) {
        Population population = new Population(size);

        for(int i = 0; i < size; i++) {
            population.set(i, new Individual());
        }

        return population;
    }

    public int size() {
        return population.length;
    }

    public Individual get(int index) {
        return population[index];
    }

    public void set(int index, Individual individual) {
        population[index] = individual;
    }

    public Individual[] getPopulation() {
        return population;
    }

    public Individual getElitist() {
        Individual elitist = population[0];

        for(int i = 0; i < population.length; i++) {
            if(elitist.getFitness() < population[i].getFitness()) {
                elitist = population[i];
            }
        }

        return elitist;
    }

    public Population removeIndividual(Individual individual) {
        Population newPopulation = new Population(population.length - 1);

        boolean encountered = false;

        for(int i = 0; i < population.length - 1; i++) {
            if(population[i] == individual) {
                encountered = true;
                i++;
            }

            if(!encountered) {
                newPopulation.set(i, population[i]);
            }
            else {
                newPopulation.set(i-1, population[i]);
            }
        }

        return newPopulation;
    }
    
    //roulettewheel
    public Individual selection() {
        double[] modifiedFitness = new double[population.length];

        double sum = 0;

        for(Individual individual : population) {
            sum += individual.getFitness();
        }

        for(int i = 0; i < population.length; i++) {
            modifiedFitness[i] = ((double) population[i].getFitness()) / sum;
        }


        double probability = Math.random();
        double sumProbability = 0;

        int index = -1;

        while(sumProbability < probability) {
            index++;
            sumProbability += modifiedFitness[index];
        }

        return population[index];
    }

    public static Individual crossover(Individual parent1, Individual parent2) {
            if(GA.CROSSOVER_RATE < Math.random()) {
                return parent1;
            }

            int index1 = (int) Math.floor(Math.random() * Individual.LENGTH);
            int index2 = (int) Math.floor(Math.random() * Individual.LENGTH);

            int beginIndex = Math.min(index1, index2);
            int endIndex = Math.max(index1, index2);

            Individual child = new Individual();

            for(int i = 0; i < beginIndex; i++) {
                child.set(i, parent1.get(i));
            }

            for(int i = beginIndex; i < endIndex; i++) {
                child.set(i, parent2.get(i));
            }

            for(int i = endIndex; i < Individual.LENGTH; i++) {
                child.set(i, parent1.get(i));
            }
 
        return child;
    }

    public void mutation() {
        for(int i = GA.ELITISM; i < population.length; i++) {
            //weight
            for(int j = 0; j < Individual.WEIGHT_LENGTH; j++) {
                if(GA.MUTATION_RATE > Math.random()) {
                    population[i].set(j, Math.random()); //randomly set from 0 to 1
                }
            }

            //bias
            for(int j = Individual.WEIGHT_LENGTH; j < Individual.LENGTH; j++) {
                if(GA.MUTATION_RATE > Math.random()) {
                    population[i].set(j, Math.random() * 2 - 1); //randomly set from -1 to 1
                }
            }
        }
    }

    public double averageFitness() {
        double sum = 0;

        for(Individual individual : population) {
            sum += individual.getFitness();
        }

        return sum / population.length;
    }

    public Population clone() {
        Population newPopulation = new Population(population.length);

        for(int i = 0; i < population.length; i++) {
            newPopulation.set(i, population[i].clone());
        }

        return newPopulation;
    }

    @Override
    public String toString() {
        String output = "";

        for(Individual individual : population) {
            output += individual.toString() + "\n";
        }

        return output.substring(0, output.length() - 2);
    }
}
