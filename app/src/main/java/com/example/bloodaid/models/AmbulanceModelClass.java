package com.example.bloodaid.models;

public class AmbulanceModelClass {
    private Integer AmbulanceId;
    private String Name;
    private String Mobile;
    private String District;
    private String Email;
    private String Details;

    public Integer getAmbulanceId() {
        return AmbulanceId;
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

    public String getEmail() {
        return Email;
    }

    public String getDetails() {
        return Details;
    }


    public void setAmbulanceId(Integer ambulanceId) {
        AmbulanceId = ambulanceId;
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

    public void setEmail(String email) {
        Email = email;
    }

    public void setDetails(String details) {
        Details = details;
    }
}
