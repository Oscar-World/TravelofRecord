package com.example.travelofrecord.Data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


// 레트로핏 userinfo getter / setter
public class User {

    @Expose
    @SerializedName("loginType") public String loginType;

    @Expose
    @SerializedName("id") public String id;

    @Expose
    @SerializedName("password") public String password;

    @Expose
    @SerializedName("phone") public String phone;

    @Expose
    @SerializedName("nickname") public String nickname;

    @Expose
    @SerializedName("memo") public String memo;

    @Expose
    @SerializedName("text") public String text;

    @Expose
    @SerializedName("imagePath") public String imagePath;

    @Expose
    @SerializedName("response") public String response;

    public String getType() {
        return loginType;
    }
    public String getId() {
        return id;
    }
    public String getPw() {
        return password;
    }
    public String getPhone() {
        return phone;
    }
    public String getNickname() {
        return nickname;
    }
    public String getMemo() { return memo; }
    public String getTextContent() { return text; }
    public String getImage() { return imagePath; }
    public String getResponse() { return response; }

    public void setType(String loginType) {
        this.loginType = loginType;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setPw(String password) {
        this.password = password;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public void setTextContent(String text) {
        this.text = text;
    }
    public void setImage(String imagePath) { this.imagePath = imagePath; }
    public void setResponse(String response) { this.response = response; }

}