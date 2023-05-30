package com.example.travelofrecord.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @Expose
    @SerializedName("roomNum") public String roomNum;

    @Expose
    @SerializedName("sender") public String sender;

    @Expose
    @SerializedName("senderImage") public String senderImage;

    @Expose
    @SerializedName("message") public String message;

    @Expose
    @SerializedName("dateMessage") public String dateMessage;

    public int viewType;


    public Chat (String roomNum, String sender, String senderImage, String message, String dateMessage, int viewType) {

        this.roomNum = roomNum;
        this.sender = sender;
        this.senderImage = senderImage;
        this.message = message;
        this.dateMessage = dateMessage;
        this.viewType = viewType;

    }


    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(String dateMessage) {
        this.dateMessage = dateMessage;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

}
