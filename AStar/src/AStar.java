import java.util.*;
public class AStar {

    char[][] map;
    int initialRow, initialCol, goalRow, goalCol;


    public AStar(char[][]map, int initialRow, int initialCol, int goalRow,int goalCol){
        this.map = map;
        this.initialRow = initialRow;
        this.initialCol = initialCol;
        this.goalRow = goalRow;
        this.goalCol = goalCol;
    }

    public void printPath(){
        Node n = searchPath();
        ArrayList<String> path = new ArrayList<>();

        if(n != null) {
            while (n.row != initialRow || n.col != initialCol) {
                path.add(0, "\"" + n.move + "\"");
                n = n.prev;
            }
            System.out.println(path);
        } else
            System.out.println("null");
    }

    private Node searchPath(){
        Node[][] nodes = new Node[map.length][map[0].length];
        Set<Node> explored = new HashSet<>();
        Queue<Node> frontier = new PriorityQueue<>();

        Node curr = new Node(initialRow, initialCol);
        nodes[initialRow][initialCol] = curr;
        frontier.add(curr);
        while(!frontier.isEmpty()){
            curr = frontier.poll();
            explored.add(curr);
            if(curr.row == goalRow && curr.col == goalCol)
                return curr;
            for(Node n : curr.neighbors()){
                if(!explored.contains(n)){
                    if(!frontier.contains(n)) {
                        frontier.add(n);
                        nodes[n.row][n.col] = n;
                    } else {
                        Node m = nodes[n.row][n.col];
                        if(m.compareTo(n) > 0) {
                            frontier.remove(m);
                            frontier.add(n);
                            nodes[n.row][n.col] = n;
                        }
                    }
                }
            }
        }
        return null;
    }

    private class Node implements Comparable<Node> {

        char terrain;
        int row, col;
        int costG, costH;
        Node prev;
        String move;

        Node(int row, int col) {
            this.row = row;
            this.col = col;
            terrain = map[row][col];
        }

        @Override
        public int compareTo(Node n) {
            if (costG + costH == n.costG + n.costH) return 0;
            if (costG + costH < n.costG + n.costH) return -1;
            return 1;
        }

        @Override
        public boolean equals(Object n) {
            Node node = (Node) n;
            return row == node.row && col == node.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        private int terrainCost(char terrain){
            if(terrain == 'r') return 1;
            if(terrain == 'f') return 2;
            if(terrain == 'h') return 5;
            if(terrain == 'm') return 10;
            return Integer.MAX_VALUE;
        }

        public ArrayList<Node> neighbors() {
            int r = map.length;
            int c = map[0].length;
            ArrayList<Node> neighbors = new ArrayList<>();
            Node n;

            if (row - 1 >= 0 && map[row - 1][col] != 'w') {
                n = new Node(row - 1, col);
                n.prev = this;
                n.move = "u";
                n.costG = costG + terrainCost(map[row - 1][col]);
                n.costH = Math.abs(goalRow - (row -1)) + Math.abs(goalCol - col);
                neighbors.add(n);
            }
            if (row + 1 < r && map[row + 1][col] != 'w') {
                n = new Node(row + 1, col);
                n.prev = this;
                n.move = "d";
                n.costG = costG + terrainCost(map[row + 1][col]);
                n.costH = Math.abs(goalRow - (row + 1)) + Math.abs(goalCol - col);
                neighbors.add(n);
            }
            if (col - 1 >= 0 && map[row][col - 1] != 'w') {
                n = new Node(row, col - 1);
                n.prev = this;
                n.move = "l";
                n.costG = costG + terrainCost(map[row][col - 1]);
                n.costH = Math.abs(goalRow - row) + Math.abs(goalCol - (col - 1));
                neighbors.add(n);
            }
            if (col + 1 < c && map[row][col + 1] != 'w') {
                n = new Node(row, col + 1);
                n.prev = this;
                n.move = "r";
                n.costG = costG + terrainCost(map[row][col + 1]);
                n.costH = Math.abs(goalRow - row) + Math.abs(goalCol - (col + 1));
                neighbors.add(n);
            }
            return neighbors;
        }
    }
}
