package com.firemanagement.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 应急联动单位表
 */
public class EmergencyLU extends BmobObject {

    private String name;//应急联动单位名字

    //在位情况 0：不在位        1：在位
    private int PostStatus;

    //应急联动单位状态 ：维修 ，缺损等
    private String PersonnelStatus;

    // 应急联动单位照片
    private BmobFile file;

    private String subordDetachment;//所属支队

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPostStatus() {
        return PostStatus;
    }

    public void setPostStatus(int postStatus) {
        PostStatus = postStatus;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public String getPersonnelStatus() {
        return PersonnelStatus;
    }

    public void setPersonnelStatus(String personnelStatus) {
        PersonnelStatus = personnelStatus;
    }

    public String getSubordDetachment() {
        return subordDetachment;
    }

    public void setSubordDetachment(String subordDetachment) {
        this.subordDetachment = subordDetachment;
    }
}
