package com.tcm.service;

import com.tcm.po.Answer;
import com.tcm.po.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azurexsyl on 2016/1/11.
 * 过滤
 */
public class AnswerFilter {
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
}
