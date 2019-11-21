package com.firemanagement.frags.Customer;


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

import com.firemanagement.R;
import com.firemanagement.db.EmergencyLU;
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
public class EmergencyLinkageUnitInquiryFragment extends BaseFragment {

    private EditText mEtSearch;
    private ImageView mIvSearch;
    private RecyclerView mRvEmergencyLincageUnitInquiry;
    private TextView mTvEmergencyLincageUnitInquiry;

    @Override
    protected int getLayout() {
        return R.layout.fragment_emergency_linkage_unit_inquiry;
    }

    @Override
    protected void initView(View view) {
        mEtSearch = (EditText) view.findViewById(R.id.et_search);
        mIvSearch = (ImageView) view.findViewById(R.id.iv_search);
        mRvEmergencyLincageUnitInquiry = (RecyclerView) view.findViewById(R.id.rv_emergency_lincage_unit_inquiry);
        mTvEmergencyLincageUnitInquiry = (TextView) view.findViewById(R.id.tv_emergency_lincage_unit_inquiry);
    }

    @Override
    protected void init() {
        initlistener();
        getAllEmergencyLinkageUnit();
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
        BmobQuery<EmergencyLU> query = new BmobQuery<>();
        query.setLimit(500);
        query.findObjects(new FindListener<EmergencyLU>() {
            @Override
            public void done(List<EmergencyLU> list, BmobException e) {
                if (e == null) {
                    List<EmergencyLU> emergencyLUList = null;
                    try {
                        emergencyLUList = new ArrayList<>();
                        for (EmergencyLU emergencyLU : list) {
                            //查询所有数据利用string的contains函数模糊判断，实现模糊查询
                            if (emergencyLU.getName() != null ||
                                    emergencyLU.getPersonnelStatus() != null ||
                                    emergencyLU.getSubordDetachment() != null) {
                                if (emergencyLU.getName().contains(trim) ||
                                        emergencyLU.getPersonnelStatus().contains(trim) ||
                                        emergencyLU.getSubordDetachment().contains(trim)) {
                                    emergencyLUList.add(emergencyLU);
                                }
                            }

                        }
                    } catch (Exception e1) {
                        emergencyLUList.clear();
                        e1.printStackTrace();
                    }
                    if (emergencyLUList.size() == 0) {
                        mRvEmergencyLincageUnitInquiry.setVisibility(View.GONE);
                        mTvEmergencyLincageUnitInquiry.setVisibility(View.VISIBLE
                        );
                    } else {
                        mTvEmergencyLincageUnitInquiry.setVisibility(View.GONE);
                        mRvEmergencyLincageUnitInquiry.setVisibility(View.VISIBLE);
                    }
                    initAdapter(emergencyLUList);
                } else {
                    Log.i(TAG, "done: -------------------" + e.getErrorCode() + "  " + e.getMessage());
                    ErrorRec.errorRec(getActivity(), e.getErrorCode());
                }
            }
        });
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
                    ErrorRec.errorRec(getActivity(), e.getErrorCode());
                }
            }
        });
    }

    private void initAdapter(List<EmergencyLU> emergencyLUList) {
        mRvEmergencyLincageUnitInquiry.setHasFixedSize(true);
        mRvEmergencyLincageUnitInquiry.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        final MyAdapter myAdapter = new MyAdapter();
        mRvEmergencyLincageUnitInquiry.setAdapter(myAdapter);
        myAdapter.add(emergencyLUList);
    }

    public class MyAdapter extends RecyclerAdapter<EmergencyLU> {

        @Override
        protected int getItemViewType(int position, EmergencyLU emergencyLU) {
            return R.layout.item_emergency_linkage_unit;
        }

        @Override
        protected ViewHolder<EmergencyLU> onCreateViewHolder(View root, int viewType) {
            return new MyViewHolder(root);
        }

        public class MyViewHolder extends RecyclerAdapter.ViewHolder<EmergencyLU> {
            public ImageView mIvEquipmentPicture;
            public TextView mTvEquipmentName;
            public TextView mTvEquipmentStatus;
            public TextView mTvEquipmentDetachment;
            public CardView mCdEmergencyLinkageUnit;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.mIvEquipmentPicture = (ImageView) itemView.findViewById(R.id.iv_equipment_picture);
                this.mTvEquipmentName = (TextView) itemView.findViewById(R.id.tv_equipment_name);
                this.mTvEquipmentStatus = (TextView) itemView.findViewById(R.id.tv_equipment_status);
                this.mTvEquipmentDetachment = (TextView) itemView.findViewById(R.id.tv_equipment_detachment);
                this.mCdEmergencyLinkageUnit = (CardView) itemView.findViewById(R.id.cd_emergency_linkage_unit);
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

            }
        }
    }

}
