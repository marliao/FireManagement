package com.firemanagement.frags.admin;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firemanagement.R;
import com.firemanagement.activities.Admin.DutySituationLogActivity;
import com.firemanagement.activities.Admin.OnDutyPersonActivity;
import com.firemanagement.activities.Admin.PersonManagerActivity;
import com.firemanagement.activities.Admin.VehicleEquipmentActivity;
import com.firemanagement.frags.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerMainFragment extends BaseFragment {

    private ImageView mIvManagerTitle;
    private GridView mGvManagerMenu;
    private List<String> valuelist;
    private List<Integer> imagelist;

    @Override
    protected int getLayout() {
        return R.layout.fragment_manager_main;
    }

    @Override
    protected void initView(View view) {
        mIvManagerTitle = (ImageView) view.findViewById(R.id.iv_manager_title);
        mGvManagerMenu = (GridView) view.findViewById(R.id.gv_manager_menu);
    }

    @Override
    protected void init() {
        initUI();
        initData();
        MyAdapter myAdapter = new MyAdapter();
        mGvManagerMenu.setAdapter(myAdapter);
        mGvManagerMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = valuelist.get(position);
                switch (title) {
                    case "用户管理":
                        startActivity(new Intent(getActivity(), PersonManagerActivity.class));
                        break;
                    case "执勤人员管理":
                        startActivity(new Intent(getActivity(), OnDutyPersonActivity.class));
                        break;
                    case "车辆装备管理":
                        startActivity(new Intent(getActivity(), VehicleEquipmentActivity.class));
                        break;
                    case "交接班记录":
                        startActivity(new Intent(getActivity(), DutySituationLogActivity.class));
                        break;
                }
            }
        });
    }

    public class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return valuelist.size();
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
                convertView = View.inflate(getActivity(), R.layout.item_manager_menu, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.iv_image.setImageResource(imagelist.get(position));
            viewHolder.tv_value.setText(valuelist.get(position));
            return convertView;
        }

        public class ViewHolder {
            public View rootView;
            public ImageView iv_image;
            public TextView tv_value;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.iv_image = (ImageView) rootView.findViewById(R.id.iv_image);
                this.tv_value = (TextView) rootView.findViewById(R.id.tv_value);
            }

        }
    }

    private void initData() {
        valuelist = new ArrayList<>();
        valuelist.add("执勤人员管理");
        valuelist.add("车辆装备管理");
        valuelist.add("交接班记录");
        valuelist.add("用户管理");
        imagelist = new ArrayList<>();
        imagelist.add(R.drawable.manager_duty_strength);
        imagelist.add(R.drawable.manager_zhuangbei_make);
        imagelist.add(R.drawable.manager_jiaoban_jilu);
        imagelist.add(R.drawable
                .manager_user_manager);
    }

    private void initUI() {
        AnimationDrawable drawable = (AnimationDrawable) mIvManagerTitle.getDrawable();
        drawable.start();
    }
}
