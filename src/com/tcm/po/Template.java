package com.tcm.po;

import java.util.List;

/**
 * 模板类
 * Created by azurexsyl on 2015/12/17.
 */
public class Template {

    private Integer paramCount;
    private String[] entity;
    private String[] style;
    private String[] param;
    private String query;

    public Template(Integer paramCount, String[] entity, String[] style, String[] param, String query) {
        this.paramCount = paramCount;
        this.entity = entity;
        this.style = style;
        this.param = param;
        this.query = query;
    }

    public Integer getPramCount() {
        return paramCount;
    }

    public void setPramCount(Integer pramCount) {
        this.paramCount = pramCount;
    }

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    public String[] getStyle() {
        return style;
    }

    public void setStyle(String[] style) {
        this.style = style;
    }

    public String[] getEntity() {
        return entity;
    }

    public void setEntity(String[] entity) {
        this.entity = entity;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Answer matchTemplate(Question question, List<String> entities) {

        Integer[] indexes = new Integer[style.length];
        int start = 0;
        for(int i = 0; i < style.length; ++i) {
            if(isEntity(style[i])) {
                // 必定可以找到
                indexes[i] = question.getFeatures("ENTITY").get(start).getIndex();
                start++;
            }else {
                // 可能找不到
                Integer index = getWordIndex(style[i], question);
                if(index == -1) {
                    return null;
                }
                else {
                    indexes[i] = index;
                }
            }
            // 每次循环完毕需要验证正确性
            if(i > 0 && (indexes[i] <= indexes[i - 1])) {
                return null;
            }
        }

        //若期间没有退出则表示匹配完毕，替换之
        String resQuery = query;
        for(int i = 0; i < entities.size(); ++i) {
            resQuery = resQuery.replace(entity[i], constructResource(entities.get(i)));
        }

        Answer answer = new Answer();
        answer.setParam(param);
        answer.setQuery(resQuery);
        return answer;
    }

    private boolean isEntity(String e) {
        for(String ee : entity) {
            if(e.equals(ee)) {
                return true;
            }
        }
        return false;
    }

    private Integer getWordIndex(String e, Question question) {
        String[] seg = question.getSeg();
        for(int i = 0; i < seg.length; ++i) {
            if(e.equals(seg[i]))
                return i;
        }
        return -1;
    }

    private String constructResource(String uri) {
        return " <" + uri + "> ";
    }
}