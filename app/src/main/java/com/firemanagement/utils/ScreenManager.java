package com.firemanagement.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.firemanagement.activities.BaseActivity;


/**
 * @ClassName ScreenManager
 * @Author AiSHan Feng
 * @Date 2018/12/30 17:34
 * @Version 1.0
 * @Description 屏幕管理类
 */
public class ScreenManager {
    private static ScreenManager instance;

    private ScreenManager() {
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            synchronized (ScreenManager.class) {
                instance = new ScreenManager();
            }
        }
        return instance;
    }

    /**
     * 窗口全屏
     */
    public void setFullScreen(boolean isChange, BaseActivity mActivity) {
        if (!isChange) {
            return;
        }
        mActivity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * [沉浸状态栏]
     */
    public void setStatusBar(boolean isChange, BaseActivity mActivity, @ColorRes int resId) {
        if (!isChange) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mActivity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (resId == -1) {
                //状态栏透明
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                window.setStatusBarColor(mActivity.getResources().getColor(resId));
            }
            //如果有虚拟按键设置成透明
            if (checkDeviceHasNavigationBar2(mActivity)) {
                window.setNavigationBarColor(Color.TRANSPARENT);
            }
        }
    }


    /**
     * 通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
     * @param activity
     * @return
     */
    public boolean checkDeviceHasNavigationBar2(Context activity) {
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * 设置横屏竖屏
     **/
    public void setScreenRoate(boolean isChange, BaseActivity mActivity) {
        if (!isChange) {
            //横屏
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            //竖屏
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


}
