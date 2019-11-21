package com.firemanagement.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 车辆装备
 */
public class VehicleEquipment extends BmobObject {
    //车辆    装备器材    人员      灭火剂     内部专家    外部专家        战情保障单位      应急联动单位      执勤实力地图查询
    private String AffiliEquipment;
    // 车辆装备图片
    private BmobFile file;

    private String EquiCategory;//装备类别

    private String ClassifName;//类别名称(如果装备类别为人员或专家，则为他们的姓名）

    private String SubordDetachment;//所属支队

    private int quantity;//数量

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public String getAffiliEquipment() {
        return AffiliEquipment;
    }

    public void setAffiliEquipment(String affiliEquipment) {
        AffiliEquipment = affiliEquipment;
    }

    public String getEquiCategory() {
        return EquiCategory;
    }

    public void setEquiCategory(String equiCategory) {
        EquiCategory = equiCategory;
    }

    public String getClassifName() {
        return ClassifName;
    }

    public void setClassifName(String classifName) {
        ClassifName = classifName;
    }

    public String getSubordDetachment() {
        return SubordDetachment;
    }

    public void setSubordDetachment(String subordDetachment) {
        SubordDetachment = subordDetachment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
