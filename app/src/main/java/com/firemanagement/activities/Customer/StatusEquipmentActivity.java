package com.firemanagement.activities.Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.db.EquipmentInfo;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.ImageUtils;
import com.firemanagement.utils.T;
import com.firemanagement.view.CustomTitleBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * 装备状态（报停，维修。。。）
 */
public class StatusEquipmentActivity extends BaseActivity {

    private CustomTitleBar mCtbStatusVehicle;
    private ImageView mIvEquipment;
    private ImageView mIvIsNomarl;
    private TextView mTvEquipmentName;
    private TextView mTvApprovalStatus;
    private TextView mTvEquipmentStatus;
    private TextView mTvFault;
    private Button mBtnOperating;
    private EquipmentInfo equipmentInfo;
    private AlertDialog alertDialog;
    private Spinner mSpinnerStatus;
    private ArrayList<Integer> imagelist;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_status_equipment;
    }

    @Override
    protected void initLayout() {

        mCtbStatusVehicle = (CustomTitleBar) findViewById(R.id.ctb_status_vehicle);
        mIvEquipment = (ImageView) findViewById(R.id.iv_equipment);
        mIvIsNomarl = (ImageView) findViewById(R.id.iv_is_nomarl);
        mTvEquipmentName = (TextView) findViewById(R.id.tv_equipment_name);
        mTvApprovalStatus = (TextView) findViewById(R.id.tv_approval_status);
        mTvEquipmentStatus = (TextView) findViewById(R.id.tv_equipment_status);
        mTvFault = (TextView) findViewById(R.id.tv_fault);
        mBtnOperating = (Button) findViewById(R.id.btn_operating);

    }

    @Override
    protected void init() {
        initUI();
        initlistener();
    }

    private void initlistener() {
        mCtbStatusVehicle.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        mTvApprovalStatus.setOnClickListener(v -> {
            //审批状态对话框
            showApprovalStatusDialog();
        });
        mTvFault.setOnClickListener(v -> {
            //故障项
            showFaultDialog();
        });
        mBtnOperating.setOnClickListener(v -> {
            if (equipmentInfo.getStopOrStart() == 1) {
                showOperatingDialog("报停");
            } else {
                showOperatingDialog("恢复执勤");
            }
        });
        mIvIsNomarl.setOnClickListener(v -> {
            //是否正常
            showIsNoamrlDialog();
        });
        mIvEquipment.setOnClickListener(v -> {
            showAlertDialog();
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StatusEquipmentActivity.this);
        alertDialog = builder.create();
        View inflate = View.inflate(StatusEquipmentActivity.this, R.layout.main_tag3_dialog, null);

        alertDialog.setView(inflate);
        alertDialog.show();
        TextView mCardView1;
        TextView mCardView2;
        TextView mCardView3;

        mCardView1 = (TextView) inflate.findViewById(R.id.cardView1);
        mCardView2 = (TextView) inflate.findViewById(R.id.cardView2);
        mCardView3 = (TextView) inflate.findViewById(R.id.cardView3);
        mCardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        mCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(StatusEquipmentActivity.this)
                        .openCamera(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                alertDialog.dismiss();
            }
        });
        mCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(StatusEquipmentActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                alertDialog.dismiss();
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
                    final String[] filePaths = new String[selectList.size()];
                    for (int i = 0; i < selectList.size(); i++) {
                        filePaths[i] = selectList.get(i).getPath();
                    }
                    BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                        @Override
                        public void onSuccess(List<BmobFile> files, List<String> urls) {
                            if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                                EquipmentInfo info = new EquipmentInfo();
                                info.setFile(files.get(0));
                                info.update(equipmentInfo.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            T.showShort("修改成功");
                                            ImageUtils.setBitmapCenterCrop(files.get(0).getUrl(), mIvEquipment);
                                        } else {
                                            ErrorRec.errorRec(StatusEquipmentActivity.this, e.getErrorCode());
                                            Log.i(TAG, "done: -----------------------" + e.getErrorCode() + "  " + e.getMessage());
                                        }
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
                    break;
            }
        }
    }


    private void showIsNoamrlDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog isNomarlDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_is_normal, null);
        Button mBtnCancel;
        Button mBtnDetermine;

        mSpinnerStatus = (Spinner) view.findViewById(R.id.spinner_status);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);
        initSpinner();
        isNomarlDialog.setView(view);
        isNomarlDialog.setCanceledOnTouchOutside(false);
        isNomarlDialog.show();
        mBtnCancel.setOnClickListener(v -> {
            isNomarlDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            int position = mSpinnerStatus.getSelectedItemPosition();
            EquipmentInfo info = new EquipmentInfo();
            info.setEquipmentStatus(position);
            info.update(equipmentInfo.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        T.showShort("修改成功");
                        switch (position) {
                            case 0:
                                mTvEquipmentStatus.setText("正常");
                                mIvIsNomarl.setImageResource(R.drawable.zhengchang);
                                break;
                            case 1:
                                mTvEquipmentStatus.setText("异常");
                                mIvIsNomarl.setImageResource(R.drawable.yichang);
                                break;
                            case 2:
                                mTvEquipmentStatus.setText("损坏");
                                mIvIsNomarl.setImageResource(R.drawable.sunhuai);
                                break;
                        }
                    } else {
                        ErrorRec.errorRec(StatusEquipmentActivity.this, e.getErrorCode());
                        Log.i(TAG, "done: -----------------------" + e.getErrorCode() + "  " + e.getMessage());
                    }
                    isNomarlDialog.dismiss();
                }
            });
        });
    }

    private void showOperatingDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定" + message + "?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EquipmentInfo info = new EquipmentInfo();
                if (equipmentInfo.getStopOrStart() == 1) {
                    info.setStopOrStart(0);
                } else {
                    info.setStopOrStart(1);
                }
                info.update(equipmentInfo.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            if (equipmentInfo.getStopOrStart() == 1) {
                                T.showShort("报停成功");
                                equipmentInfo.setStopOrStart(0);
                                mBtnOperating.setText("恢复执勤");
                            } else {
                                T.showShort("恢复执勤成功");
                                equipmentInfo.setStopOrStart(1);
                                mBtnOperating.setText("报停");
                            }
                        } else {
                            ErrorRec.errorRec(StatusEquipmentActivity.this, e.getErrorCode());
                            Log.i(TAG, "done: -----------------------" + e.getErrorCode() + "  " + e.getMessage());
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

    private void showFaultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog faultDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_fault, null);
        EditText mEtFault;
        Button mBtnCancel;
        Button mBtnDetermine;

        mEtFault = (EditText) view.findViewById(R.id.et_fault);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);

        faultDialog.setView(view);
        faultDialog.setCanceledOnTouchOutside(false);
        faultDialog.show();

        mBtnCancel.setOnClickListener(v -> {
            faultDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            String fault = mEtFault.getText().toString().trim();
            if (TextUtils.isEmpty(fault)) {
                T.showShort("请输入故障项");
                return;
            }
            EquipmentInfo info = new EquipmentInfo();
            info.setFault(fault);
            info.update(equipmentInfo.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        T.showShort("修改成功");
                        equipmentInfo.setFault(fault);
                        mTvFault.setText(fault + "（点击修改）");
                    } else {
                        ErrorRec.errorRec(StatusEquipmentActivity.this, e.getErrorCode());
                        Log.i(TAG, "done: -----------------------" + e.getErrorCode() + "  " + e.getMessage());
                    }
                    faultDialog.dismiss();
                }
            });
        });

    }

    private void showApprovalStatusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog approvalStatusDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_approval_status, null);
        EditText mEtApprovalStatus;
        Button mBtnCancel;
        Button mBtnDetermine;

        mEtApprovalStatus = (EditText) view.findViewById(R.id.tv_approval_status);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);
        approvalStatusDialog.setView(view);
        approvalStatusDialog.setCanceledOnTouchOutside(false);
        approvalStatusDialog.show();
        mBtnCancel.setOnClickListener(v -> {
            approvalStatusDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            String approvalStatus = mEtApprovalStatus.getText().toString().trim();
            if (TextUtils.isEmpty(approvalStatus)) {
                T.showShort("请输入审批状态");
                return;
            }
            EquipmentInfo info = new EquipmentInfo();
            info.setApprovalStatus(approvalStatus);
            info.update(equipmentInfo.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        T.showShort("修改成功");
                        equipmentInfo.setApprovalStatus(approvalStatus);
                        mTvApprovalStatus.setText(approvalStatus + "（点击修改）");
                    } else {
                        ErrorRec.errorRec(StatusEquipmentActivity.this, e.getErrorCode());
                        Log.i(TAG, "done: -----------------------" + e.getErrorCode() + "  " + e.getMessage());
                    }
                    approvalStatusDialog.dismiss();
                }
            });
        });
    }

    private void initUI() {
        equipmentInfo = (EquipmentInfo) getIntent().getSerializableExtra("equipmentInfo");
        if (equipmentInfo.getFile() == null) {
            mIvEquipment.setImageResource(R.drawable.car);
        } else {
            ImageUtils.setBitmapCenterCrop(equipmentInfo.getFile().getUrl(), mIvEquipment);
        }
        mTvEquipmentName.setText(equipmentInfo.getEquipmentCategory());
        if (equipmentInfo.getApprovalStatus() == null) {
            mTvApprovalStatus.setText("无（点击修改审批状态）");
        } else {
            mTvApprovalStatus.setText(equipmentInfo.getApprovalStatus() + "（点击修改）");
        }
        if (equipmentInfo.getFault() == null) {
            mTvFault.setText("无（点击添加故障项）");
        } else {
            mTvFault.setText(equipmentInfo.getFault() + "（点击修改）");
        }
        if (equipmentInfo.getStopOrStart() == 1) {
            mBtnOperating.setText("报停");
        } else {
            mBtnOperating.setText("恢复执勤");
        }
        switch (equipmentInfo.getEquipmentStatus()) {
            case 0:
                mTvEquipmentStatus.setText("正常");
                mIvIsNomarl.setImageResource(R.drawable.zhengchang);
                break;
            case 1:
                mTvEquipmentStatus.setText("异常");
                mIvIsNomarl.setImageResource(R.drawable.yichang);
                break;
            case 2:
                mTvEquipmentStatus.setText("损坏");
                mIvIsNomarl.setImageResource(R.drawable.sunhuai);
                break;
        }
    }

    private void initSpinner() {
        imagelist = new ArrayList<>();
        imagelist.add(R.drawable.zhengchang);
        imagelist.add(R.drawable.yichang);
        imagelist.add(R.drawable.sunhuai);
        MyAdapter myAdapter = new MyAdapter();
        mSpinnerStatus.setAdapter(myAdapter);
        mSpinnerStatus.setSelection(0);
    }

    public class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return imagelist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(StatusEquipmentActivity.this, R.layout.spinner_vehicle_status, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.spinner_image.setImageResource(imagelist.get(position));
            return convertView;
        }

        public class ViewHolder {
            public View rootView;
            public ImageView spinner_image;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.spinner_image = (ImageView) rootView.findViewById(R.id.spinner_image);
            }

        }
    }
}
