package com.tcm.controller;

import com.jfinal.core.Controller;
import com.tcm.po.Question;
import com.tcm.service.*;

/**
 * Created by azurexsyl on 2015/6/28.
 */
public class HomeController extends Controller{

    /***
     * 返回主页
     */
    public void index() {
        renderJsp("index.jsp");
    }

    /***
     * 返回问题答案
     */
    public void ask () {

        // 获取请求值
        String category = getPara("category");
        String content = getPara("question");
        // 初始化问题
        Question question = new Question();
        // 设置问题内容
        question.setContent(content);
        // 自然语言工具集处理
        FNLPTools.process(question);
        // 抽取特征
        FeatureExtractor.featureExtraction(question);
        // 领域分类
        ClassifierSets.domainClassifier(question);
        question.show();
        // 生成问题查询的SPARQL
        QueryGenerator.gerenateQuery(question);
        //QueryFilter
        // 运行问题查询语句
        QueryExecutor.execute(question);
        AnswerRewriter.deleteEmptyAnswer(question);
        AnswerRewriter.generateHTML(question);

        renderJson("result", question.getResult());
    }
}
