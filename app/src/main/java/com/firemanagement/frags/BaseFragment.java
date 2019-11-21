package com.firemanagement.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


/**
 * @ClassName BaseFragment
 * @Author AiSHan Feng
 * @Date 2018/12/30 17:31
 * @Version 1.0
 * @Description 所有Fragment的基类
 */
public abstract class BaseFragment extends Fragment {
    /*
     * 上下文对象,即当前依附的Activity
     */
    protected Context mContext;
    protected View mRootView;
    protected View mRoot;

    /**
     * Log标记
     */
    public final String TAG = this.getClass().getSimpleName();

    /**
     * P层对象
     */
    //protected P mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (mRoot == null) {
//            int layId = getLayout();
//            // 初始化当前的跟布局，但是不在创建时就添加到container里边
//            View root = inflater.inflate(layId, container, false);
//            initView(root);
//            mRoot = root;
//        } else {
//            if (mRoot.getParent() != null) {
//                // 把当前Root从其父控件中移除
//                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
//            }
//        }
        int layId = getLayout();
        // 初始化当前的跟布局，但是不在创建时就添加到container里边
        View root = inflater.inflate(layId, container, false);
        initView(root);
        mRoot = root;
        return mRoot;
    }

    /**
     * 获取布局Id
     *
     * @return
     */
    protected abstract int getLayout();

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            /*
        绑定Presenter
         */
//        mPresenter = creatPresenter();
//        if (mPresenter != null)
//            mPresenter.attachView((V) this);
        mRootView = view;
        initView(mRootView);
    }

    /**
     * 创建P层对象
     *
     * @return
     */
//    protected abstract P creatPresenter();
//
//    protected V getIView() {
//        return (V) this;
//    }

    /**
     * 初始化布局
     *
     * @param view
     */
    protected abstract void initView(View view);

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    /**
     * 初始化数据
     */
    protected abstract void init();

    /**
     * 返回按键触发时调用
     *
     * @return 返回True代表我已处理返回逻辑，Activity不用自己finish。
     * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mRootView != null) {
            unbindDrawables(mRootView);
        }
    }




    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mPresenter != null)
//            mPresenter.dettachView();
//        //内存泄漏观察+
//        AppClient.mRefWatcher.watch(this);

        //将Fragment的引用全部置空
//        if (mRoot != null && mRoot.getParent() != null)
//            ((ViewGroup) mRoot.getParent()).removeAllViews();
    }


}
