package com.tcm.offline.preprocess;

import com.tcm.util.FileIO;

import java.io.BufferedReader;

/**
 * Created by azurexsyl on 2016/1/6.
 */
public class FindTagError {

    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader2 = FileIO.getBufferedReader("./resources/dataset/tcm_qa/qa.txt");

        String line = "";
        while((line = bufferedReader2.readLine()) != null) {
            if(line.startsWith("0") && !line.equals("0|0|0")) {
                System.out.println(line);
            }
        }

    }
}
