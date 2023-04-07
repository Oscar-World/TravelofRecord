package com.example.travelofrecord;

import android.graphics.Bitmap;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


// 레트로핏 인터페이스
public interface ApiInterface {

    // 회원가입
    @GET("mysql_UserInfo_Insert.php")
    Call<String> insertInfo (
            @Query("loginType") String loginType,
            @Query("id") String id,
            @Query("password") String password,
            @Query("phone") String phone,
            @Query("nickname") String nickname,
            @Query("imagePath") String imagePath
    );

    // 로그인
    @GET("mysql_UserInfo_Login.php")
    Call<User> getLoginInfo (
            @Query("id") String id,
            @Query("password") String password
    );

    // 아이디 중복 확인
    @GET("mysql_UserInfo_IdCheck.php")
    Call<String> getIdCheck (
            @Query("id") String id
    );

    // 닉네임 중복 확인
    @GET("mysql_UserInfo_NicknameCheck.php")
    Call<String> getNicknameCheck (
            @Query("nickname") String nickname
    );

    // 아이디 찾기 - 핸드폰 번호 확인
    @GET("mysql_UserInfo_PhoneCheck.php")
    Call<String> getPhoneCheck (
            @Query("phone") String nickname
    );

    // 비밀번호 찾기 - 비밀번호 재설정
    @GET("mysql_UserInfo_NewPw.php")
    Call<String> getNewPw (
            @Query("id") String id,
            @Query("password") String password
    );

//    @GET("mysql_GetUser.php")
//    Call<User> getInfo (
//            @Query("type") String type,
//            @Query("id") String id,
//            @Query("password") String password,
//            @Query("phone") String phone,
//            @Query("nickname") String nickname,
//            @Query("image") String image
//    );

    // 회원 정보 가져오기 - 로그인 시, 홈 화면에 뿌려줌
    @GET("mysql_GetUserInfo.php")
    Call<User> getInfo (
            @Query("id") String id
    );

    // 내 상태 메시지 수정하기
    @GET("mysql_UserInfo_UpdateMemo.php")
    Call<User> updateMemo (
            @Query("nickname") String nickname,
            @Query("memo") String memo
    );

    // 내 프로필 사진 변경하기
    @GET("mysql_UserInfo_UpdateImage.php")
    Call<User> updateImage (
            @Query("nickname") String nickname,
            @Query("imagePath") String imagePath
    );

    // 회원 탈퇴
    @GET("mysql_UserInfo_Delete.php")
    Call<String> deleteUser (
            @Query("id") String id
    );

    @Multipart
    @POST("mysql_uploadFile.php")
    Call<String> uploadFile(
            @Part MultipartBody.Part uploaded_file
    );

    @GET("mysql_Post_Insert.php")
    Call<Post> insertFeed(
            @Query("nickname") String nickname,
            @Query("profileImage") String profileImage,
            @Query("heart") int heart,
            @Query("location") String location,
            @Query("postImage") String postImage,
            @Query("writing") String writing,
            @Query("dateCreated") String dateCreated
            );

    @GET("mysql_GetPostInfo.php")
    Call<ArrayList<Post>> getPost();


    @GET("mysql_Update.php")
    Call<User> updateInfo (
            @Query("loginType") String loginType,
            @Query("id") String id,
            @Query("password") String password,
            @Query("phone") String phone,
            @Query("nickname") String nickname
    );

    @GET("mysql_Delete.php")
    Call<User> deleteInfo (
            @Query("loginType") String loginType,
            @Query("id") String id,
            @Query("password") String password,
            @Query("phone") String phone,
            @Query("nickname") String nickname
    );

}