package com.tcm.offline.preprocess.weka;

import com.tcm.util.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by azurexsyl on 2016/1/6.
 */
public class WC2ARFF {

    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier2/vec/wc_hhh_sp_bow_w1.txt");
        BufferedReader bufferedReader2 = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier2/data/qa.txt");

        BufferedWriter bufferedWriter = FileIO.getBufferedWriter("./resources/dataset/tcm_qa/classifier2/arff/wc_hhh_sp_bow_w1.arff");

        bufferedWriter.write("@relation c1" + "\n");
        for (int i = 1; i <= 3052 + 4; ++i) {
            bufferedWriter.write("@attribute " + "A" + i + " numeric" + "\n");
        }
        bufferedWriter.write("@attribute class {药, 方, 病}" + "\n");
        bufferedWriter.write("@data" + "\n");

        String line = "";
        String sline = "";
        int count = 0;
        while ((sline = bufferedReader.readLine()) != null) {
            bufferedReader2.readLine();
            line = bufferedReader2.readLine();
            if (line.startsWith("药"))
                bufferedWriter.write(sline + ",药" + "\n");
            else if (line.startsWith("方"))
                bufferedWriter.write(sline + ",方" + "\n");
            else if(line.startsWith("病"))
                bufferedWriter.write(sline + ",病" + "\n");
        }

        bufferedWriter.close();

        System.out.println("FINISHED");
    }
}
