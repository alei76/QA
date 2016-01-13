package com.tcm.service;

import com.tcm.po.Answer;
import com.tcm.po.Question;
import com.tcm.po.WordIndexPair;
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
        System.out.println("START!");
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

        }
    }


    /***
     * 问题中存在多个实体，例如【麻黄、防风的功效是什么|单领域】【什么药可以治疗咳嗽与干嘛|跨领域】
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
        question.setQueryType("多实体-模式匹配");
        Answer answer = TemplateMatcher.multiMatch(entityURIList, question);
        if(answer != null) {
            question.getAnswers().add(answer);
            return;
        }

        // 领域知识
        question.setQueryType("多实体-领域知识");
        // 优先生成relation关系，不符合判断则不会产生
        generateSRXorXRXSmartQuery(entityURIList, question);
        // 若为空，表示没有自动生成
        if(question.getAnswers().size() == 0) {

        }

     }
        /*if(question.getQuestionDomain().equals("multi")) {
            // 判断为multi只是因为出现了多个领域的实体，如【麻黄当归防风的区别是什么】，但是其中有一个实体可以归为其他类型
            // 优先进行模板匹配
            question.setQueryType("多实体-模式匹配");
            Answer answer = TemplateMatcher.multiMatch(entityURIList, question);
            if(answer != null) {
                question.getAnswers().add(answer);
                return;
            }

            // 否则要采取一般情况下的匹配，进行属性的组合,回答如【麻黄防风的功效是什么】【什么药可以咳嗽和感冒】
            // 优先进行跨领域查询，即relation
            question.setQueryType("多实体-领域知识");
            Integer propertyCount = question.getPropertyCount();
            for(int i = 1; i <= entityCount; ++i) {
                for (int j = 1; j <= propertyCount; ++j) {
                    if (question.getPropertySet().contains("relation")) {
                        // relation 最高优先权
                        List<String> pros = question.getPropertyURI("relation" + j);
                        if(pros == null)
                            continue;
                        for(String pro : pros) {
                            Answer sanswer = new Answer();
                            sanswer.setDescription("<" + question.getFeatures("ENTITY").get(i - 1).getWord() + ">" + " <R:" + question.getFeatures("PROPERTY").get(j - 1).getWord() + ">");
                            sanswer.setQuery(generateSRXSmartQuery(entityURIList.get(i - 1) , pro, question));
                            sanswer.setParam(new String[]{"x"});
                            question.getAnswers().add(sanswer);
                        }
                    } else {
                        // 其他关系具有其次优先级，即property优先级
                        List<String> pros = question.getPropertyURI("property" + j);
                        if(pros == null)
                            continue;
                        for(String pro : pros) {
                            if(!SwitchTools.isSameDomain(entityURIList.get(i - 1), pro)) {
                                continue;
                            }
                            Answer sanswer = new Answer();
                            // TODO: 2015/12/18 可以深化，如在 Description 内添加编号，如返回结果内增加超链接，反正有唯一键关联
                            // TODO: 2015/12/20 其实也可以通过判断实体类别加关系解决，但是数据不是对应唯一实体
                            sanswer.setDescription(question.getFeatures("ENTITY").get(i - 1).getWord() + "的"  + question.getFeatures("PROPERTY").get(j - 1).getWord() + "如下：");

                            sanswer.setQuery(generateSPXSmartQuery(entityURIList.get(i - 1) , pro, question));
                            sanswer.setParam(new String[]{"x"});
                            question.getAnswers().add(sanswer);
                        }
                    }
                }
            }

            // TODO: 2015/12/20 若连属性都没有，需要另行处理，即添加规则
        } else {
            // 若是【多实体】且标签不为multi，有三种种问题的形式：1.类型相同的单一实体【麻黄三七的属性】放到上边 ;2.类型不同的单一实体【什么药可以治疗咳嗽与感冒|什么方剂包含半夏和大黄|麻黄和防风含有那些化合物】;3.类型不同且无实体【什么药性味为寒】放到下边
            // 若分到此类，则默认认为是求二者的关系，无需谓词判断
            question.setQueryType("multi-single-auto");
            generateSRXorXRXSmartQuery(entityURIList, question);

        }*/

    private static void singleEntityGenerate(Question question) {

        // 优先采取模板匹配,计算所有的实体情况
        List<String> entityURIList = new ArrayList<>();
        for(String entity : question.getDomainSet()) {
            for(String uri : question.getEntityURI(entity + "1")) {
                entityURIList.add(uri);
            }
        }

        //【典型问题-为什么 吃了 麻黄 不舒服】
        TemplateMatcher.singleMatch(entityURIList, question);

        // 有结果则退出
        if(question.getAnswers().size() != 0)
            return;

        // 【典型问题-什么药可以治疗咳嗽】
        if(!question.getQuestionDomain().equals("multi") && !question.getDomainSet().contains(question.getQuestionDomain())) {
            // 什么药可以治疗咳嗽|没有constraint，可以归一到智能生成问题】
            for(String entity : entityURIList) {
                List<String> e = new ArrayList<>();
                e.add(entity);
                question.setQueryType("multi-single-auto");
                generateSRXorXRXSmartQuery(e, question);
            }
            return;
        }


        question.setQueryType("multi-single-auto");
        if(question.getPropertyCount() > 0) {
            // 单实体有两种形式【治疗咳嗽的药是什么|没有constraint，可以归一到智能生成问题】【产地北京的治疗咳嗽的药是什么|一个constraint，一个实体】[治疗麻黄的药还可以治疗什么|难！】
            // 【麻黄的功效是什么|这类问题一般没有限制】
            // 智能问句生成，添加限制
            Integer count = question.getPropertyCount();
            for(int ii = 1; ii <= entityURIList.size(); ++ii) {
                for(int i = 1; i <= count; ++i) {
                    List<String> pros = question.getPropertyURI("property" + i);
                    if(pros != null) {
                        for(String pro : pros) {
                            Answer sanswer = new Answer();
                            //sanswer.setDescription("<" + entity + ">" + " <P:" + question.getFeatures("PROPERTY").get(i - 1).getWord() + ">");
                            sanswer.setDescription(question.getFeatures("ENTITY").get(0).getWord() + "的" + question.getFeatures("PROPERTY").get(i - 1).getWord() + "如下：");

                            sanswer.setQuery(generateSPXSmartQuery(entityURIList.get(ii - 1), pro, question));
                            sanswer.setParam(new String[]{"x"});
                            question.getAnswers().add(sanswer);
                        }
                    }
                }
            }

        } else {
            // 无属性，需要按照机器学习的方法分类,【并且添加限制？】

        }
    }

    private static void noneEntityGenerate(Question question) {
        // 无实体，且domain为none
        if(question.getQuestionDomain().equals("none")) {
            //不能回答，或者信息太少
        } else {
            // 需要提取限制对
            // 类似【背部出汗怎么治疗|单味药】【产地北京的药|单味药】
        }
    }


    // TODO: 2015/12/20 需要判断很复杂，此处只是西医
    private static String generateSRXSmartQuery(String entity, String property, Question question) {
        return "SELECT ?x WHERE { "  + addbreackets(entity) + addbreackets(property) + "?y. ?y <http://zcy.ckcest.cn/tcm/dis/tcm/property#dis_tcm_basic.name_zh> ?x.}";
    }

    private static String generateSPXSmartQuery(String entity, String property, Question question) {
        return "SELECT ?x WHERE { "  + addbreackets(entity) + addbreackets(property) + "?x.}";
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
        } if(question.getQuestionDomain().equals("chem") && question.getDomainSet().contains("med")) {
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
