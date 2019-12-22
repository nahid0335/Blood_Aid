package com.example.bloodaid;

import com.example.bloodaid.models.NotificationModelClass;
import com.example.bloodaid.models.TopDonorModelClass;
import com.example.bloodaid.models.UserModelClass;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BloodAidNotificationInterface {

    @FormUrlEncoded
    @POST("api/addtoken.php")
    Call<ResponseBody> addToken(
            @Field("userid") long userid,
            @Field("token") String token
    );


    @GET("api/newnotificationcount.php")
    Call<ResponseBody> getNewNotificationCount();


    @GET("api/bloodrequestnotification.php")
    Call<ArrayList<NotificationModelClass>> getNotificationList();

    @FormUrlEncoded
    @POST("api/makeseennotificationitem.php")
    Call<ResponseBody> makeSeenNotificationItem(@Field("id") int id);

}
