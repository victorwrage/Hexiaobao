package com.zdv.hexiaobao.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Hello_world on 2017/5/4.
 */

public class AppUtils {
    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
