package com.firemanagement.activities.Customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firemanagement.R;
import com.firemanagement.activities.Admin.OnDutyPersonActivity;
import com.firemanagement.activities.Admin.VehicleEquipmentActivity;
import com.firemanagement.activities.BaseActivity;
import com.firemanagement.db.CarInfo;
import com.firemanagement.db.Admin;
import com.firemanagement.db.DutySituation;
import com.firemanagement.db.DutySituationLog;
import com.firemanagement.db.User;
import com.firemanagement.db.VehicleEquipment;
import com.firemanagement.db.VehicleInfo;
import com.firemanagement.utils.ImageUtils;
import com.firemanagement.utils.T;
import com.firemanagement.view.CustomTitleBar;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


//交接班  执勤情况DutySituation
public class HandoverActivity extends BaseActivity {
    private ImageView ivUIcon;
    private TextView tvOnDutyName;
    private TextView tvOnDutyTime;
    private TextView tvOnDutyStatus;
    private ImageView ivCarIcon;
    private TextView tvCarDutyNumber;
    private TextView tvOnCarNumber;
    private TextView tvOnCarStatus;
    private Button btHandover;
    private Button btSuccession;
    private DutySituation dutySituation;
    private User user;
    private boolean isduty = false;
    private CustomTitleBar tv_title_handover;
    private ProgressDialog progressDialog;
    private TextView tv_more_car;
    private TextView tv_more_person;

    @Override
    protected int getLayout() {
        return R.layout.activity_handover;
    }

    @Override
    protected void initLayout() {
        ivUIcon = (ImageView) findViewById(R.id.iv_u_icon);
        tvOnDutyName = (TextView) findViewById(R.id.tv_on_duty_name);
        tvOnDutyTime = (TextView) findViewById(R.id.tv_on_duty_time);
        tvOnDutyStatus = (TextView) findViewById(R.id.tv_on_duty_status);
        ivCarIcon = (ImageView) findViewById(R.id.iv_car_icon);
        tvCarDutyNumber = (TextView) findViewById(R.id.tv_car_duty_number);
        tvOnCarNumber = (TextView) findViewById(R.id.tv_on_car_number);
        tvOnCarStatus = (TextView) findViewById(R.id.tv_on_car_status);
        tv_more_car = (TextView) findViewById(R.id.tv_more_car);
        tv_more_person = (TextView) findViewById(R.id.tv_more_person);
        btHandover = (Button) findViewById(R.id.bt_handover);
        btSuccession = (Button) findViewById(R.id.bt_succession);
        tv_title_handover = (CustomTitleBar) findViewById(R.id.tv_title_handover);
    }

    @Override
    protected void init() {
        user = BmobUser.getCurrentUser(User.class);
        progressDialog = new ProgressDialog(HandoverActivity.this);
        progressDialog.setMessage("load.....");
        progressDialog.show();
        GetDutySituation();
        InitListener();
    }

    private void InitListener() {
        tv_title_handover.setOnTitleClickListener(new CustomTitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        btHandover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isduty) {

                } else {
                    T.showShort("未接班");
                }
            }
        });
        btSuccession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userObjectId;
                String successionTime;
                if (isduty) {
                    T.showShort("已接班");
                } else {
                    boolean isfirst = false;
                    if (dutySituation == null) {
                        dutySituation = new DutySituation();
                        isfirst = true;
                    }
                    userObjectId = dutySituation.getUserObjectId();
                    successionTime = dutySituation.getSuccessionTime();
                    dutySituation.setUserObjectId(user.getObjectId());
                    dutySituation.setSuccessionTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()));
                    if (dutySituation.getOnDutySituation() == null) {
                        dutySituation.setOnDutySituation("正常");
                    }

                    if (dutySituation.getCarObjectId() == null) {
                        BmobQuery<VehicleInfo> query = new BmobQuery<>();
                        boolean finalIsfirst = isfirst;
                        query.findObjects(new FindListener<VehicleInfo>() {
                            @Override
                            public void done(List<VehicleInfo> Vehicle, BmobException e) {
                                VehicleInfo vehicleinfo = null;
                                if (Vehicle != null) {
                                    for (int i = 0; i < Vehicle.size(); i++) {

                                        if (Vehicle.get(i).getVehicleNumber() != null) {
                                            vehicleinfo = Vehicle.get(i);
                                        }
                                    }
                                    if (vehicleinfo != null) {
                                        dutySituation.setCarObjectId(vehicleinfo.getObjectId());
                                        if (dutySituation.getCarSituation() == null) {
                                            switch (vehicleinfo.getVehicleStatus()) {
                                                case 0:
                                                    dutySituation.setCarSituation("正常");
                                                    break;
                                                case 1:
                                                    dutySituation.setCarSituation("异常");
                                                    break;
                                                case 2:
                                                    dutySituation.setCarSituation("损坏");
                                                    break;
                                            }
                                        }
                                        dutySituation.setCarName(vehicleinfo.getVehicleName());
                                        UPdate(userObjectId, successionTime, finalIsfirst);
                                    }
                                }
                            }
                        });

                    } else {
                        UPdate(userObjectId, successionTime, isfirst);
                    }

                }
            }
        });
        tv_more_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HandoverActivity.this,VehicleEquipmentActivity.class));
            }
        });
        tv_more_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HandoverActivity.this, OnDutyPersonActivity.class);
                intent.putExtra("admin",false);
                startActivity(intent);
            }
        });
    }

    private void UPdate(String userObjectId, String successionTime, boolean isfirst) {
        if (isfirst) {
            dutySituation.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    GetDutySituation();
                }
            });
        } else {
            DutySituationLog dutySituationLog = new DutySituationLog();
            dutySituationLog.setUserObjectId(userObjectId);
            dutySituationLog.setUserObjectId1(dutySituation.getUserObjectId());
            dutySituationLog.setSuccessionTime(dutySituation.getSuccessionTime());
            dutySituationLog.setAfterGetOffWorkTime(successionTime);
            dutySituationLog.setCarName(dutySituationLog.getCarName());
            dutySituationLog.setCarObjectId(dutySituationLog.getCarObjectId());
            dutySituationLog.setCarSituation(dutySituation.getCarSituation());
            dutySituationLog.setOnDutySituation(dutySituation.getOnDutySituation());
            dutySituationLog.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                }
            });

            dutySituation.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    GetDutySituation();
                }
            });
        }
    }

    public static List<VehicleEquipment> list;

    private VehicleEquipment GetCar() {

        BmobQuery<VehicleEquipment> query = new BmobQuery<>();
        query.findObjects(new FindListener<VehicleEquipment>() {
            @Override
            public void done(List<VehicleEquipment> Vehicle, BmobException e) {
                list = Vehicle;
            }
        });
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                //AffiliationEquipment
                if (list.get(i).getAffiliEquipment().equals("车辆")) {
                    return list.get(i);
                }
            }
        }
        return null;
    }

    private void GetDutySituation() {
        BmobQuery<DutySituation> query = new BmobQuery<>();
        query.findObjects(new FindListener<DutySituation>() {
            @Override
            public void done(List<DutySituation> list, BmobException e) {
                if (list != null) {
                    dutySituation = list.get(0);
                }
                SetView();
            }


        });
    }

    private void SetView() {
        if (dutySituation != null) {
            if (dutySituation.getUserObjectId().equals(user.getObjectId())) {
                isduty = true;
                Setup(user);
            } else {
                isduty = false;
                BmobQuery<User> userBmobQuery = new BmobQuery<>();
                userBmobQuery.getObject(dutySituation.getUserObjectId(), new QueryListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
//                        Log.e("TAG", user.getObjectId() + user.toString());
                        if (user != null) {
                            Setup(user);
                        }
                    }
                });
            }
        }
        if (isduty) {
            btHandover.setBackgroundResource(R.drawable.bg_red_button);
            btSuccession.setBackgroundResource(R.drawable.bg_gray_button);
        } else {
            btSuccession.setBackgroundResource(R.drawable.bg_red_button);
            btHandover.setBackgroundResource(R.drawable.bg_gray_button);
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void Setup(User user) {
        BmobQuery<Admin> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("UserObjectId",user.getObjectId()).findObjects(new FindListener<Admin>() {
            @Override
            public void done(List<Admin> list, BmobException e) {
                if(list.size()>0){
                    Admin admin = list.get(0);
                    if (admin.getAvatar() != null) {
                        ImageUtils.setCircleBitmap(admin.getAvatar().getUrl(), ivUIcon);
                    } else {
                        ImageUtils.setCircleBitmap(R.drawable.user, ivUIcon);
                    }
                }
            }
        });

        tvOnDutyName.setText("当前执勤人员:" + user.getUsername());
        tvOnDutyStatus.setText("到岗情况:" + dutySituation.getOnDutySituation());
        tvOnDutyTime.setText("接班时间:" + dutySituation.getSuccessionTime());
        if (dutySituation.getCarObjectId() != null) {
            BmobQuery<VehicleInfo> query = new BmobQuery<>();
            query.getObject(dutySituation.getCarObjectId(), new QueryListener<VehicleInfo>() {
                @Override
                public void done(VehicleInfo vehicleEquipment, BmobException e) {
                    if (vehicleEquipment.getFile() != null) {
                        ImageUtils.setBitmapCenterCrop(vehicleEquipment.getFile().getUrl(), ivCarIcon);
                    }
                    switch (vehicleEquipment.getVehicleStatus()) {
                        case 0:
                            tvOnCarStatus.setText("车辆状态：正常");
                            break;
                        case 1:
                            tvOnCarStatus.setText("车辆状态：异常");
                            break;
                        case 2:
                            tvOnCarStatus.setText("车辆状态：损坏");
                            break;
                    }
                    tvCarDutyNumber.setText("车牌号:" + vehicleEquipment.getVehicleNumber());
                    tvOnCarNumber.setText("车辆名称:" + vehicleEquipment.getVehicleName());
                }
            });
        }

    }
}
