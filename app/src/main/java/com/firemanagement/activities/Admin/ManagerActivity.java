package com.firemanagement.activities.Admin;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.activities.LoginActivity;
import com.firemanagement.frags.admin.ManagerMainFragment;
import com.firemanagement.utils.SpUtils;
import com.firemanagement.utils.StaticClass;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class ManagerActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private FrameLayout mFragmentContent;
    private RelativeLayout mRlBg;
    private ListView mLvNavigationView;
    private List<String> namelist;
    private List<Integer> imagelist;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_manager;
    }

    @Override
    protected void initLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mFragmentContent = (FrameLayout) findViewById(R.id.fragment_content);
        mRlBg = (RelativeLayout) findViewById(R.id.rl_bg);
        mLvNavigationView = (ListView) findViewById(R.id.lv_navigationView);
    }

    @Override
    protected void init() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content, new ManagerMainFragment()).commit();
        initData();
        initSidebar();
        initListener();
    }

    private void initListener() {
        mLvNavigationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = namelist.get(position);
                switch (title) {
                    case "个人中心":
                        startActivity(new Intent(getApplicationContext(), PersonalCenterActivity.class));
                        break;
                    case "修改权限":
                        startActivity(new Intent(getApplicationContext(), ModifyAdminRightsActivity.class));
                        break;
                    case "修改密码":
                        startActivity(new Intent(getApplicationContext(), UpdatePasswordActivity.class));
                        break;
                    case "退出":
                        SpUtils.remove(AppClient.mContext, StaticClass.isAutoLogin);
                        SpUtils.remove(AppClient.mContext, StaticClass.isRememberPassword);
                        SpUtils.remove(AppClient.mContext, StaticClass.USERNAME);
                        SpUtils.remove(AppClient.mContext, StaticClass.PASSWORD);
                        BmobUser.logOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        break;
                }
            }
        });
    }

    private void initSidebar() {
        MyAdapter myAdapter = new MyAdapter();
        mLvNavigationView.setAdapter(myAdapter);
    }

    public class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return namelist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_slider_manager, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.iiv_image.setImageResource(imagelist.get(position));
            viewHolder.tv_name.setText(namelist.get(position));
            return convertView;
        }

        public class ViewHolder {
            public View rootView;
            public ImageView iiv_image;
            public TextView tv_name;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.iiv_image = (ImageView) rootView.findViewById(R.id.iiv_image);
                this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            }

        }
    }

    private void initData() {
        namelist = new ArrayList<>();
        namelist.add("个人中心");
        namelist.add("修改权限");
        namelist.add("修改密码");
        namelist.add("退出");
        imagelist = new ArrayList<>();
        imagelist.add(R.drawable.admin_person_center);
        imagelist.add(R.drawable.add_admin);
        imagelist.add(R.drawable.update_password);
        imagelist.add(R.drawable.exit);
    }

}
