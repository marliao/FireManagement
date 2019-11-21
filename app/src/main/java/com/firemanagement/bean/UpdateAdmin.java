package com.firemanagement.bean;

public class UpdateAdmin {
    private String name;
    private int adminStatus;
    private String adminObjectId;
    private int operate;

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(int adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getAdminObjectId() {
        return adminObjectId;
    }

    public void setAdminObjectId(String adminObjectId) {
        this.adminObjectId = adminObjectId;
    }
}
