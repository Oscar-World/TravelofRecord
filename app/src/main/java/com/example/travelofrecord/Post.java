package com.example.travelofrecord;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Post {

    @Expose
    @SerializedName("num") int num;

    @Expose
    @SerializedName("nickname") String nickname;

    @Expose
    @SerializedName("profileImage") String profileImage;

    @Expose
    @SerializedName("heart") int heart;

    @Expose
    @SerializedName("location") String location;

    @Expose
    @SerializedName("postImage") String postImage;

    @Expose
    @SerializedName("writing") String writing;

    @Expose
    @SerializedName("dateCreated") String dateCreated;

    @Expose
    @SerializedName("response") String response;

    @Expose
    @SerializedName("postNum") int postNum;

    @Expose
    @SerializedName("whoLike") String whoLike;

    @Expose
    @SerializedName("whoComment") String whoComment;

    @Expose
    @SerializedName("dateComment") String dateComment;

    @Expose
    @SerializedName("comment") String comment;

    @Expose
    @SerializedName("commentNum") int commentNum;

    boolean heartStatus;



    public Post(int num, String nickname, String profileImage, int heart, int commentNum, String location, String postImage, String writing, String dateCreated, int postNum, String whoLike, boolean heartStatus) {

        this.num = num;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.heart = heart;
        this.commentNum = commentNum;
        this.location = location;
        this.postImage = postImage;
        this.writing = writing;
        this.dateCreated = dateCreated;
        this.postNum = postNum;
        this.whoLike = whoLike;
        this.heartStatus = heartStatus;

    }

    public Post(String location, String postImage) {

        this.location = location;
        this.postImage = postImage;

    }

    public Post(int num, String nickname, String profileImage, int heart, int commentNum, String location, String postImage, String writing, String dateCreated) {

        this.num = num;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.heart = heart;
        this.commentNum = commentNum;
        this.location = location;
        this.postImage = postImage;
        this.writing = writing;
        this.dateCreated = dateCreated;

    }

    public Post(String profileImage, String whoComment, String dateComment, String comment) {

        this.profileImage = profileImage;
        this.whoComment = whoComment;
        this.dateComment = dateComment;
        this.comment = comment;

    }


    public String getNickname() {
        return nickname;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public int getHeart() { return heart; }
    public String getLocation() {
        return location;
    }
    public String getPostImage() {
        return postImage;
    }
    public String getWriting() { return writing; }
    public String getDateCreated() { return dateCreated; }
    public String getResponse() { return response; }
    public int getNum() { return num; }
    public int getPostNum() { return postNum; }
    public String getWhoLike() { return whoLike; }
    public boolean getHeartStatus() {return heartStatus; }
    public String getWhoComment() { return whoComment; }
    public String getDateComment() { return dateComment; }
    public String getComment() { return comment; }
    public int getCommentNum() { return commentNum; }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public void setHeart(int heart) {
        this.heart = heart;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
    public void setWriting(String writing) {
        this.writing = writing;
    }
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    public void setResponse(String response) { this.response = response; }
    public void setNum(int num) { this.num = num; }
    public void setPostNum(int postNum) { this.postNum = postNum; }
    public void setWhoLike(String whoLike) { this.whoLike = whoLike; }
    public void setHeartStatus(boolean heartStatus) { this.heartStatus = heartStatus; }
    public void setWhoComment(String whoComment) { this.whoComment = whoComment; }
    public void setDateComment(String dateComment) { this.dateComment = dateComment; }
    public void setComment(String comment) { this.comment = comment; }
    public void setCommentNum(int commentNum) { this.commentNum = commentNum; }

}
