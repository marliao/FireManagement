package com.firemanagement.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 车辆信息表
 */
public class VehicleInfo extends BmobObject {
    private String vehicleName;
    private String vehicleNumber;
    private String specificateModel;
    private int vehicleStatus;
    private String vehicleClass;
    private String subordDetachment;
    private BmobFile file;
    //审批状态
    private String approvalStatus;
    //故障项
    private String fault;
    //报停或或回复执勤
    private int stopOrStart;

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public int getStopOrStart() {
        return stopOrStart;
    }

    public void setStopOrStart(int stopOrStart) {
        this.stopOrStart = stopOrStart;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getSpecificateModel() {
        return specificateModel;
    }

    public void setSpecificateModel(String specificateModel) {
        this.specificateModel = specificateModel;
    }

    public int getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(int vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public String getSubordDetachment() {
        return subordDetachment;
    }

    public void setSubordDetachment(String subordDetachment) {
        this.subordDetachment = subordDetachment;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }
}
