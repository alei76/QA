package com.tcm.config;


import com.jfinal.config.*;
import com.jfinal.render.ViewType;

/**
 * Created by azurexsyl on 2015/6/27.
 */
public class QAConfig extends JFinalConfig {

    // 项目根路径
    final private String projectRoot = "/";

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

    }
}
