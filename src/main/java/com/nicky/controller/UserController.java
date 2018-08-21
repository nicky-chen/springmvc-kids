package com.nicky.controller;


import com.nicky.annotation.Autowired;
import com.nicky.annotation.Controller;
import com.nicky.annotation.RequestMapping;
import com.nicky.annotation.RequestParam;
import com.nicky.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Controller("testController")
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService jackService;

    @RequestMapping("/query.do")
    public void query(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Map map,
            @RequestParam("name") String userName, List list) {
        
        try {
            System.out.println("sdf: " + userName );
            PrintWriter pw = response.getWriter();
            String result = jackService.query(null);
            pw.write(result);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequestMapping("/insert")
    public String insert(String param) {
        return "success";
    }
    
    @RequestMapping("/update")
    public void update(HttpServletRequest request,
            HttpServletResponse response, String param) {
        try {
            PrintWriter pw = response.getWriter();
            String result = jackService.update(param);
            
            pw.write(result);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
