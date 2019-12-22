package com.example.bloodaid.models;

public class DonorPositionModelClass {
    private Integer DonorId;
    private Double Latitude;
    private Double Longitude;


    public DonorPositionModelClass(Integer donorId, Double latitude, Double longitude) {
        DonorId = donorId;
        Latitude = latitude;
        Longitude = longitude;
    }

    public Integer getDonorId() {
        return DonorId;
    }

    public void setDonorId(Integer donorId) {
        DonorId = donorId;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
