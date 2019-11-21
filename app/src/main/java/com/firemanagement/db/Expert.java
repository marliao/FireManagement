package com.firemanagement.db;

import cn.bmob.v3.BmobObject;

/**
 * 专家表
 */
public class Expert extends BmobObject {
    private String name;
    private int sex;
    private String phone;

    private String position;//职务

    private String post;//岗位

    //在位情况 0：不在位        1：在位
    private int PostStatus;

    //人员状态 ：人员不在位时填写，例：学习，住院等
    private String PersonnelStatus;

    private String ExpertField;//专家领域

    private String subordDetachment;

    public String getSubordDetachment() {
        return subordDetachment;
    }

    public void setSubordDetachment(String subordDetachment) {
        this.subordDetachment = subordDetachment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getPostStatus() {
        return PostStatus;
    }

    public void setPostStatus(int postStatus) {
        PostStatus = postStatus;
    }

    public String getPersonnelStatus() {
        return PersonnelStatus;
    }

    public void setPersonnelStatus(String personnelStatus) {
        PersonnelStatus = personnelStatus;
    }

    public String getExpertField() {
        return ExpertField;
    }

    public void setExpertField(String expertField) {
        ExpertField = expertField;
    }
}
