package com.nicky.servlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/21 at 09:49
 * https://blog.csdn.net/songhaifengshuaige/article/details/54138023
 * spi方式启动
 */
public class ServletConfig implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext container) throws ServletException {

        System.out.println("hhhhh启动加载自定义的ServletContainerInitializer");
        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcherServlet", new DispatcherServlet());
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

    }
}
