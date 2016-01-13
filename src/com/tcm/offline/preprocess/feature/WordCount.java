package com.tcm.offline.preprocess.feature;

import com.tcm.util.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by azurexsyl on 2016/1/6.
 * 可以直接输出为weka格式
 */
public class WordCount {

    public static void main(String[] args) throws Exception{

        String classifier = "2";

        int d = 4;
        // 读取词典
        BufferedReader dicReader = FileIO.getBufferedReader(".\\resources\\dataset\\tcm_qa\\dic\\words_4w_10.txt");

        // 创建输入流对象, 分词后的对象
        BufferedReader bufferedReader = FileIO.getBufferedReader(".\\resources\\dataset\\tcm_qa\\classifier" + classifier +"\\data\\seg_qa_hhh_sp_bow_w1.txt");
        // 创建输出流对象, 词数向量
        BufferedWriter bufferedWriter = FileIO.getBufferedWriter(".\\resources\\dataset\\tcm_qa\\classifier" + classifier +"\\vec\\wc_hhh_sp_bow_w1.txt");

        Map<String, Integer> map = new HashMap<>();

        String line = "";

        int count = 0;

        while ((line = dicReader.readLine()) != null) {
            map.put(line, count++);
        }
        map.put("medmedmed", count++);
        map.put("preprepre", count++);
        map.put("disdisdis", count++);
        map.put("nonnonnon", count++);

        while((line = bufferedReader.readLine()) != null) {
            Integer[] vec = new Integer[map.keySet().size()];
            for(int i = 0; i < vec.length; ++i) {
                vec[i] = 0;
            }
            String[] words = line.split(" ");
            for(String word : words) {
                if(map.get(word) != null) {
                    int i = vec[map.get(word)];
                    if(word.equals("medmedmed") || word.equals("preprepre") || word.equals("disdisdis") || word.equals("nonnonnon")) {
                        vec[map.get(word)] = i + 10;
                    }else {
                        vec[map.get(word)] = i + 1;
                    }
                }
            }

            String result =  ""+ vec[0];
            for(int i = 1; i < vec.length; ++i) {
                result += "," + vec[i];
            }
            bufferedWriter.write(result + "\n");
        }
        bufferedWriter.close();

    }
}
