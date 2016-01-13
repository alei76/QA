package com.tcm.po;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azurexsyl on 2015/12/20.
 */
public class ConstraintPair {

    private String property;
    private List<String> uris;
    private String value;

    public ConstraintPair() {
        uris = new ArrayList<>();
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
