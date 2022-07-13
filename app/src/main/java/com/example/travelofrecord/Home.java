package com.example.travelofrecord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.kakao.sdk.user.UserApiClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Home extends AppCompatActivity {

    String TAG = "홈 액티비티";

    ImageButton home_Btn;
    ImageButton heart_Btn;
    ImageButton add_Btn;
    ImageButton myProfile_Btn;

    ImageButton homeFull_Btn;
    ImageButton heartFull_Btn;
    ImageButton addFull_Btn;
    ImageButton myProfileFull_Btn;

    Button photo_Btn;
    Button map_Btn;

    ImageButton back_Btn;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_Kakao;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_Kakao;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    Fragment_Home fragment_home;
    Fragment_Heart fragment_heart;
    Fragment_add fragment_add;
    Fragment_myProfile fragment_myProfile;

    FrameLayout headFrame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate() 호출");

        setView();

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출");

        home_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.homeBody_Frame,fragment_home);
                transaction.commitAllowingStateLoss();

                headFrame.setVisibility(View.VISIBLE);

                home_Btn.setVisibility(View.GONE);
                homeFull_Btn.setVisibility(View.VISIBLE);

                heart_Btn.setVisibility(View.VISIBLE);
                add_Btn.setVisibility(View.VISIBLE);
                myProfile_Btn.setVisibility(View.VISIBLE);

                heartFull_Btn.setVisibility(View.GONE);
                addFull_Btn.setVisibility(View.GONE);
                myProfileFull_Btn.setVisibility(View.GONE);

            }
        });

        heart_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.homeBody_Frame,fragment_heart);
                transaction.commitAllowingStateLoss();

                headFrame.setVisibility(View.VISIBLE);

                heart_Btn.setVisibility(View.GONE);
                heartFull_Btn.setVisibility(View.VISIBLE);

                home_Btn.setVisibility(View.VISIBLE);
                add_Btn.setVisibility(View.VISIBLE);
                myProfile_Btn.setVisibility(View.VISIBLE);

                homeFull_Btn.setVisibility(View.GONE);
                addFull_Btn.setVisibility(View.GONE);
                myProfileFull_Btn.setVisibility(View.GONE);

            }
        });

        add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.homeBody_Frame,fragment_add);
                transaction.commitAllowingStateLoss();

                headFrame.setVisibility(View.INVISIBLE);

                add_Btn.setVisibility(View.GONE);
                addFull_Btn.setVisibility(View.VISIBLE);

                home_Btn.setVisibility(View.VISIBLE);
                heart_Btn.setVisibility(View.VISIBLE);
                myProfile_Btn.setVisibility(View.VISIBLE);

                homeFull_Btn.setVisibility(View.GONE);
                heartFull_Btn.setVisibility(View.GONE);
                myProfileFull_Btn.setVisibility(View.GONE);

            }
        });

        myProfile_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.homeBody_Frame,fragment_myProfile);
                transaction.commitAllowingStateLoss();

                headFrame.setVisibility(View.INVISIBLE);

                myProfile_Btn.setVisibility(View.GONE);
                myProfileFull_Btn.setVisibility(View.VISIBLE);

                home_Btn.setVisibility(View.VISIBLE);
                heart_Btn.setVisibility(View.VISIBLE);
                add_Btn.setVisibility(View.VISIBLE);

                homeFull_Btn.setVisibility(View.GONE);
                heartFull_Btn.setVisibility(View.GONE);
                addFull_Btn.setVisibility(View.GONE);

            }
        });


        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserApiClient.getInstance().unlink(error -> {
                    if (error != null) {
                        Log.d(TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error);
                    }else{
                        Log.d(TAG, "로그아웃 성공, SDK에서 토큰 삭제됨");
                    }
                    return null;
                });

                editor_Kakao.clear();
                editor_Kakao.commit();

                editor.clear();
                editor.commit();

                Intent i = new Intent(Home.this,Start.class);
                startActivity(i);

                finish();
            }
        });

    }

    public void setView() {

        back_Btn = findViewById(R.id.delete_Btn);
        home_Btn = findViewById(R.id.home_Btn);
        heart_Btn = findViewById(R.id.heart_Btn);
        add_Btn = findViewById(R.id.add_Btn);
        myProfile_Btn = findViewById(R.id.myProfile_Btn);

        homeFull_Btn = findViewById(R.id.homefull_Btn);
        heartFull_Btn = findViewById(R.id.heartfull_Btn);
        addFull_Btn = findViewById(R.id.addfull_Btn);
        myProfileFull_Btn = findViewById(R.id.myProfilefull_Btn);

        photo_Btn = findViewById(R.id.homePhoto_Btn);
        map_Btn = findViewById(R.id.homeMap_Btn);

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences_Kakao = getSharedPreferences("a5636c0dc6cb43c4ea8f52134f0f1337", MODE_PRIVATE);
        editor_Kakao = sharedPreferences_Kakao.edit();


        fragment_home = new Fragment_Home();
        fragment_heart = new Fragment_Heart();
        fragment_add = new Fragment_add();
        fragment_myProfile = new Fragment_myProfile();

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.homeBody_Frame,fragment_home);
        transaction.commitAllowingStateLoss();

        headFrame = findViewById(R.id.homeHead_Frame);

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