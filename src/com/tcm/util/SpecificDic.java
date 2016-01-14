package com.tcm.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by azurexsyl on 2015/12/14.
 * 词典工具集
 */
public class SpecificDic {

    private static Set<String> whDic;

    private static Set<String> triDic;

    private static Set<String> allDic;

    private static Map<String, String> domainDic;

    private static Map<String, Integer> corpusDic;

    static {

        whDic = new HashSet<>();
        triDic = new HashSet<>();
        allDic = new HashSet<>();
        domainDic = new HashMap<>();
        corpusDic = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("./resources/dic/wh_vocabulary.dic"));
            String line = "";
            while ((line = br.readLine()) != null) {
                whDic.add(line);
            }
            br = new BufferedReader(new FileReader("./resources/dic/trivial_vocabulary.dic"));
            while ((line = br.readLine()) != null) {
                triDic.add(line);
            }
            br = new BufferedReader(new FileReader("./resources/dic/type_vocabulary.dic"));
            while((line = br.readLine()) != null) {
                String[] kv = line.split(" ");
                domainDic.put(kv[0], kv[1]);
            }
            br = new BufferedReader(new FileReader("./resources/dic/qa_dic.txt"));
            while ((line = br.readLine()) != null) {
                if(line.equals("***************"))
                    break;
                allDic.add(line);
            }
            br = new BufferedReader(new FileReader("./resources/dic/words_4w_10.txt"));
            int count = 0;
            while ((line = br.readLine()) != null) {
                corpusDic.put(line, count++);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 判断一个词是否在词典内 {疑问词词典}
     * @param word
     * @return
     */
    public static boolean containsWHDic(String word) {
        if(whDic.contains(word))
            return true;
        else
            return false;
    }

    /***
     * 判断一个词是否在词典内 {无用词词典}
     * @param word
     * @return
     */
    public static boolean containsTrivialDic(String word) {
        if(triDic.contains(word))
            return true;
        else
            return false;
    }

    /***
     * 判断一个词是否在词典内 {专业词典}
     * @param word
     * @return
     */
    public static boolean specific(String word) {
        if(allDic.contains(word)) {
            return true;
        }else{
            return false;
        }
    }

    /***
     * 根据关键词返回领域类型
     * @param word
     * @return
     */
    public static String getDomainType(String word) {
        if(domainDic.keySet().contains(word))
            return domainDic.get(word);
        else
            return "none";
    }

    /***
     * 返回词在向量空间中的位置
     * @param word
     * @return
     */
    public static Integer getWordNum(String word) {
        Integer result = corpusDic.get(word);
        if(result == null)
            return -1;
        else
            return result;
    }

    /***
     * 返回语料向量空间长度
     * @return
     */
    public static int getCorpusDicLength() {
        return corpusDic.keySet().size();
    }
}
