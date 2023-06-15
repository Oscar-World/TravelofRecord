package com.example.travelofrecord.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.travelofrecord.R;

public class Loading extends AppCompatActivity {

    String TAG = "로딩 액티비티";

    TextView Title_text;

    Thread inthread;
    Thread outthread;
    Thread dmthread;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String shared;
    String postNickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding);
        Log.d(TAG, "onCreate() 호출");

        setView();

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출");

        if (postNickname != null) {
            Log.d(TAG, "getIntent : " + postNickname);
            dmthread.start();
        } else {
            Log.d(TAG, "getIntent : null");
            if (!shared.equals("")) {
                Log.d(TAG, "메인으로 진입");
                inthread.start();
            } else {
                Log.d(TAG, "시작화면 진입");
                outthread.start();
            }
        }

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


    public class InThread extends Thread {
        public void run() {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(Loading.this, Home.class);
//            Intent i = new Intent(Loading.this,Start.class);
            startActivity(i);

            finish();
        }
    }

    public class OutThread extends Thread {
        public void run() {

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(Loading.this, Start.class);
            startActivity(i);

            finish();
        }
    }

    public class DmThread extends Thread {
        public void run() {

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(Loading.this, Home.class);
            i.putExtra("postNickname", postNickname);
            startActivity(i);

            finish();
        }
    }

    public void setView() {

        Title_text = findViewById(R.id.Title_text);

        inthread = new InThread();
        outthread = new OutThread();
        dmthread = new DmThread();

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        shared = sharedPreferences.getString("id","");

        Intent i = getIntent();
        postNickname = i.getStringExtra("postNickname");

    }


}