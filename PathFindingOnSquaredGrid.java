import java.awt.*;
import java.util.*;
import java.util.List;

/*************************************************************************
 *  Author: Dr E Kapetanios
 *  Last update: 22-02-2017
 *
 *************************************************************************/

public class PathFindingOnSquaredGrid {

    // given an N-by-N matrix of open cells, return an N-by-N matrix
    // of cells reachable from the top
    private static boolean[][] randomlyGenMatrix;
    private static Node[][] graph;
    private static ArrayList<Node> nodesToGetToPath = new ArrayList<>();
    private static List<Node> closedList = new ArrayList<>();
    private static ArrayList openList = new ArrayList();;
    private static int finalIPosition;
    private static int finalJPosition;
    public static boolean[][] flow(boolean[][] open) {
        int N = open.length;

        boolean[][] full = new boolean[N][N];
        for (int j = 0; j < N; j++) {
            flow(open, full, 0, j);
        }

        return full;
    }

    // determine set of open/blocked cells using depth first search
    public static void flow(boolean[][] open, boolean[][] full, int i, int j) {
        int N = open.length;

        // base cases
        if (i < 0 || i >= N) return;    // invalid row
        if (j < 0 || j >= N) return;    // invalid column
        if (!open[i][j]) return;        // not an open cell
        if (full[i][j]) return;         // already marked as open

        full[i][j] = true;

        flow(open, full, i+1, j);   // down
        flow(open, full, i, j+1);   // right
        flow(open, full, i, j-1);   // left
        flow(open, full, i-1, j);   // up
    }

    // does the system percolate?
    public static boolean percolates(boolean[][] open) {
        int N = open.length;

        boolean[][] full = flow(open);
        for (int j = 0; j < N; j++) {
            if (full[N-1][j]) return true;
        }

        return false;
    }

    // does the system percolate vertically in a direct way?
    public static boolean percolatesDirect(boolean[][] open) {
        int N = open.length;

        boolean[][] full = flow(open);
        int directPerc = 0;
        for (int j = 0; j < N; j++) {
            if (full[N-1][j]) {
                // StdOut.println("Hello");
                directPerc = 1;
                int rowabove = N-2;
                for (int i = rowabove; i >= 0; i--) {
                    if (full[i][j]) {
                        // StdOut.println("i: " + i + " j: " + j + " " + full[i][j]);
                        directPerc++;
                    }
                    else break;
                }
            }
        }

        // StdOut.println("Direct Percolation is: " + directPerc);
        if (directPerc == N) return true;
        else return false;
    }

    // draw the N-by-N boolean matrix to standard draw
    public static void show(boolean[][] a, boolean which) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j] == which)
                    StdDraw.square(j, N-i-1, .5);
                else StdDraw.filledSquare(j, N-i-1, .5);
    }

    // draw the N-by-N boolean matrix to standard draw, including the points A (x1, y1) and B (x2,y2) to be marked by a circle
    public static void show(boolean[][] a, boolean which, int x1, int y1, int x2, int y2) {
        int N = a.length;
        StdDraw.setXscale(-1, N);;
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j] == which)
                    if ((i == x1 && j == y1) ||(i == x2 && j == y2)) {
                        StdDraw.circle(j, N-i-1, .5);
                    }
                    else StdDraw.square(j, N-i-1, .5);
                else StdDraw.filledSquare(j, N-i-1, .5);
    }

    // return a random N-by-N boolean matrix, where each entry is
    // true with probability p
    public static boolean[][] random(int N, double p) {
        boolean[][] a = new boolean[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = StdRandom.bernoulli(p);
        return a;
    }


    //New code starts below
    public static void getManhattanDistance(int x, int y, int y1, int x1, int N){
        getMovementCost(y1, x1, N);
        findNextNodeToMoveTo(x, y, N);
    }

    private static void findNextNodeToMoveTo(int x, int y, int N) {

        Node nextNodeToGoTo = getGValue(x, y, N);
        if (nextNodeToGoTo == null){
            return;
        }
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.filledSquare(nextNodeToGoTo.getxCoordinate(), N - nextNodeToGoTo.getyCoordinate()-1, .5);
        nodesToGetToPath.add(nextNodeToGoTo);

        goToNextNode(nextNodeToGoTo, N);
    }

    private static void goToNextNode(Node nextNodeToGoTo, int N) {
        findNextNodeToMoveTo(nextNodeToGoTo.getyCoordinate(), nextNodeToGoTo.getxCoordinate(), N);
    }

    private static Node getGValue(int x, int y, int N) {
        int lineCost = 10;
        int diagonalCost = 14;
        Map<Node, Integer> dataMap = new HashMap<>();
        Node minimumNode = null;
        try {
            closedList.add(graph[x][y]);
            if (((x==0)&&(y==0)&& graph[x][y].data)){
                openList.add(graph[x + 1][y + 1]);
                dataMap.put(graph[x + 1][y + 1], graph[x + 1][y + 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x + 1][y + 1].getxCoordinate(), N - graph[x + 1][y + 1].getyCoordinate()-1, .5);

                openList.add(graph[x][y + 1]);
                dataMap.put(graph[x][y + 1], graph[x][y + 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y + 1].getxCoordinate(), N - graph[x][y + 1].getyCoordinate()-1, .5);

                openList.add(graph[x + 1][y]);
                dataMap.put(graph[x + 1][y], graph[x + 1][y].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x + 1][y].getxCoordinate(), N - graph[x + 1][y].getyCoordinate()-1, .5);
            }else if (((y==0)&&(x==N-1)&& graph[x][y].data)){
                openList.add(graph[x][y + 1]);
                dataMap.put(graph[x][y + 1], graph[x][y + 1].getMovementCost() + lineCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y + 1].getxCoordinate(), N - graph[x][y + 1].getyCoordinate()-1, .5);

                openList.add(graph[x - 1][y]);
                dataMap.put(graph[x - 1][y], graph[x - 1][y].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x - 1][y].getxCoordinate(), N - graph[x - 1][y].getyCoordinate()-1, .5);

                openList.add(graph[x - 1][y+1]);
                dataMap.put(graph[x - 1][y + 1], graph[x - 1][y + 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x - 1][y + 1].getxCoordinate(), N - graph[x - 1][y + 1].getyCoordinate()-1, .5);
            }else if (((y==N-1)&&(x==N-1)&& graph[x][y].data)){
                openList.add(graph[x - 1][y - 1]);
                dataMap.put(graph[x - 1][y - 1], graph[x - 1][y - 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x - 1][y - 1].getxCoordinate(), N - graph[x - 1][y - 1].getyCoordinate()-1, .5);

                openList.add(graph[x][y - 1]);
                dataMap.put(graph[x][y - 1], graph[x][y - 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y - 1].getxCoordinate(), N - graph[x][y - 1].getyCoordinate()-1, .5);

                openList.add(graph[x - 1][y]);
                dataMap.put(graph[x - 1][y], graph[x - 1][y].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x - 1][y].getxCoordinate(), N - graph[x - 1][y].getyCoordinate()-1, .5);
            }else if (((y==N-1)&&(x==0)&& graph[x][y].data)){
                openList.add(graph[x + 1][y - 1]);
                dataMap.put(graph[x + 1][y - 1], graph[x + 1][y - 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x + 1][y - 1].getxCoordinate(), N - graph[x + 1][y - 1].getyCoordinate()-1, .5);

                openList.add(graph[x][y - 1]);
                dataMap.put(graph[x][y - 1], graph[x][y - 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y - 1].getxCoordinate(), N - graph[x][y - 1].getyCoordinate()-1, .5);

                openList.add(graph[x + 1][y]);
                dataMap.put(graph[x + 1][y], graph[x + 1][y].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x + 1][y].getxCoordinate(), N - graph[x + 1][y].getyCoordinate()-1, .5);
            }else if (((y==N-1)&& graph[x][y].data)){
                openList.add(graph[x][y - 1]);
                dataMap.put(graph[x][y - 1], graph[x][y - 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y - 1].getxCoordinate(), N - graph[x][y - 1].getyCoordinate() - 1, .5);

                openList.add(graph[x+1][y - 1]);
                dataMap.put(graph[x+1][y - 1], graph[x+1][y - 1].getMovementCost() + lineCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x+1][y - 1].getxCoordinate(), N - graph[x+1][y - 1].getyCoordinate() - 1, .5);

                openList.add(graph[x - 1][y - 1]);
                dataMap.put(graph[x - 1][y - 1], graph[x - 1][y - 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x - 1][y - 1].getxCoordinate(), N - graph[x - 1][y - 1].getyCoordinate()-1, .5);

                openList.add(graph[x - 1][y]);
                dataMap.put(graph[x - 1][y], graph[x - 1][y].getMovementCost() + lineCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x - 1][y].getxCoordinate(), N - graph[x - 1][y].getyCoordinate()-1, .5);

                openList.add(graph[x + 1][y]);
                dataMap.put(graph[x + 1][y], graph[x + 1][y].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x + 1][y].getxCoordinate(), N - graph[x + 1][y].getyCoordinate()-1, .5);
            }else if (((x==0)&& graph[x][y].data)){
                openList.add(graph[x][y - 1]);
                dataMap.put(graph[x][y - 1], graph[x][y - 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y - 1].getxCoordinate(), N - graph[x][y - 1].getyCoordinate() - 1, .5);

                openList.add(graph[x+1][y - 1]);
                dataMap.put(graph[x+1][y - 1], graph[x+1][y - 1].getMovementCost() + lineCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x+1][y - 1].getxCoordinate(), N - graph[x+1][y - 1].getyCoordinate() - 1, .5);

                openList.add(graph[x + 1][y + 1]);
                dataMap.put(graph[x + 1][y + 1], graph[x + 1][y + 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x + 1][y + 1].getxCoordinate(), N - graph[x + 1][y + 1].getyCoordinate()-1, .5);

                openList.add(graph[x + 1][y]);
                dataMap.put(graph[x + 1][y], graph[x + 1][y].getMovementCost() + lineCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x + 1][y].getxCoordinate(), N - graph[x + 1][y].getyCoordinate()-1, .5);

                openList.add(graph[x][y + 1]);
                dataMap.put(graph[x][y + 1], graph[x][y + 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y + 1].getxCoordinate(), N - graph[x][y + 1].getyCoordinate()-1, .5);
            }else if (((x==N-1)&& graph[x][y].data)){
                openList.add(graph[x][y + 1]);
                dataMap.put(graph[x][y + 1], graph[x][y + 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y + 1].getxCoordinate(), N - graph[x][y + 1].getyCoordinate() - 1, .5);

                openList.add(graph[x][y - 1]);
                dataMap.put(graph[x][y - 1], graph[x][y - 1].getMovementCost() + lineCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y - 1].getxCoordinate(), N - graph[x][y - 1].getyCoordinate() - 1, .5);

                openList.add(graph[x - 1][y - 1]);
                dataMap.put(graph[x - 1][y - 1], graph[x - 1][y - 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x - 1][y - 1].getxCoordinate(), N - graph[x - 1][y - 1].getyCoordinate()-1, .5);

                openList.add(graph[x - 1][y]);
                dataMap.put(graph[x - 1][y], graph[x - 1][y].getMovementCost() + lineCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x - 1][y].getxCoordinate(), N - graph[x - 1][y].getyCoordinate()-1, .5);

                openList.add(graph[x - 1][y + 1]);
                dataMap.put(graph[x - 1][y + 1], graph[x - 1][y + 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x - 1][y + 1].getxCoordinate(), N - graph[x - 1][y + 1].getyCoordinate()-1, .5);
            }else if(((y==0)&& graph[x][y].data)) {
                openList.add(graph[x + 1][y + 1]);
                dataMap.put(graph[x + 1][y + 1], graph[x + 1][y + 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x + 1][y + 1].getxCoordinate(), N - graph[x + 1][y + 1].getyCoordinate() - 1, .5);

                openList.add(graph[x][y + 1]);
                dataMap.put(graph[x][y + 1], graph[x][y + 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y + 1].getxCoordinate(), N - graph[x][y + 1].getyCoordinate() - 1, .5);

                openList.add(graph[x + 1][y]);
                dataMap.put(graph[x + 1][y], graph[x + 1][y].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x + 1][y].getxCoordinate(), N - graph[x + 1][y].getyCoordinate() - 1, .5);

                openList.add(graph[x + 1][y - 1]);
                dataMap.put(graph[x + 1][y - 1], graph[x + 1][y - 1].getMovementCost() + diagonalCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x + 1][y - 1].getxCoordinate(), N - graph[x + 1][y - 1].getyCoordinate() - 1, .5);

                openList.add(graph[x][y - 1]);
                dataMap.put(graph[x][y - 1], graph[x][y - 1].getMovementCost() + lineCost);
                StdDraw.setXscale(-1, N);
                StdDraw.setYscale(-1, N);
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledSquare(graph[x][y - 1].getxCoordinate(), N - graph[x][y - 1].getyCoordinate() - 1, .5);
            }else{
                if (validationIsPassed(graph[x - 1][y - 1])) {
                    openList.add(graph[x - 1][y - 1]);
                    dataMap.put(graph[x - 1][y - 1], graph[x - 1][y - 1].getMovementCost() + diagonalCost);
                    StdDraw.setXscale(-1, N);
                    StdDraw.setYscale(-1, N);
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledSquare(graph[x - 1][y - 1].getxCoordinate(), N - graph[x - 1][y - 1].getyCoordinate()-1, .5);
                }
                if (validationIsPassed(graph[x-1][y])) {
                    openList.add(graph[x - 1][y]);
                    dataMap.put(graph[x - 1][y], graph[x - 1][y].getMovementCost() + lineCost);
                    StdDraw.setXscale(-1, N);
                    StdDraw.setYscale(-1, N);
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledSquare(graph[x - 1][y].getxCoordinate(), N - graph[x - 1][y].getyCoordinate()-1, .5);
                }
                if (validationIsPassed(graph[x-1][y+1])) {
                    openList.add(graph[x - 1][y + 1]);
                    dataMap.put(graph[x - 1][y + 1], graph[x - 1][y + 1].getMovementCost() + diagonalCost);
                    StdDraw.setXscale(-1, N);
                    StdDraw.setYscale(-1, N);
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledSquare(graph[x - 1][y + 1].getxCoordinate(), N - graph[x - 1][y + 1].getyCoordinate()-1, .5);
                }
                if (validationIsPassed(graph[x][y+1])) {
                    openList.add(graph[x][y + 1]);
                    dataMap.put(graph[x][y + 1], graph[x][y + 1].getMovementCost() + lineCost);
                    StdDraw.setXscale(-1, N);
                    StdDraw.setYscale(-1, N);
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledSquare(graph[x][y + 1].getxCoordinate(), N - graph[x][y + 1].getyCoordinate()-1, .5);
                }
                if (validationIsPassed(graph[x][y-1])) {
                    openList.add(graph[x][y - 1]);
                    dataMap.put(graph[x][y - 1], graph[x][y - 1].getMovementCost() + lineCost);
                    StdDraw.setXscale(-1, N);
                    StdDraw.setYscale(-1, N);
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledSquare(graph[x][y - 1].getxCoordinate(), N - graph[x][y - 1].getyCoordinate()-1, .5);
                }
                if (validationIsPassed(graph[x+1][y-1])) {
                    openList.add(graph[x + 1][y - 1]);
                    dataMap.put(graph[x + 1][y - 1], graph[x + 1][y - 1].getMovementCost() + diagonalCost);
                    StdDraw.setXscale(-1, N);
                    StdDraw.setYscale(-1, N);
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledSquare(graph[x + 1][y - 1].getxCoordinate(), N - graph[x + 1][y - 1].getyCoordinate()-1, .5);
                }
                if (validationIsPassed(graph[x+1][y+1])) {
                    openList.add(graph[x + 1][y + 1]);
                    dataMap.put(graph[x + 1][y + 1], graph[x + 1][y + 1].getMovementCost() + lineCost);
                    StdDraw.setXscale(-1, N);
                    StdDraw.setYscale(-1, N);
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledSquare(graph[x + 1][y + 1].getxCoordinate(), N - graph[x + 1][y + 1].getyCoordinate()-1, .5);
                }
                if (validationIsPassed(graph[x+1][y])) {
                    openList.add(graph[x + 1][y]);
                    dataMap.put(graph[x + 1][y], graph[x + 1][y].getMovementCost() + diagonalCost);
                    StdDraw.setXscale(-1, N);
                    StdDraw.setYscale(-1, N);
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledSquare(graph[x + 1][y].getxCoordinate(), N - graph[x + 1][y].getyCoordinate()-1, .5);
                }
            }


        }catch (ArrayIndexOutOfBoundsException e){

        }
        Iterator it = dataMap.entrySet().iterator();
        int minimumGValue = 1000;

        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Node currentNode = (Node)pair.getKey();
            if (currentNode == null)
                continue;
            if (currentNode.isFinalNode()){
                System.out.println("Final node has been found "+ currentNode.getxCoordinate()+ ", "+ currentNode.getyCoordinate());

                return null;
            }
            if (((int) pair.getValue()) < minimumGValue){
                minimumGValue = (int) pair.getValue();
                minimumNode = currentNode;
            }
            it.remove();
        }
        if (minimumNode != null) {
            System.out.println(minimumGValue + "==" + minimumNode.nodeNumber);
        }

        return minimumNode;
    }

    private static boolean validationIsPassed(Node o) {
        boolean validationFlag;
        try {
            validationFlag = !((closedList.contains(o)) || (!o.data));
        }catch (Exception e){
            validationFlag = false;
        }

        return validationFlag;
    }

    private static void getMovementCost(int y1, int x1, int N) {
        int awayFromX=0;
        int awayFromY=0;
        int anchorPoint=0;
        graph[y1][x1].setFinalNode(true);
        //finalIPosition = y1;

        for (int i=0; i < N; i++){
            if (i > y1) {
                awayFromY += 2;
            }
            for (int j=0; j < N; j++){
                int movementCost;

                if (j > x1){
                    awayFromX++;
                    movementCost = (anchorPoint+awayFromX);
                }else {
                    movementCost = (x1 - j) + (y1 - i);
                    if (j == x1){
                        anchorPoint = movementCost;
                    }
                }
                if (i > y1){
                    movementCost += awayFromY;
                }
                graph[i][j].setMovementCost(movementCost);
                System.out.print(graph[i][j].getMovementCost()+" ");
            }
            System.out.println();
            awayFromX=0;
        }
    }

    public static void getEuclideanDistance(int x, int y, int y1, int x1, int N){
        getMovementCost(y1, x1, N);
        findNextNodeToMoveTo(x, y, N);
    }

    public static void getChebyshevDistance(int x, int y, int x1, int y1){

    }

    public static void getShortestPathAlgorithm(int N){
        graph = new Node[N][N];
        int k = 0;
        for (int i=0; i < N; i++){
            for (int j=0; j < N; j++){
                graph[i][j] = new Node(randomlyGenMatrix[i][j], k);
                graph[i][j].setxCoordinate(j);
                graph[i][j].setyCoordinate(i);
                k++;
            }
        }

    }

    // test client
    public static void main(String[] args) {
        // boolean[][] open = StdArrayIO.readBoolean2D();
        // The following will generate a 10x10 squared grid with relatively few obstacles in it
        // The lower the second parameter, the more obstacles (black cells) are generated

        int N = 10;
        randomlyGenMatrix = random(N, 1);

        StdArrayIO.print(randomlyGenMatrix);
        show(randomlyGenMatrix, true);

        System.out.println();
        System.out.println("The system percolates: " + percolates(randomlyGenMatrix));

        System.out.println();
        System.out.println("The system percolates directly: " + percolatesDirect(randomlyGenMatrix));
        System.out.println();


        // Reading the coordinates for points A and B on the input squared grid.

        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Start the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        Stopwatch timerFlow = new Stopwatch();

        Scanner in = new Scanner(System.in);
        System.out.println("Enter i for A > ");
        int Ai = in.nextInt();

        System.out.println("Enter j for A > ");
        int Aj = in.nextInt();

        System.out.println("Enter i for B > ");
        int Bi = in.nextInt();

        System.out.println("Enter j for B > ");
        int Bj = in.nextInt();


        getShortestPathAlgorithm(N);
        getManhattanDistance(Ai, Aj, Bi, Bj, N);
        for (Node n : nodesToGetToPath) {
            if (n == null){
                break;
            }
            StdDraw.setXscale(-1, N);
            StdDraw.setYscale(-1, N);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.filledSquare(n.getxCoordinate(), N - n.getyCoordinate()-1, .5);
            StdDraw.setPenColor(StdDraw.GRAY);
            //StdDraw.line(Ai, Aj, N-n.getxCoordinate(), n.getyCoordinate()-1);
            System.out.print(n.nodeNumber + "--");
        }
        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Stop the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        StdOut.println("Elapsed time = " + timerFlow.elapsedTime());

        // System.out.println("Coordinates for A: [" + Ai + "," + Aj + "]");
        // System.out.println("Coordinates for B: [" + Bi + "," + Bj + "]");

        show(randomlyGenMatrix, true, Ai, Aj, Bi, Bj);
    }

}



