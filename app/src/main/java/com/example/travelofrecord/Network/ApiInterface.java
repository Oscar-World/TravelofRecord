package com.example.travelofrecord.Network;

import com.example.travelofrecord.Data.Chat;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Data.User;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;


// 레트로핏 인터페이스
public interface ApiInterface {

    // 회원가입
    @Multipart
    @POST("mysql_UserInfo_Insert.php")
    Call<String> insertInfo (
            @Part MultipartBody.Part uploaded_file,
            @PartMap Map<String, RequestBody> map
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

    @Multipart
    @POST("mysql_UserInfo_UpdateProfile.php")
    Call<String> updateProfile (
            @Part MultipartBody.Part uploaded_file,
            @PartMap Map<String, RequestBody> map
    );

    @Multipart
    @POST("mysql_UserInfo_UpdateProfile.php")
    Call<String> updateMemo (
            @PartMap Map<String, RequestBody> map
    );

    // 회원 탈퇴
    @GET("mysql_UserInfo_Delete.php")
    Call<String> deleteUser (
            @Query("nickname") String nickname
    );

    @Multipart
    @POST("mysql_Post_Insert.php")
    Call<String> insertFeed(
            @Part MultipartBody.Part uploaded_file,
            @PartMap Map<String, RequestBody> map
    );

    // 게시글 추가
    @GET("mysql_Post_Insert.php")
    Call<PostData> insertFeed(
            @Query("postNickname") String postNickname,
            @Query("profileImage") String profileImage,
            @Query("heart") int heart,
            @Query("commentNum") int commentNum,
            @Query("location") String location,
            @Query("postImage") String postImage,
            @Query("writing") String writing,
            @Query("dateCreated") String dateCreated
            );

    // 게시글 데이터 가져오기 (메인 피드)
    @GET("mysql_GetPostInfo.php")
    Call<ArrayList<PostData>> getPosts(
            @Query("nickname") String nickname,
            @Query("pageNum") int pageNum
    );

    // 게시글 데이터 가져오기 (게시글 내부)
    @GET("mysql_GetPostInfo.php")
    Call<ArrayList<PostData>> getPost(
            @Query("nickname") String nickname,
            @Query("num") int num
    );

    @GET("mysql_Post_Delete.php")
    Call<String> deletePost(
            @Query("num") int num
    );

    // 좋아요 눌렀을 때 추가
    @GET("mysql_Heart_Insert.php")
    Call<PostData> insertWhoLike(
            @Query("postNum") int postNum,
            @Query("whoLike") String whoLike,
            @Query("heart") int heart,
            @Query("dateLiked") String dateLiked
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
            @Query("commentNum") int commentNum,
            @Query("postNum") int postNum
    );

    // 프로필 정보 가져오기
    @GET("mysql_GetProfileInfo.php")
    Call<ArrayList<PostData>> getProfile(
            @Query("postNickname") String nickname
    );

    // 채팅방 이름 가져오기
    @GET("mysql_GetRoomNum.php")
    Call<Chat> getRoomNum(
            @Query("roomNum1") String roomNum1,
            @Query("roomNum2") String roomNum2
    );

    // 채팅 내용 가져오기
    @GET("mysql_GetChatting.php")
    Call<ArrayList<Chat>> getChatting(
            @Query("roomNum") String roomNum,
            @Query("sender") String sender
    );

    // 채팅 내용 추가하기
    @GET("mysql_Chat_Insert.php")
    Call<String> insertChatting(
            @Query("roomNum") String roomNum,
            @Query("sender") String sender,
            @Query("receiver") String receiver,
            @Query("senderImage") String senderImage,
            @Query("message") String message,
            @Query("dateMessage") String dateMessage,
            @Query("messageStatus") String messageStatus,
            @Query("fcmToken") String fcmToken
    );

    // 채팅방 정보 가져오기
    @GET("mysql_GetRoomInfo.php")
    Call<ArrayList<Chat>> getRoom(
            @Query("nickname") String nickname
    );

    // 사용자 FCM 토큰 가져오기
    @GET("mysql_GetFcmToken.php")
    Call<String> getFcmToken(
            @Query("nickname") String nickname
    );

    // 사용자 FCM 토큰 수정하기
    @GET("mysql_UpdateFcmToken.php")
    Call<String> updateFcmToken(
            @Query("nickname") String nickname,
            @Query("fcmToken") String fcmToken
    );

    // 채팅방 추가하기
    @GET("mysql_ChatRoom_Insert.php")
    Call<String> insertChatRoom(
            @Query("chatRoomNum") String chatRoomNum,
            @Query("chatRoomUser1") String chatRoomUser1,
            @Query("chatRoomUser2") String chatRoomUser2,
            @Query("chatRoomMessage") String chatRoomMessage,
            @Query("chatRoomDateMessage") String chatRoomDateMessage
    );

    // 안 읽은 채팅 알람 개수 가져오기
    @GET("mysql_GetNoti.php")
    Call<String> getNoti(
            @Query("nickname") String nickname
    );

    // 좋아요 누른 사람 목록 가져오기
    @GET("mysql_GetHeartList.php")
    Call<ArrayList<User>> getHeartList(
            @Query("postNum") int postNum
    );

    // 사용자가 받은 좋아요 개수 가져오기
    @GET("mysql_GetHeartNum.php")
    Call<ArrayList<PostData>> getHeartNum(
            @Query("nickname") String nickname
    );

    // 랭킹 목록 가져오기
    @GET("mysql_GetRank.php")
    Call<ArrayList<PostData>> getRanking();

}