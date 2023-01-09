package com.example.joinme.api;

import com.example.joinme.DetailsForRecycleHistory;
import com.example.joinme.Group;
import com.example.joinme.User;
import com.example.joinme.UserRow;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @GET("presentUsersToBlock")
    Call<ArrayList<UserRow>> presentUsersToBlock(

    );

    //todo: the next functions not implement in the activitys

    @GET("presentMyCreatedHistory")
    Call<ArrayList<DetailsForRecycleHistory>> presentMyCreatedHistory(
            @Query("uid") String uid
    );

    @GET("presentMyJoinedHistory")
    Call<ArrayList<DetailsForRecycleHistory>> presentMyJoinedHistory(
            @Query("uid") String uid
    );

//    @FormUrlEncoded
//    @GET("presentUsersToBlock")
//    Call<ResponseBody> presentUsersToBlock(
//            @Field("uid") String uid
//    );


    @GET("presentReportedUsers")
    Call<ResponseBody> presentReportedUsers(
    );

    @GET("presentGroupParticipants")
    Call<ArrayList<UserRow>> presentGroupParticipants(
            @Query("gid") String gid
    );

    @GET("checkBlockedUser")
    Call<ResponseBody> checkBlockedUser(
            @Query("uid") String uid
    );

    @FormUrlEncoded
    @POST("addReportToUser")
    Call<ResponseBody> addReportToUser(
            @Field("uid")String uid
    );

    @FormUrlEncoded
    @POST("deleteUserGroups")
    Call<ResponseBody> deleteUserGroups(
            @Field("uid")String uid
    );

    @FormUrlEncoded
    @POST("deleteUserJoinedGroups")
    Call<ResponseBody> deleteUserJoinedGroups(
            @Field("uid")String uid
    );

    @FormUrlEncoded
    @POST("updateUserDetails")
    Call<ResponseBody> updateUserDetails(
            @Field("uid")String uid,
            @Field("name") String name,
            @Field("birth_date") String birth_date,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("updateGroupDetails")
    Call<ResponseBody> updateGroupDetails(
            @Field("gid")String gid,
            @Field("title") String title,
            @Field("city") String city,
            @Field("date") String date,
            @Field("time") String time,
            @Field("num_of_participant") String num_of_participant
    );

    @FormUrlEncoded
    @POST("addUser")
    Call<ResponseBody> addUser(
            @Field("user") User user
    );

    @FormUrlEncoded
    @POST("addGroup")
    Call<ResponseBody> addGroup(
            @Field("group") Group group
    );

    @FormUrlEncoded
    @POST("addUserToGroup")
    Call<ResponseBody> addUserToGroup(
            @Field("gid") String gid,
            @Field("uid") String uid
    );

    @FormUrlEncoded
    @POST("blockThisUser")
    Call<ResponseBody> blockThisUser(
            @Field("uid") String uid
    );

}
