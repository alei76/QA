package com.tcm.offline.preprocess.classifier3;

import com.tcm.util.FileIO;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by azurexsyl on 2016/1/14.
 */
public class TagStat {

    public static void main(String[] args) throws Exception{

        BufferedReader bufferedReader = FileIO.getBufferedReader("./resources/dataset/tcm_qa/classifier3/data/qa.txt");
        String line = "";
        Map<String, Integer> map = new HashMap<>();

        while((line = bufferedReader.readLine()) != null) {
            line = bufferedReader.readLine();
            if(map.get(line) == null) {
                map.put(line, 1);
            } else {
                map.put(line, map.get(line) + 1);
            }
        }

        for(String key : map.keySet()) {
            System.out.println(key + " -> " + map.get(key));
        }
    }
}
