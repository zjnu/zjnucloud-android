package com.ddmax.zjnucloud.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @author ddMax
 * @since 2015/12/15 17:14.
 */
public class PhoneUtils {

    /**
     * 获取当前的手机号
     */
    public static String getLocalNumber(Context context) {
        TelephonyManager tManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String number = tManager.getLine1Number();
        return number;
    }
}
