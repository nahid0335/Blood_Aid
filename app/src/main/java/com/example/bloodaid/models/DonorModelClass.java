package com.example.bloodaid.models;


import java.util.ArrayList;

public class DonorModelClass {
    private Integer DonorId;
   private String Name;
   private String Mobile;
   private String District;
   private String BloodGroup;
   private Integer DonateCount;
   private String LastDonate;
   private Integer Status;

    public DonorModelClass(Integer donorId, String name, String mobile, String district, String bloodGroup, Integer donateCount, String lastDonate, Integer status) {
        DonorId = donorId;
        Name = name;
        Mobile = mobile;
        District = district;
        BloodGroup = bloodGroup;
        DonateCount = donateCount;
        LastDonate = lastDonate;
        Status = status;
    }

    public Integer getDonorId() {
        return DonorId;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getDistrict() {
        return District;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public Integer getDonateCount() {
        return DonateCount;
    }

    public String getLastDonate() {
        return LastDonate;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setDonorId(Integer donorId) {
        DonorId = donorId;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public void setDonateCount(Integer donateCount) {
        DonateCount = donateCount;
    }

    public void setLastDonate(String lastDonate) {
        LastDonate = lastDonate;
    }

    public void setStatus(Integer status) {
        Status = status;
    }
}


