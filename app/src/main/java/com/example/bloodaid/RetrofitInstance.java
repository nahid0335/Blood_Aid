package com.example.bloodaid;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private final static String BASE_URL = "https://bloodaid.hipranto.com/";
    private static Retrofit retrofit;
    private static RetrofitInstance mInstance;

    public RetrofitInstance(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static synchronized Retrofit getRetrofitInstance(){
        if(retrofit == null){
            new RetrofitInstance();
        }
        return retrofit;
    }
}
