package com.firemanagement.activities.Customer;

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
import com.firemanagement.db.Expert;
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

/**
 * 专家（增删改查）
 */
public class ExpertActivity extends BaseActivity {

    private CustomTitleBar mCtbExpertManager;
    private RecyclerView mRvExpertInquiry;
    private TextView mTvExpertInquiry;
    private AlertDialog addDialog;
    private AlertDialog UpdateDialog;
    private ArrayList<String> subordinateDetachmentlist;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_expert;
    }

    @Override
    protected void initLayout() {
        mCtbExpertManager = (CustomTitleBar) findViewById(R.id.ctb_expert_manager);
        mRvExpertInquiry = (RecyclerView) findViewById(R.id.rv_expert_inquiry);
        mTvExpertInquiry = (TextView) findViewById(R.id.tv_expert_inquiry);
    }
    @Override
    protected void init() {
        initlistener();
        getAllExpert();
    }

    private void getAllExpert() {
        BmobQuery<Expert> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<Expert>() {
            @Override
            public void done(List<Expert> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        mRvExpertInquiry.setVisibility(View.GONE);
                        mTvExpertInquiry.setVisibility(View.VISIBLE);
                    } else {
                        mTvExpertInquiry.setVisibility(View.GONE);
                        mRvExpertInquiry.setVisibility(View.VISIBLE);
                    }
                    initData(list);
                } else {
                    Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(ExpertActivity.this, e.getErrorCode());
                }
            }
        });
    }

    private void initData(List<Expert> expertList) {
        mRvExpertInquiry.setHasFixedSize(true);
        mRvExpertInquiry.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        mRvExpertInquiry.setAdapter(myAdapter);
        myAdapter.add(expertList);
    }

    public class MyAdapter extends RecyclerAdapter<Expert> {
        @Override
        protected int getItemViewType(int position, Expert expert) {
            return R.layout.item_expert_manager;
        }

        @Override
        protected ViewHolder<Expert> onCreateViewHolder(View root, int viewType) {
            return new MyAdapter.MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<Expert> {
            public TextView mTvPosterStatus;
            public TextView mTvUserName;
            public TextView mTvSex;
            public TextView mTvDuty;
            public TextView mTvPost;
            public TextView mTvPhone;
            public TextView mTvSubordinateDetachment;
            private Button mBtnDelete;
            private Button mBtnUpdate;


            public MyViewHolder(View itemView) {
                super(itemView);
                this.mTvPosterStatus = (TextView) itemView.findViewById(R.id.tv_poster_status);
                this.mTvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
                this.mTvSex = (TextView) itemView.findViewById(R.id.tv_sex);
                this.mTvDuty = (TextView) itemView.findViewById(R.id.tv_duty);
                this.mTvPost = (TextView) itemView.findViewById(R.id.tv_post);
                this.mTvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
                this.mTvSubordinateDetachment = (TextView) itemView.findViewById(R.id.tv_subordinate_detachment);
                this.mBtnDelete = mBtnDelete = (Button) itemView.findViewById(R.id.btn_delete);
                this.mBtnUpdate = mBtnUpdate = (Button) itemView.findViewById(R.id.btn_update);
            }

            @Override
            protected void onBind(Expert expert, int position) {
                mTvUserName.setText(expert.getName());
                mTvPhone.setText(expert.getPhone());
                if (expert.getSex() == 1) {
                    mTvSex.setText("男");
                } else if (expert.getSex() == 0) {
                    mTvSex.setText("女");
                } else {
                    mTvSex.setText("性别不明");
                }
                mTvDuty.setText(expert.getPosition());
                mTvPost.setText(expert.getPost());
                mTvSubordinateDetachment.setText(expert.getSubordDetachment());
                if (expert.getPostStatus() == 1) {
                    mTvPosterStatus.setText("在位（在岗）");
                } else {
                    if (expert.getPersonnelStatus().equals("无")) {
                        mTvPosterStatus.setText("不在位");
                    } else {
                        mTvPosterStatus.setText(expert.getPersonnelStatus());
                    }
                }

                //删除
                mBtnDelete.setOnClickListener(v -> {
                    showDeleteDialog(expert.getObjectId());
                });
                //修改
                mBtnUpdate.setOnClickListener(v -> {
                    showUpdateDialog(expert);
                });

            }
        }

    }

    private void showUpdateDialog(Expert expert) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        UpdateDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_expert_manager, null);
        EditText mEtUsername;
        Spinner mSpinnerSex;
        EditText mEtPhone;
        EditText mEtPosition;
        EditText mEtPost;
        Spinner mSpinnerReign;
        EditText mEtCase;
        EditText mEtExpertField;
        Spinner mSpinnerSubordinateDetachment;
        Button mBtnCancel;
        Button mBtnDetermine;

        mEtUsername = (EditText) view.findViewById(R.id.et_username);
        mSpinnerSex = (Spinner) view.findViewById(R.id.spinner_sex);
        mEtPhone = (EditText) view.findViewById(R.id.et_phone);
        mEtPosition = (EditText) view.findViewById(R.id.et_position);
        mEtPost = (EditText) view.findViewById(R.id.et_post);
        mSpinnerReign = (Spinner) view.findViewById(R.id.spinner_reign);
        mEtCase = (EditText) view.findViewById(R.id.et_case);
        mEtExpertField = (EditText) view.findViewById(R.id.et_expert_field);
        mSpinnerSubordinateDetachment = (Spinner) view.findViewById(R.id.spinner_subordinate_detachment);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);

        if (expert.getPostStatus() == 1) {
            mSpinnerReign.setSelection(0);
            mEtCase.setEnabled(false);
        } else {
            mSpinnerReign.setSelection(1);
            mEtCase.setEnabled(true);
            mEtCase.setText(expert.getPersonnelStatus());
        }
        if (expert.getSex() == 1) {
            mSpinnerSex.setSelection(0);
        } else if (expert.getSex() == 0) {
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
            if (subordinateDetachmentlist.get(i).equals(expert.getSubordDetachment())) {
                mSpinnerSubordinateDetachment.setSelection(i);
            }
        }
        mEtUsername.setText(expert.getName());
        mEtPhone.setText(expert.getPhone());
        mEtPosition.setText(expert.getPosition());
        mEtPost.setText(expert.getPost());
        mEtExpertField.setText(expert.getExpertField());
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

        UpdateDialog.setView(view);
        UpdateDialog.setCanceledOnTouchOutside(false);
        UpdateDialog.show();

        mBtnCancel.setOnClickListener(v -> {
            UpdateDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            boolean b = checkParams(
                    mEtUsername.getText().toString().trim(),
                    (String) mSpinnerSex.getSelectedItem(),
                    mEtPhone.getText().toString().trim(),
                    mEtPosition.getText().toString().trim(),
                    mEtPost.getText().toString().trim(),
                    (String) mSpinnerReign.getSelectedItem(),
                    mEtCase.getText().toString().trim(),
                    mEtExpertField.getText().toString().trim(),
                    (String) mSpinnerSubordinateDetachment.getSelectedItem()
            );
            if (b) {
                updateExpert(
                        expert.getObjectId(),
                        mEtUsername.getText().toString().trim(),
                        (String) mSpinnerSex.getSelectedItem(),
                        mEtPhone.getText().toString().trim(),
                        mEtPosition.getText().toString().trim(),
                        mEtPost.getText().toString().trim(),
                        (String) mSpinnerReign.getSelectedItem(),
                        mEtCase.getText().toString().trim(),
                        mEtExpertField.getText().toString().trim(),
                        (String) mSpinnerSubordinateDetachment.getSelectedItem()
                );
            }
        });
    }

    /**
     * 修改专家信息
     *
     * @param objectId
     * @param username
     * @param sex
     * @param phone
     * @param position
     * @param post
     * @param reign
     * @param reigncase
     * @param expertField
     * @param subordinateDetachment
     */
    private void updateExpert(String objectId, String username, String sex, String phone, String position, String post, String reign, String reigncase, String expertField, String subordinateDetachment) {
        Expert expert = new Expert();
        expert.setName(username);
        if (sex.equals("男")) {
            expert.setSex(1);
        } else if (sex.equals("女")) {
            expert.setSex(0);
        } else {
            expert.setSex(2);
        }
        expert.setPhone(phone);
        expert.setPosition(position);
        expert.setPost(post);
        if (reign.equals("在位")) {
            expert.setPersonnelStatus("在位（在岗）");
            expert.setPostStatus(1);
        } else {
            expert.setPersonnelStatus(reigncase);
            expert.setPostStatus(0);
        }
        expert.setExpertField(expertField);
        expert.setSubordDetachment(subordinateDetachment);
        expert.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    T.showShort("修改成功");
                    getAllExpert();
                } else {
                    Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(ExpertActivity.this, e.getErrorCode());
                }
                UpdateDialog.dismiss();
            }
        });
    }

    /**
     * 删除专家信息
     *
     * @param objectId
     */
    private void showDeleteDialog(String objectId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要删除该专家吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Expert expert = new Expert();
                expert.delete(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            T.showShort("删除成功");
                            getAllExpert();
                        } else {
                            Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                            ErrorRec.errorRec(ExpertActivity.this, e.getErrorCode());
                        }
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

    private void initlistener() {
        mCtbExpertManager.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                showAddExpertDialog();
            }
        });
    }

    private void showAddExpertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        addDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_expert_manager, null);
        EditText mEtUsername;
        Spinner mSpinnerSex;
        EditText mEtPhone;
        EditText mEtPosition;
        EditText mEtPost;
        Spinner mSpinnerReign;
        EditText mEtCase;
        EditText mEtExpertField;
        Spinner mSpinnerSubordinateDetachment;
        Button mBtnCancel;
        Button mBtnDetermine;

        mEtUsername = (EditText) view.findViewById(R.id.et_username);
        mSpinnerSex = (Spinner) view.findViewById(R.id.spinner_sex);
        mEtPhone = (EditText) view.findViewById(R.id.et_phone);
        mEtPosition = (EditText) view.findViewById(R.id.et_position);
        mEtPost = (EditText) view.findViewById(R.id.et_post);
        mSpinnerReign = (Spinner) view.findViewById(R.id.spinner_reign);
        mEtCase = (EditText) view.findViewById(R.id.et_case);
        mEtExpertField = (EditText) view.findViewById(R.id.et_expert_field);
        mSpinnerSubordinateDetachment = (Spinner) view.findViewById(R.id.spinner_subordinate_detachment);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);

        mSpinnerSex.setSelection(2);
        mSpinnerSubordinateDetachment.setSelection(4);
        mSpinnerReign.setSelection(0);
        String initialStateSelectedItem = (String) mSpinnerReign.getSelectedItem();
        if (initialStateSelectedItem.equals("在位")) {
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

        addDialog.setView(view);
        addDialog.setCanceledOnTouchOutside(false);
        addDialog.show();

        mBtnCancel.setOnClickListener(v -> {
            addDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            //判断数据是否合理
            boolean b = checkParams(
                    mEtUsername.getText().toString().trim(),
                    (String) mSpinnerSex.getSelectedItem(),
                    mEtPhone.getText().toString().trim(),
                    mEtPosition.getText().toString().trim(),
                    mEtPost.getText().toString().trim(),
                    (String) mSpinnerReign.getSelectedItem(),
                    mEtCase.getText().toString().trim(),
                    mEtExpertField.getText().toString().trim(),
                    (String) mSpinnerSubordinateDetachment.getSelectedItem()
            );
            if (b) {
                addExpert(
                        mEtUsername.getText().toString().trim(),
                        (String) mSpinnerSex.getSelectedItem(),
                        mEtPhone.getText().toString().trim(),
                        mEtPosition.getText().toString().trim(),
                        mEtPost.getText().toString().trim(),
                        (String) mSpinnerReign.getSelectedItem(),
                        mEtCase.getText().toString().trim(),
                        mEtExpertField.getText().toString().trim(),
                        (String) mSpinnerSubordinateDetachment.getSelectedItem()
                );
            }
        });

    }

    /**
     * 添加专拣
     *
     * @param username
     * @param sex
     * @param phone
     * @param position
     * @param post
     * @param reign
     * @param reigncase
     * @param expertField
     * @param subordinateDetachment
     */
    private void addExpert(String username, String sex, String phone, String position, String post, String reign, String reigncase, String expertField, String subordinateDetachment) {
        Expert expert = new Expert();
        expert.setName(username);
        if (sex.equals("男")) {
            expert.setSex(1);
        } else if (sex.equals("女")) {
            expert.setSex(0);
        } else {
            expert.setSex(2);
        }
        expert.setPhone(phone);
        expert.setPosition(position);
        expert.setPost(post);
        if (reign.equals("在位")) {
            expert.setPersonnelStatus("在位（在岗）");
            expert.setPostStatus(1);
        } else {
            expert.setPersonnelStatus(reigncase);
            expert.setPostStatus(0);
        }
        expert.setExpertField(expertField);
        expert.setSubordDetachment(subordinateDetachment);
        expert.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    T.showShort("添加成功");
                    getAllExpert();
                } else {
                    Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(ExpertActivity.this, e.getErrorCode());
                }
                addDialog.dismiss();
            }
        });
    }

    /**
     * 检查参数
     *
     * @param username
     * @param sex
     * @param phone
     * @param position
     * @param post
     * @param reign
     * @param reigncase
     * @param expertField
     * @param subordinateDetachment
     * @return
     */
    private boolean checkParams(String username, String sex, String phone, String position, String post, String reign, String reigncase, String expertField, String subordinateDetachment) {
        if (TextUtils.isEmpty(username)) {
            T.showShort("请输入专家姓名");
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            T.showShort("请输入专家联系方式");
            return false;
        }
        if (TextUtils.isEmpty(position)) {
            T.showShort("请输入专家职务");
            return false;
        }
        if (TextUtils.isEmpty(post)) {
            T.showShort("请输入专家岗位");
            return false;
        }
        if (TextUtils.isEmpty(expertField)) {
            T.showShort("请输入专家领域");
            return false;
        }
        if (reign.equals("不在位")) {
            if (TextUtils.isEmpty(reigncase)) {
                T.showShort("请输入专家不在位状况");
                return false;
            }
        }
        if (subordinateDetachment.equals("--请选择--")) {
            T.showShort("请选择专家所属支队");
            return false;
        }
        return true;
    }
}
