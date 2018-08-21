package com.nicky.handleradapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public interface HandlerAdapter {
    
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMapping handler);
}
