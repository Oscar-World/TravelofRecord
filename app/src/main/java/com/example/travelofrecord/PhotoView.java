package com.example.travelofrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;

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

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출됨");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() 호출됨");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() 호출됨");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() 호출됨");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(TAG, "onRestart() 호출됨");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() 호출됨");
    }


    // -------------------------------------------------------------------------------------------


    public void setVariable() {

        photoView = findViewById(R.id.photoView);
        backBtn = findViewById(R.id.photoBack_Btn);

        Intent i = getIntent();
        image = i.getStringExtra("image");

    }

    public void setView() {

        Glide.with(this)
                .load(image)
                .into(photoView);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}