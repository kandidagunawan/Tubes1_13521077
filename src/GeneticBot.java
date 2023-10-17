import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.scene.control.Button;

public class GeneticBot extends Bot{
    private static final int POPULATION_SIZE = 100;
    private static final double MUTATION_RATE = 0.2;
    private static final int MAX_GENERATIONS = 100;

    public int[] move(Button[][] button, int roundLeft, boolean isBotFirst, String botChar) {
        int[][] bestChromosome = runGeneticAlgorithm(button, roundLeft, isBotFirst, botChar);
        return bestChromosome[0];
    }

    public int[][] runGeneticAlgorithm(Button[][] buttons, int roundLeft, boolean isBotFirst, String botChar){
        Random random = new Random();
        int[][] bestChromosome = null;
        int bestFitness = 0;
        for(int gen = 0; gen < MAX_GENERATIONS; gen++){
            List<int[]>emptyCells = generate_empty_cell(buttons);
            int[][][] population = initializePopulation(emptyCells, roundLeft);
            for(int[][] chromosome : population){
                int fitness = evaluateChromosome(chromosome, buttons, roundLeft, botChar);
                if(fitness > bestFitness){
                    bestFitness = fitness;
                    bestChromosome = chromosome;
                }
            }
            int[][][] newPopulation = crossoverPopulation(population, roundLeft);
            mutatePopulation(newPopulation, emptyCells, roundLeft);
            population = newPopulation;
        }
        return bestChromosome;
    }

    public int[][][]initializePopulation(List<int[]> emptyCells, int roundLeft){
        int[][][] population = new int[POPULATION_SIZE][][];
        for(int i = 0; i < POPULATION_SIZE; i++){
            int[][] chromosome = new int[roundLeft][2];
            for(int j = 0; j < roundLeft; j++){
                int index_list;
                int x, y;
                boolean hasDuplicates;
                do {
                    Random random = new Random();
                    index_list = random.nextInt(emptyCells.size());
                    x = emptyCells.get(index_list)[0];
                    y = emptyCells.get(index_list)[1];
                    hasDuplicates = false;
                    for (int k = 0; k < j; k++) {
                        if (chromosome[k][0] == x && chromosome[k][1] == y) {
                            hasDuplicates = true;
                            break;
                        }
                    }
                } while (hasDuplicates);
                chromosome[j][0] = x;
                chromosome[j][1] = y;
            }
            population[i] = chromosome;
        }
        return population;
    }

    public int evaluateChromosome(int[][] chromosome, Button[][] buttons, int roundLeft, String player){
        int chromosomeValue = 0;
        for(int[]c : chromosome){
            chromosomeValue += (objectiveFunction(c, buttons, roundLeft, player));
        }
        return chromosomeValue;
    }
    public int[][][] selectPopulation(int[][][] population){
        int[][][] as = new int[8][8][8];
        return as;
    }
    public int[][][] crossoverPopulation(int[][][] population, int roundLeft){
        Random random = new Random();
        int[][][] newPopulation = new int[POPULATION_SIZE][][];
        newPopulation = population;
        boolean skipCrossOver = false;
        for(int i = 0; i < POPULATION_SIZE; i++){
            int parent1Index = random.nextInt(POPULATION_SIZE);
            int parent2Index = random.nextInt(POPULATION_SIZE);
            int[][] parent1 = population[parent1Index];
            int[][] parent2 = population[parent2Index];
            int[][] childChromosome1 = new int[roundLeft][2];
            int[][] childChromosome2 = new int[roundLeft][2];
            childChromosome1 = parent1;
            childChromosome2 = parent2;
            int crossoverPoint = random.nextInt(roundLeft);
            for(int j = crossoverPoint; j < roundLeft; j++){
                childChromosome1[j][0] = parent2[j][0];
                childChromosome1[j][1] = parent2[j][1];
                for(int k = 0; k < roundLeft; k++){
                    if(Arrays.equals(childChromosome1[k],childChromosome1[j])&& k != j){
                        skipCrossOver = true;
                        break;
                    }
                }
                if(skipCrossOver){
                    break;
                }
                childChromosome2[j][0] = parent1[j][0];
                childChromosome2[j][1] = parent1[j][1];
            }

            newPopulation[parent1Index] = childChromosome1;
            newPopulation[parent2Index] = childChromosome2;
        }
        population = newPopulation;
        return population;
    }
    public void mutatePopulation(int[][][] population, List<int[]>emptyCells, int roundLeft) {
        // Implement mutation operation
        Random random = new Random();
        boolean noMutate = false;
        for(int i = 0; i < POPULATION_SIZE; i++){
           if(random.nextDouble() < MUTATION_RATE){
               int[][] chromosome = population[i];
               int mutationPoint = random.nextInt(roundLeft);
               int indexList = random.nextInt(emptyCells.size());
               int newX = emptyCells.get(indexList)[0];
               int newY = emptyCells.get(indexList)[1];
               for(int j = 0; j < roundLeft; j++){
                   if(chromosome[j][0] == newX && chromosome[j][1] == newY && j != mutationPoint){
                       noMutate = true;
                       break;
                   }
               }
               if(noMutate){
                   continue;
               }else{
                   chromosome[mutationPoint][0] = newX;
                   chromosome[mutationPoint][1] = newY;
                   population[i] = chromosome;
               }
           }
        }

    }
    public void printChromosome(int[][] chromosome){
        System.out.println("Best chromosome: [");
        for(int i = 0; i < chromosome.length; i++){
            System.out.println(chromosome[i]);
            if(i < chromosome.length-1){
                System.out.println(",");
            }
        }
        System.out.println("]");
    }
}
