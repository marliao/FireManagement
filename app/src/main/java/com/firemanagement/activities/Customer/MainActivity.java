package com.firemanagement.activities.Customer;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.activities.Admin.PersonalCenterActivity;
import com.firemanagement.activities.Admin.UpdatePasswordActivity;
import com.firemanagement.activities.Admin.VehicleEquipmentActivity;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.activities.LoginActivity;
import com.firemanagement.db.Admin;
import com.firemanagement.db.User;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.SpUtils;
import com.firemanagement.utils.StaticClass;
import com.firemanagement.utils.T;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private ImageView ivTop;
    private GridView gvIcon;
    private RelativeLayout rlBg;
    private ListView navigationView;
    private String[] strings;
    private int[] icons;
    private Intent[] intents;
    private ArrayList<String> namelist;
    private ArrayList<Integer> imagelist;


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ivTop = (ImageView) findViewById(R.id.iv_top);
        gvIcon = (GridView) findViewById(R.id.gv_icon);
        rlBg = (RelativeLayout) findViewById(R.id.rl_bg);
        navigationView = (ListView) findViewById(R.id.navigationView);
    }

    @Override
    protected void init() {
        DH();
        InitDate();
        SetView();
        initData();
        initSidebar();
        initListener();
    }

    private void SetView() {
        gvIcon.setAdapter(new MyAdapter());
        gvIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = strings[position];
                Intent intent = intents[position];
                searchCurrentAdmin(title, intent);
            }
        });
    }

    private void searchCurrentAdmin(String title, Intent intent) {
        BmobQuery<Admin> query = new BmobQuery<>();
        query.addWhereEqualTo("UserObjectId", BmobUser.getCurrentUser(User.class).getObjectId());
        query.findObjects(new FindListener<Admin>() {
            @Override
            public void done(List<Admin> list, BmobException e) {
                if (e == null) {
                    Admin admin = list.get(0);
                    if (admin.getOperate() == 0) {
                        if (title.equals("查询管理")) {
                            startActivity(intent);
                        } else {
                            T.showShort("您当前没有操作的权力");
                        }
                    } else if (admin.getOperate() == 1) {
                        startActivity(intent);
                    } else {
                        T.showShort("您当前没有操作的权力");
                    }
                } else {
                    Log.i(TAG, "done: --------------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(MainActivity.this, e.getErrorCode());
                }
            }
        });
    }

    private void InitDate() {
        //Handover DutyManagement StateManagement QueryManagement
        strings = new String[]{
                "交接班",
                "状态管理",
                "查询管理",
                "车辆装备",
                "专家",
                "应急联动单位"
        };
        icons = new int[]{R.drawable.icon1,
                R.drawable.icon3,
                R.drawable.icon,
                R.drawable.clzb,
                R.drawable.zj,
                R.drawable.yjlddw
        };
        intents = new Intent[]{
                new Intent(this, HandoverActivity.class),
                new Intent(this, StateManagementActivity.class),
                new Intent(this, QueryManagementActivity.class),
                new Intent(this, VehicleEquipmentActivity.class),
                new Intent(this, ExpertActivity.class),
                new Intent(this, EmergencyLinkageUnitInquiryActivity.class)
        };
    }

    private void DH() {
        try {
            ivTop.setImageResource(R.drawable.main_title);
            AnimationDrawable drawable = (AnimationDrawable) ivTop.getDrawable();
            drawable.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return strings.length;
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
            View inflate = View.inflate(getApplicationContext(), R.layout.main_gv_item, null);
            ViewHolder viewHolder = new ViewHolder(inflate);
            viewHolder.tv_title.setText(strings[position]);
            viewHolder.iv_icon.setImageResource(icons[position]);
            return inflate;
        }

        public class ViewHolder {
            public View rootView;
            public ImageView iv_icon;
            public TextView tv_title;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.iv_icon = (ImageView) rootView.findViewById(R.id.iv_icon);
                this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            }
        }
    }

    private void initListener() {
        navigationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = namelist.get(position);
                switch (title) {
                    case "个人中心":
                        startActivity(new Intent(getApplicationContext(), PersonalCenterActivity.class));
                        break;
                    case "修改密码":
                        startActivity(new Intent(getApplicationContext(), UpdatePasswordActivity.class));
                        break;
                    case "设置":
                        startActivity(new Intent(getApplicationContext(), SetingActivity.class));
                        finish();
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
        MyAdapter1 myAdapter = new MyAdapter1();
        navigationView.setAdapter(myAdapter);
    }

    public class MyAdapter1 extends BaseAdapter {


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
            ViewHolder viewHolder;
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

    private void initData() {
        namelist = new ArrayList<String>();
        namelist.add("个人中心");
        namelist.add("修改密码");
        namelist.add("设置");
        namelist.add("退出");
        imagelist = new ArrayList<Integer>();
        imagelist.add(R.drawable.admin_person_center);
        imagelist.add(R.drawable.update_password);
        imagelist.add(R.drawable.add_admin);
        imagelist.add(R.drawable.exit);
    }
}
