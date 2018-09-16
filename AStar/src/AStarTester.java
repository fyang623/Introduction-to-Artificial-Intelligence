import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class AStarTester {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            ArrayList<char[]> listMap = new ArrayList<>();
            String str = br.readLine();
            while (str != null) {
                listMap.add(str.trim().toCharArray());
                str = br.readLine();
            }
            char[][] map = new char[listMap.size()][];
            int i = 0;
            for(char[] row : listMap)
                map[i++] = row;
            int initialRow = Integer.parseInt(args[1]);
            int initialCol = Integer.parseInt(args[2]);
            int goalRow = Integer.parseInt(args[3]);
            int goalCol = Integer.parseInt(args[4]);
            AStar as = new AStar(map, initialRow, initialCol, goalRow, goalCol);
            as.printPath();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
