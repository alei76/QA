package com.tcm.service;

import com.tcm.po.Question;
import com.tcm.po.WordIndexPair;
import com.tcm.util.SpecificDic;

/**
 * Created by azurexsyl on 2015/12/14.
 */
public class ClassifierSets {

    /***
     * 实体领域分类器，需要命名疑问词抽取和命名实体识别以及启发式规则
     * @param question
     */
    public static void domainClassifier(Question question) {

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


    public static void questionValid(Question question) {

    }


    /***
     * 回答类型分类器，需要各种词、词性、词法特征
     * @param question
     */
    public static void answerTypeClassifier(Question question) {

    }

    /***
     * 查询类型分类器
     * @param question
     */
    public static void queryGeneratorClassifier(Question question) {

        if(question.getEntityCount() >= 2) {
            // 实体数目大于2个时候的处理方式
            question.setQueryType("complex");
        } else if(question.getEntityCount() == 1){
            // 实体数目为1个时候的处理方式
            // 根据分类进行模板匹配，若匹配成功
            question.setQueryType("template");
            // 若匹配失败，则查看属性情况，根据不同的属性做不同的处理，若再失败，则尝试使用挖掘的分类器
            question.setQueryType("simple");
        } else {
            question.setQueryType("none");
        }
    }

}
