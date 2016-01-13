package com.tcm.util;

import com.tcm.po.Answer;
import com.tcm.po.Question;
import com.tcm.po.Template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 特征模板匹配器
 * Created by azurexsyl on 2015/12/16.
 */
public class TemplateMatcher {

    private static Map<Integer, List<Template>> templates;


    /***
     * 初始化
     */
    static {

        templates = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("./resources/templates/templates.txt"));
            String line = "";
            while ((line = br.readLine()) != null) {
                Integer paramCount = Integer.valueOf(line);
                String[] entity = br.readLine().split(" ");
                String[] style = br.readLine().split(" ");
                String[] param = br.readLine().split(" ");
                String query = br.readLine();
                String description = br.readLine();
                Template template = new Template(paramCount, entity, style, param, query, description);
                if(templates.get(paramCount) == null) {
                    List<Template> list = new ArrayList<>();
                    list.add(template);
                    templates.put(paramCount, list);
                }else {
                    templates.get(paramCount).add(template);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 有一组url
     * @param entities
     * @param question
     * @return
     */
    public static Answer multiMatch(List<String> entities, Question question) {

        // 获取对应参数个数的模板集
        List<Template> templateList = templates.get(entities.size());

        if(templateList == null)
            return null;

        for(Template template : templateList) {
            Answer answer = template.matchTemplate(question, entities);
            if(answer != null) {
                return answer;
            }
        }

        return null;
    }

    /***
     * 多个url皆需生成
     * @param entities
     * @param question
     */
    public static void singleMatch(List<String> entities, Question question) {

        List<Template> templateList = templates.get(question.getEntityCount());

        if(templateList == null)
            return;
        for(Template template : templateList) {
            for(String entity : entities) {
                List<String> ientities = new ArrayList<>();
                ientities.add(entity);
                Answer answer = template.matchTemplate(question, ientities);
                if(answer != null) {
                    System.out.println(answer.getQuery());
                    answer.setDescription("单实体-自定义模板-答案顺序对应实体消歧");
                    question.getAnswers().add(answer);
                }
            }
        }

    }

}
