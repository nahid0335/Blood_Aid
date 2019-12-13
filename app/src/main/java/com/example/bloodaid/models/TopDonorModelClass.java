package com.example.bloodaid.models;

public class TopDonorModelClass {
    private String name;
    private String district;
    private String bloodgroup;
    private String lastdonation;

    public TopDonorModelClass(String name, String district, String bloodgroup, String lastDonation) {
        this.name = name;
        this.district = district;
        this.bloodgroup = bloodgroup;
        this.lastdonation = lastDonation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getLastDonation() {
        return lastdonation;
    }

    public void setLastDonation(String lastDonation) {
        this.lastdonation = lastDonation;
    }
}
