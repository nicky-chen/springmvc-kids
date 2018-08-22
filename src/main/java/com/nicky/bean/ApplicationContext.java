package com.nicky.bean;

import com.nicky.annotation.Autowired;
import com.nicky.annotation.ComponentScan;
import com.nicky.annotation.Controller;
import com.nicky.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/14 at 18:05
 */
@ComponentScan(basePackages = "com.nicky")
public class ApplicationContext {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private volatile static boolean beanIsInitialized = false;

    private List<String> classNames = new CopyOnWriteArrayList<>();

    private Map<String, Object> beanFactory = new ConcurrentHashMap<>();

    private ApplicationContext() {
        synchronized (ApplicationContext.class) {
            if (!beanIsInitialized) {
                beanIsInitialized = true;
                //扫描包
                scanPackage(this.getClass().getAnnotation(ComponentScan.class).basePackages()[0]);
                //实例化
                instance();
                //依赖注入
                dependencyInjection();

            } else {
                throw new RuntimeException("ApplicationContext is only be created by singleton");

            }
        }
    }

    private static class ApplicationContextHolder{

        private static final ApplicationContext CONTEXT = new ApplicationContext();
    }

    public synchronized static ApplicationContext getApplicationContext() {
        if (!beanIsInitialized) {
            return ApplicationContextHolder.CONTEXT;
        }
        return null;
    }

    public Object getBean(String beanName) {
        Object o = beanFactory.get(beanName);
        if (o == null) {
            throw new RuntimeException("bean not found");
        }
        return o;
    }

    public <T> T getBean(String beanName, Class<T> cls) {
        return cls.cast(getBean(beanName));
    }

    public Map<String, Object> getBeanFactory (){
        return beanFactory;
    }

    private void dependencyInjection() {

        if (CollectionUtils.isEmpty(beanFactory.entrySet())) {
            logger.error("instance has not been over");
            return;
        }
        beanFactory.forEach((k, v) ->{
            Class cls = v.getClass();
            if (cls.isAnnotationPresent(Controller.class) || cls.isAnnotationPresent(Service.class)) {
                Arrays.stream(cls.getDeclaredFields()).forEach(field -> {
                    if (field.isAnnotationPresent(Autowired.class) && classNames.contains(field.getType().getName())) {
                        field.setAccessible(true);
                        try {
                            field.set(v, beanFactory.get(camelNameSpell(field.getType().getSimpleName())));
                        } catch (IllegalAccessException e) {
                            logger.error("error dependencyInjection", e);
                        }
                    }
                });
                //beanFactoryAware applicationContextAware
                if (BeanFactoryAware.class.isAssignableFrom(cls)) {
                    try {
                        Method method = cls.getMethod("setBeanFactory", Map.class);
                        method.invoke(v, beanFactory);
                    } catch (ReflectiveOperationException e) {
                        logger.error("beanFactoryAware error", e);
                    }
                }
            }
        });


    }

    /**
     * 销毁方法，用于释放资源
     */
    public void close() {
        beanFactory.clear();
        beanFactory = null;
    }

    private void scanPackage(String basePackage) {

        URL url = this.getClass().getClassLoader().getResource(basePackage.replaceAll("\\.", "/"));
        File[] classFileList = getClassFileList(url.getPath());
        if (classFileList != null) {
            for (File file : classFileList) {
                if (file.isDirectory()) {
                    scanPackage(basePackage + "." + file.getName());
                }else {
                    String className = basePackage+"."+file.getName().replace(".class", "");
                    classNames.add(className);
                }
            }
        }
    }

    //获取该路径下所遇的class文件和目录
    private static File[] getClassFileList(String filePath) {
        return new File(filePath).listFiles(
                file -> file.isFile() && file.getName().endsWith(".class") || file.isDirectory());
    }

    private void instance() {
        if (CollectionUtils.isEmpty(classNames)) {
            logger.error("scan path error！");
            return;
        }

        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz.isInterface()) {
                    continue;
                }
                if (clazz.isAnnotationPresent(Controller.class)) {
                    Controller annotation = clazz.getAnnotation(Controller.class);
                    Object instance = clazz.newInstance();
                    //不指定beanName,采用驼峰命名
                    Object o = beanFactory.putIfAbsent(StringUtils.isEmpty(annotation.value()) ?
                            camelNameSpell(clazz.getSimpleName()) :
                            annotation.value(), instance);
                    if (o != null) {
                        throw new RuntimeException(String.format("duplicated beanName %s", clazz.getSimpleName()));
                    }
                }

                if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    Object instance = clazz.newInstance();
                    Object o = beanFactory.putIfAbsent(StringUtils.isEmpty(service.beanName()) ?
                            camelNameSpell(clazz.getSimpleName()) :
                            service.beanName(), instance);
                    if (o != null) {
                        throw new RuntimeException(String.format("duplicated beanName %s", clazz.getSimpleName()));
                    }
                }
            } catch (ReflectiveOperationException e) {
                logger.error("instance reflect error", e);
            }
        }
    }

    private static String camelNameSpell(String name) {
        if (StringUtils.isEmpty(name)) {
            return name;
        }
        return String.valueOf(name.charAt(0)).toLowerCase() + name.substring(1);

    }

}
