package com.example.travelofrecord;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


// 레트로핏 인터페이스
public interface ApiInterface {

    @GET("mysql_Create.php")
    Call<String> insertInfo (
            @Query("type") String type,
            @Query("id") String id,
            @Query("pw") String pw,
            @Query("phone") String phone,
            @Query("nickname") String nickname,
            @Query("image") String image
    );

    @GET("mysql_Login.php")
    Call<User> getLoginInfo (
            @Query("id") String id,
            @Query("pw") String pw
    );

    @GET("mysql_IdCheck.php")
    Call<String> getIdCheck (
            @Query("id") String id
    );

    @GET("mysql_NicknameCheck.php")
    Call<String> getNicknameCheck (
            @Query("nickname") String nickname
    );

    @GET("mysql_PhoneCheck.php")
    Call<String> getPhoneCheck (
            @Query("phone") String nickname
    );

    @GET("mysql_NewPw.php")
    Call<String> getNewPw (
            @Query("id") String id,
            @Query("pw") String pw
    );

//    @GET("mysql_GetUser.php")
//    Call<User> getInfo (
//            @Query("type") String type,
//            @Query("id") String id,
//            @Query("pw") String pw,
//            @Query("phone") String phone,
//            @Query("nickname") String nickname,
//            @Query("image") String image
//    );

    @GET("mysql_GetUser.php")
    Call<User> getInfo (
            @Query("id") String id
    );


    @GET("mysql_Update.php")
    Call<User> updateInfo (
            @Query("type") String type,
            @Query("id") String id,
            @Query("pw") String pw,
            @Query("phone") String phone,
            @Query("nickname") String nickname
    );

    @GET("mysql_Delete.php")
    Call<User> deleteInfo (
            @Query("type") String type,
            @Query("id") String id,
            @Query("pw") String pw,
            @Query("phone") String phone,
            @Query("nickname") String nickname
    );

}