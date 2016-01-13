package com.tcm.config;

import com.jfinal.config.*;
import com.jfinal.render.ViewType;

/**
 * Created by azurexsyl on 2015/6/27.
 */
public class QAConfig extends JFinalConfig {

    // 项目根路径
    private String projectRoot = "/";

    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setViewType(ViewType.JSP);
        //me.setBaseViewPath("/");
    }

    @Override
    public void configRoute(Routes me) {
        me.add(projectRoot, com.tcm.controller.HomeController.class, "/pages/views/");
    }

    @Override
    public void configPlugin(Plugins me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }

    @Override
    public void afterJFinalStart() {
        /***
         * 初始化分词器
         */
        /***
         * code
         */
        System.out.println("Initializing Analyzer Finished");

        /***
         * 初始化同义词映射表
         */
        System.out.println("Initializing Synonym Finished");


        /***
         * 读取知识图谱
         */
        System.out.println("Reading RDFS Finished");
    }
}
