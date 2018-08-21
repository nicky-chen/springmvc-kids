package com.nicky.handleradapter;

import com.nicky.annotation.Service;
import com.nicky.bean.BeanFactoryAware;
import com.nicky.resolver.HandlerMethodArgumentResolver;
import com.nicky.resolver.MethodParameter;
import com.nicky.servlet.RequestContextHolder;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
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
        
        //实现了ArgumentResolver这个接口的实现类
        Map<String, Object> argumentResolvers = getBeanInterfaceImpl(HandlerMethodArgumentResolver.class);
        
        int paramIndex = 0;
        int i = 0;
        for (Class<?> paramClazz : paramList) {
            for (Map.Entry<String, Object> entry : argumentResolvers.entrySet()) {
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
            //移除ThreadLocal副本
            RequestContextHolder.resetRequestAttributes();
        } catch (ReflectiveOperationException e) {
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
