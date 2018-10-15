import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Data {

    public static ArrayList<Point> readDataToPoint(String sourcePath) {
        ArrayList<Point> list = new ArrayList<>();
        File inFile = new File(sourcePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            String line = br.readLine();
            while (line != null) {
                double x = Double.parseDouble(line.split(",")[0]);
                double y = Double.parseDouble(line.split(",")[1]);
                double weight = Double.parseDouble(line.split(",")[2]);
                list.add(new Point(x, y, weight));
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static ArrayList<TreeNode> readDataToTreeNode(String sourcePath) {
        ArrayList<TreeNode> list = new ArrayList<>();
        File inFile = new File(sourcePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            String line = br.readLine();
            while (line != null) {
                double x = Double.parseDouble(line.split(",")[0]);
                double y = Double.parseDouble(line.split(",")[1]);
                double weight = Double.parseDouble(line.split(",")[2]);
                list.add(new TreeNode(new Point(x, y, weight)));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void writePointData(ArrayList<Point> cores, String destPath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(destPath));
            for (Point core : cores) {
                bw.write(core.toString() + "\r\n");
                for (Point point : core.getNeighbors())
                    bw.write(point.toString() + "\r\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeTreeNodeData(ArrayList<TreeNode> cores, String destPath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(destPath));
            for (TreeNode core : cores) {
                bw.write(core.toString() + "\r\n");
                for (Point point : core.getData().getNeighbors())
                    bw.write(point.toString() + "\r\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //    不知道有没有用
    public static ArrayList<Point> generateSinData(int size) {
        ArrayList<Point> points = new ArrayList<Point>(size);
        Random rd = new Random(size);
        for (int i = 0; i < size / 2; i++) {
            double x = (Math.PI / (size / 2) * (i + 1));
            double y = (Math.sin(x));
            double weight = 1;
            points.add(new Point(x, y, weight));
        }
        for (int i = 0; i < size / 2; i++) {
            double x = 1.5 + Math.PI / (size / 2) * (i + 1);
            double y = Math.cos(x);
            double weight = 1;
            points.add(new Point(x, y, weight));
        }
        return points;
    }

    public static ArrayList<Point> generateSpecialData() {
        ArrayList<Point> points = new ArrayList<Point>();
        points.add(new Point(2, 2, 1));
        points.add(new Point(3, 1, 1));
        points.add(new Point(3, 4, 1));
        points.add(new Point(3, 14, 1));
        points.add(new Point(5, 3, 1));
        points.add(new Point(8, 3, 1));
        points.add(new Point(8, 6, 1));
        points.add(new Point(9, 8, 1));
        points.add(new Point(10, 4, 1));
        points.add(new Point(10, 7, 1));
        points.add(new Point(10, 10, 1));
        points.add(new Point(10, 14, 1));
        points.add(new Point(11, 13, 1));
        points.add(new Point(12, 7, 1));
        points.add(new Point(12, 15, 1));
        points.add(new Point(14, 7, 1));
        points.add(new Point(14, 9, 1));
        points.add(new Point(14, 15, 1));
        points.add(new Point(15, 8, 1));
        return points;
    }
}
