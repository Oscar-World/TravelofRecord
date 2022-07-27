package com.example.travelofrecord;

import android.graphics.Bitmap;

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

    @Expose
    @SerializedName("memo") private String memo;

    @Expose
    @SerializedName("text") private String text;

    @Expose
    @SerializedName("image") private String image;

    @Expose
    @SerializedName("bitmapImage") private Bitmap bitmapImage;

    @Expose
    @SerializedName("noid") private String noid;

    @Expose
    @SerializedName("nopw") private String nopw;



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
    public String getMemo() { return memo; }
    public String getTextContent() { return text; }
    public String getImage() { return image; }
    public Bitmap getBitmapImage() { return bitmapImage; }
    public String getNoid() { return noid; }
    public String getNopw() { return nopw; }

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
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public void setTextContent(String text) {
        this.text = text;
    }
    public void setImage(String image) { this.image = image; }
    public void setBitmapImage(Bitmap bitmapImage) { this.bitmapImage = bitmapImage; }
    public void setNoid(String noid) { this.noid = noid; }
    public void setNopw(String nopw) { this.nopw = nopw; }

}