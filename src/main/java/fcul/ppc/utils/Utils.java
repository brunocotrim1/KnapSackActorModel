package fcul.ppc.utils;

public class Utils {
    // Utils class in order to prevent redundant declarations inside actors, and to make it easier to change
    // parameters. So i would not consider this shared memory but rather a way to make the code more readable.

    public static final int N_GENERATIONS = 500;
    public static final int POP_SIZE = 100000;
    public static final double PROB_MUTATION = 0.5;
    public static final int TOURNAMENT_SIZE = 3;
    public static final int MAX_ITERATIONS = 1;
}
