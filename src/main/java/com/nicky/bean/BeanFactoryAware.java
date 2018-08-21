package com.nicky.bean;

import java.util.Map;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/21 at 15:27
 */
public interface BeanFactoryAware {

    void setBeanFactory(Map<String, Object> beanFactory);
}
