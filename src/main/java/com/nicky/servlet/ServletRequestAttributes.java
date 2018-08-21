package com.nicky.servlet;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/20 at 21:51
 */
@Data
public class ServletRequestAttributes {

    private final HttpServletRequest request;

    private final HttpServletResponse response;

}
