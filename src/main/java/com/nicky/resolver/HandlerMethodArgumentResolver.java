package com.nicky.resolver;

import com.nicky.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodArgumentResolver {
    
    boolean supportsParameter(MethodParameter methodParameter);
    
    Object resolveArgument(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParameter);

    default int getOrderAnnotationValue(){
        Order annotation = this.getClass().getAnnotation(Order.class);
        if (annotation == null) {
            return Integer.MAX_VALUE;
        }
        return annotation.value();
    }
}
