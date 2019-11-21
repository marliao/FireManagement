package com.firemanagement.activities.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.db.Admin;
import com.firemanagement.db.User;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.T;
import com.firemanagement.view.CustomTitleBar;
import com.firemanagement.view.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class PersonManagerActivity extends BaseActivity {
    private CustomTitleBar mCtbPersonalManager;
    private RecyclerView mRvPersonalManager;
    private AlertDialog addUesrDialog;
    private List<Admin> adminList;
    private List<User> userList;
    private AlertDialog updateDialog;
    private ArrayList<String> subordinateDetachmentlist;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_person_manager;
    }

    @Override
    protected void initLayout() {
        mCtbPersonalManager = (CustomTitleBar) findViewById(R.id.ctb_personal_manager);
        mRvPersonalManager = (RecyclerView) findViewById(R.id.rv_personal_manager);
    }

    @Override
    protected void init() {
        initListener();
        initData();
    }

    private void initData() {
        adminList = new ArrayList<>();
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.setLimit(500);
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    userList = list;
                    //根据objectId查找用户详细数据
                    for (User user : list) {
                        Log.i(TAG, "done: ------------------"+user.getUsername());
                        searchAdmin(user.getObjectId(), list.size());
                    }
                } else {
                    Log.i(TAG, "done: -----------------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(PersonManagerActivity.this, e.getErrorCode());
                }
            }
        });
    }

    private void searchAdmin(String objectId, int size) {
        BmobQuery<Admin> adminBmobQuery = new BmobQuery<>();
        adminBmobQuery.addWhereEqualTo("UserObjectId", objectId);
        adminBmobQuery.findObjects(new FindListener<Admin>() {
            @Override
            public void done(List<Admin> list, BmobException e) {
                if (e == null) {
                    for (Admin admin : list) {
                        if (admin.getAccountStatus() == 1) {
                            adminList.add(admin);
                        }
                    }
                    initAdapter();
                } else {
                    Log.i(TAG, "done: -----------------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(PersonManagerActivity.this, e.getErrorCode());
                }
            }
        });
    }

    private void initAdapter() {
        mRvPersonalManager.setHasFixedSize(true);
        mRvPersonalManager.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        mRvPersonalManager.setAdapter(myAdapter);
        myAdapter.add(adminList);
    }

    public class MyAdapter extends RecyclerAdapter<Admin> {

        @Override
        protected int getItemViewType(int position, Admin admin) {
            return R.layout.item_person_manager;
        }

        @Override
        protected ViewHolder<Admin> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<Admin> {
            public TextView mTvPosterStatus;
            public TextView mTvUserName;
            public TextView mTvSex;
            public TextView mTvDuty;
            public TextView mTvPhone;
            public TextView mTvSubordinateDetachment;
            public Button mBtnDelete;
            public Button mBtnUpdate;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.mTvPosterStatus = (TextView) itemView.findViewById(R.id.tv_poster_status);
                this.mTvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
                this.mTvSex = (TextView) itemView.findViewById(R.id.tv_sex);
                this.mTvDuty = (TextView) itemView.findViewById(R.id.tv_duty);
                this.mTvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
                this.mTvSubordinateDetachment = (TextView) itemView.findViewById(R.id.tv_subordinate_detachment);
                this.mBtnDelete = (Button) itemView.findViewById(R.id.btn_delete);
                this.mBtnUpdate = (Button) itemView.findViewById(R.id.btn_update);
            }

            @Override
            protected void onBind(Admin admin, int position) {
                mTvUserName.setText(admin.getUsername());
                mTvPhone.setText(admin.getPhone());
                if (admin.getSex() == 1) {
                    mTvSex.setText("男");
                } else if (admin.getSex() == 0) {
                    mTvSex.setText("女");
                } else {
                    mTvSex.setText("性别不明");
                }
                mTvDuty.setText(admin.getPosition());
                mTvSubordinateDetachment.setText(admin.getSubordDetachment());
                if (admin.getPostStatus() == 1) {
                    mTvPosterStatus.setText("在位（在岗）");
                } else {
                    if (admin.getPersonnelStatus().equals("无")) {
                        mTvPosterStatus.setText("不在位");
                    } else {
                        mTvPosterStatus.setText(admin.getPersonnelStatus());
                    }
                }
                //修改
                mBtnUpdate.setOnClickListener(v -> {
                    for (User user : userList) {
                        if (admin.getUserObjectId().equals(user.getObjectId())) {
                            showUpdateDialog(user, admin);
                        }
                    }
                });
                //删除
                mBtnDelete.setOnClickListener(v -> {
                    for (User user : userList) {
                        if (admin.getUserObjectId().equals(user.getObjectId())) {
                            showDeleteDialog(user.getObjectId());
                        }
                    }
                });
            }
        }

    }

    private void showDeleteDialog(String objectId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定删除该用户？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BmobQuery<Admin> query = new BmobQuery<>();
                query.addWhereEqualTo("UserObjectId", objectId);
                query.findObjects(new FindListener<Admin>() {
                    @Override
                    public void done(List<Admin> list, BmobException e) {
                        if (e == null) {
                            String id = list.get(0).getObjectId();
                            Admin admin = new Admin();
                            admin.setAccountStatus(0);
                            admin.update(id, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        T.showShort("删除成功");
                                        initData();
                                    } else {
                                        T.showShort("删除失败");
                                        ErrorRec.errorRec(PersonManagerActivity.this, e.getErrorCode());
                                        Log.i(TAG, "done: -------------" + e.getErrorCode() + "  " + e.getMessage());
                                    }
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            ErrorRec.errorRec(PersonManagerActivity.this, e.getErrorCode());
                            Log.i(TAG, "done: -------------" + e.getErrorCode() + "  " + e.getMessage());
                        }
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

    private void showUpdateDialog(User user, Admin admin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        updateDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_person_mananger, null);
        EditText mEtUsername;
        Spinner mSpinnerSex;
        EditText mEtAge;
        EditText mEtPhone;
        EditText mEtPosition;
        Spinner mSpinnerSubordinateDetachment;
        Spinner mSpinnerReign;
        EditText mEtCase;
        Button mBtnCancel;
        Button mBtnDetermine;

        mEtUsername = (EditText) view.findViewById(R.id.et_username);
        mSpinnerSex = (Spinner) view.findViewById(R.id.spinner_sex);
        mEtAge = (EditText) view.findViewById(R.id.et_age);
        mEtPhone = (EditText) view.findViewById(R.id.et_phone);
        mEtPosition = (EditText) view.findViewById(R.id.et_position);
        mSpinnerSubordinateDetachment = (Spinner) view.findViewById(R.id.spinner_subordinate_detachment);
        mSpinnerReign = (Spinner) view.findViewById(R.id.spinner_reign);
        mEtCase = (EditText) view.findViewById(R.id.et_case);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);

        String selectedItem = (String) mSpinnerReign.getSelectedItem();
        if (admin.getPostStatus() == 1) {
            mSpinnerReign.setSelection(0);
            mEtCase.setEnabled(false);
        } else {
            mSpinnerReign.setSelection(1);
            mEtCase.setText(admin.getPersonnelStatus());
            mEtCase.setEnabled(true);
        }
        mSpinnerReign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) mSpinnerReign.getSelectedItem();
                if (item.equals("在位")) {
                    mEtCase.setEnabled(false);
                } else {
                    mEtCase.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //展示数据
        if (admin.getSex() == 1) {
            mSpinnerSex.setSelection(0);
        } else if (admin.getSex() == 0) {
            mSpinnerSex.setSelection(1);
        } else {
            mSpinnerSex.setSelection(2);
        }
        subordinateDetachmentlist = new ArrayList<>();
        subordinateDetachmentlist.add("昌吉地区公安消防支队");
        subordinateDetachmentlist.add("昌吉消防支队奇台县大队");
        subordinateDetachmentlist.add("昌吉消防支队阜康市大队");
        subordinateDetachmentlist.add("昌吉消防支队淮东油田大队");
        subordinateDetachmentlist.add("--请选择--");
        for (int i = 0; i < subordinateDetachmentlist.size(); i++) {
            if (subordinateDetachmentlist.get(i).equals(admin.getSubordDetachment())) {
                mSpinnerSubordinateDetachment.setSelection(i);
            }
        }
        mEtUsername.setText(admin.getUsername());
        mEtAge.setText(admin.getAge() + "");
        mEtPhone.setText(admin.getPhone() + "");
        mEtPosition.setText(admin.getPosition() + "");

        updateDialog.setView(view);
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();
        mBtnCancel.setOnClickListener(v -> {
            updateDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            boolean b = addUser(
                    mEtUsername.getText().toString().trim(),
                    mEtAge.getText().toString().trim(),
                    (String) mSpinnerSex.getSelectedItem(),
                    mEtPhone.getText().toString().trim(),
                    mEtPosition.getText().toString().trim(),
                    (String) mSpinnerSubordinateDetachment.getSelectedItem(),
                    (String) mSpinnerReign.getSelectedItem(), mEtCase.getText().toString().trim()
            );
            if (b) {
                updateUser(user.getObjectId(), mEtUsername.getText().toString().trim(),
                        mEtAge.getText().toString().trim(),
                        (String) mSpinnerSex.getSelectedItem(),
                        mEtPhone.getText().toString().trim(),
                        mEtPosition.getText().toString().trim(),
                        (String) mSpinnerSubordinateDetachment.getSelectedItem(),
                        (String) mSpinnerSex.getSelectedItem(),
                        mEtCase.getText().toString().trim(),
                        admin.getAdmin(),
                        admin.getOperate()
                );
            }
        });
    }

    private void updateUser(String objectId, String username, String age, String sex, String phone, String position, String subordinateDetachmentSelectedItem, String reign, String reignCase, int isadmin,int optera) {
        BmobQuery<Admin> query = new BmobQuery<>();
        query.addWhereEqualTo("UserObjectId", objectId);
        query.findObjects(new FindListener<Admin>() {
            @Override
            public void done(List<Admin> list, BmobException e) {
                if (e == null) {
                    String objectId = list.get(0).getObjectId();
                    Admin admin = new Admin();
                    admin.setAge(Integer.parseInt(age));
                    if (sex.equals("男")) {
                        admin.setSex(1);
                    } else if (sex.equals("女")) {
                        admin.setSex(0);
                    } else {
                        admin.setSex(2);
                    }
                    if (reign.equals("在位")) {
                        admin.setPersonnelStatus("在位（在岗）");
                        admin.setPostStatus(1);
                    } else {
                        admin.setPersonnelStatus(reignCase);
                        admin.setPostStatus(0);
                    }
                    admin.setPhone(phone);
                    admin.setUsername(username);
                    admin.setAdmin(isadmin);
                    admin.setPosition(position);
                    admin.setSubordDetachment(subordinateDetachmentSelectedItem);
                    admin.setAccountStatus(1);
                    admin.setOperate(optera);
                    admin.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                T.showShort("修改成功");
                                initData();
                            } else {
                                T.showShort("修改失败");
                                ErrorRec.errorRec(PersonManagerActivity.this, e.getErrorCode());
                                Log.i(TAG, "done: ---------------------" + e.getErrorCode() + "  " + e.getMessage());
                            }
                            updateDialog.dismiss();
                        }
                    });
                } else {
                    ErrorRec.errorRec(PersonManagerActivity.this, e.getErrorCode());
                    Log.i(TAG, "done: -------------" + e.getErrorCode() + "  " + e.getMessage());
                }
            }
        });
    }

    private void initListener() {
        mCtbPersonalManager.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                //展示添加用户对话框
                showAddPersonDialog();
            }
        });
    }

    private void showAddPersonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        addUesrDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_person_mananger, null);
        EditText mEtUsername;
        Spinner mSpinnerSex;
        EditText mEtAge;
        EditText mEtPhone;
        EditText mEtPosition;
        Spinner mSpinnerSubordinateDetachment;
        Spinner mSpinnerReign;
        EditText mEtCase;
        Button mBtnCancel;
        Button mBtnDetermine;


        mEtUsername = (EditText) view.findViewById(R.id.et_username);
        mSpinnerSex = (Spinner) view.findViewById(R.id.spinner_sex);
        mEtAge = (EditText) view.findViewById(R.id.et_age);
        mEtPhone = (EditText) view.findViewById(R.id.et_phone);
        mEtPosition = (EditText) view.findViewById(R.id.et_position);
        mSpinnerSubordinateDetachment = (Spinner) view.findViewById(R.id.spinner_subordinate_detachment);
        mSpinnerReign = (Spinner) view.findViewById(R.id.spinner_reign);
        mEtCase = (EditText) view.findViewById(R.id.et_case);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);

        mSpinnerSex.setSelection(2);
        mSpinnerSubordinateDetachment.setSelection(4);
        mSpinnerReign.setSelection(0);
        String selectedItem = (String) mSpinnerReign.getSelectedItem();
        if (selectedItem.equals("在位")) {
            mEtCase.setEnabled(false);
        } else {
            mEtCase.setEnabled(true);
        }
        mSpinnerReign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) mSpinnerReign.getSelectedItem();
                if (item.equals("在位")) {
                    mEtCase.setEnabled(false);
                } else {
                    mEtCase.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addUesrDialog.setView(view);
        addUesrDialog.setCanceledOnTouchOutside(false);
        addUesrDialog.show();
        mBtnCancel.setOnClickListener(v -> {
            addUesrDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            boolean b = addUser(
                    mEtUsername.getText().toString().trim(),
                    mEtAge.getText().toString().trim(),
                    (String) mSpinnerSex.getSelectedItem(),
                    mEtPhone.getText().toString().trim(),
                    mEtPosition.getText().toString().trim(),
                    (String) mSpinnerSubordinateDetachment.getSelectedItem(),
                    (String) mSpinnerReign.getSelectedItem(), mEtCase.getText().toString().trim()
            );
            if (b) {
                saveUser(mEtUsername.getText().toString().trim(),
                        mEtAge.getText().toString().trim(),
                        (String) mSpinnerSex.getSelectedItem(),
                        mEtPhone.getText().toString().trim(),
                        mEtPosition.getText().toString().trim(),
                        (String) mSpinnerSubordinateDetachment.getSelectedItem(),
                        (String) mSpinnerReign.getSelectedItem(),
                        mEtCase.getText().toString().trim());
            }
        });

    }

    private boolean addUser(String username, String age, String sex, String phone, String position, String subordinateDetachmentSelectedItem, String reign, String reignCase) {
        if (TextUtils.isEmpty(username)) {
            T.showShort("请输入用户名");
            return false;
        }
        if (TextUtils.isEmpty(age)) {
            T.showShort("请输入年龄");
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            T.showShort("请输入联系方式");
            return false;
        }
        if (TextUtils.isEmpty(position)) {
            T.showShort("请输入职务");
            return false;
        }
        if (sex.equals("--请选择--")) {
            T.showShort("请选择性别");
            return false;
        }
        if (subordinateDetachmentSelectedItem.equals("--请选择--")) {
            T.showShort("请选择所属支队");
            return false;
        }
        if (reign.equals("不在位")) {
            if (TextUtils.isEmpty(reignCase)) {
                T.showShort("请输入用户状态");
                return false;
            }
        }

        return true;
    }

    private void saveUser(String username, String age, String sex, String phone, String position, String subordinateDetachmentSelectedItem, String reign, String reignCase) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("123456");
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                Admin admin = new Admin();
                admin.setUserObjectId(user.getObjectId());
                admin.setPhone(phone);
                admin.setAccountStatus(1);
                admin.setSubordDetachment(subordinateDetachmentSelectedItem);
                admin.setPosition(position);
                admin.setAdmin(0);
                admin.setUsername(username);
                admin.setLoginUserName(username);
                admin.setAge(Integer.parseInt(age));
                if (sex.equals("男")) {
                    admin.setSex(1);
                } else if (sex.equals("女")) {
                    admin.setSex(0);
                } else {
                    admin.setSex(2);
                }
                if (reign.equals("在位")) {
                    admin.setPersonnelStatus("在位（在岗）");
                    admin.setPostStatus(1);
                } else {
                    admin.setPersonnelStatus(reignCase);
                    admin.setPostStatus(0);
                }
                admin.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            T.showShort("添加用户成功");
                            initData();
                            addUesrDialog.dismiss();
                        } else {
                            T.showShort("添加用户失败");
                            ErrorRec.errorRec(PersonManagerActivity.this, e.getErrorCode());
                            Log.i(TAG, "done: -------------" + e.getErrorCode() + "  " + e.getMessage());
                            addUesrDialog.dismiss();
                        }
                    }
                });
            }
        });
    }
}
