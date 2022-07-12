package com.example.travelofrecord;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


// 레트로핏 유저
public class User {

    @Expose
    @SerializedName("type") private String type;

    @Expose
    @SerializedName("id") private String id;

    @Expose
    @SerializedName("pw") private String pw;

    @Expose
    @SerializedName("phone") private String phone;

    @Expose
    @SerializedName("nickname") private String nickname;

    public String getType() {
        return type;
    }
    public String getId() {
        return id;
    }
    public String getPw() {
        return pw;
    }
    public String getPhone() {
        return phone;
    }
    public String getNickname() {
        return nickname;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public  void setNickname(String nickname) {
        this.nickname = nickname;
    }


}