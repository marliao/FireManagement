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
import com.firemanagement.db.EquipmentInfo;
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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentInformationFragment extends BaseFragment {

    private RecyclerView mRvEquipmentInfo;
    private TextView mTvEquipmentInfo;
    private MyAdapter myAdapter;
    private AlertDialog equipmentInfoDialog;
    private ImageView mEquipmentIvImage;
    private AlertDialog alertDialog;
    private String[] filePaths;
    private boolean isfirst;

    @Override
    protected int getLayout() {
        return R.layout.fragment_equipment_information;
    }

    @Override
    public void onAttach(Context context) {
        EventBus.getDefault().register(this);
        super.onAttach(context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void MessageEvent(MessageEvent messageEvent) {
        //判断添加是否成功
        if (messageEvent.isComplete()) {
            if (messageEvent.getMessageType().equals("equipment")) {
                getAllEquipmentInfo();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView(View view) {
        mRvEquipmentInfo = (RecyclerView) view.findViewById(R.id.rv_equipment_info);
        mTvEquipmentInfo = (TextView) view.findViewById(R.id.tv_equipment_info);
    }

    @Override
    protected void init() {
        getAllEquipmentInfo();
    }

    private void getAllEquipmentInfo() {
        BmobQuery<EquipmentInfo> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<EquipmentInfo>() {
            @Override
            public void done(List<EquipmentInfo> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        mRvEquipmentInfo.setVisibility(View.GONE);
                        mTvEquipmentInfo.setVisibility(View.VISIBLE);
                    } else {
                        mTvEquipmentInfo.setVisibility(View.GONE);
                        mRvEquipmentInfo.setVisibility(View.VISIBLE);
                    }
                    initAdapter(list);
                } else {
                    Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                }
            }
        });
    }

    private void initAdapter(List<EquipmentInfo> list) {
        mRvEquipmentInfo.setHasFixedSize(true);
        mRvEquipmentInfo.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        myAdapter = new MyAdapter();
        mRvEquipmentInfo.setAdapter(myAdapter);
        myAdapter.add(list);
    }

    public class MyAdapter extends RecyclerAdapter<EquipmentInfo> {
        @Override
        protected int getItemViewType(int position, EquipmentInfo equipmentInfo) {
            return R.layout.item_equipment_manager;
        }

        @Override
        protected ViewHolder<EquipmentInfo> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<EquipmentInfo> {
            public ImageView iv_equipment_picture;
            public TextView tv_equipment_type;
            public TextView tv_equipment_name;
            public TextView tv_equipment_license_plate;
            public TextView tv_equipment_status;
            public TextView tv_equipment_detachment;
            public Button btn_delete;
            public Button btn_update;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.iv_equipment_picture = (ImageView) itemView.findViewById(R.id.iv_equipment_picture);
                this.tv_equipment_type = (TextView) itemView.findViewById(R.id.tv_equipment_type);
                this.tv_equipment_name = (TextView) itemView.findViewById(R.id.tv_equipment_name);
                this.tv_equipment_license_plate = (TextView) itemView.findViewById(R.id.tv_equipment_license_plate);
                this.tv_equipment_status = (TextView) itemView.findViewById(R.id.tv_equipment_status);
                this.tv_equipment_detachment = (TextView) itemView.findViewById(R.id.tv_equipment_detachment);
                this.btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
                this.btn_update = (Button) itemView.findViewById(R.id.btn_update);
            }

            @Override
            protected void onBind(EquipmentInfo equipmentInfo, int position) {
                if (equipmentInfo.getFile() != null) {
                    ImageUtils.setBitmapCenterCrop(equipmentInfo.getFile().getUrl(), iv_equipment_picture);
                } else {
                    iv_equipment_picture.setImageResource(R.drawable.manager_zhuangbei_make);
                }
                tv_equipment_type.setText("装备类型：" + equipmentInfo.getEquipmentCategory());
                tv_equipment_name.setText("装备名称：" + equipmentInfo.getEquipmentName());
                tv_equipment_license_plate.setText("装备编号：" + equipmentInfo.getObjectId());
                switch (equipmentInfo.getEquipmentStatus()) {
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
                tv_equipment_detachment.setText("装备所属单位：" + equipmentInfo.getSubordDetachment());
                //删除
                btn_delete.setOnClickListener(v -> {
                    showDeleteDialog(equipmentInfo);
                });

                //修改
                btn_update.setOnClickListener(v -> {
                    showUpdateDialog(equipmentInfo);
                });
            }
        }

    }

    /**
     * 修改
     *
     * @param equipmentInfo
     */
    private void showUpdateDialog(EquipmentInfo equipmentInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        equipmentInfoDialog = builder.create();
        View view = View.inflate(getActivity(), R.layout.dialog_equipment_info, null);
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
        List<List<String>> lists = initTypeDataEquipment();
        //数据回显
        mEtEquipmentClassification.setText(equipmentInfo.getPackingClass());
        mEtEquipmentComposition.setText(equipmentInfo.getEquipmentComposite());
        mEtEquipmentQuantity.setText(equipmentInfo.getNumberOfEquipment());
        mEtEquipmentUse.setText(equipmentInfo.getEquipmentUse());
        switch (equipmentInfo.getSubordDetachment()) {
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
                            if (s.equals(equipmentInfo.getEquipmentName())) {
                                stringArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_type_name, lists.get(i));
                                mSpinnerEquipmentName.setAdapter(stringArrayAdapter);
                                mSpinnerEquipmentName.setSelection(j);
                            }
                        }
                    }
                    isfirst = false;
                } else {
                    if (position <= 1) {
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
        equipmentInfoDialog.setView(view);
        equipmentInfoDialog.setCanceledOnTouchOutside(false);
        equipmentInfoDialog.show();
        mEquipmentIvImage.setOnClickListener(v -> {
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
                    mEtEquipmentQuantity.getText().toString().trim(),
                    equipmentInfo.getObjectId()
            );
        });

    }

    private void uploadEquipment(String subordinateDetachment, String vehicleEquipmentCategory, String equipmentName, String equipmentClassification, String equipmentUse, String equipmentComposition, String equipmentQuantity,String objectId) {
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
                    equipmentInfo.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                T.showShort("修改成功");
                                getAllEquipmentInfo();
                                equipmentInfoDialog.dismiss();
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
                        ImageUtils.setBitmapCenterCrop(filePaths[i], mEquipmentIvImage);
                    }
                    break;
            }
        }
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


    /**
     * 删除
     *
     * @param equipmentInfo
     */
    private void showDeleteDialog(EquipmentInfo equipmentInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EquipmentInfo info = new EquipmentInfo();
                info.setObjectId(equipmentInfo.getObjectId());
                info.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            T.showShort("删除成功");
                            getAllEquipmentInfo();
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

}
