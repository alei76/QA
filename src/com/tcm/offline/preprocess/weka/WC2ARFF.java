package com.tcm.offline.preprocess.weka;

import com.tcm.util.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by azurexsyl on 2016/1/6.
 */
public class WC2ARFF {

    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier3/vec/sp.txt");
        BufferedReader bufferedReader2 = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier3/data/seg_qa_gxzz2.txt");
        BufferedWriter bufferedWriter = FileIO.getBufferedWriter("./resources/dataset/tcm_qa/classifier3/arff/sp.arff");

        bufferedWriter.write("@relation c1" + "\n");
        for (int i = 1; i <= 3052; ++i) {
            bufferedWriter.write("@attribute " + "A" + i + " numeric" + "\n");
        }
        bufferedWriter.write("@attribute class {treat, function, attention}" + "\n");
        bufferedWriter.write("@data" + "\n");

        String line = "";
        String sline = "";
        int count = 0;
        while ((sline = bufferedReader.readLine()) != null) {
            bufferedReader2.readLine();
            line = bufferedReader2.readLine();
            System.out.println(++count);
            if (line.startsWith("治疗"))
                bufferedWriter.write(sline + ",treat" + "\n");
            else if (line.startsWith("功效"))
                bufferedWriter.write(sline + ",function" + "\n");
            else if(line.startsWith("副作用"))
                bufferedWriter.write(sline + ",attention" + "\n");
        }

        bufferedWriter.close();

        System.out.println("FINISHED");
    }
}
