package com.example.travelofrecord.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.travelofrecord.Function.BackBtn;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.R;

public class Start extends AppCompatActivity {

    String TAG = "시작 액티비티";
    TextView title_Text;
    Button login_Btn;
    ImageView imageView;
    Handler handler;
    Animation animation, apperAnim;
    TextThread textThread;
    WaitThread waitThread;
    InThread inThread;
    SharedPreferences sharedPreferences;
    String shared;
    public static Context context;
    BackBtn backBtn = new BackBtn(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.d(TAG, "onCreate() 호출");

        context = this;

        setVariable();
        setView();

    } // onCreate()



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() 호출");

        if (!shared.equals("")) {
            inThread = new InThread();
            inThread.start();
        } else {
            waitThread = new WaitThread();
            waitThread.start();
        }

    } // onStart()


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() 호출됨");

        title_Text.setVisibility(View.GONE);
        login_Btn.setVisibility(View.GONE);

    } // onRestart()


    /*
    시스템 뒤로가기 버튼 클릭 이벤트
     */
    @Override
    public void onBackPressed() {
        backBtn.onBackPressed();
    } // onBackPressed()


    /*
    초기 변수 세팅
     */
    public void setVariable() {

        title_Text = findViewById(R.id.Title_text);
        login_Btn = findViewById(R.id.login_Button);
        imageView = findViewById(R.id.imageView);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_text);
        handler = new Handler();
        apperAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_appear);

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        shared = sharedPreferences.getString("id", "");

    } // setVariable()


    /*
    초기 뷰 세팅
     */
    public void setView() {

        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Intent i = new Intent(Start.this, Login.class);
                    textThread.interrupt();
                    startActivity(i);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Glide.with(this).asGif()
                .load(R.drawable.intro)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1);
                        return false;
                    }
                })
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);

    } // setView()


    /*
    타이틀 텍스트 애니메이션 처리 스레드
     */
    public class TextThread extends Thread {

        public void run() {
            for (int i = 1; i <= 50; i++) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    Log.d(TAG, "TextThread isInterrupted : " + e);
                    title_Text.clearAnimation();
                    e.printStackTrace();
                    break;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        title_Text.startAnimation(animation);
                    }
                });

            }
        }

    } // TextThread


    /*
    딜레이 스레드
     */
    public class WaitThread extends Thread {

        public void run() {

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    title_Text.setVisibility(View.VISIBLE);
                    title_Text.startAnimation(apperAnim);
                    login_Btn.setVisibility(View.VISIBLE);
                    login_Btn.startAnimation(apperAnim);
                }
            });

            textThread = new TextThread();
            textThread.start();

        }

    } // WaitThread


    /*
    딜레이 후 화면 전환 처리 스레드
     */
    public class InThread extends Thread {
        public void run() {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(Start.this, Home.class);
            startActivity(i);
            finish();
        }

    } // InThread


}