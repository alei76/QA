package com.tcm.offline.preprocess;

import com.tcm.util.FileIO;

import java.io.BufferedReader;

/**
 * Created by azurexsyl on 2015/12/27.
 *
 * 问题统计模块
 */
public class QASummery {

    public static void main(String[] args) throws Exception {
        // 创建输入流对象
        BufferedReader bufferedReader = FileIO.getBufferedReader(".\\resources\\dataset\\tcm_qa\\classifier2\\data\\qa.txt");

        classifyDomain(bufferedReader);
    }

    public static void classifyValid(BufferedReader bufferedReader) throws Exception {
        String line = "";
        int pos = 0;
        int neg = 0;
        int count = 0;
        while ((line = bufferedReader.readLine()) != null) {
            line = bufferedReader.readLine();
            ++count;
            if(line.startsWith("1"))
                pos++;
            else if(line.startsWith("0"))
                neg++;
            else {
                System.out.println(count);
            }
        }

        System.out.println("POSITIVE: " + pos);
        System.out.println("NEGATIVE: " + neg);
    }

    public static void classifyDomain(BufferedReader bufferedReader) throws Exception {
        String line = "";
        int med = 0;
        int pre = 0;
        int dis = 0;
        while ((line = bufferedReader.readLine()) != null) {
            line = bufferedReader.readLine();
            if(line.startsWith("药"))
                med++;
            else if(line.startsWith("方"))
                pre++;
            else  if(line.startsWith("病")){
                dis++;
            }
        }
        System.out.println("med " + med);
        System.out.println("pre " + pre);
        System.out.println("dis " + dis);
    }
}
