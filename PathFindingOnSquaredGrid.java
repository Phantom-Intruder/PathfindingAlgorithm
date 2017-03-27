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
    private static int numberOfLopps = 0;
    private static List<Node> openList= new ArrayList<>();
    private static int finalNodeX = 0;
    private static int finalNodeY = 0;
    private static boolean isManhatten = false;
    private static boolean isChebyshev = false;
    private static boolean isEuclidean = false;

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
                        StdDraw.circle(i, N-j-1, .5);
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
    public static void getManhattanDistance(int x, int y, int x1, int y1, int N){
        getMovementCost(y1, x1, N);
        isManhatten = true;
        searchTheNodes(x, y);
    }

    private static void getChebyshevDistance(int x, int y, int x1, int y1, int N) {
        getMovementCost(y1, x1, N);
        isChebyshev = true;
        searchTheNodes(x, y);
    }

    private static void searchTheNodes(int x, int y) {
        boolean flag = false;
        openList.add(graph[x][y]);
        finalNodeX = x;
        finalNodeY = y;
        int index = 0;
        Node currentNode = null;
        while (!flag||(index == openList.size())){
            try {
                currentNode = openList.get(index);
                if (closedList.contains(currentNode)||(!currentNode.data)){
                    index++;
                    continue;
                }
                StdDraw.setXscale(-1, numberOfLopps);
                StdDraw.setYscale(-1, numberOfLopps);
                StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                StdDraw.filledSquare(currentNode.getyCoordinate(), numberOfLopps - currentNode.getxCoordinate()-1, .5);
                closedList.add(currentNode);
                x = currentNode.getxCoordinate();
                y = currentNode.getyCoordinate();
                index++;
                if (((x==0)&&(y==0)&& graph[x][y].data)){
                    if (graph[x+1][y].isFinalNode()) flag=true;
                    else if (graph[x][y+1].isFinalNode()) flag=true;
                    else if(graph[x+1][y+1].isFinalNode()) flag=true;
                    else {
                        openList.add(graph[x+1][y]);
                        openList.add(graph[x+1][y+1]);
                        openList.add(graph[x][y+1]);

                        graph[x+1][y] = isOnClosedList(graph[x+1][y], graph[x][y]);
                        graph[x+1][y+1] = isOnClosedList(graph[x+1][y+1], graph[x][y]);
                        graph[x][y+1] = isOnClosedList(graph[x][y+1], graph[x][y]);
                    }
                    //checkBottomRight(graph[x + 1][y + 1], numberOfLopps, graph[x + 1][y + 1].getMovementCost(), dataMap);
                }else if (((y==0)&&(x==numberOfLopps-1)&& graph[x][y].data)){
                    if (graph[x][y+1].isFinalNode()) flag=true;
                    else if (graph[x-1][y].isFinalNode()) flag=true;
                    else if(graph[x-1][y+1].isFinalNode()) flag=true;
                    else {
                        openList.add(graph[x][y+1]);
                        openList.add(graph[x-1][y]);
                        openList.add(graph[x-1][y+1]);

                        graph[x][y+1] = isOnClosedList(graph[x][y+1], graph[x][y]);
                        graph[x-1][y] = isOnClosedList(graph[x-1][y], graph[x][y]);
                        graph[x-1][y+1] = isOnClosedList(graph[x-1][y+1], graph[x][y]);
                    }
                    //checkBottom(graph[x][y + 1], numberOfLopps, graph[x][y + 1].getMovementCost(), dataMap);
                }else if (((y==numberOfLopps-1)&&(x==numberOfLopps-1)&& graph[x][y].data)){
                    if (graph[x][y-1].isFinalNode()) flag=true;
                    else if (graph[x-1][y].isFinalNode()) flag=true;
                    else if(graph[x-1][y-1].isFinalNode()) flag=true;
                    else {
                        openList.add(graph[x][y-1]);
                        openList.add(graph[x-1][y]);
                        openList.add(graph[x-1][y-1]);

                        graph[x][y-1] = isOnClosedList(graph[x][y-1], graph[x][y]);
                        graph[x-1][y] = isOnClosedList(graph[x-1][y], graph[x][y]);
                        graph[x-1][y-1] = isOnClosedList(graph[x-1][y-1], graph[x][y]);
                    }
                    //checkLeft(graph[x - 1][y], numberOfLopps, graph[x - 1][y].getMovementCost(), dataMap);
                }else if (((y==numberOfLopps-1)&&(x==0)&& graph[x][y].data)){
                    if (graph[x+1][y].isFinalNode()) flag=true;
                    else if (graph[x][y-1].isFinalNode()) flag=true;
                    else if(graph[x+1][y-1].isFinalNode()) flag=true;
                    else {
                        openList.add(graph[x+1][y]);
                        openList.add(graph[x][y-1]);
                        openList.add(graph[x+1][y-1]);

                        graph[x+1][y] = isOnClosedList(graph[x+1][y], graph[x][y]);
                        graph[x][y-1] = isOnClosedList(graph[x][y-1], graph[x][y]);
                        graph[x+1][y-1] = isOnClosedList(graph[x+1][y-1], graph[x][y]);
                    }
                    //checkRight(graph[x][y-1], numberOfLopps, graph[x + 1][y].getMovementCost(), dataMap);
                }else if (((y==numberOfLopps-1)&& graph[x][y].data)){
                    if (graph[x+1][y].isFinalNode()) flag=true;
                    else if (graph[x][y-1].isFinalNode()) flag=true;
                    else if(graph[x+1][y-1].isFinalNode()) flag=true;
                    else if (graph[x-1][y-1].isFinalNode()) flag=true;
                    else if(graph[x-1][y].isFinalNode()) flag=true;
                    else {
                        openList.add(graph[x + 1][y]);
                        openList.add(graph[x][y - 1]);
                        openList.add(graph[x + 1][y - 1]);
                        openList.add(graph[x - 1][y - 1]);
                        openList.add(graph[x - 1][y]);

                        graph[x+1][y] = isOnClosedList(graph[x+1][y], graph[x][y]);
                        graph[x][y - 1] = isOnClosedList(graph[x][y-1], graph[x][y]);
                        graph[x + 1][y - 1] = isOnClosedList(graph[x+1][y-1], graph[x][y]);
                        graph[x - 1][y - 1] = isOnClosedList(graph[x-1][y-1], graph[x][y]);
                        graph[x - 1][y] = isOnClosedList(graph[x-1][y], graph[x][y]);
                    }
                    // checkRight(graph[x - 1][y], numberOfLopps, graph[x + 1][y].getMovementCost(), dataMap);
                }else if (((x==0)&& graph[x][y].data)){
                    if (graph[x+1][y].isFinalNode()) flag=true;
                    else if (graph[x][y-1].isFinalNode()) flag=true;
                    else if(graph[x+1][y-1].isFinalNode()) flag=true;
                    else if (graph[x][y+1].isFinalNode()) flag=true;
                    else if(graph[x+1][y+1].isFinalNode()) flag=true;else {
                        openList.add(graph[x+1][y]);
                        openList.add(graph[x][y-1]);
                        openList.add(graph[x+1][y-1]);
                        openList.add(graph[x][y+1]);
                        openList.add(graph[x+1][y+1]);

                        graph[x+1][y] = isOnClosedList(graph[x+1][y], graph[x][y]);
                        graph[x][y-1] = isOnClosedList(graph[x][y-1], graph[x][y]);
                        graph[x+1][y-1] = isOnClosedList(graph[x+1][y-1], graph[x][y]);
                        graph[x][y+1] = isOnClosedList(graph[x][y+1], graph[x][y]);
                        graph[x+1][y+1] = isOnClosedList(graph[x+1][y+1], graph[x][y]);
                        graph[x+1][y] = isOnClosedList(graph[x+1][y], graph[x][y]);
                    }
                    // checkBottom(graph[x][y - 1], numberOfLopps, graph[x][y + 1].getMovementCost(), dataMap);
                }else if (((x==numberOfLopps-1)&& graph[x][y].data)){
                    if (graph[x][y+1].isFinalNode()) flag=true;
                    else if (graph[x][y-1].isFinalNode()) flag=true;
                    else if(graph[x-1][y+1].isFinalNode()) flag=true;
                    else if (graph[x-1][y-1].isFinalNode()) flag=true;
                    else if(graph[x-1][y].isFinalNode()) flag=true;
                    else {
                        openList.add(graph[x][y+1]);
                        openList.add(graph[x][y-1]);
                        openList.add(graph[x-1][y+1]);
                        openList.add(graph[x-1][y-1]);
                        openList.add(graph[x-1][y]);

                        graph[x][y+1] = isOnClosedList(graph[x][y+1], graph[x][y]);
                        graph[x][y-1] = isOnClosedList(graph[x][y-1], graph[x][y]);
                        graph[x-1][y+1] = isOnClosedList(graph[x-1][y+1], graph[x][y]);
                        graph[x-1][y-1] = isOnClosedList(graph[x-1][y-1], graph[x][y]);
                        graph[x-1][y] = isOnClosedList(graph[x-1][y], graph[x][y]);
                    }
                    // checkBottomLeft(graph[x - 1][y + 1], numberOfLopps, graph[x - 1][y + 1].getMovementCost(), dataMap);
                }else if(((y==0)&& graph[x][y].data)) {
                    if (graph[x+1][y].isFinalNode()) flag=true;
                    else if (graph[x-1][y].isFinalNode()) flag=true;
                    else if(graph[x-1][y+1].isFinalNode()) flag=true;
                    else if (graph[x][y+1].isFinalNode()) flag=true;
                    else if(graph[x+1][y+1].isFinalNode()) flag=true;
                    else {
                        openList.add(graph[x+1][y]);
                        openList.add(graph[x-1][y]);
                        openList.add(graph[x-1][y+1]);
                        openList.add(graph[x][y+1]);
                        openList.add(graph[x+1][y+1]);

                        graph[x-1][y] = isOnClosedList(graph[x-1][y], graph[x][y]);
                        graph[x-1][y+1] = isOnClosedList(graph[x-1][y+1], graph[x][y]);
                        graph[x][y+1] = isOnClosedList(graph[x][y+1], graph[x][y]);
                        graph[x+1][y+1] = isOnClosedList(graph[x+1][y+1], graph[x][y]);
                        graph[x+1][y] = isOnClosedList(graph[x+1][y], graph[x][y]);
                    }
                    //checkTop(graph[x - 1][y + 1], numberOfLopps, graph[x - 1][y + 1].getMovementCost(), dataMap);
                }else{
                    if (graph[x+1][y].isFinalNode()) flag=true;
                    else if (graph[x-1][y].isFinalNode()) flag=true;
                    else if(graph[x-1][y+1].isFinalNode()) flag=true;
                    else if (graph[x][y+1].isFinalNode()) flag=true;
                    else if(graph[x+1][y+1].isFinalNode()) flag=true;
                    else if(graph[x-1][y-1].isFinalNode()) flag=true;
                    else if (graph[x][y-1].isFinalNode()) flag=true;
                    else if(graph[x+1][y-1].isFinalNode()) flag=true;else {
                        //logic
                        openList.add(graph[x+1][y]);
                        openList.add(graph[x-1][y]);
                        openList.add(graph[x-1][y+1]);
                        openList.add(graph[x][y+1]);
                        openList.add(graph[x+1][y+1]);
                        openList.add(graph[x-1][y-1]);
                        openList.add(graph[x][y-1]);
                        openList.add(graph[x+1][y-1]);

                        graph[x+1][y] = isOnClosedList(graph[x+1][y], graph[x][y]);
                        graph[x-1][y] = isOnClosedList(graph[x-1][y], graph[x][y]);
                        graph[x-1][y+1] = isOnClosedList(graph[x-1][y+1], graph[x][y]);
                        graph[x][y+1] = isOnClosedList(graph[x][y+1], graph[x][y]);
                        graph[x+1][y+1] = isOnClosedList(graph[x+1][y+1], graph[x][y]);
                        graph[x-1][y-1] = isOnClosedList(graph[x-1][y-1], graph[x][y]);
                        graph[x][y-1] = isOnClosedList(graph[x][y-1], graph[x][y]);
                        graph[x+1][y-1] = isOnClosedList(graph[x+1][y-1], graph[x][y]);
                    }
                }
                currentNode = graph[x][y];
            }catch (IndexOutOfBoundsException e){
                StdOut.print("No path found");
                return;
            }
        }
        while (currentNode.getParentNode() != null){
            StdDraw.setXscale(-1, numberOfLopps);
            StdDraw.setYscale(-1, numberOfLopps);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.filledSquare(currentNode.getyCoordinate(), numberOfLopps - currentNode.getxCoordinate()-1, .5);
            currentNode = currentNode.getParentNode();
        }
    }

    private static Node isOnClosedList(Node nodeToCheckIfOnClosedList, Node nodeToCheckAgainst) {
        if ((nodeToCheckIfOnClosedList.getxCoordinate() == finalNodeX)&&(nodeToCheckIfOnClosedList.getyCoordinate() == finalNodeY)){
            nodeToCheckIfOnClosedList.setParentNode(null);
        }else{
            if (nodeToCheckAgainst.getParentNode() == nodeToCheckIfOnClosedList){
                return nodeToCheckIfOnClosedList;
            }
            int lineCost = 10;
            int diagonalCost;
            if (isManhatten) {
                diagonalCost = 20;
            }else{
                diagonalCost = 10;
            }
            int cost = nodeToCheckIfOnClosedList.getMovementCost();
            if ((nodeToCheckIfOnClosedList.getxCoordinate()-nodeToCheckAgainst.getxCoordinate() != 0)&&(nodeToCheckIfOnClosedList.getyCoordinate()-nodeToCheckAgainst.getyCoordinate() != 0)) {
                cost += diagonalCost;
            }else{
                cost +=  lineCost;
            }
            if (nodeToCheckIfOnClosedList.getParentNode() == null){
                nodeToCheckIfOnClosedList.setParentNode(nodeToCheckAgainst);
                if ((nodeToCheckIfOnClosedList.getxCoordinate()-nodeToCheckAgainst.getxCoordinate() != 0)&&(nodeToCheckIfOnClosedList.getyCoordinate()-nodeToCheckAgainst.getyCoordinate() != 0)) {
                    nodeToCheckIfOnClosedList.setPreviousNodeCost(diagonalCost+nodeToCheckIfOnClosedList.getMovementCost());
                }else{
                    nodeToCheckIfOnClosedList.setPreviousNodeCost(lineCost+nodeToCheckIfOnClosedList.getMovementCost());
                }
            }else if(nodeToCheckIfOnClosedList.getPreviousNodeCost() > cost){
                nodeToCheckIfOnClosedList.setParentNode(nodeToCheckAgainst);
                nodeToCheckIfOnClosedList.setPreviousNodeCost(cost);
            }}
        return nodeToCheckIfOnClosedList;
    }

    private static void getMovementCost(int y1, int x1, int N) {
        int awayFromX=0;
        int awayFromY=0;
        int anchorPoint=0;
        graph[x1][y1].setFinalNode(true);

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
                if (graph[i][j].data) {
                    graph[i][j].setMovementCost(movementCost);
                }else{
                    graph[i][j].setMovementCost(1000);
                }
            }
            awayFromX=0;
        }
    }

    public static void getShortestPathAlgorithm(int N){
        graph = new Node[N][N];
        int k = 0;
        for (int i=0; i < N; i++){
            for (int j=0; j < N; j++){
                graph[i][j] = new Node(randomlyGenMatrix[i][j], k);
                graph[i][j].setxCoordinate(i);
                graph[i][j].setyCoordinate(j);
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
        numberOfLopps = N;
        randomlyGenMatrix = random(N, 0.8);

        StdArrayIO.print(randomlyGenMatrix);
        show(randomlyGenMatrix, true);
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
        //getChebyshevDistance(Aj, Ai, Bj, Bi, N);
        getManhattanDistance(Aj, Ai, Bj, Bi, N);
        for (Node n : closedList) {
            if (n == null){
                break;
            }
        }
        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Stop the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        StdOut.println("Elapsed time = " + timerFlow.elapsedTime());
        int manhattenDistance1 = ((Ai - Bi) < 0) ? ((Ai - Bi)*-1) : ((Ai - Bi));
        int manhattenDistance2 = (((Aj - Bj)) < 0) ? (((Aj - Bj))*-1) : ((Aj - Bj));
        int manhattenDistance = manhattenDistance1+manhattenDistance2;

        double euclideanDistance = Math.sqrt((((Ai - Bi)*(Ai - Bi))+((Aj - Bj)*(Aj - Bj))));
        StdOut.println("Manhattan distance: " + manhattenDistance);
        StdOut.println("Euclidean distance: " + euclideanDistance);


        show(randomlyGenMatrix, true, Ai, Aj, Bi, Bj);
    }


}

