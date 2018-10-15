
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * 这里K=2，即二叉树
 * <p>
 * KD-Tree算法原理
 * http://www.cnblogs.com/pinard/p/6061661.html
 * https://www.cs.cmu.edu/~ckingsf/bioinfo-lectures/kdtrees.pdf
 * https://courses.cs.washington.edu/courses/cse373/02au/lectures/lecture22l.pdf
 * <p>
 * KD-Tree算法参考
 * https://www.cnblogs.com/zjh225901/p/7635651.html
 * <p>
 * 算法其他参考文章
 * https://leileiluoluo.com/posts/kdtree-algorithm-and-implementation.html
 * https://blog.csdn.net/google19890102/article/details/54291615
 * https://blog.csdn.net/zhong123123123/article/details/51377717
 */
public class TreeNode implements Comparable<TreeNode> {

    private static final int COOR_X = 1;
    private static final int COOR_Y = 2;

    // KD-Tree节点数据
    private Point data;
    // 与当前查询点的距离
    private double distance;
    // 该节点的左子树、右子树及父节点
    private TreeNode left, right, parent;
    // 节点的维度 1是X轴 2是Y轴
    private int dim = -1;

    TreeNode(Point data) {
        this.data = data;
    }


    /**
     * 得到特定维度数据
     *
     * @param dim 给定维度
     * @return Point类型数据的X坐标或Y坐标
     */
    private double getDimensionData(int dim) {
        if (data == null || dim < COOR_X || dim > COOR_Y)
            return Integer.MIN_VALUE;
        return dim == COOR_X ? data.getX() : data.getY();
    }


    @Override
    public int compareTo(TreeNode o) {
        return Double.compare(this.distance, o.distance);
    }


    /**
     * 计算两个TreeNode之间的距离
     *
     * @param node 给定的TreeNode
     * @return 距离
     */
    private double computeDistance(TreeNode node) {
        if (this.data == null || node == null || node.data == null)
            return Double.MAX_VALUE;
        return this.data.getDistance(node.data);
    }


    public String toString() {
        if (data == null)
            return "null data";
        return data.toString() + " Distance(tar):" + this.distance;
    }


    /**
     * 找到方差最大的轴
     *
     * @param nodes 节点集合
     * @return COOR_X 或 COOR_Y
     */
    public static int findMaxAxis(ArrayList<TreeNode> nodes) {
        double avg_x = 0.0, avg_y = 0.0;
        int len = nodes.size();
        for (TreeNode node : nodes) {
            avg_x = avg_x + node.data.getX() / len;
            avg_y = avg_y + node.data.getY() / len;
        }

        double s_x = 0.0, s_y = 0.0;
        for (TreeNode node : nodes) {
            s_x = s_x + Math.pow(node.data.getX() - avg_x, 2) / len;
            s_y = s_y + Math.pow(node.data.getY() - avg_y, 2) / len;
        }
        return (s_x > s_y) ? COOR_X : COOR_Y;
    }


    public static int getCoorX() {
        return COOR_X;
    }

    public static int getCoorY() {
        return COOR_Y;
    }

    public Point getData() {
        return data;
    }

    public void setData(Point data) {
        this.data = data;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }


    /**
     * 设置近邻点
     *
     * @param neighbors TreeNode形式的近邻点
     */
    public void setNeighbors(ArrayList<TreeNode> neighbors) {
        ArrayList<Point> neig = new ArrayList<>();
        for (TreeNode node : neighbors)
            neig.add(node.getData());
        this.getData().setNeighbors(neig);
    }

    /**
     * 构建KD-Tree
     *
     * @param nodes TreeNode集合
     * @param deep  分割维度 [1,2]
     * @return 返回KD-Tree根节点
     */
    public static TreeNode buildKDTree(ArrayList<TreeNode> nodes, int deep) {
        if (nodes == null || nodes.size() == 0)
            return null;

        // 快速排序找到中间点
        searchForMedian(nodes, deep, 0, nodes.size() - 1);
        // 使用Collections集合得到中间点
        // Collections.sort(nodes);

        TreeNode root = nodes.get(nodes.size() / 2);
        root.dim = deep;

        // 将中位数左右侧元素分别添加到两个数组【除中位数外无序，不能使用】
        // https://blog.csdn.net/zhong123123123/article/details/51377717
        // int mid = nodes.size() / 2;
        // int l = mid, r = mid;
        // while (l >= 0 && nodes.get(l).getDimensionData(deep) == nodes.get(mid).getDimensionData(deep)) l--;
        // while (r < nodes.size() && nodes.get(r).getDimensionData(deep) == nodes.get(mid).getDimensionData(deep)) r++;
        // ArrayList<TreeNode> left = (ArrayList<TreeNode>) nodes.subList(0, l + 1);
        // ArrayList<TreeNode> right = (ArrayList<TreeNode>) nodes.subList(r, nodes.size());

        // left集合包括小于等于中值节点值的node；right集合包括大于中值节点值的node
        ArrayList<TreeNode> left = new ArrayList<>();
        ArrayList<TreeNode> right = new ArrayList<>();
        for (TreeNode tmp : nodes) {
            if (root != tmp) {
                if (tmp.getDimensionData(deep) <= root.getDimensionData(deep))
                    left.add(tmp);
                else
                    right.add(tmp);
            }
        }

        // 使用计算得到的最大方差分割左右子树
        // root.left = buildKDTree(left, findMaxAxis(left));
        // root.right = buildKDTree(right, findMaxAxis(right));

        // 按照顺序分割子树，根据公式 L=(J mod k)+1:J为当前节点的深度,k为k维空间（维度）,默认根节点深度为0.
        // 此公式可看为：依次循环实例点的k个维所对应的坐标轴。
        // 本例中只有二维坐标，所以index有效范围就是[1,2],不可能超过3
        if (++deep > 2) deep = 1;
        root.left = buildKDTree(left, deep);
        root.right = buildKDTree(right, deep);

        if (root.left != null)
            root.left.parent = root;
        if (root.right != null)
            root.right.parent = root;
        return root;
    }


    /**
     * 通过快速排序找到数组的中间点，无需对数据进行全排序
     *
     * @param nodes TreeNode集合
     * @param dim   维度
     * @param left  数组排序左下标
     * @param right 数组排序右下标
     */
    public static void searchForMedian(ArrayList<TreeNode> nodes, int dim, int left, int right) {
        if (nodes == null || nodes.size() == 0 || left >= right)
            return;
        TreeNode stand = nodes.get(left);
        double key = stand.getDimensionData(dim);
        int start = left, end = right;

        while (start < end) {
            while (nodes.get(end).getDimensionData(dim) >= key && start < end)
                end--;
            if (nodes.get(end).getDimensionData(dim) <= key)
                nodes.set(start, nodes.get(end));

            while (nodes.get(start).getDimensionData(dim) <= key && start < end)
                start++;
            if (nodes.get(start).getDimensionData(dim) >= key)
                nodes.set(end, nodes.get(start));
        }

        nodes.set(start, stand);

        // 当排序完后判断start位置
        // 当为K时，应该判断start-left==k近邻数；这里K=2简化算法
        if (start == nodes.size() / 2)
            return;
        else if (start < nodes.size() / 2)
            searchForMedian(nodes, dim, start + 1, left);
        else if (start > nodes.size() / 2)
            searchForMedian(nodes, dim, left, start - 1);
    }


    /**
     * 近邻搜索
     * (1)给定目标点，在KD树里面找到包含目标点的叶子节点。
     * (2)以目标点为圆心，目标点到叶子节点的距离为半径，得到超球体，最近邻的点在这个超球体内部。
     * (3)返回叶子节点的父节点，检查另一个子节点包含的超矩形体是否和超球体相交，
     * 如果相交就到这个子节点寻找是否有更加近的近邻,有的话就更新最近邻。
     * 如果不相交直接返回父节点的父节点，在另一个子树继续搜索最近邻。
     * (4)当回溯到根节点时算法结束，此时保存的最近邻节点就是最终的最近邻。
     * <p>
     * 这里通过父节点指针回溯；另一篇文章中使用栈辅助回溯
     *
     * @param root   KD-Tree根节点
     * @param cur    当前节点
     * @param k      搜索点数
     * @param dim    首先查找维度
     * @param minDis 给定的距离阈值
     * @return 近邻点组成的ArrayList
     */
    public static ArrayList<TreeNode> searchKNN(TreeNode root, TreeNode cur, int k, int dim, double minDis, Logger logger) {
        ArrayList<TreeNode> knn = new ArrayList<>();
        searchBrother(root, cur, k, dim, minDis, knn, logger);
        return knn;
    }


    /**
     * 遍历其兄弟节点
     *
     * @param root   KD-Tree根节点
     * @param target 当前节点
     * @param k      近邻数
     * @param dim    首先查找维度
     * @param knn    返回近邻点集合
     */
    private static void searchBrother(TreeNode root, TreeNode target, int k, int dim, double minDis, ArrayList<TreeNode> knn, Logger logger) {
        // 包含目标点的叶子节点
        TreeNode leaf = searchLeaf(root, target, dim);
        // 最近近邻点与当前查询点距离，即球体半径
        double curDis = target.computeDistance(leaf);
        leaf.distance = curDis;

        if (leaf != target)
            maintainMaxHeap(knn, leaf, k, minDis, logger);

        while (leaf != root) {
            TreeNode brother = getBrother(leaf);
            if (brother != null) {
                // 判断与分割面是否相交 或 knn近邻点集合个数少于k
                if (curDis > Math.abs(target.getDimensionData(leaf.parent.dim) - leaf.parent.getDimensionData(leaf.parent.dim)) || knn.size() < k) {
                    // 个人认为在查找兄弟节点时应该用兄弟节点的分割方式
                    searchBrother(brother, target, k, brother.dim, minDis, knn, logger);
                }
            }

            // 向上回溯直到到根节点
            leaf = leaf.parent;
            double rootDis = target.computeDistance(leaf);
            leaf.distance = rootDis;

            if (leaf != target)
                maintainMaxHeap(knn, leaf, k, minDis, logger);
        }
    }


    /**
     * 查找包含目标节点cur的叶子节点
     * 假定查找数据是根据维度++顺序，即 X -> Y 或 Y -> X
     *
     * @param root 根节点
     * @param cur  当前节点
     * @param dim  首先查找维度
     * @return 叶子节点
     */
    private static TreeNode searchLeaf(TreeNode root, TreeNode cur, int dim) {
        TreeNode leaf = root, next;
        while (leaf.left != null || leaf.right != null) {
            // 若root节点值>cur节点值，进入左子树；若大于则进入右子树；否则比较节点间距离，进入距离较小的子树
            if (cur.getDimensionData(dim) < leaf.getDimensionData(dim))
                next = leaf.left;
            else if (cur.getDimensionData(dim) > leaf.getDimensionData(dim))
                next = leaf.right;
            else
                next = cur.computeDistance(leaf.left) < cur.computeDistance(leaf.right) ? leaf.left : leaf.right;

            if (next == null)
                break;

            leaf = next;

            if (++dim > 2) dim = 1;
        }
        return leaf;
    }


    /**
     * 利用大根堆特性维护最近点集合
     *
     * @param knn    当前最近点
     * @param minDis 距离阈值 超过此阈值的点舍弃
     */
    private static void maintainMaxHeap(ArrayList<TreeNode> knn, TreeNode node, int k, double minDis, Logger logger) {
        if (node.distance > minDis)
            return;

        if (knn.size() < k)
            fixed2Up(knn, node);
        else
            fixed2Down(knn, node, logger);
    }


    /**
     * 当knn集合元素不足k时，添加元素从数组最后一个元素开始调整
     *
     * @param knn  近邻点集合
     * @param node 当前节点
     */
    private static void fixed2Up(ArrayList<TreeNode> knn, TreeNode node) {
        knn.add(node);
        int i = knn.size() - 1;
        int j = (i + 1) / 2 - 1;
        while (j >= 0) {
            if (knn.get(j).distance >= knn.get(i).distance)
                break;
            TreeNode tmp = knn.get(j);
            knn.set(j, knn.get(i));
            knn.set(i, tmp);

            i = j;
            j = (j + 1) / 2 - 1;
        }
    }


    /**
     * knn集合元素个数达到k，向其中添加元素时重新更新knn集合中的排列顺序
     *
     * @param knn  近邻点集合
     * @param node 当前节点
     */
    private static void fixed2Down(ArrayList<TreeNode> knn, TreeNode node, Logger logger) {
        if (node.distance > knn.get(0).distance)
            return;

        //System.out.println("      ··· knn集合弹出元素： " + knn.get(0).toString());
        logger.info("      ··· knn集合弹出元素： " + knn.get(0).toString());

        knn.set(0, node);
        int i = 0;
        int j = i * 2 + 1;

        while (j < knn.size()) {
            if (j + 1 < knn.size() && knn.get(j).distance < knn.get(j + 1).distance)
                j++;
            if (knn.get(i).distance >= knn.get(j).distance)
                break;

            TreeNode tmp = knn.get(i);
            knn.set(i, knn.get(j));
            knn.set(j, tmp);

            i = j;
            j = j * 2 + 1;
        }
    }


    /**
     * 得到当前节点的兄弟节点
     *
     * @param cur 当前节点
     * @return 兄弟节点
     */
    private static TreeNode getBrother(TreeNode cur) {
        return cur == cur.parent.left ? cur.parent.right : cur.parent.left;
    }
}