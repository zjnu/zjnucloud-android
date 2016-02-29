package com.ddmax.zjnucloud.database.aa;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * @author ddMax
 * @since 2015/12/14 20:59.
 */
public class ActiveAndroidExclusionStrategy implements ExclusionStrategy {

    private final Class<?> excludedThisClass;
    private final Class<?> excludedThisClassFields;

    /***
     * 过滤器初始化
     *
     * @param excludedThisClass      该类和继承自该类的对象实例将被忽略
     * @param excluedThisClassFields 该类的属性将不被序列化
     */
    public ActiveAndroidExclusionStrategy(Class<?> excludedThisClass, Class<?> excluedThisClassFields) {
        this.excludedThisClass = excludedThisClass;
        this.excludedThisClassFields = excluedThisClassFields;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        if (clazz == null) return false;
        if (clazz.equals(excludedThisClass)) return true;
        return shouldSkipClass(clazz.getSuperclass());
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaringClass().equals(excludedThisClassFields);
    }
}
