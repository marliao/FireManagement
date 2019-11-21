package com.firemanagement.activities.Admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.db.DutyOfficer;
import com.firemanagement.utils.T;
import com.firemanagement.view.CustomTitleBar;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

//执勤人员信息
public class OnDutyPersonActivity extends BaseActivity {
    private CustomTitleBar title_on_duty;
    private ListView lv_on_duy_item;
    private MyAdapter myAdapter;
    private BmobQuery<DutyOfficer> query;
    private boolean isadmin = true;


    @Override
    protected int getLayout() {
        return R.layout.activity_onduty_person;
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void init() {
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        title_on_duty.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                if(isadmin){
                    ShowAddDialog();
                }else {
                    T.showShort("您没有新增执勤人员的权限");
                }


            }
        });
    }

    private void ShowAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OnDutyPersonActivity.this);
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(OnDutyPersonActivity.this, R.layout.dialog_onduty, null);
        EditText tvName;
        Spinner tvSex;
        EditText tvAge;
        EditText tvZw;
        EditText tvTel;
        EditText tvEmail;
        Button btnDelete;
        Button btnUpdate;
        tvName = (EditText) inflate.findViewById(R.id.tv_name);
        tvSex = (Spinner) inflate.findViewById(R.id.tv_sex);
        tvAge = (EditText) inflate.findViewById(R.id.tv_age);
        tvZw = (EditText) inflate.findViewById(R.id.tv_zw);
        tvTel = (EditText) inflate.findViewById(R.id.tv_tel);
        tvEmail = (EditText) inflate.findViewById(R.id.tv_email);
        btnDelete = (Button) inflate.findViewById(R.id.btn_delete);
        btnUpdate = (Button) inflate.findViewById(R.id.btn_update);
        alertDialog.setView(inflate);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        tvSex.setAdapter(new ArrayAdapter<String>(OnDutyPersonActivity.this, android.R.layout.simple_list_item_1, new String[]{"男", "女"}));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = tvName.getText().toString();
                String s2 = tvSex.getSelectedItem().toString();
                String s3 = tvAge.getText().toString();
                String s4 = tvZw.getText().toString();
                String s5 = tvTel.getText().toString();
                String s6 = tvEmail.getText().toString();
                boolean sumbit = Sumbit(s1, s2, s3, s4, s5, s6);
                if (sumbit) {
                    DutyOfficer dutyOfficer = new DutyOfficer();
                    dutyOfficer.setName(s1);
                    dutyOfficer.setSex(s2);
                    dutyOfficer.setAge(s3);
                    dutyOfficer.setPosition(s4);
                    dutyOfficer.setTelnumber(s5);
                    dutyOfficer.setEmail(s6);
                    dutyOfficer.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            T.showShort("保存成功");
                            initData();
                            alertDialog.dismiss();
                        }
                    });
                } else {
                    T.showShort("请填写完信息");
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private boolean Sumbit(String s1, String s2, String s3, String s4, String s5, String s6) {
        if (TextUtils.isEmpty(s1))
            return false;
        if (TextUtils.isEmpty(s2))
            return false;
        if (TextUtils.isEmpty(s3))
            return false;
        if (TextUtils.isEmpty(s4))
            return false;
        if (TextUtils.isEmpty(s5))
            return false;
        if (TextUtils.isEmpty(s6))
            return false;
        return true;
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null)
            isadmin = intent.getBooleanExtra("admin", true);
        if (query == null) {
            query = new BmobQuery<>();
        }
        query.findObjects(new FindListener<DutyOfficer>() {
            @Override
            public void done(List<DutyOfficer> list, BmobException e) {
                if (list != null) {
                    SetView(list);
                } else {
                    if (myAdapter != null)
                        myAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void initView() {
        title_on_duty = (CustomTitleBar) findViewById(R.id.title_on_duty);
        lv_on_duy_item = (ListView) findViewById(R.id.lv_on_duy_item);
    }

    private void SetView(List<DutyOfficer> list) {
        myAdapter = new MyAdapter(list);
        lv_on_duy_item.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter {
        private List<DutyOfficer> list;

        public MyAdapter(List<DutyOfficer> list) {
            this.list = list;
        }

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
            DutyOfficer dutyOfficer = list.get(position);
            View inflate = View.inflate(getApplicationContext(), R.layout.activity_onduty_person_item, null);
            ViewHolder viewHolder = new ViewHolder(inflate);
            viewHolder.tv_delete.setText(Html.fromHtml("<u color='#FF1200'>删除</u>"));
            viewHolder.tv_modify.setText(Html.fromHtml("<u color='#FF1200'>修改</u>"));
            viewHolder.tv_user_name.setText("姓名：" + dutyOfficer.getName());
            viewHolder.tv_user_zw.setText(dutyOfficer.getPosition());
            viewHolder.tv_number.setText(position + 1 + "");
            viewHolder.tv_user_sex.setText("性别：" + dutyOfficer.getSex());
            viewHolder.tv_tel.setText(dutyOfficer.getTelnumber());
            viewHolder.tv_age.setText(dutyOfficer.getAge() + "岁");
            viewHolder.tv_user_serial_number.setText("编号：" + dutyOfficer.getObjectId().substring(0, dutyOfficer.getObjectId().length() - 1));
            viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isadmin){
                        dutyOfficer.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                T.showShort("删除成功");
                                initData();
                            }
                        });
                    }else {
                        T.showShort("您不是管理员，无法删除");
                    }

                }
            });
            viewHolder.tv_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isadmin){
                    ShowUpdateDialog(dutyOfficer);
                    }else {
                        T.showShort("您不是管理员，无法修改");
                    }
                }
            });
            return inflate;
        }

        public class ViewHolder {
            public View rootView;
            public TextView tv_number;
            public TextView tv_user_name;
            public TextView tv_user_sex;
            public TextView tv_user_zw;
            public TextView tv_modify;
            public TextView tv_delete;
            public TextView tv_age;
            public TextView tv_tel;
            public TextView tv_user_serial_number;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tv_number = (TextView) rootView.findViewById(R.id.tv_number);
                this.tv_user_name = (TextView) rootView.findViewById(R.id.tv_user_name);
                this.tv_user_sex = (TextView) rootView.findViewById(R.id.tv_user_sex);
                this.tv_user_zw = (TextView) rootView.findViewById(R.id.tv_user_zw);
                this.tv_modify = (TextView) rootView.findViewById(R.id.tv_modify);
                this.tv_delete = (TextView) rootView.findViewById(R.id.tv_delete);
                this.tv_age = (TextView) rootView.findViewById(R.id.tv_user_age);
                this.tv_tel = (TextView) rootView.findViewById(R.id.tv_user_tel);
                this.tv_user_serial_number = (TextView) rootView.findViewById(R.id.tv_user_serial_number);
            }

        }
    }

    private void ShowUpdateDialog(DutyOfficer dutyOfficer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OnDutyPersonActivity.this);
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(OnDutyPersonActivity.this, R.layout.dialog_onduty, null);
        EditText tvName;
        Spinner tvSex;
        EditText tvAge;
        EditText tvZw;
        EditText tvTel;
        EditText tvEmail;
        Button btnDelete;
        Button btnUpdate;
        tvName = (EditText) inflate.findViewById(R.id.tv_name);
        tvSex = (Spinner) inflate.findViewById(R.id.tv_sex);
        tvAge = (EditText) inflate.findViewById(R.id.tv_age);
        tvZw = (EditText) inflate.findViewById(R.id.tv_zw);
        tvTel = (EditText) inflate.findViewById(R.id.tv_tel);
        tvEmail = (EditText) inflate.findViewById(R.id.tv_email);
        btnDelete = (Button) inflate.findViewById(R.id.btn_delete);
        btnUpdate = (Button) inflate.findViewById(R.id.btn_update);
        alertDialog.setView(inflate);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        tvSex.setAdapter(new ArrayAdapter<String>(OnDutyPersonActivity.this, android.R.layout.simple_list_item_1, new String[]{"男", "女"}));
        if (dutyOfficer.getSex().equals("女")) {
            tvSex.setSelection(1);
        }
        tvName.setText(dutyOfficer.getName());
        tvAge.setText(dutyOfficer.getAge());
        tvZw.setText(dutyOfficer.getPosition());
        tvTel.setText(dutyOfficer.getTelnumber());
        tvEmail.setText(dutyOfficer.getEmail());
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = tvName.getText().toString();
                String s2 = tvSex.getSelectedItem().toString();
                String s3 = tvAge.getText().toString();
                String s4 = tvZw.getText().toString();
                String s5 = tvTel.getText().toString();
                String s6 = tvEmail.getText().toString();
                if (!TextUtils.isEmpty(s1))
                    dutyOfficer.setName(s1);
                if (!TextUtils.isEmpty(s2))
                    dutyOfficer.setSex(s2);
                if (!TextUtils.isEmpty(s3))
                    dutyOfficer.setAge(s3);
                if (!TextUtils.isEmpty(s4))
                    dutyOfficer.setPosition(s4);
                if (!TextUtils.isEmpty(s5))
                    dutyOfficer.setTelnumber(s5);
                if (!TextUtils.isEmpty(s6))
                    dutyOfficer.setEmail(s6);
                dutyOfficer.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        T.showShort("修改成功");
                        initData();
                        alertDialog.dismiss();
                    }
                });
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
