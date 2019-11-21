package com.firemanagement.activities.Customer;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.view.View;
import android.widget.TextView;


import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.activities.LoginActivity;
import com.firemanagement.utils.PackageCode;
import com.firemanagement.utils.SpUtils;
import com.firemanagement.utils.StaticClass;
import com.firemanagement.view.CustomTitleBar;

import cn.bmob.v3.BmobUser;

public class SetingActivity extends BaseActivity {
    private TextView mTvVersionNumber;
    private TextView mTvFeedback;
    private TextView mTvCheckForUpdates;
    private TextView mTvSoftwareDescription;
    private TextView mTvLogout;
    private TextView mTvSignOut;
    private CustomTitleBar mTvSetingTitle;

    @Override
    protected int getLayout() {
        return R.layout.activity_seting;
    }

    @Override
    protected void initLayout() {
        mTvSetingTitle = (CustomTitleBar) findViewById(R.id.tv_seting_title);
        mTvVersionNumber = (TextView) findViewById(R.id.tv_VersionNumber);
        mTvFeedback = (TextView) findViewById(R.id.tv_feedback);
        mTvCheckForUpdates = (TextView) findViewById(R.id.tv_CheckForUpdates);
        mTvSoftwareDescription = (TextView) findViewById(R.id.tv_SoftwareDescription);
        mTvLogout = (TextView) findViewById(R.id.tv_logout);
        mTvSignOut = (TextView) findViewById(R.id.tv_SignOut);
    }

    @Override
    protected void init() {
        PackageInfo getpackcode = PackageCode.getpackcode();
      //  System.out.println("versionCode:"+getpackcode.versionCode+" versionName:"+getpackcode.versionName);
        mTvVersionNumber.setText("版本号 "+getpackcode.versionName+"."+getpackcode.versionCode);
        mTvSetingTitle.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                startActivity(new Intent(getApplication(), MainActivity.class));
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

        mTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SpUtils.put(AppClient.mContext, StaticClass.USERNAME, "");
//                SpUtils.put(AppClient.mContext, StaticClass.PASSWORD, "");
                SpUtils.remove(AppClient.mContext, StaticClass.isAutoLogin);
                SpUtils.remove(AppClient.mContext, StaticClass.isRememberPassword);
                SpUtils.remove(AppClient.mContext, StaticClass.USERNAME);
                SpUtils.remove(AppClient.mContext, StaticClass.PASSWORD);
                BmobUser.logOut();
                startActivity(new Intent(getApplication(),LoginActivity.class));
                finish();
            }
        });
        mTvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplication(), MainActivity.class));
        finish();
    }
}
