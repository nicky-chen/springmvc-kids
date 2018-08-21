package com.nicky.service.impl;

import com.nicky.annotation.Service;
import com.nicky.service.UserService;

@Service(beanName = "userService")
public class UserServiceImpl implements UserService {
    
    @Override
    public String query(String param) {
       return "User:{name = nicky, age = 23}" ;
    }

}
