package com.nicky.handleradapter;

import com.nicky.annotation.Service;
import com.nicky.bean.BeanFactoryAware;
import com.nicky.resolver.HandlerMethodArgumentResolver;
import com.nicky.resolver.MethodParameter;
import com.nicky.servlet.RequestContextHolder;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service(beanName = "handlerAdapter")
public class HttpRequestHandlerAdapter implements HandlerAdapter, BeanFactoryAware {

    private Map<String, Object> beanFactory;
    
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMapping handler) {

        Method method = handler.getMethod();
        Class<?>[] paramList = method.getParameterTypes();
        
        Object[] args = new Object[paramList.length];
        
        //获取接口的实现
        Map<String, Object> resolvers = getBeanInterfaceImpl(HandlerMethodArgumentResolver.class);
        
        int paramIndex = 0, i = 0;
        for (Class<?> paramClazz : paramList) {
            for (Map.Entry<String, Object> entry : resolvers.entrySet()) {
                HandlerMethodArgumentResolver resolver = (HandlerMethodArgumentResolver)entry.getValue();
                MethodParameter warp = new MethodParameter(paramClazz, paramIndex, method);
                if (resolver.supportsParameter(warp)) {
                    args[i++] = resolver.resolveArgument(request, response, warp);
                }
            }
            paramIndex++;
        }
        Map<String, Object> model = new HashMap<>(1);
        ModelAndView view = new ModelAndView();
        Object obj;
        try {
            Object instance = beanFactory.get(handler.getControllerBeanName());
            obj = method.invoke(instance, args);
            //如果调用方法没有传入HttpServletResponse参数，则自己封装
            if (!Arrays.asList(paramList).contains(HttpServletResponse.class)) {
                PrintWriter writer = response.getWriter();
                writer.write(obj.toString());
            }
            //移除ThreadLocal副本
            RequestContextHolder.resetRequestAttributes();
        } catch (ReflectiveOperationException | IOException e) {
            view.setStatus(HttpStatus.EXPECTATION_FAILED);
            model.put("result", e.getLocalizedMessage());
            view.setModel(model);
            return view;
        }
        view.setStatus(HttpStatus.OK);
        model.put("result",obj);
        view.setModel(model);
        return view;
    }
    
    private Map<String, Object> getBeanInterfaceImpl(Class<?> interfaceType) {
        
        Map<String, Object> result = new HashMap<>();
        beanFactory.forEach((k, v) ->{
            if (interfaceType.isAssignableFrom(v.getClass())) {
                result.put(k, v);
            }
        });
        return result;
    }

    @Override
    public void setBeanFactory(Map<String, Object> beanFactory) {
        this.beanFactory = beanFactory;
    }
}
