package com.nicky.resolver;

import com.nicky.annotation.Order;
import com.nicky.annotation.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service(beanName = "httpServletRequestArgumentResolver")
@Order(10)
public class HttpServletRequestArgumentResolver extends AbstractOrderResolver {
    
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return paramConfirm(methodParameter, ServletRequest.class);
    }
    
    @Override
    public Object resolveArgument(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParameter) {
        System.out.println("ServletRequest.class");
        return request;
    }

}
