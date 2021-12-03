package Exp1;

import java.util.*;

/**
 * 利用决策树ID3算法实现《能否找到对象》
 *
 * @author 柳笑辰
 * @keyword 信息论、信息增益、信息熵
 * <p>
 * TODO: 2021/11/29
 * </p>
 */


public class ID3 {
    //计算log2
    public static final double log2(int num1, int num2) {
        double number = (double) num1 / (double) num2;
        return Math.log(number) / Math.log(2);
    }

    public static final String[] dataSetTitle = new String[]{
            "相貌", "身高", "体型", "性格", "结果"
    };
    //row的length
    public static final int[] dataSetCount = new int[]{
            3, 3, 3, 2, 2
    };
    //训练集
    public static final int[][] dataSet = new int[][]{
            /**
             *
             *     0相貌      1身高      2体型     3性格    4结果
             *     相貌    0丑     1一般    2好看
             *     身高    0矮     1正常    2高
             *     体型    0胖     1匀称    2瘦
             *     性格    0暴躁   1平静
             *     结果    0否     1能
             *
            */

            {0, 0, 0, 0, 0},
            {0, 2, 2, 1, 0},
            {0, 2, 0, 1, 0},
            {0, 0, 2, 0, 0},
            {0, 2, 2, 1, 1},
            {0, 1, 0, 1, 0},
            {0, 1, 2, 0, 0},
            {0, 1, 2, 1, 1},
            {0, 0, 1, 1, 0},
            {0, 1, 1, 0, 0},
            {0, 2, 1, 1, 1},
            {1, 0, 2, 1, 1},
            {1, 2, 0, 1, 0},
            {1, 2, 2, 0, 0},
            {1, 0, 0, 1, 1},
            {1, 2, 1, 1, 1},
            {1, 1, 1, 1, 1},
            {1, 1, 1, 0, 0},
            {1, 1, 2, 1, 1},
            {1, 0, 0, 1, 0},
            {1, 1, 2, 1, 1},
            {1, 2, 2, 0, 0},
            {2, 0, 2, 1, 1},
            {2, 2, 0, 1, 1},
            {2, 1, 1, 0, 0},
            {2, 1, 1, 1, 1},
            {2, 1, 2, 1, 1},
            {2, 0, 2, 0, 0},
            {2, 1, 0, 1, 1},
            {2, 2, 2, 0, 0},
            {2, 0, 0, 1, 0},
            {2, 2, 1, 1, 1},
            {2, 2, 2, 0, 0}
    };


    /**
     * 节点信息
     * title          :  分支的标题
     * node           :  该分支的数字标号 包含父节点
     * parentChoice   :  父节点们的选项
     * choice         :  分支选项
     * childNode      :  分支选项下面的节点
     */
    static class Node {
        private String title;
        private ArrayList<Integer> node;
        private ArrayList<Integer> parentChoice;
        private HashMap<Integer, Node> childNode;

        public Node() {

        }

        public Node(int row, ArrayList<Integer> parentNode, ArrayList<Integer> parentParentChoice) {
            this.title = dataSetTitle[row];
            try {
                node = (ArrayList<Integer>) parentNode.clone();
            } catch (java.lang.NullPointerException e) {
                node = new ArrayList<Integer>();
            } finally {
                node.add(row);
            }
            try {
                parentChoice = (ArrayList<Integer>) parentParentChoice.clone();
            } catch (java.lang.NullPointerException e) {
                parentChoice = new ArrayList<>();
            }


            childNode = new HashMap<>();
            int choices = dataSetCount[row];
            for (int i = 0; i < choices; i++) {
                childNode.put(i, new Node());
            }

        }

        public String getTitle() {
            return title;
        }


        public ArrayList<Integer> getNode() {
            return node;
        }

        public HashMap<Integer, Node> getChildNode() {
            return childNode;
        }

        public void printChildNode() {
            Set<Integer> set = childNode.keySet();
            for (Integer i : set) {
                System.out.print("  " + i + "->" + childNode.get(i).getTitle());
            }
        }


    }
    public static void output(Node node) {
        System.out.print(node.getTitle());
        if (node.getNode() != null && !"不确定".equals(node.title)) {
            System.out.print("    父节点的标号（包含本节点） : " + node.getNode());
            System.out.print("      子节点 : ");
            node.printChildNode();
        }else {
            if(!"不确定".equals(node.title)) {
                System.out.println("   概率为：" + calculateH(node.node, node.parentChoice));
            }else{
                int count = findAndCount(node.node, node.parentChoice,0)+findAndCount(node.node, node.parentChoice , 1);
                System.out.println("   能的概率是："+(double)findAndCount(node.node,node.parentChoice,1)/count);
            }
        }

    }

    /**
     * 计数
     *
     * @param row          指定的列
     * @param rowAimNumber 指定的列的目标值
     * @param num          结果
     * @return row.get(i)       指定第i列           相貌
     * rowAimNumber(i)  第i列的关键字         丑
     */

    public static int findAndCount(ArrayList<Integer> row, ArrayList<Integer> rowAimNumber, int num) {
        int count = 0;


        for (int i = 0; i < dataSet.length; i++) {
            if (row == null) {
                if (dataSet[i][dataSet[0].length - 1] == num) {
                    count++;
                }
            } else {
                // row              =    [0,1]
                // rowAimNumber     =    [0,0]
                // 相貌=丑 && 身高=矮  在该条件下的能/否 数目
                // 针对每一行进行每一个元素的比较
                boolean flag = true;
                for (int j = 0; j < row.size(); j++) {
                    if (dataSet[i][row.get(j)] == rowAimNumber.get(j) && dataSet[i][dataSet[0].length - 1] == num) {
                        ;
                    } else {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 计算熵
     *
     * @param list          条件列坐标
     * @param listAimNumber 该列下的目标值
     * @return
     */
    public static double calculateH(ArrayList<Integer> list, ArrayList<Integer> listAimNumber) {

        double res;
        int negative = findAndCount(list, listAimNumber, 0);
        int positive = findAndCount(list, listAimNumber, 1);
        int sum = negative + positive;
        if (negative == 0) {
            return 0d;
        } else if (positive == 0) {
            return 0d;
        }
        //计算概率
        double negativeRate = (double) negative / sum;
        double positiveRate = (double) positive / sum;
        res = negativeRate * log2(sum, negative) + positiveRate * log2(sum, positive);
        return res;
    }

    /**
     * 计算该列项目的信息增益
     * IG = H - sum( P(t)*H(t) )
     * list和listAimNumber都为条件
     *
     * @param row           计算的列
     * @param list          条件列坐标
     * @param listAimNumber 条件列下的目标值
     * @return
     */
    public static double calculateIG(int row, ArrayList<Integer> list, ArrayList<Integer> listAimNumber) {
        double IG = calculateH(list, listAimNumber);
        double sum = 0d;
        //count符合条件的行数
        int count = list.size() == 0 ? findAndCount(list, listAimNumber, 0) : findAndCount(list, listAimNumber, 0) + findAndCount(list, listAimNumber, 1);
        //以row列的长度值建立概率数组
        double[] probability = new double[dataSetCount[row]];
        for (int i = 0; i < probability.length; i++) {
            list.add(row);
            listAimNumber.add(i);

            int tempCountNo = findAndCount(list, listAimNumber, 0);
            int tempCountYes = findAndCount(list, listAimNumber, 1);
            int sumCount = tempCountNo + tempCountYes;
            //如果有一项是0 直接加0
            if (tempCountYes == 0 || tempCountNo == 0) {
                sum += 0;
            } else {
                probability[i] = (double) sumCount / count;
                sum += probability[i] * ((double) tempCountNo / sumCount * log2(sumCount, tempCountNo) + (double) tempCountYes / sumCount * log2(sumCount, tempCountYes));
            }
            list.remove(list.size() - 1);
            listAimNumber.remove(listAimNumber.size() - 1);
        }
        return IG - sum;
    }

    /**
     * 高可复用性m叉决策树机器学习框架
     * 初始化决策树
     * @param node
     */
    public static Node initChosenTree(Node node) {
        //初始化两个arraylist
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> listAimNumber = new ArrayList<>();
        //获取最大数据
        int maxRow = 0;
        double maxRowData = 0.0;
        for (int i = 0; i < dataSetTitle.length - 1; i++) {
            double tempData = calculateIG(i, list, listAimNumber);
            if (tempData > maxRowData) {
                maxRow = i;
                maxRowData = tempData;
            }
        }
        //将最大的数据作为根结点
        node = new Node(maxRow, null, null);
        Set<Integer> set = node.childNode.keySet();
        for (Integer i : set) {
            //遍历孩子节点
            node.childNode.put(i, setChildNode(node, i));
        }
        return node;
    }

    /**
     * 设置根结点node的子节点信息
     * @param node              根结点
     * @param parentChoice      父节点们的选择
     * @return
     */
    public static Node setChildNode(Node node, int parentChoice) {
        //将变量保存并更新
        ArrayList<Integer> list = (ArrayList<Integer>) node.node.clone();
        ArrayList<Integer> listAimNumber = (ArrayList<Integer>) node.parentChoice.clone();
        listAimNumber.add(parentChoice);
        //判断是否有唯一情况
        double c = calculateH(list, listAimNumber);
        if (c == 0) {
            Node node1 = new Node();
            for (int i = 0; i < dataSet.length; i++) {
                boolean flag = true;
                for (int j = 0; j < list.size(); j++) {
                    if (dataSet[i][list.get(j)] == listAimNumber.get(j)) {
                        ;
                    } else {
                        flag = false;
                    }
                }
                if (flag) {
                    if (dataSet[i][dataSet[0].length - 1] == 0) {
                        node1.title = "否";
                    } else {
                        node1.title = "能";
                    }
                }
            }
            return node1;
        }
        //大于等于最大数组长度时 既然有不唯一的情况，那么就是不确定的状况
        else if (list.size() >= dataSet[0].length) {
            Node res = new Node();
            res.node = list;
            res.parentChoice = listAimNumber;
            res.title = "不确定";
            return res;
        }
        //一般情况
        else {
            int maxRow = 0;
            double maxRowData = 0.0;
            for (int i = 0; i < dataSetTitle.length - 1; i++) {
                //抛弃list中已存在的
                if (list.contains(i)) {
                    continue;
                }
                //计算该列的IG值 并挑出来最大的
                else {
                    double tempData = calculateIG(i, list, listAimNumber);
                    if (tempData > maxRowData) {
                        maxRow = i;
                        maxRowData = tempData;
                    }
                }
            }
            //递归构造子节点
            Node resNode = new Node(maxRow, list, listAimNumber);
            Set<Integer> set = resNode.childNode.keySet();
            for (Integer i : set) {
                resNode.childNode.put(i, setChildNode(resNode, i));
            }
            return resNode;
        }
    }

    public static void getResult(HashMap<Integer,Integer> hashMap , Node node){
        while(node.childNode != null){
            int index = node.node.get(node.node.size()-1);
            node = node.childNode.get(hashMap.get(index));
        }
        output(node);
        switch (node.title){
            case "能":
                System.out.println("哟！真厉害呢！");break;
            case "否":
                System.out.println("哈哈哈！就知道！");break;
            default:
                System.out.println("不确定？那就是不能了呗！hhh");break;
        }
    }


    public static void main(String[] args) {
        System.out.println("来测一测你能不能找到对象！");
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入你的名字：");
        String name = scanner.next();
        System.out.println("你好，"+name+"！在下面输入你的个人基本情况！");
        HashMap<Integer , Integer> hashMap = new HashMap<>();

        System.out.print("相貌：（0 丑   1 一般   2 好看）");hashMap.put(0, scanner.nextInt());
        System.out.print("身高：（0 矮   1 正常   2 高）");hashMap.put(1,scanner.nextInt());
        System.out.print("体型：（0 胖   1 匀称   2 瘦）");hashMap.put(2,scanner.nextInt());
        System.out.print("性格：（0 暴躁  1  平静）");hashMap.put(3,scanner.nextInt());

        long startTime = System.currentTimeMillis();
        Node node = new Node();
        node = initChosenTree(node);
        getResult(hashMap,node);
        long endTime = System.currentTimeMillis();
        System.out.println("耗时:"+(endTime-startTime)+"ms");

    }
}
