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


    public Post(int num, String nickname, String profileImage, int heart, String location, String postImage, String writing, String dateCreated) {

        this.num = num;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.heart = heart;
        this.location = location;
        this.postImage = postImage;
        this.writing = writing;
        this.dateCreated = dateCreated;

    }

    public Post(String location, String postImage) {

        this.location = location;
        this.postImage = postImage;

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
}
