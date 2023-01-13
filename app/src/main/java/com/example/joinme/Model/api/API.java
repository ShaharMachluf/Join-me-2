package com.example.joinme.Model.api;

import com.example.joinme.Model.Category;
import com.example.joinme.Model.Contact;
import com.example.joinme.Model.DetailsForRecycleHistory;
import com.example.joinme.Model.Group;
import com.example.joinme.Model.User;
import com.example.joinme.Model.UserRow;

import java.util.ArrayList;

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

    @GET("presentMyCreatedHistory")
    Call<ArrayList<DetailsForRecycleHistory>> presentMyCreatedHistory(
            @Query("uid") String uid
    );

    @GET("presentMyJoinedHistory")
    Call<ArrayList<DetailsForRecycleHistory>> presentMyJoinedHistory(
            @Query("uid") String uid
    );


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

    @GET("getUserDetails")
    Call<User> getUserDetails(
            @Query("uid") String uid
    );

    @GET("getCategories")
    Call<ArrayList<Category>> getCategories(

    );

    @GET("getGroupDetails")
    Call<Group> getGroupDetails(
            @Query("gid") String gid
    );

    @GET("getGroups")
    Call<ArrayList<Contact>> getGroups(
            @Query("title") String title
    );

    @GET("getGroupsCity")
    Call<ArrayList<Contact>> getGroupsCity(
            @Query("title") String title,
            @Query("city") String city
    );

    @FormUrlEncoded
    @POST("addCategory")
    Call<ResponseBody> addCategory(
            @Field("category") String category
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
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("isHappened")
    Call<ResponseBody> isHappened(
            @Field("gid") String gid,
            @Field("flag") Boolean flag
    );

    @FormUrlEncoded
    @POST("addGroup")
    Call<ResponseBody> addGroup(
            @Field("title") String title,
            @Field("city") String city,
            @Field("time") String time,
            @Field("date") String date,
            @Field("head_uid") String head_uid,
            @Field("min") int min,
            @Field("max") int max
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
