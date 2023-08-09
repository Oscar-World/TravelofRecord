package com.example.travelofrecord.Data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import ted.gun0912.clustering.TedMarker;
import ted.gun0912.clustering.clustering.TedClusterItem;
import ted.gun0912.clustering.geometry.TedLatLng;
import ted.gun0912.clustering.naver.TedNaverClustering;

public class Markers implements TedClusterItem {

    double LAT;
    double LNG;
    String address;

    public Markers(double LAT, double LNG, String address) {
        this.LAT = LAT;
        this.LNG = LNG;
        this.address = address;
    }

    @NonNull
    @Override
    public TedLatLng getTedLatLng() {
        return new TedLatLng(LAT, LNG);
    }

}
