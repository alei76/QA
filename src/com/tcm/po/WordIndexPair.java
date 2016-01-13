package com.tcm.po;

/**
 * Created by azurexsyl on 2015/12/14.
 * 用来存储词以及词下标的结构
 */
public class WordIndexPair {

    private String word;
    private Integer index;

    public WordIndexPair(String word, Integer index) {
        this.word = word;
        this.index = index;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
