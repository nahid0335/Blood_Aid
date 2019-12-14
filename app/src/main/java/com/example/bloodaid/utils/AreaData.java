package com.example.bloodaid.utils;

import java.util.HashMap;

public class AreaData {
    HashMap<String, Integer> district = new HashMap<>();

    public AreaData(){
        district.put("Choose District", 0);
        district.put("Bagerhat", 31);
        district.put("Bandarban", 7);
        district.put("Barguna", 1);
        district.put("Barisal", 2);
        district.put("Bhola", 3);
        district.put("Bogra", 45);
        district.put("Brahmanbaria", 8);
        district.put("Chandpur", 9);
        district.put("Chapai Nawabganj", 49);
        district.put("Chittagong", 10);
        district.put("Chuadanga", 32);
        district.put("Comilla", 11);
        district.put("Cox's Bazar", 12);
        district.put("Dhaka", 18);
        district.put("Dinajpur", 53);
        district.put("Faridpur", 19);
        district.put("Feni", 13);
        district.put("Gaibandha", 54);
        district.put("Gazipur", 20);
        district.put("Gopalganj", 21);
        district.put("Habiganj", 61);
        district.put("Jamalpur", 41);
        district.put("Jessore", 33);
        district.put("Jhalokati", 4);
        district.put("Jhenaidah", 34);
        district.put("Joypurhat", 46);
        district.put("Khagrachhari", 14);
        district.put("Khulna", 35);
        district.put("Kishoreganj", 22);
        district.put("Kurigram", 55);
        district.put("Kushtia", 36);
        district.put("Lakshmipur", 15);
        district.put("Lalmonirhat", 56);
        district.put("Madaripur", 23);
        district.put("Magura", 37);
        district.put("Manikganj", 24);
        district.put("Meherpur", 38);
        district.put("Moulvibazar", 62);
        district.put("Munshiganj", 25);
        district.put("Mymensingh", 42);
        district.put("Naogaon", 47);
        district.put("Narail", 39);
        district.put("Narayanganj", 26);
        district.put("Narsingdi", 27);
        district.put("Natore", 48);
        district.put("Netrokona", 43);
        district.put("Nilphamari", 57);
        district.put("Noakhali", 16);
        district.put("Pabna", 50);
        district.put("Panchagarh", 58);
        district.put("Patuakhali", 5);
        district.put("Pirojpur", 6);
        district.put("Rajbari", 28);
        district.put("Rajshahi", 51);
        district.put("Rangamati", 17);
        district.put("Rangpur", 59);
        district.put("Satkhira", 40);
        district.put("Shariatpur", 29);
        district.put("Sherpur", 44);
        district.put("Sirajganj", 52);
        district.put("Sunamganj", 63);
        district.put("Sylhet", 64);
        district.put("Tangail", 30);
        district.put("Thakurgaon", 60);
    }

    public HashMap<String, Integer> getAreaData(){
        return district;
    }


    public Integer getAreaId(String ss){
        return district.get(ss);
    }

}
