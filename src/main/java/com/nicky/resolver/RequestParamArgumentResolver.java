package com.nicky.resolver;


import com.nicky.annotation.RequestParam;
import com.nicky.annotation.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Service
public class RequestParamArgumentResolver extends AbstractOrderResolver {
    
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        
        Annotation[][] an = methodParameter.getMethod().getParameterAnnotations();
        
        Annotation[] paramAns = an[methodParameter.getParamIndex()];
        
        for (Annotation paramAn : paramAns) {
            if (RequestParam.class.isAssignableFrom(paramAn.getClass())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Object resolveArgument(HttpServletRequest request,
            HttpServletResponse response, Class<?> type, int paramIndex,
            Method method) {
        
        Annotation[][] an = method.getParameterAnnotations();
        
        Annotation[] paramAns = an[paramIndex];
        
        for (Annotation paramAn : paramAns) {
            if (RequestParam.class.isAssignableFrom(paramAn.getClass())) {
                RequestParam rp = (RequestParam)paramAn;
                
                String value = rp.value();
                System.out.println("value : " + value);
               return request.getParameter(value);
            }
        }
        
        return null;
    }

    @Override
    public void setOrder() {
        order = 100;
    }
}