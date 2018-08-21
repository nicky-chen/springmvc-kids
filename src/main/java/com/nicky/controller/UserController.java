package com.nicky.controller;

import com.nicky.annotation.Autowired;
import com.nicky.annotation.Controller;
import com.nicky.annotation.RequestMapping;
import com.nicky.annotation.RequestParam;
import com.nicky.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller("testController")
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private UserService userService;

    @RequestMapping("/query.do")
    public void queryUserInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam("userName") String userName) {
        logger.info("userName: {}", userName );
        try {
            PrintWriter pw = response.getWriter();
            String result = userService.query(userName);
            pw.write(result);
        }
        catch (IOException e) {
            logger.error("test", e);
        }
    }

    @RequestMapping("/insert.do")
    public String insert(String param) {
        return "success";
    }
}
