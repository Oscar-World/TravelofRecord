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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.sdk.user.UserApiClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_Kakao;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_Kakao;
    String sharedInfo;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    Fragment_Home fragment_home;
    Fragment_Heart fragment_heart;
    Fragment_add fragment_add;
    Fragment_myProfile fragment_myProfile;
    Fragment_Post fragment_post;

    LinearLayout homeFootLayout;
    FrameLayout homeBodyLayout;

    Bundle bundle;

    String user_type;
    String user_id;
    String user_pw;
    String user_phone;
    String user_nickname;
    String user_memo;
    String user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate() 호출");

        setView();
        Log.d(TAG, "쉐어드 정보 : " + sharedInfo);

//        getInfo(sharedInfo);

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출");

        home_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(0);

                    home_Btn.setVisibility(View.GONE);
                    homeFull_Btn.setVisibility(View.VISIBLE);

                    heart_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    heartFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        heart_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(1);

                    heart_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(2);

                    add_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    heart_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        myProfile_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(3);

                    myProfile_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    heart_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void setView() {

        home_Btn = findViewById(R.id.home_Btn);
        heart_Btn = findViewById(R.id.heart_Btn);
        add_Btn = findViewById(R.id.add_Btn);
        myProfile_Btn = findViewById(R.id.myProfile_Btn);

        homeFull_Btn = findViewById(R.id.homefull_Btn);
        heartFull_Btn = findViewById(R.id.heartfull_Btn);
        addFull_Btn = findViewById(R.id.addfull_Btn);
        myProfileFull_Btn = findViewById(R.id.myProfilefull_Btn);

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedInfo = sharedPreferences.getString("id","");

        sharedPreferences_Kakao = getSharedPreferences("a5636c0dc6cb43c4ea8f52134f0f1337", MODE_PRIVATE);
        editor_Kakao = sharedPreferences_Kakao.edit();

        fragment_home = new Fragment_Home();
        fragment_heart = new Fragment_Heart();
        fragment_add = new Fragment_add();
        fragment_myProfile = new Fragment_myProfile();
        fragment_post = new Fragment_Post();

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.homeBody_Frame,fragment_home);
        transaction.commitAllowingStateLoss();

        bundle = new Bundle();

        homeFootLayout = findViewById(R.id.homeFoot_Layout);
        homeBodyLayout = findViewById(R.id.homeBody_Frame);

    }


    // 프래그먼트 이동
    public void fragmentChange(int index) {

        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_home).commitAllowingStateLoss();
            homeFootLayout.setVisibility(View.VISIBLE);
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_heart).commitAllowingStateLoss();
            homeFootLayout.setVisibility(View.VISIBLE);
        } else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_add).commitAllowingStateLoss();
            homeFootLayout.setVisibility(View.VISIBLE);
        } else if (index == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_myProfile).commitAllowingStateLoss();
            homeFootLayout.setVisibility(View.VISIBLE);
        } else if (index == 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_post).commitAllowingStateLoss();
            homeFootLayout.setVisibility(View.GONE);
        }

    }

    public void goPostFragment(Bundle bundle) {
        fragment_post.setArguments(bundle);
        fragmentChange(4);
    }


    // 홈프래그먼트로 이동 + 번들 전달
    public void goHomeFragment(Bundle bundle) {

        fragment_home.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_home).commitAllowingStateLoss();

        home_Btn.setVisibility(View.GONE);
        homeFull_Btn.setVisibility(View.VISIBLE);

        heart_Btn.setVisibility(View.VISIBLE);
        add_Btn.setVisibility(View.VISIBLE);
        myProfile_Btn.setVisibility(View.VISIBLE);

        heartFull_Btn.setVisibility(View.GONE);
        addFull_Btn.setVisibility(View.GONE);
        myProfileFull_Btn.setVisibility(View.GONE);

    }


    // 서버에서 데이터 받아옴 (현재 필요 없음. 쉐어드에 사용자 정보 저장해서 사용)
//    public void getInfo(String id) {
//        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//        Call<User> call = apiInterface.getInfo(id);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//
//                if (response.isSuccessful()) {
//
//                    user_type = response.body().getType();
//                    user_id = response.body().getId();
//                    user_pw = response.body().getPw();
//                    user_phone = response.body().getPhone();
//                    user_nickname = response.body().getNickname();
//                    user_memo = response.body().getMemo();
//                    user_image = response.body().getImage();
//
//                    Log.d(TAG, "서버에서 전달 받은 코드 : " + user_type + "\n" + user_id + "\n" + user_pw + "\n" + user_phone + "\n" + user_nickname + "\n" + user_memo + "\n" + user_image);
//
//                } else {
//                    Log.d(TAG, "onResponse: 리스폰스 실패");
//                }
//
//            }   // onResponse
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
//            }
//
//        });
//
//    }  // getInfo()


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