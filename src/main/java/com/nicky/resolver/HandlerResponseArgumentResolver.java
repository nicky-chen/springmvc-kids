package com.nicky.resolver;

import com.nicky.annotation.Order;
import com.nicky.annotation.Service;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service(beanName = "handlerResponseArgumentResolver")
@Order(1)
public class HandlerResponseArgumentResolver extends AbstractOrderResolver {
    
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return paramConfirm(methodParameter, ServletResponse.class);
    }
    
    @Override
    public Object resolveArgument(HttpServletRequest request,
            HttpServletResponse response, MethodParameter methodParameter) {
        System.out.println("ServletResponse.class");
        return response;
    }
    
}
