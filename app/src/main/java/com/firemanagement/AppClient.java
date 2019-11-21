package com.firemanagement;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

public class AppClient extends Application {

    public static Context mContext;
    public static boolean statuManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Bmob.initialize(this, "0d3e03ddc73139f5236caf644aa5fed1");
    }

}
