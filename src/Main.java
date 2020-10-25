import java.io.*;
import java.util.Stack;

public class Main {
    private static int row = 10;
    private static int col = 20;
    private static int[] test = new int[19];
    private static int[][] result = new int[row][col];
    private static int label;
    private static double usage;
    private int[][][] arrayMap = {
            {{0, 0}, {1, 0}, {1, -1}, {0, -1}}, {{0, 0}, {1, 0}, {2, 0}, {3, 0}}, {{0, 0}, {0, -1}, {0, -2}, {0, -3}},
            {{0, 0}, {1, 0}, {2, 0}, {1, -1}}, {{0, 0}, {1, 0}, {1, 1}, {1, -1}},

            {{0, 0}, {1, 0}, {2, 0}, {1, 1}}, {{0, 0}, {0, -1}, {0, -2}, {1, -1}}, {{0, 0}, {0, -1}, {0, -2}, {1, -2}},
            {{0, 0}, {1, 0}, {2, 0}, {0, -1}}, {{0, 0}, {1, 0}, {1, -1}, {1, -2}},

            {{0, 0}, {1, 0}, {2, 0}, {2, 1}}, {{0, 0}, {1, 0}, {1, 1}, {1, 2}}, {{0, 0}, {0, -1}, {1, -1}, {2, -1}},
            {{0, 0}, {1, 0}, {0, -1}, {0, -2}}, {{0, 0}, {1, 0}, {2, 0}, {2, -1}},

            {{0, 0}, {1, 0}, {0, -1}, {0, -2}, {1, -2}}, {{0, 0}, {0, -1}, {1, 0}, {2, 0}, {2, -1}},
            {{0, 0}, {1, 0}, {1, -1}, {1, -2}, {0, -2}}, {{0, 0}, {0, -1}, {1, -1}, {2, -1}, {2, 0}}
    };

    public static void main(String[] args) {

        Main main = new Main();
        main.begin();
        System.out.println();
    }

    //初始化结果数组，第六行、第十三列置为零，表示为路；其余置为-1表示可以摆摊；
    private void initResult() {
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = 0;
            }
        }
    }

    //开始函数
    private void begin() {
        //读取数据到test数组
        readTest_case();
        System.out.println();
    }

    //读取数据到test[]数组，并调用给出计划函数plansSelect();
    private void readTest_case() {
        try {
            String fileName = "test_case.txt";
            BufferedReader br = new BufferedReader(new FileReader(fileName)); //reader类操作
            String str = "";
            //行列计数初始化
            int rank = 0;
            int coulumn = 0;
            while ((str = br.readLine()) != null) {
//                System.out.println(str);//输出读取的行
                rank++;     //行计数
                String[] arryStr = str.split("\\s+");//表示一个或多个空格截取
                //建立对应的double数组
                coulumn = arryStr.length;
                //String转换为Double型
                for (int i = 0; i < 19; i++) {
                    if (!arryStr[i].trim().equals("")) {
                        test[i] = Integer.parseInt(arryStr[i]);
                    }
                }
//                显示double数据
//                for(int i=0;i<19;i++){
//                    System.out.print("test["+i+"]:"+test[i]+" ");
//                }
//                System.out.println();
                plansSelect();
            }
            System.out.println("一共有" + rank + "行" + "；每行有" + coulumn + "列");
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //根据读取的数据，给出摆摊方案，比出利用率最高的一种，存在result数组中，然后追加入result.txt
    //调用了initResult();givePlan();WriteResult();
    private void plansSelect() throws IOException {
        //每次计算都给result 数组赋值了，所以每下一次需要初始化result数组
        //test数组每次都重新读取不需要初始化
        initResult();

        result = givePlan();//记录利用率最高的摆摊情况
        //输出
        System.out.println("本计划的利用率："+usage);
        usage = 0;

        //写入文件
        writeResult();
    }

    //递归给出路径,调用了calUtilizationRate();
    private int[][] givePlan() {
        Stack stack = new Stack<>();
        int[] indexTest = new int[19];  //记录每个形态是否入栈的数组，0表示未入栈
        int pIndexTest = 0;
        boolean sample = false;
        int[][] temp = new int[12][20];
//        changeIntoArray(indexTest);
        for (int i = 0; i < 19; i++) { //遍历五十种形态
            if (test[i] > 0 && indexTest[i] == 0) {       //当前形状没有入栈
                stack.push(arrayMap[i]);
                indexTest[i]++;       //表示已入栈一次
            }
            for (int q = 0; q < test.length; q++) {
                if (indexTest[q] < test[q]) {  //入栈个数小于该形状的个数
                    stack.push(arrayMap[q]);
                    indexTest[q]++;       //表示已入栈一次
                    break;
                }
                while (!stack.isEmpty()) {
                    int[][] tmp = (int[][]) stack.peek();   //入栈表示放置该形状
                    LL:
                    for (int n = 0; n < result[0].length; n++) {
                        for (int m = 0; m < result.length; m++) {
                            if (result[m][n] == 0 && m != 5 && n != 12) {//当前的地点不识是路
                                label++;
                                for (int k = 0; k < tmp.length; k++) {      //循环放置该摊位的占地面积
                                    if ((m + tmp[k][0]) == 5 || (n + tmp[k][1]) == 12) {//如果是路
                                        indexTest[i]--;//表示并未入栈
                                        label--;
                                        //还原面积
                                        for (int p = 0; p < k; p++) {
                                            result[m + tmp[p][0]][n + tmp[p][1]] = 0;
                                        }
                                        break LL;//不摆置该摊位
                                    }
                                    //不是路的话：
                                    //还要判断是否有摊位在
                                    if (result[m + tmp[k][0]][n + tmp[k][1]] == 0) {
                                        sample = true;
                                        result[m + tmp[k][0]][n + tmp[k][1]] = label;
                                    } else {
                                        sample = false;
                                        indexTest[i]--;//表示并未入栈
                                        label--;
                                        //还原面积
                                        for (int p = 0; p < k; p++) {
                                            result[m + tmp[p][0]][n + tmp[p][1]] = 0;
                                        }
                                        break LL;//不摆置该摊位
                                    }
                                }
                            }
                        }
                    }
                    stack.pop();
                }
            }
            label = 0;
            double next = calUtilizationRate();
            if(usage < next){
                usage = next;
                temp = result;
            }
        }
        return temp;
    }

    //计算操场面积利用率
    private double calUtilizationRate() {
        int label = 0;
        for (int[] ints : result) {
            for (int i = 0; i < ints.length; i++) {
                if (ints[i] == -1) {    //表示没有利用的操场面积
                    label++;
                }
            }
        }
        return (1 - (double) label / (row * col));
    }

    //把数据写入文件
    private void writeResult() throws IOException {
        File file = new File("result.txt");  //存放数组数据的文件
        FileWriter out = new FileWriter(file, true);  //文件写入流
        //将数组中的数据写入到文件中。每行各数据之间TAB间隔
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                out.write(result[i][j] + "  ");
            }
            out.write("\r\n");
        }
        out.write("\r\n");
        out.write("\r\n");
        out.close();
    }
}
