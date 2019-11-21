package com.firemanagement.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;


import com.firemanagement.frags.BaseFragment;
import com.firemanagement.utils.L;
import com.firemanagement.utils.ScreenManager;

import java.util.List;


/**
 * @ClassName BaseActivity
 * @Author AiSHan Feng
 * @Date 2018/12/30 17:07
 * @Version 1.0
 * @Description 所有Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Log标记
     */
    public final String TAG = this.getClass().getSimpleName();

    /**
     * 上下文对象
     */
    protected Activity mContext;

    /**
     * P层对象
     */


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.i(TAG, "--->onCreate()");

        /*
        在界面未初始化之前调用的初始化窗口
         */
        initWidows();


        /*
        布局设置
         */
        setContentView(getLayout());


        /*
        绑定Presenter和View,可能不止一个Present和View,放到子类,由用户具体实现绑定和解除
         */
//        mPresenter = creatPresenter();
//        if (mPresenter != null)
//            mPresenter.attachView((V) this);
        /*
        添加activity到Stack
         */
        //ActivityStackManager.getActivityStackManager().pushActivity(this);
        /*
        一些初始化工作
         */
        initLayout();
        init();
    }


    /**
     * 创建P层对象
     *
     * @return
     */
    // protected abstract P creatPresenter();


    /**
     * 获取子aitivity的布局ID
     *
     * @return
     */
    protected abstract int getLayout();


    /**
     * 初始化布局
     */
    protected abstract void initLayout();


    /**
     * 初始化窗口
     */
    private void initWidows() {
           /*
        屏幕设置
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ScreenManager.getInstance().setFullScreen(isAllowFullScreen(), this);
        ScreenManager.getInstance().setScreenRoate(isAllowScreenRoate(), this);
        ScreenManager.getInstance().setStatusBar(isSetStatusBar(), this, getStatusBarColorResId());
    }

    /**
     * 状态栏颜色
     * 为-1 透明
     *
     * @return
     */
    public int getStatusBarColorResId() {
        return -1;
    }


    /**
     * 是否是全面屏
     *
     * @return
     */
    protected boolean isAllowFullScreen() {
        //默认不是全面屏
        return false;
    }

    /**
     * 是否禁止旋转
     *
     * @return
     */
    protected boolean isAllowScreenRoate() {
        return true;
    }

    /**
     * 是否是沉浸式状态栏,需要在布局文件中加个paddingTop高度
     *
     * @return true  默认值,是沉浸式
     */
    protected boolean isSetStatusBar() {
        return true;
    }


    /**
     * 子类activity的初始化方法
     */
    protected abstract void init();


    @Override
    protected void onStart() {
        super.onStart();
        L.i(TAG, "--->onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.i(TAG, "--->onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L.i(TAG, "--->onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.i(TAG, "--->onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.i(TAG, "--->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.i(TAG, "--->onDestroy()");
        /**
         * 解除View和的Presenter绑定mPresenter.dettachView()
         */
//        if (mPresenter != null)
//            mPresenter.dettachView();
        /*
        将activity从Stack中移除
         */
        //  ActivityStackManager.getActivityStackManager().popActivity(this);
        //  AppClient.mRefWatcher.watch(this);
    }


    /**
     * 跳转Activity,并finish当前Activity
     *
     * @param activity 当前Activity
     * @param cls      目标Activity
     */
    public static void startActivity(Activity activity, Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        // 得到当前Activity下的所有Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断是否为空
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                // 判断是否为我们能够处理的Fragment类型
                if (fragment instanceof BaseFragment && fragment.isVisible()) {
                    // 判断是否拦截了返回按钮
                    if (((BaseFragment) fragment).onBackPressed()) {
                        // 如果有直接Return
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
    }


    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStateBarHeight() {
        int statusBarHeight1 = -1;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        L.e("-------", "状态栏-方法1:" + statusBarHeight1);
        return statusBarHeight1;
    }

}
