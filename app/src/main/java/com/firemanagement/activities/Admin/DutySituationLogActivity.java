package com.firemanagement.activities.Admin;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.datepicker.CustomDatePicker;
import com.firemanagement.datepicker.DateFormatUtils;
import com.firemanagement.db.Admin;
import com.firemanagement.db.DutySituationLog;
import com.firemanagement.db.User;
import com.firemanagement.utils.ImageUtils;
import com.firemanagement.view.CustomTitleBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

//交接班记录

public class DutySituationLogActivity extends BaseActivity {
    private ImageView iv_left;
    private ImageView iv_right;
    private ListView lv_log;
    private CustomTitleBar tv_log_title;
    private TextView tv_date;
    private String today;
    private String time;
    private ArrayList<DutySituationLog> list;
    private MyAdapter myAdapter;
    private long l;
    private CustomDatePicker mDatePicker;


    @Override
    protected int getLayout() {
        return R.layout.activity_dutysituationlog;
    }

    @Override
    protected void initLayout() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        lv_log = (ListView) findViewById(R.id.lv_log);
        tv_log_title = (CustomTitleBar) findViewById(R.id.tv_log_title);
        tv_date = (TextView) findViewById(R.id.tv_date);
    }

    @Override
    protected void init() {
        inittime();
        initDatePicker();
        initView();
        initListener();
    }

    private void inittime() {
        try {
            today = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
            time = today;
            l = new SimpleDateFormat("yyyy-MM-dd").parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        //前一天
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    l = new SimpleDateFormat("yyyy-MM-dd").parse(time).getTime() - 1000 * 60 * 60 * 24;
                    l -=  1000 * 60 * 60 * 24;
                    time = new SimpleDateFormat("yyyy-MM-dd").format(l);
                    initView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //后一天
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    l = new SimpleDateFormat("yyyy-MM-dd").parse(time).getTime() + 1000 * 60 * 60 * 24;
                    l +=   1000 * 60 * 60 * 24;
                    time = new SimpleDateFormat("yyyy-MM-dd").format(l);
                    initView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //长按删除
        lv_log.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ShowDeleteDiglog(list.get(position));
                return true;
            }
        });
        //左侧标题栏退出
        tv_log_title.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePicker.show(tv_date.getText().toString());
            }
        });
    }

    private void ShowDeleteDiglog(DutySituationLog dutySituationLog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认删除此条记录");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dutySituationLog.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        initView();
                        dialog.dismiss();
                    }
                });

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void SetIv() {
        if (today.equals(time)) {
            iv_right.setImageResource(R.drawable.enter);
            iv_right.setEnabled(false);
        } else {
            iv_right.setImageResource(R.drawable.enter_1);
            iv_right.setEnabled(true);
        }
    }

    private void initView() {
        tv_date.setText(time);
        SetIv();
        initDate();
    }

    private void initDate() {
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        GetAllDutySituationLog();
    }

    private void GetAllDutySituationLog() {
        BmobQuery<DutySituationLog> query = new BmobQuery<>();
        query.findObjects(new FindListener<DutySituationLog>() {
            @Override
            public void done(List<DutySituationLog> dutySituationLogList, BmobException e) {
                if (dutySituationLogList != null) {
                    for (int i = 0; i < dutySituationLogList.size(); i++) {
                        try {
                            long l1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dutySituationLogList.get(i).getAfterGetOffWorkTime()).getTime();
                            long l2 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dutySituationLogList.get(i).getSuccessionTime()).getTime();
                           boolean ishad=true;
                           if(l1>l+60*60*24*1000|l2<l)
                               ishad=false;
                            if (ishad) {
                                list.add(dutySituationLogList.get(i));
                            }
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
                Setup();
            }
        });
    }

    private void Setup() {
        if (myAdapter == null) {
            myAdapter = new MyAdapter();
            lv_log.setAdapter(myAdapter);
        } else {
            myAdapter.notifyDataSetChanged();
        }
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
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
            DutySituationLog dutySituationLog = list.get(position);
            String userObjectId1 = dutySituationLog.getUserObjectId1();
            String userObjectId = dutySituationLog.getUserObjectId();
            View inflate = View.inflate(getApplicationContext(), R.layout.dutysituationlog_item, null);
            ViewHolder viewHolder = new ViewHolder(inflate);
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("M月d日 HH:mm");
                long l = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dutySituationLog.getAfterGetOffWorkTime()).getTime();
                long l1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dutySituationLog.getSuccessionTime()).getTime();
                viewHolder.tv_time.setText(dateFormat.format(l) + " ~ " + dateFormat.format(l1) );

            } catch ( Exception e) {
                e.printStackTrace();
            }

                   BmobQuery<User> userBmobQuery = new BmobQuery<>();
            userBmobQuery.getObject(userObjectId, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if(user!=null){
                        viewHolder.tv_employees.setText("接班人员:          " + user.getUsername());
                    }
                }
            });
            BmobQuery<User> userBmobQuery1 = new BmobQuery<>();
            userBmobQuery1.getObject(userObjectId1, new QueryListener<User>() {
                @Override
                public void done(User user1, BmobException e) {
                    if(user1!=null){
                        viewHolder.tv_duty_Officer.setText("执勤人员:          " + user1.getUsername());
                        BmobQuery<Admin> query = new BmobQuery<>();
                        query.addWhereEqualTo("UserObjectId",user1.getObjectId()).findObjects(new FindListener<Admin>() {
                            @Override
                            public void done(List<Admin> list, BmobException e) {
                                if(list.size()>0){
                                    Admin admin = list.get(0);
                                    if (admin.getAvatar() != null) {
                                        ImageUtils.setCircleBitmap(admin.getAvatar().getUrl(), viewHolder.iv_u_icon);
                                    }else{
                                        ImageUtils.setCircleBitmap(R.drawable.user, viewHolder.iv_u_icon);
                                    }
                                }
                            }
                        });
                    }

                }
            });

//            viewHolder.tv_position_Status.setText("人员情况:          " + dutySituationLog.getOnDutySituation());
            return inflate;
        }

        public class ViewHolder {
            public View rootView;
            public TextView tv_time;
            public TextView tv_duty_Officer;
            public TextView tv_employees;
//            public TextView tv_position_Status;
            public ImageView iv_u_icon;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tv_time = (TextView) rootView.findViewById(R.id.tv_time);
                this.tv_duty_Officer = (TextView) rootView.findViewById(R.id.tv_duty_Officer);
                this.tv_employees = (TextView) rootView.findViewById(R.id.tv_employees);
//                this.tv_position_Status = (TextView) rootView.findViewById(R.id.tv_position_Status);
                this.iv_u_icon = (ImageView) rootView.findViewById(R.id.iv_u_icon);
            }

        }
    }
    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        tv_date.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                time=new SimpleDateFormat("yyyy-MM-dd").format(timestamp);
                l=timestamp;
                initView();
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatePicker.onDestroy();
    }
}
