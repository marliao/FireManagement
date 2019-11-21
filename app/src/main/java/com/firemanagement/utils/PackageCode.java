package com.firemanagement.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.firemanagement.AppClient;

public class PackageCode {

    private static int versionCode;
    private static String versionName;
    private static PackageInfo packageInfo;

    public static PackageInfo getpackcode() {
        PackageManager packageManager = AppClient.mContext.getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(AppClient.mContext.getPackageName(), 0);
            //版本号
            //versionCode = packageInfo.versionCode;
            //版本名称
            //versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }
}
