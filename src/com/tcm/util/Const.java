package com.tcm.util;

/**
 * Created by azurexsyl on 2016/1/18.
 */
public class Const {

    private static String path = Const.class.getResource("/").getPath().replace("%20", " ");
    // 开发环境
    //public static String RESOURCE_BASE_DIR = "./resources/";
    // 生产环境
    public static String RESOURCE_BASE_DIR = path ;
}
