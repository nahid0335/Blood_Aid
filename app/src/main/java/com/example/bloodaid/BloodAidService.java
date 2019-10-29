package com.example.bloodaid;

import com.example.bloodaid.models.DonorModelClass;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @GET("api/donorlist.php")
    Call<List<DonorModelClass>> donorList();

    @FormUrlEncoded
    @POST("api/sendrequest.php")
    Call<ResponseBody> bloodRequestSend(
        @Field("name")String name,
        @Field("phone")String phone,
        @Field("area_id")int area_id,
        @Field("blood_unit")int blood_unit,
        @Field("hospital")String hospital,
        @Field("reason")String reason,
        @Field("need_date")String need_date,
        @Field("blood_group")String blood_group
    );

    @FormUrlEncoded
    @POST("api/deleteDonorListItem.php")
    Call<ResponseBody> deleteDonor(
            @Field("donorid") Integer donorid
    );

}
