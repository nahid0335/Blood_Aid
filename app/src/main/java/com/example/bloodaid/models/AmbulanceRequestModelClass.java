package com.example.bloodaid.models;

public class AmbulanceRequestModelClass {
    private Integer AmbulanceRequestId;
    private String Name;
    private String Mobile;
    private String District;
    private String Email;
    private String Details;

    public Integer getAmbulanceRequestId() {
        return AmbulanceRequestId;
    }

    public void setAmbulanceRequestId(Integer ambulanceRequestId) {
        AmbulanceRequestId = ambulanceRequestId;
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

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }
}
