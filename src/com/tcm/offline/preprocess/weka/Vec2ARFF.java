package com.tcm.offline.preprocess.weka;

import com.tcm.util.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by azurexsyl on 2016/1/6.
 * 把doc2vec转化成arff格式
 */
public class Vec2ARFF {

    public static void main(String[] args) throws Exception {

        // 读取原始向量
        BufferedReader bufferedReader = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier2/vec/d2v.txt");
        BufferedReader bufferedReader2 = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier2/data/qa.txt");

        BufferedWriter bufferedWriter = FileIO.getBufferedWriter("./resources/dataset/tcm_qa/classifier2/arff/vec.arff");

        bufferedWriter.write("@relation c1" + "\n");
        for(int i = 1; i <= 200; ++i) {
            bufferedWriter.write("@attribute " + "A" + i + " numeric" + "\n");
        }
        bufferedWriter.write("@attribute class {药,方,病}" + "\n");
        bufferedWriter.write("@data" + "\n");

        String line = "";
        String sline = "";
        int count = 0;
        while((line = bufferedReader.readLine()) != null) {
            if(count++ == 464)
                break;
            int index = line.indexOf(" *** ");
            String subs = line.substring(index + 5);
            bufferedReader2.readLine();
            line = bufferedReader2.readLine();
            if(line.startsWith("药"))
                bufferedWriter.write(subs + ",药" + "\n");
            else if(line.startsWith("方"))
                bufferedWriter.write(subs +  ",方" + "\n");
            else if(line.startsWith("病")) {
                bufferedWriter.write(subs + ",病" + "\n");

            }
        }

        bufferedWriter.close();

        System.out.println("FINISHED");
    }
}
