package com.tcm.util;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by azurexsyl on 2016/1/11.
 * Weka工具集
 */
public class WekaTools {

    private static List<Attribute> c2_atts = new ArrayList<>();
    private static List<String> c2_classes = new ArrayList<>();
    private static Instances instances = null;

    static {
        try {
            File file = new File(Const.RESOURCE_BASE_DIR + "/dataset/model/wc_sp_w2_instances.arff");
            ArffLoader loader = new ArffLoader();
            loader.setFile(file);
            instances = loader.getDataSet();
            instances.setClassIndex(instances.numAttributes() - 1);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 0-药；1-方；2-病
     * @param instance
     * @return
     * @throws Exception
     */
    public static int classifyDomain(Instance instance) throws Exception{
        instance.setDataset(instances);
        Classifier classifier = (Classifier) weka.core.SerializationHelper.read(Const.RESOURCE_BASE_DIR + "dataset/model/bow_sp_svm.model");
        double class_value = classifier.classifyInstance(instance);
        int predict_result = (int) class_value;
        return predict_result;
    }

    /***
     * 方剂分类：0-treat；2-function；3-attention
     * @param instance
     * @return
     * @throws Exception
     */
    public static int classifyPreProperty(Instance instance) throws Exception {
        Classifier classifier = (Classifier) weka.core.SerializationHelper.read(Const.RESOURCE_BASE_DIR + "dataset/model/wc_gxzz.model");
        double class_value = classifier.classifyInstance(instance);
        int predict_result = (int) class_value;
        return predict_result;
    }
}
