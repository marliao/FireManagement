package com.firemanagement.db;

import cn.bmob.v3.BmobObject;

public class VehicleInspection extends BmobObject {
    // 车牌号
    private String CarNumber;
    //检查结果
    private String TestResult;
    //检查时间
    private String checkTheTime;
    //检查人
    private String checker;

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String carNumber) {
        CarNumber = carNumber;
    }

    public String getTestResult() {
        return TestResult;
    }

    public void setTestResult(String testResult) {
        TestResult = testResult;
    }

    public String getCheckTheTime() {
        return checkTheTime;
    }

    public void setCheckTheTime(String checkTheTime) {
        this.checkTheTime = checkTheTime;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }
}
