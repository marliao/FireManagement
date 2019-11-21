package com.firemanagement.activities.Admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.bean.MessageEvent;
import com.firemanagement.db.EquipmentInfo;
import com.firemanagement.db.VehicleInfo;
import com.firemanagement.frags.admin.EquipmentInformationFragment;
import com.firemanagement.frags.admin.VehicleInformationFragment;
import com.firemanagement.utils.ImageUtils;
import com.firemanagement.utils.T;
import com.firemanagement.view.CustomTitleBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * 车辆装备管理
 */
public class VehicleEquipmentActivity extends BaseActivity implements View.OnClickListener {

    private CustomTitleBar mCtbVehicleEquipment;
    private FrameLayout mFragmentInfoManager;
    private LinearLayout mLlVehicleInfo;
    private LinearLayout mLlEquipmentInfo;
    private String fragment_name = "";
    private AlertDialog VehicleEquipmentDialog;
    private ImageView mVehicleIvImage;
    private boolean isfirst;
    private AlertDialog alertDialog;
    private String[] filePaths;
    private boolean isActivityImage = false;
    private AlertDialog equipmentInfoDialog;
    private ImageView mEquipmentIvImage;

    @Override
    protected void onNewIntent(Intent intent) {
        EventBus.getDefault().register(this);
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_vehicle_equipment;
    }

    @Override
    protected void initLayout() {
        mCtbVehicleEquipment = (CustomTitleBar) findViewById(R.id.ctb_vehicle_equipment);
        mFragmentInfoManager = (FrameLayout) findViewById(R.id.fragment_info_manager);
        mLlVehicleInfo = (LinearLayout) findViewById(R.id.ll_vehicle_info);
        mLlEquipmentInfo = (LinearLayout) findViewById(R.id.ll_equipment_info);
    }

    @Override
    protected void init() {
        fragment_name = "vehicle";
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_info_manager, new VehicleInformationFragment()).commit();
        mLlVehicleInfo.setBackgroundColor(Color.parseColor("#E3F7FF"));
        mLlEquipmentInfo.setBackgroundColor(Color.parseColor("#ffffff"));
        initListener();
    }

    private void initListener() {
        mCtbVehicleEquipment.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                switch (fragment_name) {
                    case "vehicle":
                        showAddDialog();
                        break;
                    case "equipment":
                        showAddEquipmentDialog();
                        break;
                }
            }
        });
        mLlVehicleInfo.setOnClickListener(this);
        mLlEquipmentInfo.setOnClickListener(this);
    }

    /**
     * 装备数据添加的对话框
     */
    private void showAddEquipmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        equipmentInfoDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_equipment_info, null);
        Spinner mSpinnerSubordinateDetachment;
        Spinner mSpinnerVehicleEquipmentCategory;
        Spinner mSpinnerEquipmentName;
        EditText mEtEquipmentClassification;
        EditText mEtEquipmentQuantity;
        EditText mEtEquipmentUse;
        EditText mEtEquipmentComposition;
        Button mBtnCancel;
        Button mBtnDetermine;
        mSpinnerSubordinateDetachment = (Spinner) view.findViewById(R.id.spinner_subordinate_detachment);
        mSpinnerVehicleEquipmentCategory = (Spinner) view.findViewById(R.id.spinner_vehicle_equipment_category);
        mSpinnerEquipmentName = (Spinner) view.findViewById(R.id.spinner_equipment_name);
        mEtEquipmentClassification = (EditText) view.findViewById(R.id.et_equipment_classification);
        mEtEquipmentQuantity = (EditText) view.findViewById(R.id.et_equipment_quantity);
        mEtEquipmentUse = (EditText) view.findViewById(R.id.et__equipment_use);
        mEtEquipmentComposition = (EditText) view.findViewById(R.id.et__equipment_composition);
        mEquipmentIvImage = (ImageView) view.findViewById(R.id.iv_image);
        mBtnCancel = (Button) view.findViewById(R.id.btn_new_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);
        mSpinnerSubordinateDetachment.setSelection(4);
        mSpinnerVehicleEquipmentCategory.setSelection(3);
        List<List<String>> lists = initTypeDataEquipment();
        mSpinnerVehicleEquipmentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> stringArrayAdapter = null;
                if (isfirst) {
                    stringArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_type_name, lists.get(position - 1));
                    mSpinnerEquipmentName.setAdapter(stringArrayAdapter);
                    mSpinnerEquipmentName.setSelection(lists.get(position - 1).size() - 1);
                    isfirst = false;
                } else {
                    if (position <= 1) {
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
        equipmentInfoDialog.setView(view);
        equipmentInfoDialog.setCanceledOnTouchOutside(false);
        equipmentInfoDialog.show();
        mEquipmentIvImage.setOnClickListener(v -> {
            isActivityImage = true;
            showAlertDialog();
        });
        mBtnCancel.setOnClickListener(v -> {
            equipmentInfoDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            uploadEquipment(
                    (String) mSpinnerSubordinateDetachment.getSelectedItem(),
                    (String) mSpinnerVehicleEquipmentCategory.getSelectedItem(),
                    (String) mSpinnerEquipmentName.getSelectedItem(),
                    mEtEquipmentClassification.getText().toString().trim(),
                    mEtEquipmentUse.getText().toString().trim(),
                    mEtEquipmentComposition.getText().toString().trim(),
                    mEtEquipmentQuantity.getText().toString().trim()
            );
        });

    }

    private void uploadEquipment(String subordinateDetachment, String vehicleEquipmentCategory, String equipmentName, String equipmentClassification, String equipmentUse, String equipmentComposition, String equipmentQuantity) {
        if (subordinateDetachment.equals("--请选择--")) {
            T.showShort("请选择车辆所属支队");
            return;
        }
        if (vehicleEquipmentCategory.equals("--请选择--")) {
            T.showShort("请选择车辆类型");
            return;
        }
        if (equipmentName.equals("--请选择--")) {
            T.showShort("请选择车辆名称");
            return;
        }
        if (TextUtils.isEmpty(equipmentClassification)) {
            T.showShort("请输入装备分类");
            return;
        }
        if (TextUtils.isEmpty(equipmentUse)) {
            T.showShort("请输入装备用途");
            return;
        }
        if (TextUtils.isEmpty(equipmentComposition)) {
            T.showShort("请输入装备组成");
            return;
        }
        if (TextUtils.isEmpty(equipmentQuantity)) {
            T.showShort("请输入装备数量");
            return;
        }
        if (filePaths == null) {
            T.showShort("请上传车辆图片");
            return;
        }
        if (filePaths.length == 0) {
            T.showShort("请上传车辆图片");
            return;
        }
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //查询数据库中是否有相同归属支队，相同装备类型，相同装备的记录，如果有，修改数量，否则插入新数据
//                getEquipmentInTheDatabase(files, urls, SubordinateDetachment, VehicleEquipmentCategory, EquipmentName, equipmentNumber);
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    //do something
                    EquipmentInfo equipmentInfo = new EquipmentInfo();
                    equipmentInfo.setSubordDetachment(subordinateDetachment);
                    equipmentInfo.setEquipmentName(equipmentName);
                    equipmentInfo.setEquipmentCategory(vehicleEquipmentCategory);
                    equipmentInfo.setPackingClass(equipmentClassification);
                    equipmentInfo.setFile(files.get(0));
                    equipmentInfo.setEquipmentUse(equipmentUse);
                    equipmentInfo.setEquipmentComposite(equipmentComposition);
                    equipmentInfo.setNumberOfEquipment(equipmentQuantity);
                    equipmentInfo.setEquipmentStatus(1);
                    equipmentInfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            MessageEvent messageEvent = new MessageEvent();
                            messageEvent.setMessageType("equipment");
                            if (e == null) {
                                T.showShort("添加成功");
                                messageEvent.setComplete(true);
                                equipmentInfoDialog.dismiss();
                            } else {
                                messageEvent.setComplete(false);
                                T.showShort("添加失败");
                                Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                            }
                            EventBus.getDefault().postSticky(messageEvent);
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
     * 车辆数据添加的对话框
     */
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        VehicleEquipmentDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_vehicle_info, null);
        Spinner mSpinnerSubordinateDetachment;
        Spinner mSpinnerVehicleEquipmentCategory;
        Spinner mSpinnerEquipmentName;
        EditText mEtSpecificationModel;
        EditText mEtNumberOfEquipment;
        Button mBtnNewCancel;
        Button mBtnDetermine;
        mSpinnerSubordinateDetachment = (Spinner) view.findViewById(R.id.spinner_subordinate_detachment);
        mSpinnerVehicleEquipmentCategory = (Spinner) view.findViewById(R.id.spinner_vehicle_equipment_category);
        mSpinnerEquipmentName = (Spinner) view.findViewById(R.id.spinner_equipment_name);
        mEtSpecificationModel = (EditText) view.findViewById(R.id.et_specification_model);
        mEtNumberOfEquipment = (EditText) view.findViewById(R.id.et_number_of_equipment);
        mVehicleIvImage = (ImageView) view.findViewById(R.id.iv_image);
        mBtnNewCancel = (Button) view.findViewById(R.id.btn_new_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);
        List<List<String>> lists = initTypeDataVehicle();
        mSpinnerSubordinateDetachment.setSelection(4);
        mSpinnerVehicleEquipmentCategory.setSelection(2);
        isfirst = true;
        mSpinnerVehicleEquipmentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> stringArrayAdapter = null;
                if (isfirst) {
                    stringArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_type_name, lists.get(position - 1));
                    mSpinnerEquipmentName.setAdapter(stringArrayAdapter);
                    mSpinnerEquipmentName.setSelection(lists.get(position - 1).size() - 1);
                    isfirst = false;
                } else {
                    if (position <= 2) {
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
        VehicleEquipmentDialog.setView(view);
        VehicleEquipmentDialog.setCanceledOnTouchOutside(false);
        VehicleEquipmentDialog.show();
        mBtnNewCancel.setOnClickListener(v -> {
            VehicleEquipmentDialog.dismiss();
        });
        mVehicleIvImage.setOnClickListener(v -> {
            isActivityImage = true;
            showAlertDialog();
        });
        mBtnDetermine.setOnClickListener(v -> {
            uploadVehicle((String) mSpinnerSubordinateDetachment.getSelectedItem(),
                    (String) mSpinnerVehicleEquipmentCategory.getSelectedItem(),
                    (String) mSpinnerEquipmentName.getSelectedItem(),
                    mEtNumberOfEquipment.getText().toString().trim(),
                    mEtSpecificationModel.getText().toString().trim()
            );
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
    private void uploadVehicle(String SubordinateDetachment, String VehicleEquipmentCategory, String EquipmentName, String equipmentNumber, String specificationModel) {
        if (SubordinateDetachment.equals("--请选择--")) {
            T.showShort("请选择车辆所属支队");
            return;
        }
        if (VehicleEquipmentCategory.equals("--请选择--")) {
            T.showShort("请选择车辆类型");
            return;
        }
        if (EquipmentName.equals("--请选择--")) {
            T.showShort("请选择车辆名称");
            return;
        }
        if (TextUtils.isEmpty(equipmentNumber)) {
            T.showShort("请输入车牌号");
            return;
        }
        if (TextUtils.isEmpty(specificationModel)) {
            T.showShort("请输入规格信号");
            return;
        }
        if (filePaths == null) {
            T.showShort("请上传车辆图片");
            return;
        }
        if (filePaths.length == 0) {
            T.showShort("请上传车辆图片");
            return;
        }

        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //查询数据库中是否有相同归属支队，相同装备类型，相同装备的记录，如果有，修改数量，否则插入新数据
//                getEquipmentInTheDatabase(files, urls, SubordinateDetachment, VehicleEquipmentCategory, EquipmentName, equipmentNumber);
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    //do something
                    VehicleInfo vehicleInfo = new VehicleInfo();
                    vehicleInfo.setFile(files.get(0));
                    vehicleInfo.setSpecificateModel(specificationModel);
                    vehicleInfo.setSubordDetachment(SubordinateDetachment);
                    vehicleInfo.setVehicleClass(VehicleEquipmentCategory);
                    vehicleInfo.setVehicleName(EquipmentName);
                    vehicleInfo.setVehicleNumber(equipmentNumber);
                    vehicleInfo.setVehicleStatus(1);
                    vehicleInfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            MessageEvent messageEvent = new MessageEvent();
                            messageEvent.setMessageType("vehicle");
                            if (e == null) {
                                T.showShort("添加成功");
                                messageEvent.setComplete(true);
                                VehicleEquipmentDialog.dismiss();
                            } else {
                                messageEvent.setComplete(false);
                                T.showShort("添加失败");
                                Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                            }
                            EventBus.getDefault().postSticky(messageEvent);
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
                PictureSelector.create(VehicleEquipmentActivity.this)
                        .openCamera(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                alertDialog.dismiss();
            }
        });
        mCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(VehicleEquipmentActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isActivityImage) {
            isActivityImage = false;
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case PictureConfig.CHOOSE_REQUEST:
                        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                        filePaths = new String[selectList.size()];
                        for (int i = 0; i < selectList.size(); i++) {
                            filePaths[i] = selectList.get(i).getPath();
                        }
                        for (int i = 0; i < filePaths.length; i++) {
                            switch (fragment_name) {
                                case "vehicle":
                                    ImageUtils.setBitmapCenterCrop(filePaths[i], mVehicleIvImage);
                                    break;
                                case "equipment":
                                    ImageUtils.setBitmapCenterCrop(filePaths[i], mEquipmentIvImage);
                                    break;
                            }
                        }
                        break;
                }
            }
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_info_manager);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private List<List<String>> initTypeDataVehicle() {
        List<List<String>> valueslist = new ArrayList<>();
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

    private List<List<String>> initTypeDataEquipment() {
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
        return valueslist;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_vehicle_info://车辆信息
                fragment_name = "vehicle";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_info_manager, new VehicleInformationFragment()).commit();
                mLlVehicleInfo.setBackgroundColor(Color.parseColor("#E3F7FF"));
                mLlEquipmentInfo.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case R.id.ll_equipment_info://装备信息
                fragment_name = "equipment";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_info_manager, new EquipmentInformationFragment()).commit();
                mLlVehicleInfo.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlEquipmentInfo.setBackgroundColor(Color.parseColor("#E3F7FF"));
                break;
        }
    }
}
