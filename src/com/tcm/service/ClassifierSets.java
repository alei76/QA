package com.tcm.service;

import com.tcm.po.Question;
import com.tcm.po.WordIndexPair;
import com.tcm.util.SpecificDic;
import com.tcm.util.WekaTools;
import weka.core.Instance;

/**
 * Created by azurexsyl on 2015/12/14.
 */
public class ClassifierSets {

    /***
     * 实体领域分类器，需要命名疑问词抽取和命名实体识别以及启发式规则
     * @param question
     */
    public static void domainClassifierHeuristic(Question question) {

        // 首先查找{疑问词+后位置}的强特征
        for(WordIndexPair pair : question.getFeatures("WH")) {
            Integer index = pair.getIndex();
            if(((index + 1) < question.getLen()) && !SpecificDic.getDomainType(question.getSeg()[index + 1]).equals("none")) {
                String domain = SpecificDic.getDomainType(question.getSeg()[index + 1]);
                // 设置领域值
                question.setQuestionDomain(domain);
                return;
            }

            if(((index + 2) < question.getLen()) && question.getPos()[index + 1].equals("量词") && !SpecificDic.getDomainType(question.getSeg()[index + 2]).equals("none")) {
                String domain = SpecificDic.getDomainType(question.getSeg()[index + 2]);
                // 设置领域值
                question.setQuestionDomain(domain);
                return;
            }
        }

        // 再次查找助词[后置位]的强特征
        for(int i = 0; i < question.getLen(); ++i) {
            if(question.getPos().equals("助词结构")) {
                if((i + 1) < question.getLen() && !SpecificDic.getDomainType(question.getSeg()[i + 1]).equals("none")) {
                    String domain = SpecificDic.getDomainType(question.getSeg()[i + 1]);
                    question.setQuestionDomain(domain);
                    return;
                }
            }
        }

        // 若不存在强特征，则进行弱特征的匹配，且只有标注为实体的需要弱特征匹配
        if(question.getDomainSet().size() == 0) {
            question.setQuestionDomain("none");
        } else if (question.getDomainSet().size() >= 2) {
                question.setQuestionDomain("multi");
        } else {
            for(String domain : question.getDomainSet()) {
                question.setQuestionDomain(domain);
                return;
            }
        }
    }

    /***
     * 领域分类，要计算BoW+SP特征
     * @param question
     */
    public static  void domainClassifierML(Question question) {
        double[] vec = new double[SpecificDic.getCorpusDicLength()];
        for(int i = 0; i < vec.length; ++i)
            vec[i] = 0.0;
        String[] words = question.getSeg();
        for(String word : words) {
            if(SpecificDic.getWordNum(word) != -1) {
                vec[SpecificDic.getWordNum(word)] += 1;
                if(SpecificDic.specific(word)) {
                    vec[SpecificDic.getWordNum(word)] += 2;
                }
            }
        }

        Instance instance = new Instance(1, vec);
        try {
            int result = WekaTools.classifyDomain(instance);
            if(result == 0) {
                // 药
                question.setQuestionDomain("med");
            } else if(result == 1) {
                // 方
                question.setQuestionDomain("pre");
            } else if(result == 2) {
                // 病
                question.setQuestionDomain("dis");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
