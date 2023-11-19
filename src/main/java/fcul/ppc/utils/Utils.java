package fcul.ppc.utils;

import java.util.Random;

public class Utils {
    public static final int N_GENERATIONS = 500;
    public static final int POP_SIZE = 100000;
    public static final double PROB_MUTATION = 0.5;
    public static final int TOURNAMENT_SIZE = 3;
    public static int max_iterations = 3;
    public static Individual tournament(int tournamentSize, Random r, Individual[] population) {
        Individual best = population[r.nextInt(POP_SIZE)];
        for (int i = 0; i < tournamentSize; i++) {
            Individual other = population[r.nextInt(POP_SIZE)];
            if (other.fitness > best.fitness) {
                best = other;
            }
        }
        return best;
    }

    public static Individual bestOfPopulation(Individual[] population) {
        /*
         * Returns the best individual of the population.
         */
        Individual best = population[0];
        for (Individual other : population) {
            if (other.fitness > best.fitness) {
                best = other;
            }
        }
        return best;
    }
}
