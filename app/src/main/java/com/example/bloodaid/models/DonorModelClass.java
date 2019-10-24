package com.example.bloodaid.models;


public class DonorModelClass {
   private String Name;
   private String Mobile;
   private String District;
   private String BloodGroup;
   private Integer DonateCount;
   private String LastDonate;
   private Integer Status;

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
