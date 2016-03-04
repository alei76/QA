package com.tcm.service;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

/**
 * Created by azurexsyl on 2015/12/29.
 * HanLP 调用
 */
public class HNLPTools {

    /***
     * 分词模块
     * @param sent 问题文本
     * @return 分词并拼接后的文本
     */
    public static String segment(String sent) {
        List<Term> termList = HanLP.segment(sent);
        String result = "";
        for(Term term : termList) {
            result += term.word + " ";
        }
        return result;
    }
}
