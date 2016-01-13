package com.tcm.offline.preprocess.classifier2;

import com.tcm.util.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by azurexsyl on 2016/1/5.
 * 语料排序，为了与选取的语料对应
 */
public class CorpusResort {

    public static void main(String[] args) throws Exception {

        // 原始语料
        BufferedReader bufferedReader1 = FileIO.getBufferedReader("./resources/dataset/tcm_qa/refined_question.txt");

        // 标注语料
        BufferedReader bufferedReader2 = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier2/data/qa.txt");

        // 对齐语料
        BufferedWriter bufferedWriter = FileIO.getBufferedWriter("./resources/dataset/tcm_qa/classifier2/data/corpus.txt");


        Set<String> tag = new HashSet<>();
        List<String> res = new ArrayList<>();
        List<String> other = new ArrayList<>();

        String line = "";
        while((line = bufferedReader2.readLine()) != null) {
            tag.add(line);
            res.add(line);
            bufferedReader2.readLine();
        }

        System.out.println(res.size());

        while((line = bufferedReader1.readLine()) != null) {
            if(tag.contains(line))
                continue;
            else
                other.add(line);
        }

        for(String s : res) {
            bufferedWriter.write(s + "\n");
        }

        for(String s : other) {
            bufferedWriter.write(s + "\n");
        }

        bufferedWriter.close();

    }
}
