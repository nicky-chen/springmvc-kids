package com.nicky.service.impl;

import com.nicky.annotation.Service;
import com.nicky.service.UserService;

@Service(beanName = "userService")
public class UserServiceImpl implements UserService {
    
    @Override
    public String query(String param) {
        
        return this.getClass().getName() + "query";
    }
    
    @Override
    public String insert(String param) {
        // TODO Auto-generated method stub
        return this.getClass().getName() + "insert";
    }
    
    @Override
    public String update(String param) {
        // TODO Auto-generated method stub
        return this.getClass().getName() + "update";
    }
    
}
