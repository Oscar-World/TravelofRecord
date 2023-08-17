package com.example.travelofrecord.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostData {

    @Expose
    @SerializedName("num") public int num;

    @Expose
    @SerializedName("postNickname") public String postNickname;

    @Expose
    @SerializedName("profileImage") public String profileImage;

    @Expose
    @SerializedName("heart") public int heart;

    @Expose
    @SerializedName("location") public String location;

    @Expose
    @SerializedName("postImage") public String postImage;

    @Expose
    @SerializedName("writing") public String writing;

    @Expose
    @SerializedName("dateCreated") public String dateCreated;

    @Expose
    @SerializedName("response") public String response;

    @Expose
    @SerializedName("postNum") public int postNum;

    @Expose
    @SerializedName("whoLike") public String whoLike;

    @Expose
    @SerializedName("whoComment") public String whoComment;

    @Expose
    @SerializedName("dateComment") public String dateComment;

    @Expose
    @SerializedName("comment") public String comment;

    @Expose
    @SerializedName("commentNum") public int commentNum;

    @Expose
    @SerializedName("commentProfileImage") public String commentProfileImage;

    public boolean heartStatus;

    @Expose
    @SerializedName("nickname") public String nickname;

    @Expose
    @SerializedName("imagePath") public String imagePath;

    @Expose
    @SerializedName("memo") public String memo;

    @Expose
    @SerializedName("pagingStatus") public String pagingStatus;

    @Expose
    @SerializedName("pageNum") public int pageNum;

    @Expose
    @SerializedName("dateLiked") public String dateLiked;

    public int rank;

    public int heartNum;



    public PostData(int rank, String profileImage, String postNickname, int heartNum) {

        this.rank = rank;
        this.profileImage = profileImage;
        this.postNickname = postNickname;
        this.heartNum = heartNum;

    }

    public PostData(int num, String postNickname, String profileImage, int heart, int commentNum, String location, String postImage, String writing, String dateCreated, int postNum, String whoLike, boolean heartStatus) {

        this.num = num;
        this.postNickname = postNickname;
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

    // getHeartFeed()
    public PostData(String location, String postImage) {

        this.location = location;
        this.postImage = postImage;

    }

    public PostData(int num, String postNickname, String profileImage, int heart, int commentNum, String location, String postImage, String writing, String dateCreated) {

        this.num = num;
        this.postNickname = postNickname;
        this.profileImage = profileImage;
        this.heart = heart;
        this.commentNum = commentNum;
        this.location = location;
        this.postImage = postImage;
        this.writing = writing;
        this.dateCreated = dateCreated;

    }

    // getComment()
    public PostData(String profileImage, String whoComment, String dateComment, String comment, int postNum, int commentNum) {

        this.commentProfileImage = profileImage;
        this.whoComment = whoComment;
        this.dateComment = dateComment;
        this.comment = comment;
        this.postNum = postNum;
        this.commentNum = commentNum;

    }

    public PostData(String nickname, String imagePath, String memo, int num, String postNickname, String profileImage, int heart, int commentNum, String location, String postImage, String writing, String dateCreated) {

        this.nickname = nickname;
        this.imagePath = imagePath;
        this.memo = memo;
        this.num = num;
        this.postNickname = postNickname;
        this.profileImage = profileImage;
        this.heart = heart;
        this.commentNum = commentNum;
        this.location = location;
        this.postImage = postImage;
        this.writing = writing;
        this.dateCreated = dateCreated;

    }


    public String getNickname() { return nickname; }
    public String getImagePath() { return imagePath; }
    public String getMemo() { return memo; }
    public String getPostNickname() {
        return postNickname;
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
    public String getCommentProfileImage() { return commentProfileImage; }
    public String getPagingStatus() { return pagingStatus; }
    public int getPageNum() { return pageNum; }
    public String getDateLiked() { return dateLiked; }
    public int getRank() { return rank; }
    public int getHeartNum() { return heartNum; }

    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setMemo(String memo) { this.memo = memo; }
    public void setPostNickname(String postNickname) {
        this.postNickname = postNickname;
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
    public void setCommentProfileImage(String commentProfileImage) { this.commentProfileImage = commentProfileImage; }
    public void setPagingStatus(String pagingStatus) { this.pagingStatus = pagingStatus; }
    public void setPageNum(int pageNum) { this.pageNum = pageNum; }
    public void setDateLiked(String dateLiked) { this.dateLiked = dateLiked; }
    public void setRank(int rank) { this.rank = rank; }
    public void setHeartNum(int heartNum) { this.heartNum = heartNum; }

}
