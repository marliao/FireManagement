package com.firemanagement.frags.Customer;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.activities.Customer.StatusEquipmentActivity;
import com.firemanagement.db.EquipmentInfo;
import com.firemanagement.frags.BaseFragment;
import com.firemanagement.utils.ErrorRec;
import com.firemanagement.utils.ImageUtils;
import com.firemanagement.utils.T;
import com.firemanagement.view.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class EquipmentInquiryFragment extends BaseFragment {
    private RecyclerView mRvEquipmentInquiry;
    private TextView mTvVehicleInquiry;
    private EditText mEtSearch;
    private ImageView mIvSearch;


    @Override
    protected int getLayout() {
        return R.layout.fragment_equipment_inquiry;
    }

    @Override
    protected void initView(View view) {
        mRvEquipmentInquiry = (RecyclerView) view.findViewById(R.id.rv_equipment_inquiry);
        mTvVehicleInquiry = (TextView) view.findViewById(R.id.tv_vehicle_inquiry);
        mEtSearch = (EditText) view.findViewById(R.id.et_search);
        mIvSearch = (ImageView) view.findViewById(R.id.iv_search);
    }

    @Override
    protected void init() {
        initlistener();
        initData();
    }

    private void initlistener() {
        mIvSearch.setOnClickListener(v -> {
            String trim = mEtSearch.getText().toString().trim();
            if (TextUtils.isEmpty(trim)) {
                T.showShort("请输入要查询的内容");
                return;
            }
            searchData(trim);
        });
    }

    private void searchData(String trim) {
        BmobQuery<EquipmentInfo> query = new BmobQuery<>();
        query.setLimit(20);
        query.findObjects(new FindListener<EquipmentInfo>() {
            @Override
            public void done(List<EquipmentInfo> list, BmobException e) {
                if (e == null) {
                    List<EquipmentInfo> equipmentInfoList = new ArrayList<>();
                    try {
                        for (EquipmentInfo equipmentInfo : list) {
                            //查询所有数据利用string的contains函数模糊判断，实现模糊查询
                            if (equipmentInfo.getEquipmentCategory()!=null ||
                                    equipmentInfo.getSubordDetachment()!=null ||
                                    equipmentInfo.getEquipmentName()!=null ||
                                    equipmentInfo.getEquipmentSituation()!=null ||
                                    equipmentInfo.getEquipmentComposite()!=null ||
                                    equipmentInfo.getEquipmentUse()!=null ||
                                    equipmentInfo.getFault()!=null) {
                                if (equipmentInfo.getEquipmentCategory().contains(trim) ||
                                        equipmentInfo.getSubordDetachment().contains(trim) ||
                                        equipmentInfo.getEquipmentName().contains(trim) ||
                                        equipmentInfo.getEquipmentSituation().contains(trim) ||
                                        equipmentInfo.getEquipmentComposite().contains(trim) ||
                                        equipmentInfo.getEquipmentUse().contains(trim) ||
                                        equipmentInfo.getFault().contains(trim)) {
                                    equipmentInfoList.add(equipmentInfo);
                                }
                            }

                        }
                    } catch (Exception e1) {
                        equipmentInfoList.clear();
                        e1.printStackTrace();
                    }
                    if (equipmentInfoList.size() == 0) {
                        mRvEquipmentInquiry.setVisibility(View.GONE);
                        mTvVehicleInquiry.setVisibility(View.VISIBLE);
                    } else {
                        mTvVehicleInquiry.setVisibility(View.GONE);
                        mRvEquipmentInquiry.setVisibility(View.VISIBLE);
                    }
                    initAdapter(equipmentInfoList);

                } else {
                    Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                }
            }
        });
    }


    private void initData() {
        BmobQuery<EquipmentInfo> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<EquipmentInfo>() {
            @Override
            public void done(List<EquipmentInfo> list, BmobException e) {
                if (e == null) {
                    List<EquipmentInfo> equipmentInfoList = new ArrayList<>();
                    equipmentInfoList = list;
                    if (equipmentInfoList.size() == 0) {
                        mRvEquipmentInquiry.setVisibility(View.GONE);
                        mTvVehicleInquiry.setVisibility(View.VISIBLE);
                    } else {
                        mTvVehicleInquiry.setVisibility(View.GONE);
                        mRvEquipmentInquiry.setVisibility(View.VISIBLE);
                    }
                    initAdapter(equipmentInfoList);
                } else {
                    Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                }
            }
        });
    }

    private void initAdapter(List<EquipmentInfo> carInfoList) {
        mRvEquipmentInquiry.setHasFixedSize(true);
        mRvEquipmentInquiry.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        mRvEquipmentInquiry.setAdapter(myAdapter);
        myAdapter.add(carInfoList);
    }

    public class MyAdapter extends RecyclerAdapter<EquipmentInfo> {
        @Override
        protected int getItemViewType(int position, EquipmentInfo equipmentInfo) {
            return R.layout.item_vehicle_inquiry;
        }

        @Override
        protected ViewHolder<EquipmentInfo> onCreateViewHolder(View root, int viewType) {
            return new MyAdapter.MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<EquipmentInfo> {
            public ImageView iv_equipment_picture;
            public TextView tv_equipment_type;
            public TextView tv_equipment_name;
            public TextView tv_equipment_license_plate;
            public TextView tv_equipment_status;
            public TextView tv_equipment_detachment;
            public CardView cd_vehicle;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.iv_equipment_picture = (ImageView) itemView.findViewById(R.id.iv_equipment_picture);
                this.tv_equipment_type = (TextView) itemView.findViewById(R.id.tv_equipment_type);
                this.tv_equipment_name = (TextView) itemView.findViewById(R.id.tv_equipment_name);
                this.tv_equipment_license_plate = (TextView) itemView.findViewById(R.id.tv_equipment_license_plate);
                this.tv_equipment_status = (TextView) itemView.findViewById(R.id.tv_equipment_status);
                this.tv_equipment_detachment = (TextView) itemView.findViewById(R.id.tv_equipment_detachment);
                this.cd_vehicle = (CardView) itemView.findViewById(R.id.cd_vehicle);
            }

            @Override
            protected void onBind(EquipmentInfo equipmentInfo, int position) {
                ImageUtils.setBitmapCenterCrop(equipmentInfo.getFile().getUrl(), iv_equipment_picture);
                tv_equipment_type.setText("装备类型：" + equipmentInfo.getEquipmentCategory());
                tv_equipment_name.setText("装备名称：" + equipmentInfo.getEquipmentCategory());
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
                String classificationName = equipmentInfo.getEquipmentCategory();
                tv_equipment_detachment.setText("装备所属支队：" + equipmentInfo.getSubordDetachment());

                //跳转到状态页面
                cd_vehicle.setOnClickListener(v -> {
                    if (AppClient.statuManager) {
                        Intent intent = new Intent(getActivity(), StatusEquipmentActivity.class);
                        intent.putExtra("equipmentInfo", equipmentInfo);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
