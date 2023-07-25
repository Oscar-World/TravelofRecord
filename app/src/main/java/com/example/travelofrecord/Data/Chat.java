package com.example.travelofrecord.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @Expose
    @SerializedName("roomNum") public String roomNum;

    @Expose
    @SerializedName("sender") public String sender;

    @Expose
    @SerializedName("receiver") public String receiver;

    @Expose
    @SerializedName("senderImage") public String senderImage;

    @Expose
    @SerializedName("message") public String message;

    @Expose
    @SerializedName("dateMessage") public String dateMessage;

    @Expose
    @SerializedName("messageStatus") public String messageStatus;

    @Expose
    @SerializedName("roomName") public String roomName;

    @Expose
    @SerializedName("lastMessage") public String lastMessage;

    @Expose
    @SerializedName("lastDate") public String lastDate;

    @Expose
    @SerializedName("notReadMessage") public int notReadMessage;

    public int viewType;

    public boolean roomCheck;


    public Chat (String roomNum, String sender, String senderImage, String message, String dateMessage, int viewType, String messageStatus) {

        this.roomNum = roomNum;
        this.sender = sender;
        this.senderImage = senderImage;
        this.message = message;
        this.dateMessage = dateMessage;
        this.viewType = viewType;
        this.messageStatus = messageStatus;

    }

    public Chat (String roomName, String lastMessage, String lastDate, int notReadMessage, String senderImage) {

        this.roomName = roomName;
        this.lastMessage = lastMessage;
        this.lastDate = lastDate;
        this.notReadMessage = notReadMessage;
        this.senderImage = senderImage;

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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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

    public boolean getRoomCheck() { return roomCheck; }

    public void setRoomCheck(boolean roomCheck) { this.roomCheck = roomCheck; }

    public String getMessageStatus() { return messageStatus; }

    public void setMessageStatus(String messageStatus) { this.messageStatus = messageStatus; }

    public String getRoomName() { return roomName; }

    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getLastMessage() { return lastMessage; }

    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public String getLastDate() { return lastDate; }

    public void setLastDate(String lastDate) { this.lastDate = lastDate; }

    public int getNotReadMessage() { return notReadMessage; }

    public void setNotReadMessage(int notReadMessage) { this.notReadMessage = notReadMessage; }

}
