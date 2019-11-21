package com.firemanagement.frags.admin;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
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
import com.firemanagement.bean.MessageEvent;
import com.firemanagement.db.VehicleInfo;
import com.firemanagement.frags.BaseFragment;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.ImageUtils;
import com.firemanagement.utils.T;
import com.firemanagement.view.RecyclerAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleInformationFragment extends BaseFragment {

    private RecyclerView mRvVehicleInfo;
    private TextView mTvVehicleInfo;
    private MyAdapter myAdapter;
    private AlertDialog updateDialog;
    private boolean isfirst;
    private AlertDialog alertDialog;
    private String[] filePaths;
    private ImageView mIvImage;

    @Override
    protected int getLayout() {
        return R.layout.fragment_vehicle_information;
    }

    @Override
    public void onAttach(Context context) {
        EventBus.getDefault().register(this);
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView(View view) {
        mRvVehicleInfo = (RecyclerView) view.findViewById(R.id.rv_vehicle_info);
        mTvVehicleInfo = (TextView) view.findViewById(R.id.tv_vehicle_info);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void MessageEvent(MessageEvent messageEvent) {
        if (messageEvent.isComplete()) {
            String messageType = messageEvent.getMessageType();
            if (messageEvent.equals("vehicle")) {
                getAllVehicleInfo();
            }
        }
    }

    @Override
    protected void init() {
        getAllVehicleInfo();
    }

    private void getAllVehicleInfo() {
        BmobQuery<VehicleInfo> query = new BmobQuery<>();
        query.findObjects(new FindListener<VehicleInfo>() {
            @Override
            public void done(List<VehicleInfo> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        mRvVehicleInfo.setVisibility(View.GONE);
                        mTvVehicleInfo.setVisibility(View.VISIBLE);
                    } else {
                        mTvVehicleInfo.setVisibility(View.GONE);
                        mRvVehicleInfo.setVisibility(View.VISIBLE);
                    }
                    initAdapter(list);
                } else {
                    Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                }
            }
        });
    }

    private void initAdapter(List<VehicleInfo> vehicleInfoList) {
        mRvVehicleInfo.setHasFixedSize(true);
        mRvVehicleInfo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        myAdapter = new MyAdapter();
        mRvVehicleInfo.setAdapter(myAdapter);
        myAdapter.add(vehicleInfoList);
    }

    public class MyAdapter extends RecyclerAdapter<VehicleInfo> {

        @Override
        protected int getItemViewType(int position, VehicleInfo vehicleInfo) {
            return R.layout.item_vehicleequipment_manager;
        }

        @Override
        protected ViewHolder<VehicleInfo> onCreateViewHolder(View root, int viewType) {
            return new MyAdapter.MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<VehicleInfo> {
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
            protected void onBind(VehicleInfo vehicleInfo, int position) {
                ImageUtils.setBitmapCenterCrop(vehicleInfo.getFile().getUrl(), iv_equipment_picture);
                tv_equipment_type.setText("车辆类型：" + vehicleInfo.getVehicleClass());
                tv_equipment_name.setText("车辆名称：" + vehicleInfo.getVehicleName());
                tv_equipment_license_plate.setText("车辆车牌：" + vehicleInfo.getVehicleNumber());
                switch (vehicleInfo.getVehicleStatus()) {
                    case 0:
                        tv_equipment_status.setText("车辆状态：正常");
                        break;
                    case 1:
                        tv_equipment_status.setText("车辆状态：异常");
                        break;
                    case 2:
                        tv_equipment_status.setText("车辆状态：损坏");
                        break;
                }
                tv_equipment_detachment.setText("车俩所属单位：" + vehicleInfo.getSubordDetachment());
                //删除
                btn_delete.setOnClickListener(v -> {
                    showDeleteDialog(vehicleInfo);
                });
                //修改
                btn_update.setOnClickListener(v -> {
                    showUpdateDialog(vehicleInfo, position);
                });
            }
        }
    }

    /**
     * 修改
     *
     * @param position
     */
    private void showUpdateDialog(VehicleInfo vehicleInfo, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        updateDialog = builder.create();
        View view = View.inflate(getActivity(), R.layout.dialog_vehicle_info, null);
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
        mIvImage = (ImageView) view.findViewById(R.id.iv_image);
        mBtnNewCancel = (Button) view.findViewById(R.id.btn_new_cancel);
        mBtnDetermine = (Button) view.findViewById(R.id.btn_determine);
        //回显数据
        mEtNumberOfEquipment.setText(vehicleInfo.getVehicleNumber());
        mEtSpecificationModel.setText(vehicleInfo.getSpecificateModel());
        switch (vehicleInfo.getSubordDetachment()) {
            case "昌吉地区公安消防支队":
                mSpinnerSubordinateDetachment.setSelection(0);
                break;
            case "昌吉消防支队奇台县大队":
                mSpinnerSubordinateDetachment.setSelection(1);
                break;
            case "昌吉消防支队阜康市大队":
                mSpinnerSubordinateDetachment.setSelection(2);
                break;
            case "昌吉消防支队淮东油田大队":
                mSpinnerSubordinateDetachment.setSelection(3);
                break;
        }
        List<List<String>> lists = initTypeData();
        isfirst = true;
        mSpinnerVehicleEquipmentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> stringArrayAdapter = null;
                if (isfirst) {
                    //装备名称
                    for (int i = 0; i < lists.size(); i++) {
                        List<String> stringList = lists.get(i);
                        for (int j = 0; j < stringList.size(); j++) {
                            String s = stringList.get(j);
                            if (s.equals(vehicleInfo.getVehicleName())) {
                                stringArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_type_name, lists.get(i));
                                mSpinnerEquipmentName.setAdapter(stringArrayAdapter);
                                mSpinnerEquipmentName.setSelection(j);
                            }
                        }
                    }
                    isfirst = false;
                } else {
                    if (position != 2) {
                        stringArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_type_name, lists.get(position));
                        mSpinnerEquipmentName.setAdapter(stringArrayAdapter);
                        mSpinnerEquipmentName.setSelection(lists.get(position).size() - 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateDialog.setView(view);
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();
        mIvImage.setOnClickListener(v -> {
            showAlertDialog();
        });
        mBtnNewCancel.setOnClickListener(v -> {
            updateDialog.dismiss();
        });
        mBtnDetermine.setOnClickListener(v -> {
            uploadVehicleEquipment((String) mSpinnerSubordinateDetachment.getSelectedItem(),
                    (String) mSpinnerVehicleEquipmentCategory.getSelectedItem(),
                    (String) mSpinnerEquipmentName.getSelectedItem(),
                    mEtNumberOfEquipment.getText().toString().trim(),
                    mEtSpecificationModel.getText().toString().trim(),
                    vehicleInfo.getObjectId());
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
    private void uploadVehicleEquipment(String SubordinateDetachment, String VehicleEquipmentCategory, String EquipmentName, String equipmentNumber, String specificationModel, String objectId) {
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
                    vehicleInfo.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                T.showShort("修改成功");
                                getAllVehicleInfo();
                                updateDialog.dismiss();
                            } else {
                                T.showShort("修改失败");
                                Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
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

    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        alertDialog = builder.create();
        View inflate = View.inflate(getActivity(), R.layout.main_tag3_dialog, null);

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
                PictureSelector.create(getActivity())
                        .openCamera(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                alertDialog.dismiss();
            }
        });
        mCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(getActivity())
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
                        ImageUtils.setBitmapCenterCrop(filePaths[i], mIvImage);
                    }
                    break;
            }
        }
    }


    /**
     * 删除
     */
    private void showDeleteDialog(VehicleInfo vehicleInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                VehicleInfo info = new VehicleInfo();
                info.setObjectId(vehicleInfo.getObjectId());
                info.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            T.showShort("删除成功");
                            getAllVehicleInfo();
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

    private List<List<String>> initTypeData() {
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


}
