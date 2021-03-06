package com.example.travelofrecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Start extends AppCompatActivity {

    String TAG = "시작 액티비티";

    TextView title_Text;
    Button login_Btn;
    Button start_Btn;
    ImageView imageView;

    Handler handler;
    Animation animation;
    TextThread textThread;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.d(TAG, "onCreate() 호출");

        setView();

        Glide.with(this).load(R.drawable.travel).into(imageView);
        textThread.start();

    }

    public class TextThread extends Thread {

        public void run() {
            for (int i = 1; i <= 50; i++) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        title_Text.startAnimation(animation);
                    }
                });

            }
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출");

        if (!shared.equals("")) {
            Log.d(TAG, "쉐어드 존재 O: " + shared);
            login_Btn.setVisibility(View.INVISIBLE);
            start_Btn.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "쉐어드 존재 X: " + shared);
            start_Btn.setVisibility(View.INVISIBLE);
            login_Btn.setVisibility(View.VISIBLE);
        }


        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Start.this,Login.class);
                String kakao = "kakao";
//                i.putExtra("kakao",kakao);
                startActivity(i);
                finish();
            }
        });

        start_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Start.this,Home.class);
                startActivity(i);
                finish();
            }
        });



    }

    public void setView() {

        title_Text = findViewById(R.id.Title_text);
        login_Btn = findViewById(R.id.login_Button);
        start_Btn = findViewById(R.id.start_Button);
        imageView = findViewById(R.id.imageView);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.start_text);
        handler = new Handler();
        textThread = new TextThread();

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        shared = sharedPreferences.getString("로그인", "");

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


}