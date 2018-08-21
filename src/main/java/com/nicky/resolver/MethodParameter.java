package com.nicky.resolver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/20 at 16:42
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MethodParameter {

    private Class<?> paramType;

    private int paramIndex;

    private Method method;

}
