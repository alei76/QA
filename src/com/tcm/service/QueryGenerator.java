package com.tcm.service;

import com.tcm.po.Answer;
import com.tcm.po.Question;
import com.tcm.po.WordIndexPair;
import com.tcm.util.SpecificDic;
import com.tcm.util.SwitchTools;
import com.tcm.util.TemplateMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azurexsyl on 2015/12/16.
 * 问句生成策略
 */
public class QueryGenerator {

    /***
     * 输入Question，根据识别到的实体数量进行分类
     * 不同实体数目采取的策略不同
     * @param question
     */
    public static void gerenateQuery(Question question) {
        if(question.getEntityCount() >= 2 ) {
            question.setAnswerType("多实体");
            multiEntityGenerate(question);
        } else if(question.getEntityCount() == 1) {
            question.setAnswerType("单实体");
            singleEntityGenerate(question);
        } else {
            question.setAnswerType("无实体");
            noneEntityGenerate(question);
        }
        // 如果没有生成答案，则要依靠机器学习生成
        if(question.getAnswers().size() == 0) {
            machineLearningGenerate(question);
        }
    }


    /***
     * 问题中存在多个实体，例如【麻黄、防风的功效是什么|单领域】【什么药可以治疗咳嗽与感冒|跨领域】
     * @param question
     */
    private static void multiEntityGenerate(Question question) {

        /*** 获取实体列表，由于同名异实体的情况存在，优先生成同领域实体列表 ***/
        List<String> entityURIList = new ArrayList<>();
        Integer entityCount = question.getEntityCount();
        // 从domain遍历，保证一个domain完整
        for(String domain : question.getDomainSet()) {
            // 遍历一个domain的URI
            for(int i = 1; i <= entityCount; ++i) {
                List<String> tmp = question.getEntityURI(domain + i);
                if(tmp == null) {
                    break;
                } else {
                    // 此处默认第一个
                    entityURIList.add(tmp.get(0));
                }
            }
            if(entityURIList.size() == entityCount) {
                break;
            } else {
                entityURIList.clear();
            }
        }
        // 从词序遍历，保证词完整，在计算获得的URI个数不足时实施
        if(entityURIList.size() != entityCount) {
            for(int i = 1; i <= entityCount; ++i) {
                for(String domain : question.getDomainSet()) {
                    List<String> tmp = question.getEntityURI(domain + i);
                    if(tmp != null) {
                        entityURIList.add(tmp.get(0));
                        break;
                    }
                }
            }
        }
        // 模式匹配
        System.out.println("尝试【模式匹配】");
        question.setQueryType("多实体-模式匹配");
        Answer answer = TemplateMatcher.multiMatch(entityURIList, question);
        if(answer != null) {
            question.getAnswers().add(answer);
            return;
        }
        // 领域知识
        System.out.println("尝试【领域知识-关系】");
        question.setQueryType("多实体-领域知识");
        if(!question.getQuestionDomain().equals("multi")) {
            //若非multi，则说明类型统一，是关系的可能性很大
            generateSRXorXRXSmartQuery(entityURIList, question);
        }
        System.out.println("尝试【领域知识-属性】");
        // 若为空，表示没有自动生成，并且不再考虑关系
        if(question.getAnswers().size() == 0) {
            Integer propertyCount = question.getPropertyCount();
            for(int i = 1; i <= entityCount; ++i) {
                for (int j = 1; j <= propertyCount; ++j) {
                    List<String> pros = question.getPropertyURI("property" + j);
                    if(pros == null)
                        continue;
                    for(String pro : pros) {
                        Answer sanswer = new Answer();
                        sanswer.setDescription(question.getFeatures("ENTITY").get(i - 1).getWord() + "的"  + question.getFeatures("PROPERTY").get(j - 1).getWord() + "如下：");
                        sanswer.setQuery(generateSPXSmartQuery(entityURIList.get(i - 1) , pro, question));
                        sanswer.setParam(new String[]{"x"});
                        question.getAnswers().add(sanswer);
                    }
                }
            }
        }
     }

    /***
     * 只存在一个实体
     * @param question
     */
    private static void singleEntityGenerate(Question question) {

        // 优先采取模板匹配,计算所有的实体情况
        List<String> entityURIList = new ArrayList<>();
        for(String entity : question.getDomainSet()) {
            for(String uri : question.getEntityURI(entity + "1")) {
                entityURIList.add(uri);
            }
        }

        // 模式匹配
        System.out.println("尝试【模式匹配】");
        question.setQueryType("模式匹配");
        TemplateMatcher.singleMatch(entityURIList, question);
        // 有结果则退出
        if(question.getAnswers().size() != 0)
            return;

        System.out.println("尝试【领域知识-关系】");
        question.setQueryType("领域知识");
        // 尝试使用关系直接生成
        if(!question.getQuestionDomain().equals("multi")) {
            for (String entity : entityURIList) {
                List<String> e = new ArrayList<>();
                e.add(entity);
                generateSRXorXRXSmartQuery(e, question);
            }
        }
        System.out.println("尝试【领域知识-属性】");
        if(question.getAnswers().size() == 0) {
            Integer propertyCount = question.getPropertyCount();
            for(int i = 1; i <= entityURIList.size(); ++i) {
                for(int j = 1; j <= propertyCount; ++j) {
                    List<String> pros = question.getPropertyURI("property" + j);
                    if(pros == null)
                        continue;
                    for(String pro : pros) {
                        Answer sanswer = new Answer();
                        sanswer.setDescription(question.getFeatures("ENTITY").get(0).getWord() + "的" + question.getFeatures("PROPERTY").get(j - 1).getWord() + "如下：");
                        sanswer.setQuery(generateSPXSmartQuery(entityURIList.get(i - 1), pro, question));
                        sanswer.setParam(new String[]{"x"});
                        question.getAnswers().add(sanswer);
                    }
                }
            }
        }
    }

    /***
     * 没有实体存在，这种情况下都尝试调用分类器
     * @param question
     */
    private static void noneEntityGenerate(Question question) {
        if(question.getQuestionDomain().equals("none")) {

        } else {

        }
    }

    /***
     * 以机器学习方式生成问句
     * @param question
     */
    private static void machineLearningGenerate(Question question) {
        // 领域分类
        question.setQueryType("机器学习");
        ClassifierSets.domainClassifierML(question);
        if(question.getQuestionDomain().equals("pre")) {
            System.out.println("尝试【机器学习-方剂】");
            ClassifierSets.prePropertyClassifierML(question);
            if(question.getAnswerType().equals("主治")) {
                // 治疗首先获得治疗的描述
                List<String> treatDes = new ArrayList<>();
                for(String treat : question.getSeg()) {
                    if(SpecificDic.isZZ(treat)) {
                        treatDes.add(treat);
                    }
                }
                System.out.println("症状描述词" + treatDes.size() + "个");
                // 生成第一个查询SPARQL，方剂主治{描述}
                Answer answer = new Answer();
                answer.setDescription("以下方剂主治上述症状");
                String query = "SELECT ?x WHERE {?y <http://zcy.ckcest.cn/tcm/pre/property#pre_basic.name> ?x. ?y <http://zcy.ckcest.cn/tcm/pre/property#pre_function.treat> ?z.";
                for(String t : treatDes) {
                    query += " FILTER regex(?z, '" + t + "' ).";
                }
                query += "}";
                answer.setQuery(query);
                answer.setParam(new String[]{"x"});
                question.getAnswers().add(answer);
                // 生成第二个查询SPARQL，方剂治疗疾病{症状}
                answer = new Answer();
                answer.setDescription("以下方剂治疗上述症状");
                query = "SELECT ?x WHERE {?y <http://zcy.ckcest.cn/tcm/pre/property#pre_basic.name> ?x. ?y <http://zcy.ckcest.cn/tcm/relation#treats> ?z. ?z <http://zcy.ckcest.cn/tcm/dis/tcm/property#dis_diagnosis.symptom> ?t.";
                for(String t : treatDes) {
                    query += " FILTER regex(?t, '" + t + "' ).";
                }
                query += "}";
                answer.setQuery(query);
                answer.setParam(new String[]{"x"});
                question.getAnswers().add(answer);
            } else if(question.getAnswerType().equals("功效")) {
                // 功效的问题，许多都是XXX的功效，已经先前基于领域知识的方法过滤
            } else if(question.getAnswerType().equals("副作用")) {
                // 查找所有方剂实体
                List<String> preURIList = new ArrayList<>();
                Integer entityCount = question.getEntityCount();
                String domain = "pre";
                for(int i = 1; i <= entityCount; ++i) {
                    List<String> tmp = question.getEntityURI(domain + i);
                    if(tmp == null) {
                        break;
                    } else {
                        preURIList.add(tmp.get(0));
                    }
                }
                int count = preURIList.size();
                String[] params = new String[count];
                Answer answer = new Answer();
                answer.setDescription("方剂的禁忌");
                String query = "SELECT ";
                for(int i = 0; i < count; ++i) {
                    query += "?x" + i + " ";
                    params[i] = "x" + i;
                }
                query += "WHERE { ";
                for(int i = 0; i < count; ++i) {
                    query += addbreackets(preURIList.get(i)) + " <http://zcy.ckcest.cn/tcm/pre/property#pre_function.attention> ?x" + i + ". ";
                }
                query += "}";
                answer.setQuery(query);
                answer.setParam(params);
                question.getAnswers().add(answer);
            }
        }
    }

    private static String generateSPXSmartQuery(String entity, String property, Question question) {
        if(SwitchTools.isRelation(property)) {
            if(SwitchTools.getURIDomain(entity).equals("med") && SwitchTools.getURIDomain(property).equals("relation")) {
                return "SELECT ?x WHERE { " + addbreackets(entity) + addbreackets(property) + "?y. { { ?y <http://zcy.ckcest.cn/tcm/dis/tcm/property#dis_tcm_basic.name_zh> ?x } UNION { ?y <http://zcy.ckcest.cn/tcm/dis/wm/property#dis_wm_basic.name_zh> ?x } } }";
            }else {
                return "";
            }
        }else {
            return "SELECT ?x WHERE { " + addbreackets(entity) + addbreackets(property) + "?x.}";
        }
    }

    // TODO: 2015/12/20 理论上有18组

    /***
     * 按照领域知识生成
     * @param entities
     * @param question
     */
    private static void generateSRXorXRXSmartQuery(List<String>  entities, Question question) {
        // 获得关键词列表
        String des = "";
        List<WordIndexPair> list = question.getFeatures("ENTITY");
        for(WordIndexPair wordIndexPair : list) {
            des += " " + wordIndexPair.getWord();
        }
        if(question.getQuestionDomain().equals("pre") && question.getDomainSet().contains("dis")) {
            //  【方剂】治疗疾病
            Answer answer = new Answer();
            answer.setDescription("治疗疾病" + des + " 的方剂有：");
            answer.setQuery(treatsXRO(entities, "http://zcy.ckcest.cn/tcm/pre/property#pre_basic.name"));
            answer.setParam(new String[]{"x"});
            question.getAnswers().add(answer);
        } else if(question.getQuestionDomain().equals("dis") && question.getDomainSet().contains("pre")) {
            //  【疾病】被方剂治疗
            Answer answer = new Answer();
            answer.setDescription("方剂" + des + " 能治疗的中医疾病有：");
            answer.setQuery(treatsSRX(entities, "http://zcy.ckcest.cn/tcm/dis/tcm/property#dis_tcm_basic.name_zh"));
            answer.setParam(new String[]{"x"});
            question.getAnswers().add(answer);
            answer = new Answer();
            answer.setDescription("方剂" + des + " 能治疗的西医疾病有：");
            answer.setQuery(treatsSRX(entities, "http://zcy.ckcest.cn/tcm/dis/wm/property#dis_wm_basic.name_zh"));
            answer.setParam(new String[]{"x"});
            question.getAnswers().add(answer);
        } else if(question.getQuestionDomain().equals("pre") && question.getDomainSet().contains("med")) {
            // 【方剂】包含单味药
            Answer answer = new Answer();
            answer.setDescription("包含单味药" + des + " 的方剂有：");
            answer.setQuery(containsXRO(entities, "http://zcy.ckcest.cn/tcm/pre/property#pre_basic.name"));
            answer.setParam(new String[]{"x"});
            question.getAnswers().add(answer);
        } else if(question.getQuestionDomain().equals("med") && question.getDomainSet().contains("pre")) {
            // 【单味药】被方剂含有
            Answer answer = new Answer();
            answer.setDescription("方剂" + des + " 包含的单味药有：");
            answer.setQuery(containsSRX(entities, "http://zcy.ckcest.cn/tcm/med/property#med_basic.med_name_zh"));
            answer.setParam(new String[]{"x"});
            question.getAnswers().add(answer);
        } else if(question.getQuestionDomain().equals("chem") && question.getDomainSet().contains("med")) {
            // 【单味药】包含化合物
            Answer answer = new Answer();
            answer.setDescription("单味药" + des + " 包含的化合物有：");
            answer.setQuery(containsSRX(entities, "http://zcy.ckcest.cn/tcm/chem/property#chem_cname.cname"));
            answer.setParam(new String[]{"x"});
            question.getAnswers().add(answer);
        }
    }


    /***
     * 知道疾病求方剂
     * @param entites
     * @param name_property
     * @return
     */
    private static String treatsXRO(List<String> entites, String name_property) {
        String query = "SELECT ?x WHERE { ";
        for(String entity : entites) {
            query += "?y <http://zcy.ckcest.cn/tcm/relation#treats> " + addbreackets(entity) + ".";
        }
        query += "?y <" + name_property + "> ?x. }";
        return query;
    }

    /***
     * 知道方剂求疾病
     * @param entites
     * @param name_property
     * @return
     */
    private static String treatsSRX(List<String> entites, String name_property) {
        String query = "SELECT ?x WHERE { ";
        for(String entity : entites) {
            query += addbreackets(entity) +"<http://zcy.ckcest.cn/tcm/relation#treats> ?y.";
        }
        query += "?y <" + name_property + "> ?x. }";
        return query;
    }

    private static String containsXRO(List<String> entites, String name_property) {
        String query = "SELECT ?x WHERE { ";
        for(String entity : entites) {
            query += "?y <http://zcy.ckcest.cn/tcm/relation#contains> " + addbreackets(entity) + ".";
        }
        query += "?y <" + name_property + "> ?x. }";
        return query;
    }

    private static String containsSRX(List<String> entites, String name_property) {
        String query = "SELECT ?x WHERE { ";
        for(String entity : entites) {
            query += addbreackets(entity) +"<http://zcy.ckcest.cn/tcm/relation#contains> ?y.";
        }
        query += "?y <" + name_property + "> ?x. }";
        return query;
    }

    private static String addbreackets(String src) {
        return " <" + src + "> ";
    }
}
