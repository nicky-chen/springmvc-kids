package com.nicky.handleradapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface HandlerAdapter {
    
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMapping handler);
}
