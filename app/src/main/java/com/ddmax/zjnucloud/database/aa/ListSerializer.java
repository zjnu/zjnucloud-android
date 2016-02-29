package com.ddmax.zjnucloud.database.aa;

import com.activeandroid.Model;
import com.ddmax.zjnucloud.model.course.Course;
import com.ddmax.zjnucloud.model.exam.Exam;
import com.ddmax.zjnucloud.model.score.Semester;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author ddMax
 * @since 2015/12/14 21:11.
 * 说明：ActiveAndroid的模型List<Exam>集合序列化
 */
public class ListSerializer extends Serializer {

    @Override
    public Class<?> getDeserializedType() {
        return List.class;
    }

    @Override
    public List<? extends Model> deserialize(Object data) {
        if (data == null) {
            return null;
        }
        // TODO: Implement in a more elegant way
        String strData = (String) data;
        if (strData.contains("semester")) {
            return gson.fromJson((String) data, new TypeToken<List<Semester>>() {
            }.getType());
        } else if (strData.contains("place")) {
            return gson.fromJson((String) data, new TypeToken<List<Exam>>() {
            }.getType());
        } else if (strData.contains("classroom")) {
            return gson.fromJson((String) data, new TypeToken<List<Course>>() {
            }.getType());
        }
        return gson.fromJson((String) data, new TypeToken<List>() {
        }.getType());
    }
}