package com.nicky.resolver;


import com.nicky.annotation.Order;
import com.nicky.annotation.Service;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Service(beanName = "httpServletResponseArgumentResolver")
@Order(1)
public class HttpServletResponseArgumentResolver extends AbstractOrderResolver {
    
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return paramConfirm(methodParameter, ServletResponse.class);
    }
    
    @Override
    public Object resolveArgument(HttpServletRequest request,
            HttpServletResponse response, Class<?> type, int paramIndex,
            Method method) {
        System.out.println("ServletResponse.class");
        return response;
    }
    
}
