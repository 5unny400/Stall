import java.io.*;
public class Test {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO Auto-generated method stub
        try {
            BufferedReader br = new BufferedReader(new FileReader("1.txt")); //reader类操作
            String str = "";
            //行列计数初始化
            int rank = 0;int coulumn = 0;

            while((str=br.readLine())!=null){
                System.out.println(str);
                rank++;     //行计数
                String [] arryStr = str.split("\\s+");//表示一个或多个空格截取
                //建立对应的double数组
                Double [] result = new Double[8];
                coulumn=arryStr.length;
                //String转换为Double型
                for(int i=0;i<8;i++){
                    if(!arryStr[i].trim().equals("")){
                        result[i]=Double.parseDouble(arryStr[i]);
                    }
                }
                //显示double数据
                for(int i=0;i<8;i++){
                    System.out.println("s"+i+":"+result[i]);
                }

            }
            System.out.println("一共有"+rank+"行"+"；每行有"+coulumn+"列");
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}