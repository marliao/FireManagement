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
import com.firemanagement.activities.Customer.StatusVehicleActivity;
import com.firemanagement.db.VehicleInfo;
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
public class VehicleInquiryFragment extends BaseFragment {

    private RecyclerView mRvVehicleInquiry;
    private TextView mTvVehicleInquiry;
    private EditText mEtSearch;
    private ImageView mIvSearch;
    private List<VehicleInfo> vehicleInfoList;


    @Override
    protected int getLayout() {
        return R.layout.fragment_vehicle_inquiry;
    }

    @Override
    protected void initView(View view) {
        mRvVehicleInquiry = (RecyclerView) view.findViewById(R.id.rv_vehicle_inquiry);
        mTvVehicleInquiry = (TextView) view.findViewById(R.id.tv_vehicle_inquiry);
        mEtSearch = (EditText) view.findViewById(R.id.et_search);
        mIvSearch = (ImageView) view.findViewById(R.id.iv_search);
    }

    @Override
    protected void init() {
        initlistener();
    }

    @Override
    public void onStart() {
        super.onStart();
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
        BmobQuery<VehicleInfo> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<VehicleInfo>() {
            @Override
            public void done(List<VehicleInfo> list, BmobException e) {
                if (e == null) {
                    vehicleInfoList = new ArrayList<>();
                    try {
                        for (VehicleInfo vehicleInfo : list) {
                            //查询所有数据利用string的contains函数模糊判断，实现模糊查询
                            if (vehicleInfo.getVehicleNumber() != null ||
                                    vehicleInfo.getVehicleName() != null ||
                                    vehicleInfo.getVehicleClass() != null ||
                                    vehicleInfo.getSubordDetachment() != null ||
                                    vehicleInfo.getSpecificateModel() != null) {
                                if (vehicleInfo.getVehicleNumber().contains(trim) ||
                                        vehicleInfo.getVehicleName().contains(trim) ||
                                        vehicleInfo.getVehicleClass().contains(trim) ||
                                        vehicleInfo.getSubordDetachment().contains(trim) ||
                                        vehicleInfo.getSpecificateModel().contains(trim)) {
                                    vehicleInfoList.add(vehicleInfo);
                                }
                            }
                        }
                    } catch (Exception e1) {
                        vehicleInfoList.clear();
                        e1.printStackTrace();
                    }
                    if (vehicleInfoList.size() == 0) {
                        mRvVehicleInquiry.setVisibility(View.GONE);
                        mTvVehicleInquiry.setVisibility(View.VISIBLE);
                    } else {
                        mTvVehicleInquiry.setVisibility(View.GONE);
                        mRvVehicleInquiry.setVisibility(View.VISIBLE);
                    }
                    initAdapter(vehicleInfoList);
                } else {
                    Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                }
            }
        });
    }

    private void initData() {
        BmobQuery<VehicleInfo> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<VehicleInfo>() {
            @Override
            public void done(List<VehicleInfo> list, BmobException e) {
                if (e == null) {
                    vehicleInfoList = new ArrayList<>();
                    vehicleInfoList = list;
                    if (vehicleInfoList.size() == 0) {
                        mRvVehicleInquiry.setVisibility(View.GONE);
                        mTvVehicleInquiry.setVisibility(View.VISIBLE);
                    } else {
                        mTvVehicleInquiry.setVisibility(View.GONE);
                        mRvVehicleInquiry.setVisibility(View.VISIBLE);
                    }
                    initAdapter(vehicleInfoList);
                } else {
                    Log.i(TAG, "done: ------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(AppClient.mContext, e.getErrorCode());
                }
            }
        });
    }

    private void initAdapter(List<VehicleInfo> carInfoList) {
        mRvVehicleInquiry.setHasFixedSize(true);
        mRvVehicleInquiry.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        MyAdapter myAdapter = new MyAdapter();
        mRvVehicleInquiry.setAdapter(myAdapter);
        myAdapter.add(carInfoList);
    }

    public class MyAdapter extends RecyclerAdapter<VehicleInfo> {
        @Override
        protected int getItemViewType(int position, VehicleInfo vehicleInfo) {
            return R.layout.item_vehicle_inquiry;
        }

        @Override
        protected ViewHolder<VehicleInfo> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<VehicleInfo> {
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
                tv_equipment_detachment.setText("车辆所属支队：" + vehicleInfo.getSubordDetachment());

                //跳转到状态页面
                cd_vehicle.setOnClickListener(v -> {
                    if (AppClient.statuManager) {
                        Intent intent = new Intent(getActivity(), StatusVehicleActivity.class);
                        intent.putExtra("vehicleInfo", vehicleInfo);
                        startActivity(intent);
                    }
                });
            }
        }
    }

}
