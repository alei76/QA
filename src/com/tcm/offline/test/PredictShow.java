package com.tcm.offline.test;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;

/**
 * Created by azurexsyl on 2016/1/6.
 */
public class PredictShow {

    public static void main(String[] args) throws Exception{

        // 读取测试集
        File file = new File("./resources/dataset/tcm_qa/classifier1/tfidf.arff");
        ArffLoader loader = new ArffLoader();
        loader.setFile(file);

        Classifier classifier = (Classifier) weka.core.SerializationHelper
                .read("./resources/dataset/model/classify1_svm.model");

        Instances test = loader.getDataSet();
        test.setClassIndex(test.numAttributes() - 1);

        int num_class = test.numClasses();
        int num_instances = test.numInstances();
        for(int j = 0; j < num_instances; ++j) {
            Instance test_instance = test.instance(j);
            int real_label = (int) test_instance.classValue();
            double class_value = classifier.classifyInstance(test_instance);
            int predict_result = (int) class_value;
            if (predict_result != real_label && predict_result == 0) {
                System.out.println("ps:" + predict_result  + "rr:" + real_label);
                System.out.println(j + 1);
            }
        }
    }
}
