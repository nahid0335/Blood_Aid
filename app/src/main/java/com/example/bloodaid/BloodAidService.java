package com.example.bloodaid;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BloodAidService {

    @FormUrlEncoded
    @POST("api/login.php")
    Call<ResponseBody> loginUser(
            @Field("phone") String phone,
            @Field("password") String password
    );



    @FormUrlEncoded
    @POST("api/registration.php")
    Call<ResponseBody> createUser(
        @Field("name")String name,
        @Field("phone")String phone,
        @Field("email")String email,
        @Field("password")String password,
        @Field("donar_status")String donar_status,
        @Field("area_id")long area_id,
        @Field("admin_status")String admin_status,
        @Field("latitude")Double latitude,
        @Field("longitude")Double longitude,
        @Field("blood_group")String blood_group
    );

    @FormUrlEncoded
    @POST("api/adminLogin.php")
    Call<ResponseBody> loginAdmin(
            @Field("phone") String phone,
            @Field("password") String password
    );

}
