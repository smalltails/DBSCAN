import java.util.ArrayList;

//clusterID 初始为0，噪声点为-1，分类后值为聚簇ID
public class Point {
    private double x;
    private double y;
    private double weight;
    private boolean isVisited;
    private boolean isCore;
    private int clusterID;
    private ArrayList<Point> neighbors;

    Point(double x, double y, double weight) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.isVisited = false;
        this.isCore = false;
        this.clusterID = 0;
        this.neighbors = null;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ArrayList<Point> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Point> neighbors) {
        this.neighbors = neighbors;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean isCore() {
        return isCore;
    }

    public void setCore(boolean core) {
        isCore = core;
    }

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public double getDistance(Point point) {
        return Math.sqrt(Math.pow((this.x - point.x), 2) + Math.pow((this.y - point.y), 2)) + this.weight + point.weight;
    }

    public String toString() {
        return "X:" + x + " Y:" + y + " W:" + weight + " C:" + clusterID + (isCore ? " c" : " n") + (isVisited ? " v" : " u");
    }
}
