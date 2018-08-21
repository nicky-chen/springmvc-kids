package com.nicky.servlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/21 at 10:27
 */
@Configuration
public class ContainerConfig {


    @Bean
    public ServletRegistrationBean MyServlet1(){
        ServletRegistrationBean bean = new ServletRegistrationBean();
        bean.setServlet(new DispatcherServlet());
        bean.addUrlMappings("/*");
        return bean;
    }

}
