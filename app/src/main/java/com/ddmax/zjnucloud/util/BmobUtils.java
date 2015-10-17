package com.ddmax.zjnucloud.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import cn.bmob.v3.datatype.BmobFile;

/**
 * @author ddMax
 * @since 2015/10/16 12:55.
 */
public class BmobUtils {

    private static final String TAG = "BmobUtils";

    public static BmobFile getImage(Bitmap bitmap, Context context, @Nullable String fileName) {
        // 设置缓存文件名
        String cacheName;
        if (TextUtils.isEmpty(fileName)) {
            cacheName = UUID.randomUUID().toString();
        } else {
            cacheName = fileName;
        }
        File file = CacheUtils.getCacheDirectory(context, true, cacheName);
        try {
            if (!file.exists()) {
                FileOutputStream fos = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)) {
                    fos.flush();
                    fos.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final BmobFile image = new BmobFile(file);
        return image;
    }
}
