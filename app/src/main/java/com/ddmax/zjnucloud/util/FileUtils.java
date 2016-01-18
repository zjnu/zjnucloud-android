package com.ddmax.zjnucloud.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * @author ddMax
 * @since 2015/03/25 22:23.
 */
public class FileUtils {

    /**
     * 返回图像Uri对应的File
     * @param imageUri
     * @param contentResolver
     * @return File
     */
    public static File getImageFileFromUri(Uri imageUri, ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = contentResolver.query(imageUri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return new File(filePath);
    }
}
