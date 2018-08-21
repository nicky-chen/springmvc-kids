package com.nicky.servlet;


/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/8/20 at 17:39
 */
public final class RequestContextHolder {

    private static final ThreadLocal<ServletRequestAttributes> requestHolder =
            new NamedThreadLocal<>("Request attributes");


    private static final ThreadLocal<ServletRequestAttributes> inheritableRequestHolder =
            new NamedInheritableThreadLocal<>("Request context");


    public static void resetRequestAttributes() {
        requestHolder.remove();
        inheritableRequestHolder.remove();
    }

    public static void setRequestAttributes(ServletRequestAttributes attributes) {
        setRequestAttributes(attributes, false);
    }

    public static void setRequestAttributes(ServletRequestAttributes attributes, boolean inheritable) {
        if (attributes == null) {
            resetRequestAttributes();
        }
        else {
            if (inheritable) {
                inheritableRequestHolder.set(attributes);
                requestHolder.remove();
            }
            else {
                requestHolder.set(attributes);
                inheritableRequestHolder.remove();
            }
        }
    }


    public static ServletRequestAttributes getRequestAttributes() {
        ServletRequestAttributes attributes = requestHolder.get();
        if (attributes == null) {
            attributes = inheritableRequestHolder.get();
        }
        return attributes;
    }





}
