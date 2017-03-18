
public class Node {
    boolean data;
    int nodeNumber;
    private int movementCost;
    Node(boolean d, int nodeNum){
        data = d;
        nodeNumber = nodeNum;
    }

    public int getMovementCost() {
        return movementCost;
    }

    public void setMovementCost(int movementCost) {
        this.movementCost = movementCost;
    }
}
