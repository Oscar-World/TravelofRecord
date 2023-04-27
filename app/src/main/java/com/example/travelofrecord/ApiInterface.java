package com.example.travelofrecord;

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

    // 회원 정보 가져오기 - 로그인 시, 홈 화면에 뿌려줌
    @GET("mysql_GetUserInfo.php")
    Call<User> getInfo (
            @Query("id") String id
    );

    // 내 프로필 수정하기
    @GET("mysql_UserInfo_UpdateProfile.php")
    Call<User> updateProfile (
            @Query("nickname") String nickname,
            @Query("memo") String memo,
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

    // 게시글 추가
    @GET("mysql_Post_Insert.php")
    Call<PostData> insertFeed(
            @Query("nickname") String nickname,
            @Query("profileImage") String profileImage,
            @Query("heart") int heart,
            @Query("commentNum") int commentNum,
            @Query("location") String location,
            @Query("postImage") String postImage,
            @Query("writing") String writing,
            @Query("dateCreated") String dateCreated
            );

    // 게시글 데이터 가져오기
    @GET("mysql_GetPostInfo.php")
    Call<ArrayList<PostData>> getPost();

    // 좋아요 눌렀을 때 추가
    @GET("mysql_Heart_Insert.php")
    Call<PostData> insertWhoLike(
            @Query("postNum") int postNum,
            @Query("whoLike") String whoLike,
            @Query("heart") int heart
    );

    // 좋아요 취소 눌렀을 때 삭제
    @GET("mysql_Heart_Delete.php")
    Call<PostData> deleteWhoLike(
            @Query("postNum") int postNum,
            @Query("whoLike") String whoLike,
            @Query("heart") int heart
    );

    // 좋아요 누른 게시글 불러오기
    @GET("mysql_GetHeartFeed.php")
    Call<ArrayList<PostData>> getHeart(
            @Query("nickname") String nickname
    );

    // 접속중인 유저가 올린 게시글 불러오기
    @GET("mysql_GetMyPost.php")
    Call<ArrayList<PostData>> getMyPost(
            @Query("nickname") String nickname
    );

    // 댓글 추가하기
    @GET("mysql_Comment_Insert.php")
    Call<String> insertComment(
            @Query("postNum") int postNum,
            @Query("profileImage") String profileImage,
            @Query("whoComment") String whoComment,
            @Query("dateComment") String dateComment,
            @Query("comment") String comment,
            @Query("commentNum") int commentNum
    );

    // 댓글 불러오기
    @GET("mysql_GetComment.php")
    Call<ArrayList<PostData>> getComment(
            @Query("postNum") int postNum
    );

    // 댓글 삭제하기
    @GET("mysql_Comment_Delete.php")
    Call<String> deleteComment(
            @Query("whoComment") String whoComment,
            @Query("dateComment") String dateComment,
            @Query("commentNum") int commentNum
    );


}