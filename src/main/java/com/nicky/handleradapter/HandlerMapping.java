package com.nicky.handleradapter;

import lombok.*;

import java.lang.reflect.Method;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/21 at 16:38
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HandlerMapping {

    private String controllerBeanName;

    private Method method;

}
