package com.firemanagement.db;

import cn.bmob.v3.BmobObject;

public class DutyOfficer extends BmobObject {
    private String UserObjectId;
    // 编号
    private String SerialNumber;
    //姓名
    private String name;
    // 性别
    private String sex;
    // 年龄
    private String age;
    // 职务
    private String position;
    // 手机号码
    private String telnumber;
    // 电子邮箱
    private String email;

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTelnumber() {
        return telnumber;
    }

    public void setTelnumber(String telnumber) {
        this.telnumber = telnumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
