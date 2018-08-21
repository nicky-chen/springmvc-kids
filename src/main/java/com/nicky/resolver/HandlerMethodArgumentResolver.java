package com.nicky.resolver;

import com.nicky.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

public interface HandlerMethodArgumentResolver {
    
    boolean supportsParameter(MethodParameter methodParameter);
    
    Object resolveArgument(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex,
            Method method);

    default int getOrderAnnotationValue(){
        Order annotation = this.getClass().getAnnotation(Order.class);
        if (annotation == null) {
            return Integer.MAX_VALUE;
        }
        return annotation.value();
    }
}
