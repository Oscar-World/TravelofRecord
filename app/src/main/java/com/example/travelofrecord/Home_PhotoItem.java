package com.example.travelofrecord;

import android.graphics.Bitmap;

public class Home_PhotoItem {

    Bitmap home_Image;

    public Home_PhotoItem(Bitmap home_Image) {
        this.home_Image = home_Image;
    }

    public Bitmap getHomeImage() {
        return home_Image;
    }

    public void setHomeImage(Bitmap home_Image) {
        this.home_Image = home_Image;
    }

}
