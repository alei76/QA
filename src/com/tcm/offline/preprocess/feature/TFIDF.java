package com.tcm.offline.preprocess.feature;

import com.tcm.util.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by azurexsyl on 2016/1/6.
 */
public class TFIDF {

    public static void main(String[] args) throws Exception{

        int docNum = 464;
        // 读取向量
        BufferedReader bufferedReader = FileIO.getBufferedReader(".\\resources\\dataset\\tcm_qa\\classifier2\\vec\\wc.txt");

        // 读取标注
        BufferedReader bufferedReader2 = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier2/data/qa.txt");

        BufferedWriter bufferedWriter = FileIO.getBufferedWriter("./resources/dataset/tcm_qa/classifier2/arff/tfidf.arff");

        // 存储一篇文档的词汇总数
        Map<Integer, Integer> docCount = new HashMap<>();
        for(int i = 1; i <= docNum; ++i) {
            docCount.put(i, 0);
        }

        // 存储包含某个词的文档
        Map<Integer, Integer> wordCount = new HashMap<>();
        for(int i = 0; i < 3052; ++i) {
            wordCount.put(i, 0);
        }

        String line = "";
        Integer docc = 0;
        while ((line = bufferedReader.readLine()) != null) {
            String[] nums = line.split(",");
            docc++;
            for(int i = 0 ; i < 3052; ++i) {
                String snum = nums[i];
                if(!snum.equals("0")) {
                    Integer wc = Integer.parseInt(snum);
                    docCount.put(docc, docCount.get(docc) + wc);
                    wordCount.put(i, wordCount.get(i) + i);
                }
            }
        }


        bufferedWriter.write("@relation c1" + "\n");
        for (int i = 1; i <= 3052; ++i) {
            bufferedWriter.write("@attribute " + "A" + i + " numeric" + "\n");
        }
        bufferedWriter.write("@attribute class {药, 方, 病}" + "\n");
        bufferedWriter.write("@data" + "\n");

        bufferedReader.close();
        bufferedReader = FileIO.getBufferedReader(".\\resources\\dataset\\tcm_qa\\classifier2\\vec\\wc.txt");

        docc = 0;
        while ((line = bufferedReader.readLine()) != null) {
            docc++;
            String result = "";
            String[] nums = line.split(",");
            for(int i = 0; i < 3052; ++i) {
                String snum = nums[i];
                if(!snum.equals("0")) {
                    Integer wc = Integer.parseInt(snum);
                    Double tf = (wc * 1.0 )/ docCount.get(docc);
                    Double idf = Math.log(800.0 / (wordCount.get(i) + 1));
                    result +=  "" + tf * idf + ",";
                }else {
                    result += "0,";
                }
            }

            bufferedReader2.readLine();
            line = bufferedReader2.readLine();
            if (line.startsWith("药"))
                bufferedWriter.write(result + "药" + "\n");
            else if (line.startsWith("方"))
                bufferedWriter.write(result + "方" + "\n");
            else if(line.startsWith("病")) {
                bufferedWriter.write(result + "病" + "\n");
            }
        }

        bufferedWriter.close();

    }
}
