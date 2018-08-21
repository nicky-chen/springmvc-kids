package com.nicky.servlet;

import com.nicky.annotation.Controller;
import com.nicky.annotation.RequestMapping;
import com.nicky.annotation.Service;
import com.nicky.bean.ApplicationContext;
import com.nicky.handleradapter.HandlerAdapter;
import com.nicky.handleradapter.HandlerMapping;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servlet implementation class DispatcherServlet
 * @author cx520
 */
@NoArgsConstructor
@Service
public class DispatcherServlet extends FrameworkServlet {

    private static final long serialVersionUID = -6961498140472266321L;

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    
    private Map<String, HandlerMapping> handlerMappings = new ConcurrentHashMap<>();
    
    private static Properties prop;

    private ApplicationContext applicationContext;

    private final String handlerAdapter = "spring.bean.handlerAdapter";

    static {

        InputStream is = DispatcherServlet.class.getResourceAsStream("/application.properties");
        prop = new Properties();
        try {
            prop.load(is);
        } catch (IOException e) {
            logger.error("static block error", e);
        }
    }


    @Override
    public void init(ServletConfig config) {
        applicationContext = ApplicationContext.getApplicationContext();
        //处理映射
        handlerMapping();
        
    }
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
        ServletRequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        doPost(requestAttributes.getRequest(), requestAttributes.getResponse());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        String uri = request.getRequestURI();
        String context = request.getContextPath();
        String path = uri.replace(context, "");
        logger.warn("access path :{}", path);

        String className = prop.getProperty(handlerAdapter);
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("adapter not found", e);
        }
        Service annotation = cls.getAnnotation(Service.class);
        HandlerAdapter ha = (HandlerAdapter) applicationContext.getBeanFactory().get(annotation.beanName());
        HandlerMapping handlerMapping = handlerMappings.get(path);
        if (handlerMapping == null) {
            try {
                PrintWriter pw = response.getWriter();
                pw.write("404, page not found");
            } catch (IOException e) {
                e.getLocalizedMessage();
            }
        }
        ha.handle(request, response, handlerMapping);
        
    }
    
    private void handlerMapping() {

        for (Map.Entry<String, Object> entry : applicationContext.getBeanFactory().entrySet()) {
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();

            if (clazz.isAnnotationPresent(Controller.class)) {
                Controller annotation = clazz.getAnnotation(Controller.class);
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                String rootPath = requestMapping.value();

                for (Method method : clazz.getMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping subMethod = method.getAnnotation(RequestMapping.class);
                        String methodPath = subMethod.value();
                        handlerMappings.putIfAbsent(rootPath + methodPath, new HandlerMapping(annotation.value(), method));
                    }
                }
            }
        }
    }

}
