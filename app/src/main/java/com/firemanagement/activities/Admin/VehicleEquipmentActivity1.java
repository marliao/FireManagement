package com.firemanagement.activities.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.db.CarInfo;
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
 * 此类编写代码中被废弃
 */
public class VehicleEquipmentActivity1 extends BaseActivity {

    private CustomTitleBar mCtbVehicleEquipment;
    private RecyclerView mRvVehicleEquipment;
    private TextView mTvVehicleEquipment;
    private AlertDialog VehicleEquipmentDialog;
    private boolean isfirst = true;
    private AlertDialog alertDialog;
    private Spinner mSpinnerSubordinateDetachment;
    private Spinner mSpinnerVehicleEquipmentCategory;
    private Spinner mSpinnerEquipmentName;
    private ImageView mIvImage;
    private Button mBtnNewCancel;
    private Button mBtnDetermine;
    private EditText mEtNumberOfEquipment;
    private String[] filePaths;
    private MyAdapter myAdapter;
    private AlertDialog updateDialog;
    private Spinner mUodateSpinnerSubordinateDetachment;
    private Spinner mUodateSpinnerVehicleEquipmentCategory;
    private Spinner mUodateSpinnerEquipmentName;
    private EditText mUodateEtNumberOfEquipment;
    private ImageView mUodateIvImage;
    private Button mUpdateBtnNewCancel;
    private Button mUpdateBtnDetermine;
    private boolean isInsertDialog = false;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_vehicle_equipment1;
    }

    @Override
    protected void initLayout() {
        mCtbVehicleEquipment = (CustomTitleBar) findViewById(R.id.ctb_vehicle_equipment);
        mRvVehicleEquipment = (RecyclerView) findViewById(R.id.rv_vehicle_equipment);
        mTvVehicleEquipment = (TextView) findViewById(R.id.tv_vehicle_equipment);
    }

    @Override
    protected void init() {
        initlistener();
        getAllVehicleEquipment();
    }

    private void getAllVehicleEquipment() {
        BmobQuery<CarInfo> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<CarInfo>() {
            @Override
            public void done(List<CarInfo> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        mRvVehicleEquipment.setVisibility(View.GONE);
                        mTvVehicleEquipment.setVisibility(View.VISIBLE);
                    } else {
                        mTvVehicleEquipment.setVisibility(View.GONE);
                        mRvVehicleEquipment.setVisibility(View.VISIBLE);
                    }
                    initAdapter(list);
                } else {
                    Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                }
            }
        });
    }

    private void initAdapter(List<CarInfo> list) {
        mRvVehicleEquipment.setHasFixedSize(true);
        mRvVehicleEquipment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myAdapter = new MyAdapter();
        mRvVehicleEquipment.setAdapter(myAdapter);
        myAdapter.add(list);
    }

    public class MyAdapter extends RecyclerAdapter<CarInfo> {

        @Override
        protected int getItemViewType(int position, CarInfo carInfo) {
            return R.layout.item_vehicleequipment_manager;
        }

        @Override
        protected ViewHolder<CarInfo> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<CarInfo> {
            public ImageView iv_equipment_picture;
            public TextView tv_equipment_type;
            public TextView tv_equipment_name;
            public TextView tv_equipment_license_plate;
            public TextView tv_equipment_status;
            public TextView tv_equipment_detachment;
            public Button btn_update;
            public Button btn_delete;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.iv_equipment_picture = (ImageView) itemView.findViewById(R.id.iv_equipment_picture);
                this.tv_equipment_type = (TextView) itemView.findViewById(R.id.tv_equipment_type);
                this.tv_equipment_name = (TextView) itemView.findViewById(R.id.tv_equipment_name);
                this.tv_equipment_license_plate = (TextView) itemView.findViewById(R.id.tv_equipment_license_plate);
                this.tv_equipment_status = (TextView) itemView.findViewById(R.id.tv_equipment_status);
                this.tv_equipment_detachment = (TextView) itemView.findViewById(R.id.tv_equipment_detachment);
                this.btn_update = (Button) itemView.findViewById(R.id.btn_update);
                this.btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
            }

            @Override
            protected void onBind(CarInfo carInfo, int position) {
                ImageUtils.setBitmapCenterCrop(carInfo.getFile().getUrl(), iv_equipment_picture);
                tv_equipment_type.setText("装备类型：" + carInfo.getClassificationName());
                tv_equipment_name.setText("装备名称：" + carInfo.getEquipmentCategory());
                tv_equipment_license_plate.setText("装备车牌：" + carInfo.getEquipmentNumber());
                switch (carInfo.getVehicleStatus()) {
                    case 0:
                        tv_equipment_status.setText("装备状态：正常");
                        break;
                    case 1:
                        tv_equipment_status.setText("装备状态：异常");
                        break;
                    case 2:
                        tv_equipment_status.setText("装备状态：损坏");
                        break;
                }
                String classificationName = carInfo.getClassificationName();
                if (classificationName.equals("居高类消防车") || classificationName.equals("灭火类消防车")) {
                    tv_equipment_license_plate.setVisibility(View.VISIBLE);
                } else {
                    tv_equipment_license_plate.setVisibility(View.GONE);
                }
                tv_equipment_detachment.setText("装备所属支队：" + carInfo.getSubordDetachment());
                //删除
                btn_delete.setOnClickListener(v -> {
                    showDeleteDialog(carInfo);
                });
                //修改
                btn_update.setOnClickListener(v -> {
//                    showUpdateDialog(carInfo, position);
                });
            }
        }
    }

    /**
     * 修改
     *
     * @param carInfo
     * @param position
     */
    private void showUpdateDialog(CarInfo carInfo, int position) {
        isInsertDialog = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        updateDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_vehicle_info, null);
        mUodateSpinnerSubordinateDetachment = (Spinner) view.findViewById(R.id.spinner_subordinate_detachment);
        mUodateSpinnerVehicleEquipmentCategory = (Spinner) view.findViewById(R.id.spinner_vehicle_equipment_category);
        mUodateSpinnerEquipmentName = (Spinner) view.findViewById(R.id.spinner_equipment_name);
        mUodateEtNumberOfEquipment = (EditText) view.findViewById(R.id.et_number_of_equipment);
        mUodateIvImage = (ImageView) view.findViewById(R.id.iv_image);
        mUpdateBtnNewCancel = (Button) view.findViewById(R.id.btn_new_cancel);
        mUpdateBtnDetermine = (Button) view.findViewById(R.id.btn_determine);
        List<List<String>> lists = initTypeData();
        mUodateEtNumberOfEquipment.setText(carInfo.getEquipmentNumber());
        //回显Spinner
        echoSpinner(carInfo);
        String category = (String) mUodateSpinnerVehicleEquipmentCategory.getSelectedItem();
        if (category.equals("居高类消防车") || category.equals("灭火类消防车")) {
            mUodateEtNumberOfEquipment.setEnabled(true);
        } else {
            mUodateEtNumberOfEquipment.setEnabled(false);
        }
        ImageUtils.setBitmapCenterCrop(carInfo.getFile().getUrl(), mUodateIvImage);

        updateDialog.setView(view);
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();
        mUpdateBtnNewCancel.setOnClickListener(v -> {
            updateDialog.dismiss();
        });
        isfirst = true;
        mUodateSpinnerVehicleEquipmentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) mUodateSpinnerVehicleEquipmentCategory.getSelectedItem();
                if (category.equals("居高类消防车") || category.equals("灭火类消防车")) {
                    mUodateEtNumberOfEquipment.setEnabled(true);
                } else {
                    mUodateEtNumberOfEquipment.setEnabled(false);
                }
                ArrayAdapter<String> stringArrayAdapter = null;
                if (isfirst) {
                    //装备名称
                    stringArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_type_name, lists.get(position));
                    mUodateSpinnerEquipmentName.setAdapter(stringArrayAdapter);
                    for (int i = 0; i < lists.size(); i++) {
                        List<String> stringList = lists.get(i);
                        for (int j = 0; j < stringList.size(); j++) {
                            String s = stringList.get(j);
                            if (s.equals(carInfo.getEquipmentCategory())) {
                                mUodateSpinnerEquipmentName.setSelection(j);
                            }
                        }
                    }
                    isfirst = false;
                } else {
                    if (position != 5) {
                        stringArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_type_name, lists.get(position));
                        mUodateSpinnerEquipmentName.setAdapter(stringArrayAdapter);
                        mUodateSpinnerEquipmentName.setSelection(lists.get(position).size() - 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mUodateIvImage.setOnClickListener(v -> {
            showAlertDialog();
        });
        mUpdateBtnDetermine.setOnClickListener(v -> {
            uploadVehicleEquipment((String) mUodateSpinnerSubordinateDetachment.getSelectedItem(),
                    (String) mUodateSpinnerVehicleEquipmentCategory.getSelectedItem(),
                    (String) mUodateSpinnerEquipmentName.getSelectedItem(), mUodateEtNumberOfEquipment.getText().toString().trim(), "update", carInfo.getObjectId());
        });
    }

    private void echoSpinner(CarInfo carInfo) {
        //所属支队
        switch (carInfo.getSubordDetachment()) {
            case "昌吉地区公安消防支队":
                mUodateSpinnerSubordinateDetachment.setSelection(0);
                break;
            case "昌吉消防支队奇台县大队":
                mUodateSpinnerSubordinateDetachment.setSelection(1);
                break;
            case "昌吉消防支队阜康市大队":
                mUodateSpinnerSubordinateDetachment.setSelection(2);
                break;
            case "昌吉消防支队淮东油田大队":
                mUodateSpinnerSubordinateDetachment.setSelection(3);
                break;
        }

        //装备类别
        switch (carInfo.getClassificationName()) {
            case "消防器具类":
                mUodateSpinnerVehicleEquipmentCategory.setSelection(0);
                break;
            case "消防泵类":
                mUodateSpinnerVehicleEquipmentCategory.setSelection(1);
                break;
            case "居高类消防车":
                mUodateSpinnerVehicleEquipmentCategory.setSelection(2);
                break;
            case "灭火类消防车":
                mUodateSpinnerVehicleEquipmentCategory.setSelection(3);
                break;
            case "专勤类消防":
                mUodateSpinnerVehicleEquipmentCategory.setSelection(4);
                break;
        }
    }

    /**
     * 删除
     *
     * @param carInfo
     */
    private void showDeleteDialog(CarInfo carInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CarInfo info = new CarInfo();
                info.setObjectId(carInfo.getObjectId());
                info.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            T.showShort("删除成功");
                            getAllVehicleEquipment();
                            dialog.dismiss();
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

    private void initlistener() {
        mCtbVehicleEquipment.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                //展示添加车辆装备信息的对话框
//                showVehicleEquipmentDialog();
            }
        });
    }

    /**
     * 增加
     */
    private void showVehicleEquipmentDialog() {
        isInsertDialog = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        VehicleEquipmentDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_vehicle_info, null);

        mSpinnerSubordinateDetachment = (Spinner) view.findViewById(R.id.spinner_subordinate_detachment);
        mSpinnerVehicleEquipmentCategory = (Spinner) view.findViewById(R.id.spinner_vehicle_equipment_category);
        mSpinnerEquipmentName = (Spinner) view.findViewById(R.id.spinner_equipment_name);
        mIvImage = (ImageView) view.findViewById(R.id.iv_image);
        mBtnNewCancel = (Button) view.findViewById(R.id.btn_new_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);
        mEtNumberOfEquipment = (EditText) view.findViewById(R.id.et_number_of_equipment);

        List<List<String>> lists = initTypeData();
        mSpinnerSubordinateDetachment.setSelection(4);
        mSpinnerVehicleEquipmentCategory.setSelection(5);
        String category = (String) mSpinnerVehicleEquipmentCategory.getSelectedItem();
        if (category.equals("居高类消防车") || category.equals("灭火类消防车")) {
            mEtNumberOfEquipment.setEnabled(true);
        } else {
            mEtNumberOfEquipment.setEnabled(false);
        }

        VehicleEquipmentDialog.setView(view);
        VehicleEquipmentDialog.setCanceledOnTouchOutside(false);
        VehicleEquipmentDialog.show();
        mBtnNewCancel.setOnClickListener(v -> {
            VehicleEquipmentDialog.dismiss();
        });
        isfirst = true;
        mSpinnerVehicleEquipmentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) mSpinnerVehicleEquipmentCategory.getSelectedItem();
                if (category.equals("居高类消防车") || category.equals("灭火类消防车")) {
                    mEtNumberOfEquipment.setEnabled(true);
                } else {
                    mEtNumberOfEquipment.setEnabled(false);
                }
                ArrayAdapter<String> stringArrayAdapter = null;
                if (isfirst) {
                    stringArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_type_name, lists.get(position - 1));
                    mSpinnerEquipmentName.setAdapter(stringArrayAdapter);
                    mSpinnerEquipmentName.setSelection(lists.get(position - 1).size() - 1);
                    isfirst = false;
                } else {
                    if (position != 5) {
                        stringArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_type_name, lists.get(position));
                        mSpinnerEquipmentName.setAdapter(stringArrayAdapter);
                        mSpinnerEquipmentName.setSelection(lists.get(position).size() - 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mIvImage.setOnClickListener(v -> {
            showAlertDialog();
        });
        mBtnDetermine.setOnClickListener(v -> {
            uploadVehicleEquipment((String) mSpinnerSubordinateDetachment.getSelectedItem(),
                    (String) mSpinnerVehicleEquipmentCategory.getSelectedItem(),
                    (String) mSpinnerEquipmentName.getSelectedItem(), mEtNumberOfEquipment.getText().toString().trim(), "insert", null);
        });

    }

    /**
     * 上传数据
     *
     * @param SubordinateDetachment
     * @param VehicleEquipmentCategory
     * @param EquipmentName
     * @param equipmentNumber
     */
    private void uploadVehicleEquipment(String SubordinateDetachment, String VehicleEquipmentCategory, String EquipmentName, String equipmentNumber, String operating, String objectId) {
        if (SubordinateDetachment.equals("--请选择--")) {
            T.showShort("请选择装备所属支队");
            return;
        }
        if (VehicleEquipmentCategory.equals("--请选择--")) {
            T.showShort("请选择装备类型");
            return;
        }
        if (EquipmentName.equals("--请选择--")) {
            T.showShort("请选择装备名称");
            return;
        }

        if (VehicleEquipmentCategory.equals("居高类消防车") || VehicleEquipmentCategory.equals("灭火类消防车")) {
            if (TextUtils.isEmpty(equipmentNumber)) {
                T.showShort("请输入装备数量");
                return;
            }
        }
        if (filePaths == null) {
            T.showShort("请上传装备图片");
            return;
        }
        if (filePaths.length == 0) {
            T.showShort("请上传装备图片");
            return;
        }

        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //查询数据库中是否有相同归属支队，相同装备类型，相同装备的记录，如果有，修改数量，否则插入新数据
//                getEquipmentInTheDatabase(files, urls, SubordinateDetachment, VehicleEquipmentCategory, EquipmentName, equipmentNumber);
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    //do something
                    CarInfo carInfo = new CarInfo();
                    carInfo.setSubordDetachment(SubordinateDetachment);
                    carInfo.setClassificationName(VehicleEquipmentCategory);
                    carInfo.setEquipmentCategory(EquipmentName);
                    carInfo.setFile(files.get(0));
                    carInfo.setEquipmentNumber(equipmentNumber + "");
                    carInfo.setVehicleStatus(0);
                    switch (operating) {
                        case "update":
                            carInfo.update(objectId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        T.showShort("修改成功");
                                        getAllVehicleEquipment();
                                        updateDialog.dismiss();
                                    } else {
                                        T.showShort("修改失败");
                                        Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                                        ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                                    }
                                }
                            });
                            break;
                        case "insert":
                            carInfo.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        T.showShort("添加成功");
                                        getAllVehicleEquipment();
                                        VehicleEquipmentDialog.dismiss();
                                    } else {
                                        T.showShort("添加失败");
                                        Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                                        ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                                    }
                                }
                            });
                            break;
                    }
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

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        View inflate = View.inflate(this, R.layout.main_tag3_dialog, null);

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
                PictureSelector.create(VehicleEquipmentActivity1.this)
                        .openCamera(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                alertDialog.dismiss();
            }
        });
        mCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(VehicleEquipmentActivity1.this)
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
                    filePaths = new String[selectList.size()];
                    for (int i = 0; i < selectList.size(); i++) {
                        filePaths[i] = selectList.get(i).getPath();
                    }
                    for (int i = 0; i < filePaths.length; i++) {
                        if (isInsertDialog) {
                            ImageUtils.setBitmapCenterCrop(filePaths[i], mIvImage);
                        } else {
                            ImageUtils.setBitmapCenterCrop(filePaths[i], mUodateIvImage);
                        }
                    }
                    break;
            }
        }
    }

    private List<List<String>> initTypeData() {
        List<List<String>> valueslist = new ArrayList<>();
        List<String> type1list = new ArrayList<>();
        type1list.add("消防炮");
        type1list.add("其他消防器具");
        type1list.add("消防枪");
        type1list.add("灭火器");
        type1list.add("输水器具");
        type1list.add("--请选择--");
        valueslist.add(type1list);

        List<String> type2list = new ArrayList<>();
        type2list.add("其他消防泵");
        type2list.add("低压消防泵");
        type2list.add("中低压消防泵");
        type2list.add("--请选择--");
        valueslist.add(type2list);

        List<String> type3list = new ArrayList<>();
        type3list.add("举高喷射消防车（JP）");
        type3list.add("登高平台消防车（DG）");
        type3list.add("--请选择--");
        valueslist.add(type3list);

        List<String> type4list = new ArrayList<>();
        type4list.add("水罐消防车（SG）");
        type4list.add("泡沫消防车（PM）");
        type4list.add("--请选择--");
        valueslist.add(type4list);

        List<String> type5list = new ArrayList<>();
        type5list.add("抢险救援消防车（JY）");
        type5list.add("其他专勤消防");
        type5list.add("--请选择--");
        valueslist.add(type5list);
        return valueslist;
    }
}
