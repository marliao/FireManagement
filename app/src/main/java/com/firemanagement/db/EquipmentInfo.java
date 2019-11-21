package com.firemanagement.db;

import android.app.PendingIntent;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 装备信息表
 */
public class EquipmentInfo extends BmobObject {
    private String subordDetachment;//所属单位
    private String EquipmentName;//装备名称
    private String equipmentCategory;//装备类别
    private String packingClass;//装备分类
    private BmobFile file;//装备图片
    private String equipmentUse;//装备用途
    private String EquipmentComposite;//装备组成
    private String numberOfEquipment;//装备数量
    private String equipmentSituation;//装备用途
    private int equipmentStatus;//装备状态
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

    public String getSubordDetachment() {
        return subordDetachment;
    }

    public void setSubordDetachment(String subordDetachment) {
        this.subordDetachment = subordDetachment;
    }

    public String getEquipmentName() {
        return EquipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        EquipmentName = equipmentName;
    }

    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }

    public String getPackingClass() {
        return packingClass;
    }

    public void setPackingClass(String packingClass) {
        this.packingClass = packingClass;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public String getEquipmentUse() {
        return equipmentUse;
    }

    public void setEquipmentUse(String equipmentUse) {
        this.equipmentUse = equipmentUse;
    }

    public String getEquipmentComposite() {
        return EquipmentComposite;
    }

    public void setEquipmentComposite(String equipmentComposite) {
        EquipmentComposite = equipmentComposite;
    }

    public String getNumberOfEquipment() {
        return numberOfEquipment;
    }

    public void setNumberOfEquipment(String numberOfEquipment) {
        this.numberOfEquipment = numberOfEquipment;
    }

    public String getEquipmentSituation() {
        return equipmentSituation;
    }

    public void setEquipmentSituation(String equipmentSituation) {
        this.equipmentSituation = equipmentSituation;
    }

    public int getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(int equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }
}
