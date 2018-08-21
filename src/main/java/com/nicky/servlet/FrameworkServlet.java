package com.nicky.servlet;


import com.nicky.bean.ApplicationContext;
import com.nicky.bean.ApplicationContextAware;
import com.nicky.bean.BeanFactoryAware;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/20 at 17:30
 */
public abstract class FrameworkServlet extends HttpServlet {

    protected final void processRequest(HttpServletRequest request, HttpServletResponse response) {

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
    }

}
