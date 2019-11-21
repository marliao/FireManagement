package com.firemanagement.activities.Customer;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.firemanagement.AppClient;
import com.firemanagement.R;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.frags.Customer.EmergencyLinkageUnitInquiryFragment;
import com.firemanagement.frags.Customer.EquipmentInquiryFragment;
import com.firemanagement.frags.Customer.ExpertInquiryFragment;
import com.firemanagement.frags.Customer.VehicleInquiryFragment;
import com.firemanagement.view.CustomTitleBar;

/**
 * 查询管理
 */
public class QueryManagementActivity extends BaseActivity implements View.OnClickListener {

    private CustomTitleBar mCtbQueryManagement;
    private FrameLayout mFragmentQueryManagement;
    private LinearLayout mLlVehicleInquiry;
    private LinearLayout mLlEquipmentInquiry;
    private LinearLayout mLlExpertInquiry;
    private LinearLayout mLlEmergencyLinkageUnitInquiry;

    @Override
    public int getStatusBarColorResId() {
        return -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_querymanagement;
    }

    @Override
    protected void initLayout() {
        mCtbQueryManagement = (CustomTitleBar) findViewById(R.id.ctb_query_management);
        mFragmentQueryManagement = (FrameLayout) findViewById(R.id.fragment_query_management);
        mLlVehicleInquiry = (LinearLayout) findViewById(R.id.ll_vehicle_inquiry);
        mLlEquipmentInquiry = (LinearLayout) findViewById(R.id.ll_equipment_inquiry);
        mLlExpertInquiry = (LinearLayout) findViewById(R.id.ll_expert_inquiry);
        mLlEmergencyLinkageUnitInquiry = (LinearLayout) findViewById(R.id.ll_emergency_linkage_unit_inquiry);
    }

    @Override
    protected void init() {
        AppClient.statuManager = false;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_query_management, new VehicleInquiryFragment()).commit();
        mLlVehicleInquiry.setBackgroundColor(Color.parseColor("#E3F7FF"));
        mLlEquipmentInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
        mLlExpertInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
        mLlEmergencyLinkageUnitInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
        initlistener();
    }

    private void initlistener() {
        mCtbQueryManagement.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        mLlVehicleInquiry.setOnClickListener(this);
        mLlEquipmentInquiry.setOnClickListener(this);
        mLlExpertInquiry.setOnClickListener(this);
        mLlEmergencyLinkageUnitInquiry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_vehicle_inquiry://车辆查询
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_query_management, new VehicleInquiryFragment()).commit();
                mLlVehicleInquiry.setBackgroundColor(Color.parseColor("#E3F7FF"));
                mLlEquipmentInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlExpertInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlEmergencyLinkageUnitInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case R.id.ll_equipment_inquiry://装备查询
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_query_management, new EquipmentInquiryFragment()).commit();
                mLlVehicleInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlEquipmentInquiry.setBackgroundColor(Color.parseColor("#E3F7FF"));
                mLlExpertInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlEmergencyLinkageUnitInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case R.id.ll_expert_inquiry://专家查询
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_query_management, new ExpertInquiryFragment()).commit();
                mLlVehicleInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlEquipmentInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlExpertInquiry.setBackgroundColor(Color.parseColor("#E3F7FF"));
                mLlEmergencyLinkageUnitInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
            case R.id.ll_emergency_linkage_unit_inquiry://应急联动单位拆线呢
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_query_management, new EmergencyLinkageUnitInquiryFragment()).commit();
                mLlVehicleInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlEquipmentInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlExpertInquiry.setBackgroundColor(Color.parseColor("#ffffff"));
                mLlEmergencyLinkageUnitInquiry.setBackgroundColor(Color.parseColor("#E3F7FF"));
                break;
        }
    }
}
