package com.firemanagement.activities.Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.db.EmergencyLU;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.ImageUtils;
import com.firemanagement.utils.T;
import com.firemanagement.view.CustomTitleBar;
import com.firemanagement.view.RecyclerAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * 应急联动单位（增删改查）
 */
public class EmergencyLinkageUnitInquiryActivity extends BaseActivity {


    private CustomTitleBar mCtbEmergencyLincageUnitInquiry;
    private RecyclerView mRvEmergencyLincageUnitInquiry;
    private TextView mTvEmergencyLincageUnitInquiry;
    private AlertDialog addDialog;
    private AlertDialog ImageDialog;
    private String[] filePaths;
    private ImageView mIvImageadd;
    private ImageView mIvImageupdate;
    private boolean isInsertDialog;
    private AlertDialog UpdateDialog;
    private ArrayList<String> subordinateDetachmentlist;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_emergency_linkage_unit;
    }

    @Override
    protected void initLayout() {
        mCtbEmergencyLincageUnitInquiry = (CustomTitleBar) findViewById(R.id.ctb_emergency_lincage_unit_inquiry);
        mRvEmergencyLincageUnitInquiry = (RecyclerView) findViewById(R.id.rv_emergency_lincage_unit_inquiry);
        mTvEmergencyLincageUnitInquiry = (TextView) findViewById(R.id.tv_emergency_lincage_unit_inquiry);
    }

    @Override
    protected void init() {
        initListener();
        getAllEmergencyLinkageUnit();
    }

    private void getAllEmergencyLinkageUnit() {
        BmobQuery<EmergencyLU> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<EmergencyLU>() {
            @Override
            public void done(List<EmergencyLU> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        mRvEmergencyLincageUnitInquiry.setVisibility(View.GONE);
                        mTvEmergencyLincageUnitInquiry.setVisibility(View.VISIBLE);
                    } else {
                        mTvEmergencyLincageUnitInquiry.setVisibility(View.GONE);
                        mRvEmergencyLincageUnitInquiry.setVisibility(View.VISIBLE);
                    }
                    initAdapter(list);
                } else {
                    Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(EmergencyLinkageUnitInquiryActivity.this, e.getErrorCode());
                }
            }
        });
    }

    private void initAdapter(List<EmergencyLU> emergencyLUList) {
        mRvEmergencyLincageUnitInquiry.setHasFixedSize(true);
        mRvEmergencyLincageUnitInquiry.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        mRvEmergencyLincageUnitInquiry.setAdapter(myAdapter);
        myAdapter.add(emergencyLUList);
    }

    public class MyAdapter extends RecyclerAdapter<EmergencyLU> {

        @Override
        protected int getItemViewType(int position, EmergencyLU emergencyLU) {
            return R.layout.item_emergency_linkage_unit_manager;
        }

        @Override
        protected ViewHolder<EmergencyLU> onCreateViewHolder(View root, int viewType) {
            return new MyAdapter.MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<EmergencyLU> {
            public ImageView mIvEquipmentPicture;
            public TextView mTvEquipmentName;
            public TextView mTvEquipmentStatus;
            public TextView mTvEquipmentDetachment;
            private Button mBtnDelete;
            private Button mBtnUpdate;


            public MyViewHolder(View itemView) {
                super(itemView);
                this.mIvEquipmentPicture = (ImageView) itemView.findViewById(R.id.iv_equipment_picture);
                this.mTvEquipmentName = (TextView) itemView.findViewById(R.id.tv_equipment_name);
                this.mTvEquipmentStatus = (TextView) itemView.findViewById(R.id.tv_equipment_status);
                this.mTvEquipmentDetachment = (TextView) itemView.findViewById(R.id.tv_equipment_detachment);
                this.mBtnDelete = mBtnDelete = (Button) itemView.findViewById(R.id.btn_delete);
                this.mBtnUpdate = mBtnUpdate = (Button) itemView.findViewById(R.id.btn_update);
            }

            @Override
            protected void onBind(EmergencyLU emergencyLU, int position) {
                if (emergencyLU.getFile() == null) {
                    mIvEquipmentPicture.setImageResource(R.drawable.yjlddw);
                } else {
                    ImageUtils.setBitmapCenterCrop(emergencyLU.getFile().getUrl(), mIvEquipmentPicture);
                }
                mTvEquipmentName.setText("应急联动单位名称：" + emergencyLU.getName());
                if (emergencyLU.getPostStatus() == 1) {
                    mTvEquipmentStatus.setText("应急联动单位状态：在位（在岗）");
                } else {
                    if (emergencyLU.getPersonnelStatus().equals("无")) {
                        mTvEquipmentStatus.setText("应急联动单位状态：不在位");
                    } else {
                        mTvEquipmentStatus.setText("应急联动单位状态：" + emergencyLU.getPersonnelStatus());
                    }
                }
                mTvEquipmentDetachment.setText("应急联动单位所属支队：" + emergencyLU.getSubordDetachment());

                //删除
                mBtnDelete.setOnClickListener(v -> {
                    showDeleteDialog(emergencyLU.getObjectId());
                });
                //修改
                mBtnUpdate.setOnClickListener(v -> {
                    showUpdateDialog(emergencyLU);
                });
            }
        }
    }

    /**
     * 修改应急联动单位信息
     *
     * @param emergencyLU
     */
    private void showUpdateDialog(EmergencyLU emergencyLU) {
        isInsertDialog = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        UpdateDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_emergency_linkage_unit_manager, null);
        Spinner mSpinnerSubordinateDetachment;
        EditText mEtName;
        EditText mEtStatus;
        Button mBtnNewCancel;
        Button mBtnDetermine;
        Spinner mSpinnerReign;


        mSpinnerSubordinateDetachment = (Spinner) view.findViewById(R.id.spinner_subordinate_detachment);
        mSpinnerReign = (Spinner) view.findViewById(R.id.spinner_reign);
        mEtName = (EditText) view.findViewById(R.id.et_name);
        mEtStatus = (EditText) view.findViewById(R.id.et_status);
        mIvImageupdate = (ImageView) view.findViewById(R.id.iv_image);
        mBtnNewCancel = (Button) view.findViewById(R.id.btn_new_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);


        mEtName.setText(emergencyLU.getName());
        if (emergencyLU.getPostStatus() == 1) {
            mSpinnerReign.setSelection(0);
            mEtStatus.setEnabled(false);
        } else {
            mSpinnerReign.setSelection(1);
            mEtStatus.setEnabled(true);
            mEtStatus.setText(emergencyLU.getPersonnelStatus());
        }
        subordinateDetachmentlist = new ArrayList<>();
        subordinateDetachmentlist.add("昌吉地区公安消防支队");
        subordinateDetachmentlist.add("昌吉消防支队奇台县大队");
        subordinateDetachmentlist.add("昌吉消防支队阜康市大队");
        subordinateDetachmentlist.add("昌吉消防支队淮东油田大队");
        subordinateDetachmentlist.add("--请选择--");
        for (int i = 0; i < subordinateDetachmentlist.size(); i++) {
            if (subordinateDetachmentlist.get(i).equals(emergencyLU.getSubordDetachment())) {
                mSpinnerSubordinateDetachment.setSelection(i);
            }
        }
        mSpinnerReign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) mSpinnerReign.getSelectedItem();
                if (item.equals("在位")) {
                    mEtStatus.setEnabled(false);
                } else {
                    mEtStatus.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        UpdateDialog.setView(view);
        UpdateDialog.setCanceledOnTouchOutside(false);
        UpdateDialog.show();
        mIvImageupdate.setOnClickListener(v -> {
            showAlertDialog();
        });
        mBtnNewCancel.setOnClickListener(v -> {
            UpdateDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            boolean b = checkParams(
                    mEtName.getText().toString().trim(),
                    mEtStatus.getText().toString().trim(),
                    (String) mSpinnerSubordinateDetachment.getSelectedItem().toString(),
                    (String) mSpinnerReign.getSelectedItem().toString()
            );
            if (b) {
                updateEmergencyLinkageUnit(
                        emergencyLU.getObjectId(),
                        mEtName.getText().toString().trim(),
                        mEtStatus.getText().toString().trim(),
                        (String) mSpinnerSubordinateDetachment.getSelectedItem(),
                        (String) mSpinnerReign.getSelectedItem().toString()
                );
            }
        });
    }

    private void updateEmergencyLinkageUnit(String objectId, String name, String status, String subordinateDetachmentSelectedItem, String reign) {
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //查询数据库中是否有相同归属支队，相同装备类型，相同装备的记录，如果有，修改数量，否则插入新数据
//                getEquipmentInTheDatabase(files, urls, SubordinateDetachment, VehicleEquipmentCategory, EquipmentName, equipmentNumber);
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    EmergencyLU emergencyLU = new EmergencyLU();
                    emergencyLU.setFile(files.get(0));
                    emergencyLU.setName(name);
                    if (reign.equals("在位")) {
                        emergencyLU.setPostStatus(1);
                    } else {
                        emergencyLU.setPostStatus(0);
                        emergencyLU.setPersonnelStatus(status);
                    }
                    emergencyLU.setSubordDetachment(subordinateDetachmentSelectedItem);
                    emergencyLU.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                T.showShort("修改成功");
                                getAllEmergencyLinkageUnit();
                            } else {
                                Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                                ErrorRec.errorRec(EmergencyLinkageUnitInquiryActivity.this, e.getErrorCode());
                            }
                            UpdateDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                T.showShort("错误码" + statuscode + ",错误描述：" + errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
            }
        });
    }

    /**
     * 删除应急联动单位
     *
     * @param objectId
     */
    private void showDeleteDialog(String objectId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要删除该应急联动单位吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EmergencyLU emergencyLU = new EmergencyLU();
                emergencyLU.delete(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            T.showShort("删除成功");
                            getAllEmergencyLinkageUnit();
                        } else {
                            Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                            ErrorRec.errorRec(EmergencyLinkageUnitInquiryActivity.this, e.getErrorCode());
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

    private void initListener() {
        mCtbEmergencyLincageUnitInquiry.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                showAddEmergencyLinkageUnitDialog();
            }
        });
    }

    private void showAddEmergencyLinkageUnitDialog() {
        isInsertDialog = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        addDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_emergency_linkage_unit_manager, null);
        Spinner mSpinnerSubordinateDetachment;
        EditText mEtName;
        EditText mEtStatus;
        Button mBtnNewCancel;
        Button mBtnDetermine;
        Spinner mSpinnerReign;


        mSpinnerSubordinateDetachment = (Spinner) view.findViewById(R.id.spinner_subordinate_detachment);
        mSpinnerReign = (Spinner) view.findViewById(R.id.spinner_reign);
        mEtName = (EditText) view.findViewById(R.id.et_name);
        mEtStatus = (EditText) view.findViewById(R.id.et_status);
        mIvImageadd = (ImageView) view.findViewById(R.id.iv_image);
        mBtnNewCancel = (Button) view.findViewById(R.id.btn_new_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);

        mSpinnerSubordinateDetachment.setSelection(4);
        mSpinnerReign.setSelection(0);
        mSpinnerReign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) mSpinnerReign.getSelectedItem();
                if (item.equals("在位")) {
                    mEtStatus.setEnabled(false);
                } else {
                    mEtStatus.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addDialog.setView(view);
        addDialog.setCanceledOnTouchOutside(false);
        addDialog.show();
        mIvImageadd.setOnClickListener(v -> {
            showAlertDialog();
        });
        mBtnNewCancel.setOnClickListener(v -> {
            addDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            boolean b = checkParams(
                    mEtName.getText().toString().trim(),
                    mEtStatus.getText().toString().trim(),
                    (String) mSpinnerSubordinateDetachment.getSelectedItem().toString(),
                    (String) mSpinnerReign.getSelectedItem().toString()
            );
            if (b) {
                addEmergencyLinkageUnit(
                        mEtName.getText().toString().trim(),
                        mEtStatus.getText().toString().trim(),
                        (String) mSpinnerSubordinateDetachment.getSelectedItem(),
                        (String) mSpinnerReign.getSelectedItem().toString()
                );
            }
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ImageDialog = builder.create();
        View inflate = View.inflate(this, R.layout.main_tag3_dialog, null);

        ImageDialog.setView(inflate);
        ImageDialog.show();
        TextView mCardView1;
        TextView mCardView2;
        TextView mCardView3;

        mCardView1 = (TextView) inflate.findViewById(R.id.cardView1);
        mCardView2 = (TextView) inflate.findViewById(R.id.cardView2);
        mCardView3 = (TextView) inflate.findViewById(R.id.cardView3);
        mCardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialog.dismiss();
            }
        });
        mCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(EmergencyLinkageUnitInquiryActivity.this)
                        .openCamera(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                ImageDialog.dismiss();
            }
        });
        mCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(EmergencyLinkageUnitInquiryActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                ImageDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    filePaths = new String[selectList.size()];
                    for (int i = 0; i < selectList.size(); i++) {
                        filePaths[i] = selectList.get(i).getPath();
                    }
                    for (int i = 0; i < filePaths.length; i++) {
                        if (isInsertDialog) {
                            ImageUtils.setBitmapCenterCrop(filePaths[i], mIvImageadd);
                        } else {
                            ImageUtils.setBitmapCenterCrop(filePaths[i], mIvImageupdate);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 添加应急联动单位
     *
     * @param name
     * @param status
     * @param subordinateDetachmentSelectedItem
     * @param reign
     */
    private void addEmergencyLinkageUnit(String name, String status, String subordinateDetachmentSelectedItem, String reign) {
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //查询数据库中是否有相同归属支队，相同装备类型，相同装备的记录，如果有，修改数量，否则插入新数据
//                getEquipmentInTheDatabase(files, urls, SubordinateDetachment, VehicleEquipmentCategory, EquipmentName, equipmentNumber);
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    EmergencyLU emergencyLU = new EmergencyLU();
                    emergencyLU.setFile(files.get(0));
                    emergencyLU.setName(name);
                    if (reign.equals("在位")) {
                        emergencyLU.setPostStatus(1);
                    } else {
                        emergencyLU.setPostStatus(0);
                        emergencyLU.setPersonnelStatus(status);
                    }
                    emergencyLU.setSubordDetachment(subordinateDetachmentSelectedItem);
                    emergencyLU.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                T.showShort("添加成功");
                                getAllEmergencyLinkageUnit();
                            } else {
                                Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                                ErrorRec.errorRec(EmergencyLinkageUnitInquiryActivity.this, e.getErrorCode());
                            }
                            addDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                T.showShort("错误码" + statuscode + ",错误描述：" + errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
            }
        });
    }

    private boolean checkParams(String name, String status, String subordinateDetachmentSelectedItem, String reign) {
        if (TextUtils.isEmpty(name)) {
            T.showShort("请输入应急联动单位名称");
            return false;
        }
        if (reign.equals("不在位")) {
            if (TextUtils.isEmpty(status)) {
                T.showShort("请输入应急联动单位状态");
                return false;
            }
        }
        if (subordinateDetachmentSelectedItem.equals("--请选择--")) {
            T.showShort("请选择应急联动单位所属支队");
            return false;
        }
        if (filePaths == null) {
            T.showShort("请上传图片");
            return false;
        }
        return true;
    }
}
