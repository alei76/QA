package com.tcm.offline.preprocess.weka;

import com.tcm.po.Question;
import com.tcm.service.ClassifierSets;
import com.tcm.service.FNLPTools;
import com.tcm.service.FeatureExtractor;
import com.tcm.util.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by azurexsyl on 2016/1/10.
 */
public class Trivial2ARFF {

    public static void main(String[] args) throws Exception{

        // 读取原始向量
        BufferedReader bufferedReader = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier2/data/qa.txt");
        BufferedWriter bufferedWriter = FileIO.getBufferedWriter("./resources/dataset/tcm_qa/classifier2/arff/hhh.arff");

        bufferedWriter.write("@relation c1" + "\n");
        for(int i = 1; i <= 4; ++i) {
            bufferedWriter.write("@attribute " + "A" + i + " numeric" + "\n");
        }
        bufferedWriter.write("@attribute class {药,方,病}" + "\n");
        bufferedWriter.write("@data" + "\n");

        String line = "";
        while((line = bufferedReader.readLine()) != null) {
            //System.out.println(line);
            Question question = new Question();
            question.setContent(line);
            FNLPTools.process(question);
            FeatureExtractor.featureExtractionQAClassification(question);
            ClassifierSets.domainClassifier(question);
            line = bufferedReader.readLine();
            //System.out.println(line);
            String key = line.substring(0, 1);
            if(question.getQuestionDomain().equals("med")) {
                bufferedWriter.write("1,0,0,0," + key + "\n");
            } else if(question.getQuestionDomain().equals("pre")) {
                bufferedWriter.write("0,1,0,0," + key +"\n");
            } else if(question.getQuestionDomain().equals("dis")) {
                bufferedWriter.write("0,0,1,0," + key + "\n");
            } else {
                bufferedWriter.write("0,0,0,1," + key + "\n");
            }
        }
        bufferedWriter.close();
    }
}
