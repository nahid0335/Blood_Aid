package com.example.bloodaid.utils;

import java.util.HashMap;

public class AreaData {
    HashMap<String, Integer> district = new HashMap<>();

    public AreaData(){
        district.put("Choose District", 0);
        district.put("Khulna", 1);
        district.put("Gaibandha", 2);
        district.put("Rangpur", 3);
        district.put("Natore", 4);
        district.put("Rajshahi", 5);
    }

    public HashMap<String, Integer> getAreaData(){
        return district;
    }


    public Integer getAreaId(String ss){
        return district.get(ss);
    }

}
