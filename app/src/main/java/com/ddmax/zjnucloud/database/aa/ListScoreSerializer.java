package com.ddmax.zjnucloud.database.aa;

import com.ddmax.zjnucloud.model.score.Score;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author ddMax
 * @since 2015/12/14 21:11.
 * 说明：ActiveAndroid的模型List<Course>集合序列化
 */
public class ListScoreSerializer extends Serializer {
    @Override
    public Class<?> getDeserializedType() {
        return List.class;
    }

    @Override
    public List deserialize(Object data) {
        if (data == null) {
            return null;
        }
        return gson.fromJson((String) data, new TypeToken<List<Score>>() {
        }.getType());
    }
}