

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {

        System.out.println(">>> Program Started ...");

        String sourcePath = "E:\\Study\\项目\\生态福建\\Process\\inPoints.txt";
        String destFile = "E:\\Study\\项目\\生态福建\\Process\\outPoints.txt";
        double radius = 5;
        int minPoints = 3;
        int deep = 1;

        Logger logger = Logger.getLogger("DBSCAN");
        PropertyConfigurator.configure("log4j.properties");
        //System.out.println(">>> (2/8) Logger Configure Finished ...");
        logger.info(" (1/6) Logger Configure Finished ...");

        // step 读取数据
        ArrayList<Point> allPoints = Data.readDataToPoint(sourcePath);
        ArrayList<TreeNode> allNodes = Data.readDataToTreeNode(sourcePath);
        //System.out.println(">>> (3/8) Reading Data Finished ...");
        logger.info(" (2/6) Reading Data Finished ...");

        // step 初始化DBSCAN
        DBSCAN dbscan = new DBSCAN(radius, minPoints);

        // step 建立KD-Tree
        TreeNode root = TreeNode.buildKDTree(allNodes, deep);
        //System.out.println(">>> (4/8) Building KD-Tree Finished ...");
        logger.info(" (3/6) Building KD-Tree Finished ...");

        // step 得到核心点
        ArrayList<Point> cores1 = dbscan.findPointCores(allPoints);
        ArrayList<TreeNode> cores = dbscan.findTreeNodeCores(minPoints, radius, root, allNodes, logger);
        if (cores == null || cores.size() == 0) {
            System.out.println("Can not find any core points.");
            return;
        }
        //System.out.println(">>> (5/8) Finding Core Point Finished ...");
        logger.info(" (4/6) Finding Core Point Finished ...");

        // step 开始计算
        //dbscan.processPoint(cores, allPoints);
        dbscan.processTreeNode(cores);
        //System.out.println(">>> (6/8) Processing Algorithm Finished ...");
        logger.info(" (5/6) Processing Algorithm Finished ...");

        // step 保存结果
        Data.writeTreeNodeData(cores, destFile);
        //System.out.println(">>> (6/8) Writing Data Finished ...");
        logger.info(" (6/6) Writing Data Finished ...");

        System.out.println(">>> Process Finished ...");

        System.out.println();
    }
}
