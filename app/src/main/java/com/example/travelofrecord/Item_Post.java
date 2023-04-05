package com.example.travelofrecord;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item_Post {

    String nickname;
    String profileImage;
    int heart;
    String location;
    String postImage;
    String writing;
    String dateCreated;

    public Item_Post() {

    }

    public Item_Post(String nickname, String profileImage, int heart, String location, String postImage, String writing, String dateCreated) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.heart = heart;
        this.location = location;
        this.postImage = postImage;
        this.writing = writing;
        this.dateCreated = dateCreated;

    }

    public String getProfileImage() { return profileImage; }

    public String getNickname() { return nickname; }

    public String getLocation() { return location; }

    public String getPostImage() { return postImage; }

    public String getWriting() { return writing; }

    public String getDateCreated() { return dateCreated; }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public void setLocation(String location) { this.location = location; }

    public void setPostImage(String postImage) { this.postImage = postImage; }

    public void setWriting(String writing) { this.writing = writing; }

    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }

}
