package com.firemanagement.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 权限表
 */
public class Admin extends BmobObject {
    private String UserObjectId;
    private int admin;//0 普通成员  1  管理员

    private int operate;//普通用户的操作权限  0  只能查看  1可以做任何操作

    private String username;

    private int sex;//0：女     1：男       2:性别不详

    private int age;
    private String phone;

    private String position;//职务

    private String SubordDetachment;//所属支队
    //头像
    private BmobFile avatar;
    //在位情况 0：不在位        1：在位
    private int PostStatus;

    //人员状态 ：人员不在位时填写，例：学习，住院等
    private String PersonnelStatus;

    private int accountStatus;//0： 注销       1：正常

    private String loginUserName;//登录用户名（User表的不可修改，不可查找属性)

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubordDetachment() {
        return SubordDetachment;
    }

    public void setSubordDetachment(String subordDetachment) {
        SubordDetachment = subordDetachment;
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

    public int getSex() {
        return sex;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public void setSex(int sex) {
        if (sex == 0) {
            this.sex = 2;
        } else {
            this.sex = sex;
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age > 100) {
            this.age = 18;
        } else {
            this.age = age;
        }
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUserObjectId() {
        return UserObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        UserObjectId = userObjectId;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "UserObjectId='" + UserObjectId + '\'' +
                ", admin=" + admin +
                ", sex=" + sex +
                ", age=" + age +
                ", position='" + position + '\'' +
                ", SubordDetachment='" + SubordDetachment + '\'' +
                ", avatar=" + avatar +
                ", PostStatus=" + PostStatus +
                ", PersonnelStatus='" + PersonnelStatus + '\'' +
                '}';
    }
}
