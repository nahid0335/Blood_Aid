package com.example.bloodaid.models;

public class BloodRequestModelClass {
    String name;
    String phone;
    String district;
    String hospital;
    String reason;
    String blood_group;

    public BloodRequestModelClass(){}

    public BloodRequestModelClass(String name, String phone, String district, String hospital, String reason, String blood_group) {
        this.name = name;
        this.phone = phone;
        this.district = district;
        this.hospital = hospital;
        this.reason = reason;
        this.blood_group = blood_group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }
}
