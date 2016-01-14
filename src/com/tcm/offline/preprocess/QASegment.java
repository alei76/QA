package com.tcm.offline.preprocess;

import com.tcm.po.Question;
import com.tcm.service.ClassifierSets;
import com.tcm.service.FNLPTools;
import com.tcm.service.FeatureExtractor;
import com.tcm.util.FileIO;
import com.tcm.util.SpecificDic;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by azurexsyl on 2016/1/4.
 *
 * 对问句进行分词
 */
public class QASegment {

    public static void main(String[] args) throws Exception{

        int weight = 1;

        BufferedReader bufferedReader = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier2/data/qa.txt");

        BufferedWriter bufferedWriter = FileIO.getBufferedWriter("./resources/dataset/tcm_qa/classifier2/data/seg_qa_hhh_sp_bow_w"+ weight +".txt");

        String line = "";
        int count = 0;
        while((line = bufferedReader.readLine()) != null) {
            System.out.println(++count);
            Question question = new Question();
            question.setContent(line);
            FNLPTools.process(question);
            FeatureExtractor.featureExtraction(question);
            ClassifierSets.domainClassifierHeuristic(question);
            String[] segments = question.getSeg();

            String sent = "" + segments[0];
            if(SpecificDic.specific(segments[0])) {
                sent += " " + segments[0];
            }

            for(int i = 1; i < segments.length; ++i) {
                sent += " " + segments[0];
                if(SpecificDic.specific(segments[0])) {
                    for (int j = 0; j < weight; ++j) {
                        sent += " " + segments[i];
                    }
                }
            }
            /*List<WordIndexPair> list = question.getFeatures("WH");
            for(WordIndexPair wordIndexPair : list) {
                for(int i = 0; i < weight; i++) {
                    sent += " " + wordIndexPair.getWord();
                }
            }*/

            if(question.getQuestionDomain().equals("med")) {
                sent += " " + "medmedmed";
            } else if(question.getQuestionDomain().equals("pre")) {
                sent += " " + "preprepre";
            } else if(question.getQuestionDomain().equals("dis")) {
                sent += " " + "disdisdis";
            } else {
                sent += " " + "nonnonnon";
            }
            bufferedReader.readLine();
            bufferedWriter.write(sent + "\n");
        }

        bufferedReader.close();
        bufferedWriter.close();
    }
}
