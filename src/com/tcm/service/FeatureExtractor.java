package com.tcm.service;

import com.tcm.po.ConstraintPair;
import com.tcm.po.Question;
import com.tcm.po.WordIndexPair;
import com.tcm.util.JedisTools;
import com.tcm.util.SpecificDic;
import com.tcm.util.SwitchTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by azurexsyl on 2015/12/14.
 * 特征提取器
 */
public class FeatureExtractor {

    /***
     * 特征抽取
     * @param question
     */
    public static void featureExtraction(Question question) {
        extractWH(question);
        extractEntityAndProperty(question);
        extractKey(question);
        extractHed(question);
        extractContraint(question);
        extractSP(question);
    }

    /**
     * 疑问词特征的提取
     * @param question
     * @return
     */
    private static void extractWH(Question question) {
        List<WordIndexPair> list = new ArrayList<WordIndexPair>();
        String[] seg = question.getSeg();
        String[] pos = question.getPos();
        for(int i = 0; i < seg.length; ++i) {
            if(SpecificDic.containsWHDic(seg[i]) && !pos[i].equals("形容词") && !pos[i].equals("副词")) {
                list.add(new WordIndexPair(seg[i], i));
            }
        }
        question.putFeatures("WH", list);
    }

    /**
     * 核心词(疑问词一、二级依存词和问句的中心语)特征的提取
     * @param question
     * @return
     */
    private static void extractKey(Question question) {
        List<WordIndexPair> list = new ArrayList<>();
        List<WordIndexPair> directWordsList = new ArrayList<>();
        List<WordIndexPair> indirectWordsList = new ArrayList<>();
        String[] seg = question.getSeg();
        int[] head = question.getHead();
        // 获取一级疑问词特征
        List<WordIndexPair> whFeature = question.getFeatures("WH");
        for(WordIndexPair wordIndexPair : whFeature) {
            Integer root = wordIndexPair.getIndex();
            if(head[root] != 0) {
                directWordsList.add(new WordIndexPair(seg[head[root]], head[root]));
            }
        }
        // 获取二级疑问词特征
        for(WordIndexPair wordIndexPair : directWordsList) {
            Integer root = wordIndexPair.getIndex();
            if(head[root] != 0) {
                indirectWordsList.add(new WordIndexPair(seg[head[root]], head[root]));
            }
        }
        // 获取中心词
        for(int i = 0; i < question.getLen(); ++i) {
            if(head[i] == 0) {
                list.add(new WordIndexPair(seg[i], i));
                break;
            }
        }
        // 排除无用词并整合核心词
        for(WordIndexPair wordIndexPair : directWordsList) {
            if(!SpecificDic.containsTrivialDic(wordIndexPair.getWord()))
                list.add(wordIndexPair);
        }
        for(WordIndexPair wordIndexPair : indirectWordsList) {
            if(!SpecificDic.containsTrivialDic(wordIndexPair.getWord()))
                list.add(wordIndexPair);
        }
        question.putFeatures("KEY", list);
    }


    /**
     * 实体与属性特征的提取
     * @param question
     */
    private static void extractEntityAndProperty(Question question) {
        Integer entityCount = 0;
        Integer propertyCount = 0;
        List<WordIndexPair> entityList = new ArrayList<>();
        List<WordIndexPair> propertyList = new ArrayList<WordIndexPair>();
        String[] seg = question.getSeg();
        for(int i = 0; i < seg.length; ++i) {

            // 获取实体-URI对照
            Set<String> uris = JedisTools.getURI(seg[i]);

            // 若不存在URI，则继续
            if(uris.size() == 0)
                continue;

            // 获取类型
            String type = "entity";
            for(String uri : uris) {
                String domain = SwitchTools.getURIDomain(uri);
                if(domain.equals("property") || domain.equals("relation")) {
                    type = "property";
                    break;
                }
            }

            System.out.println(type + ":" + seg[i]);
            if(type.equals("entity")) {
                entityCount++;
                for(String uri : uris) {
                    System.out.println(uri);
                    String domain = SwitchTools.getURIDomain(uri);
                    question.putDomain(domain);
                    question.putEntityURI(domain + entityCount, uri);
                }
                entityList.add(new WordIndexPair(seg[i], i));

            } else {
                propertyCount++;
                for(String uri : uris) {
                    System.out.println(uri);
                    String domain = SwitchTools.getURIDomain(uri);
                    question.putPropertySet(domain);
                    question.putPropertyURI(domain + propertyCount, uri);
                }
                propertyList.add(new WordIndexPair(seg[i], i));
            }
        }
        question.setEntityCount(entityCount);
        question.setPropertyCount(propertyCount);
        question.putFeatures("ENTITY", entityList);
        question.putFeatures("PROPERTY", propertyList);
    }


    /**
     * 主谓特征提取，即核心词
     * @param question
     */
    private static void extractHed(Question question) {
        List<WordIndexPair> list = new ArrayList<>();
        String[] seg = question.getSeg();
        String[] dep = question.getDep();
        int[] head = question.getHead();
        int index = 0;
        for(int i = 0; i < question.getLen(); ++i) {
            if(head[i] == index) {
                index = head[i];
                break;
            }
        }
        for(int i = 0; i < question.getLen(); ++i) {
            if(head[i] == index) {
                list.add(new WordIndexPair(seg[i], i));
            }
        }
        question.putFeatures("HED", list);
    }


    /***
     * 限制特征提取
     * @param question
     */
    private static void extractContraint(Question question) {
        // 限制特征只与Property相关
        List<WordIndexPair> propertyList = question.getFeatures("PROPERTY");
        for(int i = 1; i < propertyList.size(); ++i){
            // 获取属性关键词
            WordIndexPair property = propertyList.get(i - 1);
            String word = property.getWord();
            // 如{产地+北京}
            Integer ind = property.getIndex() + 1;
            if(ind < question.getLen()) {
                if(question.getPos()[ind].equals("名词") || question.getPos()[ind].equals("地名") || question.getPos()[ind].equals("副词") || question.getPos()[ind].equals("形容词")) {
                    ConstraintPair constraintPair = new ConstraintPair();
                    constraintPair.setProperty(word);
                    constraintPair.setValue(question.getSeg()[ind]);
                    if(question.getPropertyURI("property" + i) != null) {
                        constraintPair.setUris(question.getPropertyURI("property" + i));
                        question.putConstraints(constraintPair);
                        continue;
                    }
                }
            }
            // 如{产地+为+北京}
            ind += 1;
            if(ind < question.getLen()) {
                if(question.getPos()[ind - 1].equals("介词") && (question.getPos()[ind].equals("名词") || question.getPos()[ind].equals("地名") || question.getPos()[ind].equals("副词") || question.getPos()[ind].equals("形容词"))) {
                    ConstraintPair constraintPair = new ConstraintPair();
                    constraintPair.setProperty(word);
                    constraintPair.setValue(question.getSeg()[ind]);
                    if(question.getPropertyURI("property" + i) != null) {
                        constraintPair.setUris(question.getPropertyURI("property" + i));
                        question.putConstraints(constraintPair);
                        continue;
                    }
                }
            }
        }
    }

    /***
     * 抽取单复数特征
     * 此特征实际上并未使用
     * @param question
     */
    private static void extractSP(Question question) {
        List<WordIndexPair> list = new ArrayList<>();
        String[] seg = question.getSeg();
        String[] pos = question.getPos();
        for(int i = 0; i < question.getLen(); ++i) {
            if(seg[i].equals("一")  && pos[i].equals("数词")) {
                list.add(new WordIndexPair("单", -1));
                question.putFeatures("SP", list);
                return;
            }
        }
        list.add(new WordIndexPair("复", -1));
        question.putFeatures("SP", list);
    }
}
