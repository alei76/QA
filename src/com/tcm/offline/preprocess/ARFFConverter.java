package com.tcm.offline.preprocess;

import com.tcm.util.Const;
import com.tcm.util.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by azurexsyl on 2016/1/19.
 */
public class ARFFConverter {

    public static void main(String[] args) throws Exception{

        // 读取源文件
        BufferedReader reader = FileIO.getBufferedReader(Const.RESOURCE_BASE_DIR + "dataset/model/wc_sp_w2_instances.arff");

        // 写文件
        BufferedWriter writer = FileIO.getBufferedWriter(Const.RESOURCE_BASE_DIR + "dataset/model/instances.arff");

        String line = "";

        while((line = reader.readLine()) != null) {
            //System.out.println(line);
            writer.write(line + "\n");
        }
        writer.close();

    }
}
