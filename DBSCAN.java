import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * 算法原理
 * https://www.cnblogs.com/pinard/p/6208966.html
 * <p>
 * 算法参考
 * https://blog.csdn.net/allenalex/article/details/50926032
 * https://www.cnblogs.com/zhangchaoyang/articles/2182748.html
 * <p>
 * 程序结构参考
 * https://github.com/lincolnmi/algorithms/blob/master/src/Cluster/DBScan/DBScan.java
 */
class DBSCAN {
    private double radius;
    private int minPoints;

    DBSCAN(double radius, int minPoints) {
        this.radius = radius;
        this.minPoints = minPoints;
    }


    /**
     * 找到所有核心点  Point
     *
     * @param allPoints 所有点集合
     * @return 核心Point集合
     */
    public ArrayList<Point> findPointCores(ArrayList<Point> allPoints) {
        ArrayList<Point> corePoints = new ArrayList<>();
        for (Point p : allPoints) {
            // TODO: 2018/9/27 需要根据KD-Tree改进查找算法
            ArrayList<Point> neighbors = getNeighbors(p, allPoints);
            if (neighbors.size() >= minPoints) {
                p.setCore(true);
                p.setNeighbors(neighbors);
                corePoints.add(p);
            }
        }
        return corePoints;
    }


    /**
     * 找到所有核心点 TreeNode
     *
     * @param allNodes 所有TreeNode集合
     * @return 核心TreeNode集合
     */
    public ArrayList<TreeNode> findTreeNodeCores(int minPoints, double minDis, TreeNode root, ArrayList<TreeNode> allNodes, Logger logger) {
        ArrayList<TreeNode> corePoints = new ArrayList<>();
        for (TreeNode node : allNodes) {
            ArrayList<TreeNode> neighbors = TreeNode.searchKNN(root, node, minPoints, 1, minDis, logger);
            if (neighbors.size() >= minPoints) {
                node.getData().setCore(true);
                node.setNeighbors(neighbors);
                corePoints.add(node);
            }
        }
        return corePoints;
    }


    /**
     * 开始DBSCAN算法 Point形式
     *
     * @param cores     核心点集合
     * @param allPoints 所有点集合
     */
    public void processPoint(ArrayList<Point> cores, ArrayList<Point> allPoints) {

        // 不需将聚类结果输出
        // ArrayList<ArrayList<Point>> process(ArrayList<Point> cores, ArrayList<Point> allPoints) {
        // ArrayList<ArrayList<Point>> clusters = new ArrayList<ArrayList<Point>>();

        // 若此核心点已在某个聚簇中
        // if (core.getClusterID() > 0) continue;
        // 不需将聚类结果输出
        // ArrayList<Point> curCluster = new ArrayList<Point>();
        // curCluster.add(core);

        // core.setClusterID(clusterID);

        // 将能直达的的点聚类到该clusterID中
        // densityConnected(core, clusterID, allPoints);

        // 不需将聚类结果输出
        // densityConnected(core, clusterID, curCluster, allPoints);
        // clusters.add(curCluster);

        // clusterID++;


        // 参考https://www.cnblogs.com/zhangchaoyang/articles/2182748.html文章思路
        int clusterID = 0;
        for (Point core : cores) {
            if (core.isVisited())
                continue;
            // TODO: 2018/10/12
            core.setVisited(true);
            if (core.getClusterID() > 0)
                expand(core, core.getNeighbors(), core.getClusterID());
            else {
                clusterID++;
                expand(core, core.getNeighbors(), clusterID);
            }
        }
        // return clusters;
    }


    /**
     * 开始DBSCAN算法 TreeNode形式
     *
     * @param cores 核心点集合
     */
    public void processTreeNode(ArrayList<TreeNode> cores) {
        int clusterID = 0;
        for (TreeNode node : cores) {
            if (node.getData().isVisited())
                continue;
            node.getData().setVisited(true);
            if (node.getData().getClusterID() > 0)
                expand(node.getData(), node.getData().getNeighbors(), node.getData().getClusterID());
            else {
                clusterID++;
                expand(node.getData(), node.getData().getNeighbors(), clusterID);
            }
        }
    }


    /**
     * 扩张聚簇
     *
     * @param core      遍历到的核心点
     * @param neighbors 该核心点的近邻点
     * @param clusterID 聚簇ID
     */
    private void expand(Point core, ArrayList<Point> neighbors, int clusterID) {
        core.setClusterID(clusterID);
        // 遍历核心点的近邻
        for (Point p : neighbors) {
            // p未被访问过
            if (!p.isVisited()) {
                p.setVisited(true);
                if (p.isCore()) {
                    ArrayList<Point> curNeighbors = p.getNeighbors();
                    for (Point cur : curNeighbors)
                        if (cur.getClusterID() <= 0)
                            cur.setClusterID(clusterID);
                }
            }
            // p不属于任何簇时
            if (p.getClusterID() <= 0)
                p.setClusterID(clusterID);
        }
    }


    /**
     * 得到近邻点集合
     *
     * @param core      核心点
     * @param clusterID 该核心点所在的clusterID
     *                  //     * @param curCluster 当前聚类
     * @param allPoints 所有点集合
     */
    private void densityConnected(Point core, int clusterID, ArrayList<Point> allPoints) {
        if (core.getClusterID() > 0) return;
        // TODO: 2018/9/27 使用KD-Tree优化
        ArrayList<Point> neighbors = getNeighbors(core, allPoints);
        for (Point p : neighbors) {
            if (p.getClusterID() <= 0) {
                // curCluster.add(p);
                p.setClusterID(clusterID);
                if (p.isCore())
                    densityConnected(p, clusterID, allPoints);
            }
        }
    }


    /***
     * 找到未访问的近邻点
     * @param cur       当前Point
     * @param points    所有点集合
     * @return 近邻点集合
     */
    private ArrayList<Point> getNeighbors(Point cur, ArrayList<Point> points) {
        ArrayList<Point> neighborPoints = new ArrayList<>();
        for (Point p : points) {
            if (p.getClusterID() > 0) continue;
            double distance = cur.getDistance(p);
            if (distance <= radius && distance != 0)
                neighborPoints.add(p);
        }
        return neighborPoints;
    }
}
