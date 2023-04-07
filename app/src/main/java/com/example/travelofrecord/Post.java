package com.example.travelofrecord;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Post {

    @Expose
    @SerializedName("nickname") private String nickname;

    @Expose
    @SerializedName("profileImage") private String profileImage;

    @Expose
    @SerializedName("heart") private int heart;

    @Expose
    @SerializedName("location") private String location;

    @Expose
    @SerializedName("postImage") private String postImage;

    @Expose
    @SerializedName("writing") private String writing;

    @Expose
    @SerializedName("dateCreated") private String dateCreated;

    @Expose
    @SerializedName("response") private String response;

    @Expose
    @SerializedName("num") private int num;

    @Expose
    @SerializedName("data")  private ArrayList<Item_Post> data;

    public ArrayList<Item_Post> getData() {
        return data;
    }

    public void setData(ArrayList<Item_Post> data) {
        this.data = data;
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

}
