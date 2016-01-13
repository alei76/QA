package com.tcm.offline.preprocess;

import com.tcm.po.Question;
import com.tcm.service.FNLPTools;

/**
 * Created by azurexsyl on 2015/12/27.
 */
public class NLPTest {

    public static void main(String[] args) {
        Question question = new Question();
        question.setContent("麻黄 的 功效 有 哪些 ？");
        FNLPTools.process(question);
        question.show();
    }
}
