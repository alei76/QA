package com.tcm.offline.preprocess.classifier3;

import com.tcm.po.Question;
import com.tcm.service.ClassifierSets;
import com.tcm.service.FNLPTools;
import com.tcm.service.FeatureExtractor;
import com.tcm.util.FileIO;
import com.tcm.util.SpecificDic;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by azurexsyl on 2016/1/14.
 */
public class SegQA {
    public static void main(String[] args) throws Exception{

        int weight = 1;

        BufferedReader bufferedReader = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier3/data/qa.txt");
        BufferedWriter bufferedWriter = FileIO.getBufferedWriter("./resources/dataset/tcm_qa/classifier3/data/seg_qa_spsp.txt");

        String line = "";
        while((line = bufferedReader.readLine()) != null ) {
            Question question = new Question();
            question.setContent(line);
            FNLPTools.process(question);
            FeatureExtractor.featureExtraction(question);
            ClassifierSets.domainClassifierHeuristic(question);
            String[] segments = question.getSeg();
            String sent = "";/* + segments[0];*/
            if (SpecificDic.isZZ(segments[0]) || SpecificDic.isGX(segments[0])) {
                for (int j = 0; j < weight; ++j) {
                    sent += " " + segments[0];
                }
            }

            for (int i = 1; i < segments.length; ++i) {
                /*sent += " " + segments[i];*/
                if (SpecificDic.isZZ(segments[i]) || SpecificDic.isGX(segments[i])) {
                    for (int j = 0; j < weight; ++j) {
                        sent += " " + segments[i];
                    }
                }
            }

            String type = bufferedReader.readLine();
            if(type.equals("用法") || type.equals("其他")) {

            } else {
                bufferedWriter.write(sent + "\n");
                bufferedWriter.write(type + "\n");
            }
        }

        bufferedWriter.close();
    }
}
