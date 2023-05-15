package com.example.travelofrecord.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.travelofrecord.Fragment.Fragment_Dm;
import com.example.travelofrecord.Fragment.Fragment_Heart;
import com.example.travelofrecord.Fragment.Fragment_Home;
import com.example.travelofrecord.Fragment.Fragment_add;
import com.example.travelofrecord.Fragment.Fragment_myProfile;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.R;

public class Home extends AppCompatActivity {

    String TAG = "홈 액티비티";

    ImageButton home_Btn;
    ImageButton heart_Btn;
    ImageButton add_Btn;
    ImageButton dm_Btn;
    ImageButton myProfile_Btn;

    ImageButton homeFull_Btn;
    ImageButton heartFull_Btn;
    ImageButton addFull_Btn;
    ImageButton dmFull_Btn;
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
    Fragment_Dm fragment_dm;
    Fragment_myProfile fragment_myProfile;

    LinearLayout homeFootLayout;
    FrameLayout homeBodyLayout;

    Bundle bundle;

    int networkStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate() 호출");

        setVariable();
        setView();
        Log.d(TAG, "쉐어드 정보 : " + sharedInfo);
        networkStatus = NetworkStatus.getConnectivityStatus(this);

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출");
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


    // --------------------------------------------------------------------------------------------


    public void setVariable() {

        home_Btn = findViewById(R.id.home_Btn);
        heart_Btn = findViewById(R.id.heart_Btn);
        add_Btn = findViewById(R.id.add_Btn);
        dm_Btn = findViewById(R.id.dm_Btn);
        myProfile_Btn = findViewById(R.id.myProfile_Btn);

        homeFull_Btn = findViewById(R.id.homefull_Btn);
        heartFull_Btn = findViewById(R.id.heartfull_Btn);
        addFull_Btn = findViewById(R.id.addfull_Btn);
        dmFull_Btn = findViewById(R.id.dmfull_Btn);
        myProfileFull_Btn = findViewById(R.id.myProfilefull_Btn);

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedInfo = sharedPreferences.getString("id","");

        sharedPreferences_Kakao = getSharedPreferences("a5636c0dc6cb43c4ea8f52134f0f1337", MODE_PRIVATE);
        editor_Kakao = sharedPreferences_Kakao.edit();

        fragment_home = new Fragment_Home();
        fragment_heart = new Fragment_Heart();
        fragment_add = new Fragment_add();
        fragment_dm = new Fragment_Dm();
        fragment_myProfile = new Fragment_myProfile();

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.homeBody_Frame,fragment_home);
        transaction.commitAllowingStateLoss();

        bundle = new Bundle();

        homeFootLayout = findViewById(R.id.homeFoot_Layout);
        homeBodyLayout = findViewById(R.id.homeBody_Frame);

    }

    public void setView() {

        home_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(0);

                    home_Btn.setVisibility(View.GONE);
                    homeFull_Btn.setVisibility(View.VISIBLE);

                    heart_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);
                    dm_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    heartFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);
                    dmFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        heart_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(1);

                    heart_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);
                    dm_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);
                    dmFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(2);

                    add_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    heart_Btn.setVisibility(View.VISIBLE);
                    dm_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.GONE);
                    dmFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dm_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(3);

                    dm_Btn.setVisibility(View.GONE);
                    dmFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    heart_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        myProfile_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(4);

                    myProfile_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    heart_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);
                    dm_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);
                    dmFull_Btn.setVisibility(View.GONE);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    } // setView()


    // ----------------------------------------------------------------------------------------


    // 프래그먼트 이동
    public void fragmentChange(int index) {

        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_home).commitAllowingStateLoss();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_heart).commitAllowingStateLoss();
        } else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_add).commitAllowingStateLoss();
        } else if (index == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_dm).commitAllowingStateLoss();
        } else if (index == 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeBody_Frame,fragment_myProfile).commitAllowingStateLoss();
        }

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


}