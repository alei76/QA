package com.tcm.service;

import com.tcm.po.Question;
import com.tcm.util.Const;
import org.fnlp.ml.types.Dictionary;
import org.fnlp.nlp.cn.tag.CWSTagger;
import org.fnlp.nlp.cn.tag.POSTagger;
import org.fnlp.nlp.parser.dep.DependencyTree;
import org.fnlp.nlp.parser.dep.JointParser;

/**
 * Created by azurexsyl on 2015/12/13.
 * 自然语言处理工具集，首先调用 HanLP ，之后调用 FNLP
 *
 */
public class FNLPTools {

    private static CWSTagger cwsTagger;
    private static POSTagger posTagger;
    private static JointParser jointParser;

    static {
        try {
            cwsTagger = new CWSTagger(Const.RESOURCE_BASE_DIR +"/models/seg.m", new Dictionary(Const.RESOURCE_BASE_DIR + "/dic/qa_dic.txt"));
            posTagger = new POSTagger(cwsTagger, Const.RESOURCE_BASE_DIR + "/models/pos.m", new Dictionary(Const.RESOURCE_BASE_DIR + "/dic/qa_dic.txt"), true);
            jointParser = new JointParser(Const.RESOURCE_BASE_DIR +"/models/dep.m");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 对问题进行自然语言工具处理
     * @param question 问题对象
     */
    public static void process(Question question) {

        // 在此处调用HNLP，获得分词后的结果
        String content = HNLPTools.segment(question.getContent());

        String[][] s1 = posTagger.tag2Array(content);
        DependencyTree s2 = jointParser.parse2T(s1[0], s1[1]);

        question.setSeg(s1[0]);
        question.setPos(s1[1]);
        question.setDep(s2.getTypes().split(" "));
        question.setHead(s2.toHeadsArray());
        question.setLen(s1[0].length);

    }

}
