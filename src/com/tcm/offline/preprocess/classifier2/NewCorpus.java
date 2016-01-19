package com.tcm.offline.preprocess.classifier2;

import com.tcm.util.FileIO;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by azurexsyl on 2016/1/6.
 * 构造分类器{?}的语料
 */
public class NewCorpus {

    public static void main(String[] args) throws Exception{

        BufferedReader bufferedReader = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier2/data/qa.txt");
        BufferedWriter bufferedWriter = FileIO.getBufferedWriter("./resources/dataset/tcm_qa/classifier3/data/qa.txt");

        String line = "";
        while((line = bufferedReader.readLine()) != null) {
            String content = line;
            line = bufferedReader.readLine();
            if(line.startsWith("方"))
                bufferedWriter.write(content + "\n" + line.substring(line.indexOf("方|") + 2) + "\n");
        }
        bufferedWriter.close();
    }
}
