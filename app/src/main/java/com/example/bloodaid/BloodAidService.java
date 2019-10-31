package com.example.bloodaid;

import com.example.bloodaid.models.AmbulanceModelClass;
import com.example.bloodaid.models.AmbulanceRequestModelClass;
import com.example.bloodaid.models.DonorModelClass;
import com.example.bloodaid.models.DonorRequestModelClass;
import com.example.bloodaid.models.HospitalModelClass;
import com.example.bloodaid.models.HospitalRequestModelClass;
import com.example.bloodaid.models.OrganizationModelClass;
import com.example.bloodaid.models.OrganizationRequestModelClass;

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

    //Frontend Start


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


    //Frontend End






    //Backend Start

    //Admin Login Start

    @FormUrlEncoded
    @POST("api/adminLogin.php")
    Call<ResponseBody> loginAdmin(
            @Field("phone") String phone,
            @Field("password") String password
    );

    //Admin Login End


    //Donor List start

    @GET("api/readDonorList.php")
    Call<List<DonorModelClass>> donorList();

    @FormUrlEncoded
    @POST("api/deleteDonorListItem.php")
    Call<ResponseBody> deleteDonor(
            @Field("donorid") Integer donorid
    );

    //Donor List end



    //Donor Request Start

    @GET("api/readDonorRequest.php")
    Call<List<DonorRequestModelClass>> donorRequestList();

    @FormUrlEncoded
    @POST("api/deleteDonorRequestItem.php")
    Call<ResponseBody> deleteDonorRequest(
            @Field("donorrequestid") Integer donorrequestid
    );

    @FormUrlEncoded
    @POST("api/acceptDonorRequestItem.php")
    Call<ResponseBody> acceptDonorRequest(
            @Field("donorrequestid") Integer donorrequestid
    );

    //Donor Request end


    //Ambulance List start
    @GET("api/readAmbulanceList.php")
    Call<List<AmbulanceModelClass>> ambulanceList();

    @FormUrlEncoded
    @POST("api/deleteAmbulanceListItem.php")
    Call<ResponseBody> deleteAmbulance(
            @Field("ambulanceid") Integer ambulanceid
    );
    //Ambulance List end


    //Ambulance Request Start
    @GET("api/readAmbulanceRequest.php")
    Call<List<AmbulanceRequestModelClass>> ambulanceRequestList();

    @FormUrlEncoded
    @POST("api/deleteAmbulanceRequestItem.php")
    Call<ResponseBody> deleteAmbulanceRequest(
            @Field("ambulancerequestid") Integer ambulancerequestid
    );

    @FormUrlEncoded
    @POST("api/acceptAmbulanceRequestItem.php")
    Call<ResponseBody> acceptAmbulanceRequest(
            @Field("ambulancerequestid") Integer ambulancerequestid
    );
    //Ambulance Request End


    //Hospital List start
    @GET("api/readHospitalList.php")
    Call<List<HospitalModelClass>> hospitalList();

    @FormUrlEncoded
    @POST("api/deleteHospitalListItem.php")
    Call<ResponseBody> deleteHospital(
            @Field("hospitalid") Integer hospitalid
    );
    //Hospital List end


    //Hospital Request Start
    @GET("api/readHospitalRequest.php")
    Call<List<HospitalRequestModelClass>> hospitalRequestList();

    @FormUrlEncoded
    @POST("api/deleteHospitalRequestItem.php")
    Call<ResponseBody> deleteHospitalRequest(
            @Field("hospitalrequestid") Integer hospitalrequestid
    );

    @FormUrlEncoded
    @POST("api/acceptHospitalRequestItem.php")
    Call<ResponseBody> acceptHospitalRequest(
            @Field("hospitalrequestid") Integer hospitalrequestid
    );
    //Hospital Request End



    //Organization List start
    @GET("api/readOrganizationList.php")
    Call<List<OrganizationModelClass>> organizationList();

    @FormUrlEncoded
    @POST("api/deleteOrganizationListItem.php")
    Call<ResponseBody> deleteOrganization(
            @Field("organizationlid") Integer organizationid
    );
    //Organization List end


    //Organization Request Start
    @GET("api/readOrganizationRequest.php")
    Call<List<OrganizationRequestModelClass>> organizationRequestList();

    @FormUrlEncoded
    @POST("api/deleteOrganizationRequestItem.php")
    Call<ResponseBody> deleteOrganizationRequest(
            @Field("organizationrequestid") Integer organizationrequestid
    );

    @FormUrlEncoded
    @POST("api/acceptOrganizationRequestItem.php")
    Call<ResponseBody> acceptOrganizationRequest(
            @Field("organizationrequestid") Integer organizationrequestid
    );
    //Organization Request End





    //Backend End

}
