package com.tcm.controller;

import com.jfinal.core.Controller;
import com.tcm.po.Question;
import com.tcm.service.*;
import com.tcm.util.Const;

/**
 * Created by azurexsyl on 2015/6/28.
 */
public class HomeController extends Controller{

    /***
     * 返回主页
     */
    public void index() {
        System.out.println(Const.RESOURCE_BASE_DIR);
        render("index.html");
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
        // 采用启发式规则对问题进行分类
        ClassifierSets.domainClassifierHeuristic(question);
        question.show();
        // 生成问题查询的SPARQL
        QueryGenerator.gerenateQuery(question);
        // 过滤无效问题
        QueryFilter.filter(question);
        // 运行问题查询语句
        QueryExecutor.execute(question);
        // 删除空问题
        AnswerFilter.deleteEmptyAnswer(question);
        // 生成HTML答案
        AnswerRewriter.generateHTML(question);
        renderJson("result", question.getResult());
    }
}
