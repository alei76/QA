package com.tcm.po;

import java.util.*;

/**
 * Created by azurexsyl on 2015/12/13.
 */
public class Question {

    // 原始问题
    private String content;

    // 返回问题形式
    private String result;

    // 问题领域
    private String questionDomain;
    // 回答类型
    private String answerType;
    // 查询类型
    private String queryType;

    public List<String> getQuery() {
        return query;
    }

    public void setQuery(List<String> query) {
        this.query = query;
    }

    public List<String> getQueryDes() {
        return queryDes;
    }

    public void setQueryDes(List<String> queryDes) {
        this.queryDes = queryDes;
    }

    private List<String> query;
    private List<String> queryDes;

    // 分词长度
    private int len;

    // 分词结果
    private String[] seg;
    // 词性标注结果
    private String[] pos;
    // 语法树结果
    private String[] dep;

    // 回答集合
    private List<Answer> answers;

    // 特征集合
    private Map<String, List<WordIndexPair>> features;

    // 语法依存数组
    private int[] head;

    // 问题分类集合
    private Set<String> domainSet;
    private Set<String> propertySet;
    // 实体集
    private Integer entityCount;
    private Map<String, List<String>> entityURI;

    // 属性集合
    private Integer propertyCount;
    private Map<String, List<String>> propertyURI;

    // 限制集合
    private List<ConstraintPair> constraints;

    public Question() {
        entityCount = 0;
        propertyCount = 0;
        questionDomain = "none";
        features = new HashMap<>();
        domainSet = new HashSet<>();
        propertySet = new HashSet<>();
        entityURI = new HashMap<>();
        propertyURI = new HashMap<>();
        answers = new ArrayList<>();
        constraints = new ArrayList<>();
    }

    public int[] getHead() {
        return head;
    }

    public void setHead(int[] head) {
        this.head = head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestionDomain() {
        return questionDomain;
    }

    public void setQuestionDomain(String questionDomain) {
        this.questionDomain = questionDomain;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String[] getSeg() {
        return seg;
    }

    public void setSeg(String[] seg) {
        this.seg = seg;
    }

    public String[] getPos() {
        return pos;
    }

    public void setPos(String[] pos) {
        this.pos = pos;
    }

    public String[] getDep() {
        return dep;
    }

    public void setDep(String[] dep) {
        this.dep = dep;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public void putDomain(String domain) {
        domainSet.add(domain);
    }

    public Set<String> getDomainSet() {
        return domainSet;
    }

    public Set<String> getPropertySet() {
        return propertySet;
    }

    public void putPropertySet(String property) {
        propertySet.add(property);
    }

    public int domainSetSize() {
        return domainSet.size();
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public Integer getEntityCount() {
        return entityCount;
    }

    public void setEntityCount(Integer entityCount) {
        this.entityCount = entityCount;
    }

    public Integer getPropertyCount() {
        return propertyCount;
    }

    public void setPropertyCount(Integer propertyCount) {
        this.propertyCount = propertyCount;
    }

    public List<WordIndexPair> getFeatures(String featureKey) {
        return features.get(featureKey);
    }

    public void putFeatures(String featureKey, List<WordIndexPair> featureValue) {
        this.features.put(featureKey, featureValue);
    }

    public void putEntityURI(String domain, String uri) {
        if(entityURI.get(domain) == null) {
            List<String> list = new ArrayList<>();
            list.add(uri);
            entityURI.put(domain, list);
        } else {
            entityURI.get(domain).add(uri);
        }
    }

    public List<String> getEntityURI(String domain) {
        return entityURI.get(domain);
    }

    public void putPropertyURI(String domain, String uri) {
        if(propertyURI.get(domain) == null) {
            List<String> list = new ArrayList<>();
            list.add(uri);
            propertyURI.put(domain, list);
        }else {
            propertyURI.get(domain).add(uri);
        }
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void putAnswers(Answer answer) {
        answers.add(answer);
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<ConstraintPair> getConstraints() {return constraints;};

    public void putConstraints(ConstraintPair constraintPair) {constraints.add(constraintPair);};

    public List<String> getPropertyURI(String domain) {return propertyURI.get(domain);};

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public void show() {
        // 问题
        System.out.println("content: " + content);
        // 分词结果
        System.out.print("seg: ");
        for(String s: seg) {
            System.out.print(s + " ");
        }
        System.out.println();
        // 词性标注结果
        System.out.print("pos: ");
        for(String s: pos) {
            System.out.print(s + " ");
        }
        System.out.println();
        // 语法树结构
        System.out.print("dep: ");
        for(String s: dep) {
            System.out.print(s + " ");
        }
        System.out.println();
        // 语法树层次
        System.out.print("head: ");
        for(int s: head) {
            System.out.print(s + " ");
        }
        System.out.println();
        // 疑问词集合
        System.out.print("WH Feature: ");
        for(WordIndexPair wordIndexPair : features.get("WH")) {
            System.out.print(wordIndexPair.getWord() + " : " + wordIndexPair.getIndex() + "  ");
        }
        System.out.println();
        // 核心词集合
        System.out.print("Key Feature: ");
        for(WordIndexPair wordIndexPair : features.get("KEY")) {
            System.out.print(wordIndexPair.getWord() + " : " + wordIndexPair.getIndex() + "  ");
        }
        System.out.println();
        // 主谓集合
        System.out.print("HED Feature: ");
        for(WordIndexPair wordIndexPair : features.get("HED")) {
            System.out.print(wordIndexPair.getWord() + " : " + wordIndexPair.getIndex() + "  ");
        }
        System.out.println();
        // 单复集合
        System.out.print("SP Feature: ");
        for(WordIndexPair wordIndexPair : features.get("SP")) {
            System.out.print(wordIndexPair.getWord() + " : " + wordIndexPair.getIndex() + "  ");
        }
        System.out.println();
        // 分类集合
        System.out.println("DomainSet: ");
        for(String s : domainSet) {
            System.out.print(s + " ");
        }
        System.out.println();
        // 分类结果
        System.out.print("Domain: ");
        System.out.println(questionDomain);
        // 实体数量
        System.out.print("ENTITY COUNT: ");
        System.out.println(entityCount);
        // 属性数量
        System.out.print("PROPERTY COUNT: ");
        System.out.println(propertyCount);
        // 限制对
        System.out.print("Constraints: ");
        System.out.println(constraints.size());
        for(ConstraintPair constraintPair : constraints) {
            System.out.println("P: " + constraintPair.getProperty());
            System.out.println("V: " + constraintPair.getValue());
            for(String u : constraintPair.getUris()) {
                System.out.print(u + " ");
            }
            System.out.println();
        }
    }
}
