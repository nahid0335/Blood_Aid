package com.example.bloodaid;

import com.example.bloodaid.models.UserModelClass;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BloodAidNotificationInterface {

    @FormUrlEncoded
    @POST("api/addtoken.php")
    Call<ResponseBody> addToken(
            @Field("userid") long userid,
            @Field("token") String token
    );

}
