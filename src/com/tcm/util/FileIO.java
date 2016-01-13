package com.tcm.util;

import java.io.*;

/**
 * Created by azurexsyl on 2015/12/15.
 * 文件读写包装类
 */
public class FileIO {

    public static BufferedReader getBufferedReader (String str) throws FileNotFoundException {
        Reader reader = new FileReader(str);
        return new BufferedReader(reader);
    }

    public static BufferedWriter getBufferedWriter (String str) throws IOException {
        Writer writer = new FileWriter(str);
        return new BufferedWriter(writer);
    }
}
