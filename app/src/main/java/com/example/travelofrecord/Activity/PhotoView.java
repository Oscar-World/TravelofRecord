package com.example.travelofrecord.Activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.travelofrecord.R;

public class PhotoView extends AppCompatActivity {

    String TAG = "포토뷰 액티비티";

    com.github.chrisbanes.photoview.PhotoView photoView;
    ImageButton backBtn;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        Log.d(TAG, "onCreate() 호출됨");

        setVariable();
        setView();

    } // onCreate()



    // -------------------------------------------------------------------------------------------


    /*
    변수 초기화
     */
    public void setVariable() {

        photoView = findViewById(R.id.photoView);
        backBtn = findViewById(R.id.photoBack_Btn);

        Intent i = getIntent();
        image = i.getStringExtra("image");

    } // setVariable()


    /*
    뷰 초기화
     */
    public void setView() {

        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        Glide.with(this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(photoView);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    } // setView()

}