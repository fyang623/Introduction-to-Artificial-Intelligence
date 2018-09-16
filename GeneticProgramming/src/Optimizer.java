import javax.json.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;

public class Optimizer {
    public static void main(String[] args) {
        try{
            String data = args[0];
            JsonArray JsonData = Json.createReader(new BufferedReader(new FileReader(data))).readArray();
            Optimizer optimizer = new Optimizer();
            JsonValue function = optimizer.optimize(JsonData);
            System.out.println("Best Scoring Function:\t" + function);
            System.out.println("Sum of Squared Errors:\t" + Calculator.computeError(JsonData, function));
            System.out.println("Fitness:\t" + Calculator.computeFitness(JsonData, function));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private int NumTree = 10000;  //the population
    private int NumGen = 1000;  //limit of generations
    private int NumSave = 1000;  //save the best batch to next round
    private Crossover crossoverer = new Crossover();
    private Initializer initializer = new Initializer();
    private Mutator mutator = new Mutator();
    private Trimmer trimmer = new Trimmer();
    private Random random = new Random();

    private class ExpTree implements Comparable<ExpTree> {
        JsonValue exp;
        double fitness;

        public ExpTree(JsonValue JsonExp, JsonArray JsonData){
            exp = trimmer.trim(JsonExp);
            fitness = Calculator.computeFitness(JsonData, exp);
        }

        public int compareTo(ExpTree t){
            return Double.compare(fitness, t.fitness);
        }
    }

    private JsonValue optimize(JsonArray data){
        ExpTree[] currGen = new ExpTree[NumTree];
        ExpTree[] nextGen = new ExpTree[NumTree];
        ExpTree[] temp;
        JsonValue[] children;
        double[] scores = {10000, 20000, 30000, 40000, 50000, 60000};
        int i1, i2;

        for(int i = 0; i < NumTree; i++)
            currGen[i] = new ExpTree(initializer.initialize(), data);

        for(int gen = 0; gen < NumGen; gen++){
            Arrays.sort(currGen);

            if(gen % 10 == 0) {
                scores[5] = currGen[0].fitness < scores[5] ? currGen[0].fitness : scores[5];
                Arrays.sort(scores);
                if(scores[5] - scores[0] < 0.1 || scores[0] < 0.05){
                    System.out.println("\nSum of squared errors is no longer decreasing! Optimization goal achieved!\n");
                    break;
                } else {
                    System.out.println(+gen + "th Generation:");
                    System.out.println("\tBest Scoring Function\t" + currGen[0].exp);
                    System.out.println("\tSum of Squared Errors\t" + Calculator.computeError(data, currGen[0].exp));
                    System.out.println("\tFitness:\t" + currGen[0].fitness + "\n");
                }
            }

            System.arraycopy(currGen, 0, nextGen, 0, NumSave);

            for(int i = NumSave; i < NumTree; i += 2) {
                i1 = (int) (random.nextDouble() * NumTree % NumTree / 2);
                i2 = (int) (random.nextDouble() * NumTree % NumTree / 2);
                children = crossoverer.crossover(currGen[i1].exp, currGen[i2].exp);
                if(random.nextInt(100) < 5)
                    children[0] = mutator.mutate(children[0]);
                if(random.nextInt(100) < 5)
                    children[1] = mutator.mutate(children[1]);
                nextGen[i] = new ExpTree(children[0], data);
                nextGen[i + 1] = new ExpTree(children[1], data);
            }
            temp = currGen;
            currGen = nextGen;
            nextGen = temp;
        }
        Arrays.sort(currGen);
        return currGen[0].exp;
    }
}
