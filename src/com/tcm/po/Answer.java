package com.tcm.po;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azurexsyl on 2015/12/13.
 */
public class Answer {

    private String description;
    private String query;
    private String[] param;
    private String result;
    private List<String> oris = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }



    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    public void addOris(String ori) {
        oris.add(ori);
    };

    public List<String> getOris() {
        return oris;
    }
}
