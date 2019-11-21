package com.firemanagement.db;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

//车辆信息
public class CarInfo extends BmobObject implements Serializable {

    //装备类别
    private String EquipmentCategory;
    //装备名称
    private String ClassificationName;
    //所属支队
    private String SubordDetachment;
    // 装备图片
    private BmobFile file;
    //装备车牌
    private String equipmentNumber;
    //车辆状态
    private int vehicleStatus;
    //审批状态
    private String approvalStatus;
    //故障项
    private String fault;
    //报停或或回复执勤
    private int stopOrStart;

    public int getStopOrStart() {
        return stopOrStart;
    }

    public void setStopOrStart(int stopOrStart) {
        this.stopOrStart = stopOrStart;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public String getEquipmentCategory() {
        return EquipmentCategory;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        EquipmentCategory = equipmentCategory;
    }

    public String getClassificationName() {
        return ClassificationName;
    }

    public void setClassificationName(String classificationName) {
        ClassificationName = classificationName;
    }

    public String getSubordDetachment() {
        return SubordDetachment;
    }

    public void setSubordDetachment(String subordDetachment) {
        SubordDetachment = subordDetachment;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public int getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(int vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }
}
