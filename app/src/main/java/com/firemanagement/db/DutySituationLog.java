package com.firemanagement.db;

import cn.bmob.v3.BmobObject;

public class DutySituationLog extends BmobObject {
    //上个岗位人员ID
    private String UserObjectId;
    //执勤人员ID
    private String UserObjectId1;
    //下班时间
    private String SuccessionTime;
    //接班时间
    private String AfterGetOffWorkTime;
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

    public String getUserObjectId1() {
        return UserObjectId1;
    }

    public void setUserObjectId1(String userObjectId1) {
        UserObjectId1 = userObjectId1;
    }

    public String getSuccessionTime() {
        return SuccessionTime;
    }

    public void setSuccessionTime(String successionTime) {
        SuccessionTime = successionTime;
    }

    public String getAfterGetOffWorkTime() {
        return AfterGetOffWorkTime;
    }

    public void setAfterGetOffWorkTime(String afterGetOffWorkTime) {
        AfterGetOffWorkTime = afterGetOffWorkTime;
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
