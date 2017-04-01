
public class Node {
    boolean data;
    int nodeNumber;
    private int movementCost;
    private int xCoordinate;
    private int yCoordinate;
    private boolean finalNode = false;
    private Node parentNode = null;
    private int previousNodeCost = 0;
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

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public boolean isFinalNode() {
        return finalNode;
    }

    public void setFinalNode(boolean finalNode) {
        this.finalNode = finalNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public int getPreviousNodeCost() {
        return previousNodeCost;
    }

    public void setPreviousNodeCost(int previousNodeCost) {
        this.previousNodeCost = previousNodeCost;
    }
}
