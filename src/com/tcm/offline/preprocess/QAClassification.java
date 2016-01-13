package com.tcm.offline.preprocess;

import com.tcm.po.Question;
import com.tcm.service.ClassifierSets;
import com.tcm.service.FeatureExtractor;
import com.tcm.util.FileIO;
import com.tcm.service.FNLPTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Set;

/**
 * Created by azurexsyl on 2015/12/15.
 */
public class QAClassification {

    public static void main(String[] args) throws Exception {

        // 创建输入流对象
        BufferedReader bufferedReader = FileIO.getBufferedReader(".\\resources\\data\\refined_question.txt");

        // 创建输出流对象
        BufferedWriter bufferedWriter_med = FileIO.getBufferedWriter(".\\resources\\data\\question_class\\med_qa.txt");
        BufferedWriter bufferedWriter_pre = FileIO.getBufferedWriter(".\\resources\\data\\question_class\\pre_qa.txt");
        BufferedWriter bufferedWriter_dis = FileIO.getBufferedWriter(".\\resources\\data\\question_class\\dis_qa.txt");
        BufferedWriter bufferedWriter_syn = FileIO.getBufferedWriter(".\\resources\\data\\question_class\\syn_qa.txt");
        BufferedWriter bufferedWriter_chem = FileIO.getBufferedWriter(".\\resources\\data\\question_class\\chem_qa.txt");
        BufferedWriter bufferedWriter_ori = FileIO.getBufferedWriter(".\\resources\\data\\question_class\\ori_qa.txt");
        BufferedWriter bufferedWriter_multi = FileIO.getBufferedWriter(".\\resources\\data\\question_class\\multi_qa.txt");
        BufferedWriter bufferedWriter_none = FileIO.getBufferedWriter(".\\resources\\data\\question_class\\none_qa.txt");

        Set<String> dic;

        String line = "";
        int count = 0;
        while((line = bufferedReader.readLine()) != null) {
            System.out.println(++count);
            Question question = new Question();
            question.setContent(line);
            FNLPTools.process(question);
            FeatureExtractor.featureExtractionQAClassification(question);
            ClassifierSets.domainClassifier(question);

            switch (question.getQuestionDomain()) {
                case "med":
                    bufferedWriter_med.write(line + "\n");
                    break;
                case "pre":
                    bufferedWriter_pre.write(line + "\n");
                    break;
                case "dis":
                    bufferedWriter_dis.write(line + "\n");
                    break;
                case "syn":
                    bufferedWriter_syn.write(line + "\n");
                    break;
                case "chem":
                    bufferedWriter_chem.write(line + "\n");
                    break;
                case "ori":
                    bufferedWriter_ori.write(line + "\n");
                    break;
                case "multi":
                    bufferedWriter_multi.write(line + "\n");
                    break;
                case "none":
                    bufferedWriter_none.write(line + "\n");
                    break;
                default:
            }
        }

    }
}
