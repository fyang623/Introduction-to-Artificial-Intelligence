import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NaiveBayesClassifier {
    public static void main(String[] args){
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.train(args[0]);
        classifier.predict(args[1]);
    }

    private double[][] democrat = new double[3][16];
    private double[][] republican = new double[3][16];
    private double numDemocrat, numRepublican;

    private void train(String trainningData){
        try {
            BufferedReader br = new BufferedReader(new FileReader(trainningData));
            String line = br.readLine();
            double[][] curr;
            while (line != null){
                String[] tokens = line.trim().split(",");
                if(tokens[0].equals("democrat"))
                    curr = democrat;
                else
                    curr = republican;

                for(int i = 0; i < 16; i++)
                    if(tokens[i + 1].equals("y"))
                        curr[0][i] += 1;
                    else if(tokens[i + 1].equals("n"))
                        curr[1][i] += 1;
                    else
                        curr[2][i] += 1;
                line = br.readLine();
            }
            br.close();

            numDemocrat = democrat[0][0] + democrat[1][0] + democrat[2][0];
            numRepublican = republican[0][0] + republican[1][0] + republican[2][0];
            for(int i = 0; i < 3; i++)
                for (int j = 0; j < 16; j++) {
                    democrat[i][j] /= numDemocrat;
                    republican[i][j] /= numRepublican;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void predict(String testData){
        try {
            BufferedReader br = new BufferedReader(new FileReader(testData));
            String line = br.readLine();
            while(line != null){
                double democratPr = numDemocrat/(numDemocrat + numRepublican);
                double republicanPr = numRepublican/(numDemocrat + numRepublican);
                String[] tokens = line.trim().split(",");
                for(int i = 0; i < 16; i++) {
                    if(tokens[i + 1].equals("y")) {
                        democratPr *= democrat[0][i];
                        republicanPr *= republican[0][i];
                    }
                    else if(tokens[i + 1].equals("n")){
                        democratPr *= democrat[1][i];
                        republicanPr *= republican[1][i];
                    } else {
                        democratPr *= democrat[2][i];
                        republicanPr *= republican[2][i];
                    }
                }
                System.out.println(democratPr / (democratPr + republicanPr));
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
