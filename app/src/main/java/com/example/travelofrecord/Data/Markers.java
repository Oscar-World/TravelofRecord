package com.example.travelofrecord.Data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.Serializable;

import ted.gun0912.clustering.TedMarker;
import ted.gun0912.clustering.clustering.TedClusterItem;
import ted.gun0912.clustering.geometry.TedLatLng;
import ted.gun0912.clustering.naver.TedNaverClustering;

public class Markers implements Serializable, TedClusterItem {

    double LAT;
    double LNG;

    String location;
    String postImage;
    String writing;
    String dateCreated;
    int num;

    public Markers(double LAT, double LNG, String location, String postImage, String writing, String dateCreated, int num) {
        this.LAT = LAT;
        this.LNG = LNG;
        this.location = location;
        this.postImage = postImage;
        this.writing = writing;
        this.dateCreated = dateCreated;
        this.num = num;
    }


    @NonNull
    @Override
    public TedLatLng getTedLatLng() {
        return new TedLatLng(LAT, LNG);
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }

    public double getLNG() {
        return LNG;
    }

    public void setLNG(double LNG) {
        this.LNG = LNG;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
