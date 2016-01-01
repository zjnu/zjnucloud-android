package com.ddmax.zjnucloud.common.aa;

import com.activeandroid.Model;
import com.activeandroid.serializer.TypeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author ddMax
 * @since 2015/12/14 21:00.
 */
public abstract class Serializer extends TypeSerializer {

    protected Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ActiveAndroidExclusionStrategy(null, Model.class))
            .create();

    //序列化后的Class类型
    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    //序列化为String
    @Override
    public Object serialize(Object data) {
        if (data == null) {
            return null;
        }
        return gson.toJson(data);
    }
}