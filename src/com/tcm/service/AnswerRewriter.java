package com.tcm.service;

import com.tcm.po.Answer;
import com.tcm.po.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azurexsyl on 2015/12/22.
 */
public class AnswerRewriter {

    public static void deleteEmptyAnswer(Question question) {
        List<Answer> answers = question.getAnswers();
        List<Answer> newAnswers = new ArrayList<>();
        for(Answer answer : answers) {
            if(answer.getOris().size() != 0) {
                newAnswers.add(answer);
            }
        }
        question.setAnswers(newAnswers);
    }

    public static void generateHTML(Question question) {
        String result = "";
        List<Answer> answers = question.getAnswers();
        if(answers.size() == 0) {
            question.setResult("暂无答案！！！<br/>");
            return;
        }
        for(Answer answer : answers) {
            result += answer.getDescription() + "<br/>";
            for(String s : answer.getOris()) {
                String ss[] = s.split(" XXX ");
                for(String sss : ss) {
                    result += sss;
                }
                result += "<br/>";
            }
            result += "<br/>";
        }
        question.setResult(result);
    }
}
