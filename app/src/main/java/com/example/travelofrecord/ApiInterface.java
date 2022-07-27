package com.example.travelofrecord;

import android.graphics.Bitmap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("mysql_UpdateMemo.php")
    Call<User> updateMemo (
            @Query("nickname") String nickname,
            @Query("memo") String memo
    );

    @GET("mysql_UpdateImage.php")
    Call<User> updateImage (
            @Query("nickname") String nickname,
            @Query("image") String image
    );

    @Multipart
    @POST("mysql_uploadFile.php")
    Call<String> uploadFile(
            @Part MultipartBody.Part uploaded_file
    );

    @GET("mysql_CreateFeed.php")
    Call<User> insertFeed(
            @Query("id") String id,
            @Query("text") String text,
            @Query("image") String image
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