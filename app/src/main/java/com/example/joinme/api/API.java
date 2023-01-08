package com.example.joinme.api;

import com.example.joinme.UserRow;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {

    @GET("presentUsersToBlock")
    Call<ArrayList<UserRow>> presentUsersToBlock(

    );

    //todo: the next functions not implement in the activitys
    @FormUrlEncoded
    @GET("presentMyCreatedHistory")
    Call<ResponseBody> presentMyCreatedHistory(
            @Field("uid") String uid
    );

    @FormUrlEncoded
    @GET("presentUsersToBlock")
    Call<ResponseBody> presentUsersToBlock(
            @Field("uid") String uid
    );

    @FormUrlEncoded
    @GET("presentGroupParticipants")
    Call<ResponseBody> presentGroupParticipants(
            @Field("gid") String gid
    );

    @FormUrlEncoded
    @POST("addReportToUser")
    Call<ResponseBody> addReportToUser(
            @Field("uid")String uid
    );
}
