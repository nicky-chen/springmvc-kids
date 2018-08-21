package com.nicky.resolver;

import org.springframework.util.ClassUtils;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/20 at 16:47
 */
public abstract class AbstractOrderResolver implements HandlerMethodArgumentResolver {

    protected int order = Integer.MAX_VALUE;

    public AbstractOrderResolver() {
        setOrder();
    }

    protected boolean paramConfirm(MethodParameter parameter, Class targetClass) {

        try {
            return ClassUtils.isAssignable(targetClass, parameter.getParamType());
        }
        catch (Exception ex) {
            return false;
        }
    }

    public void setOrder(){
        order = this.getOrderAnnotationValue();
    }

    public int getOrder() {
        return order;
    }
}
