package com.example.bloodaid.models;

public class AdminRequestModelClass {
    private Integer AdminRequestId;
    private String Name;
    private String Mobile;
    private String District;
    private String Email;

    public Integer getAdminRequestId() {
        return AdminRequestId;
    }

    public void setAdminRequestId(Integer adminRequestId) {
        AdminRequestId = adminRequestId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
