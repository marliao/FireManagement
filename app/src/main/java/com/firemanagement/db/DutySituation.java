package com.firemanagement.db;

import cn.bmob.v3.BmobObject;

public class DutySituation extends BmobObject {
    //执勤人员ID
    private String UserObjectId;
    //接班时间
    private String SuccessionTime;
    //岗位状态
    private String OnDutySituation;
    //车辆ID
    private String CarObjectId;
    //车辆名称
    private String CarName;
    //车辆状态
    private String CarSituation;

    public String getUserObjectId() {
        return UserObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        UserObjectId = userObjectId;
    }

    public String getSuccessionTime() {
        return SuccessionTime;
    }

    public void setSuccessionTime(String successionTime) {
        SuccessionTime = successionTime;
    }

    public String getOnDutySituation() {
        return OnDutySituation;
    }

    public void setOnDutySituation(String onDutySituation) {
        OnDutySituation = onDutySituation;
    }

    public String getCarObjectId() {
        return CarObjectId;
    }

    public void setCarObjectId(String carObjectId) {
        CarObjectId = carObjectId;
    }

    public String getCarName() {
        return CarName;
    }

    public void setCarName(String carName) {
        CarName = carName;
    }

    public String getCarSituation() {
        return CarSituation;
    }

    public void setCarSituation(String carSituation) {
        CarSituation = carSituation;
    }
}
