package com.ddmax.zjnucloud.util;

/**
 * @author ddMax
 * @since 2015/12/16 22:06.
 */

import android.app.Activity;
import android.content.Context;

public class DensityUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获得当前Activity所在Window的宽度
     */
    public static int getWidth(Activity activity) {
        int width;
        width = activity.getWindowManager().getDefaultDisplay().getWidth();
        return width;
    }
}