package com.tcm.offline.preprocess;

import com.tcm.util.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 构建词向量
 * Created by azurexsyl on 2015/12/23.
 */
public class WordDictionary {


    public static void main(String[] args) throws Exception {

        // 创建输入流对象
        BufferedReader bufferedReader = FileIO.getBufferedReader(".\\resources\\dataset\\tcm_qa\\seg_question_4w.txt");
        // 创建输出流对象
        BufferedWriter bufferedWriter = FileIO.getBufferedWriter(".\\resources\\dataset\\tcm_qa\\dic\\words_4w_10.txt");
        //BufferedWriter bufferedWriter_seg = FileIO.getBufferedWriter(".\\resources\\data\\question_class\\seg_question.txt");

        Map<String, Integer> map = new HashMap<>();

        String line = "";
        int count = 0;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(++count);
            /*Question question = new Question();
            question.setContent(line);
            FNLPTools.process(question);
            String[] segs = question.getSeg();
            String sent = segs[0];
            for(int i = 1; i < segs.length; ++i) {
                sent += " " + segs[i];
            }
            bufferedWriter_seg.write(sent + "\n");*/
            String[] segs = line.split(" ");
            for(String seg : segs) {
                if(map.get(seg) == null) {
                    map.put(seg, 1);
                } else {
                    map.put(seg, map.get(seg) + 1);
                }
            }
        }

        System.out.println("共有 " + map.keySet().size() + " 个词");

        for(String seg : map.keySet()) {
            if(map.get(seg) >= 10)
                bufferedWriter.write(seg + "\n");
        }
        bufferedReader.close();
        bufferedWriter.close();
    }

}
