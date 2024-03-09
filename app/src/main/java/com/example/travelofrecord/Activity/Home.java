package com.example.travelofrecord.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.travelofrecord.Function.BackBtn;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    String TAG = "홈 액티비티";
    String sharedInfo, currentNickname, fcmToken;
    int networkStatus;
    boolean messageStatus = false;
    ImageButton home_Btn, heart_Btn, add_Btn, dm_Btn, myProfile_Btn, homeFull_Btn, heartFull_Btn, addFull_Btn, dmFull_Btn,
            myProfileFull_Btn, dmRed_Btn, dmRedFull_Btn;
    LinearLayout homeFootLayout;
    FrameLayout homeBodyLayout;
    SharedPreferences sharedPreferences, sharedPreferences_Kakao;
    SharedPreferences.Editor editor, editor_Kakao;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    Fragment_Home fragment_home;
    Fragment_Heart fragment_heart;
    Fragment_add fragment_add;
    Fragment_Dm fragment_dm;
    Fragment_myProfile fragment_myProfile;
    Bundle bundle;
    BackBtn backBtn;
    OnBackPressedListener mBackListener;
    Handler handler;


    /*
    인텐트 응답 대기
     */
    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
        super.onNewIntent(intent);
    } // onNewIntent()

    private void processIntent(Intent intent) {
        if (intent != null) {

            messageStatus = true;

            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment.isVisible()) {
                    if (fragment instanceof Fragment_Dm) {
                        Log.d(TAG, "fragment : Fragment_Dm");

                        dm_Btn.setVisibility(View.GONE);
                        dmFull_Btn.setVisibility(View.GONE);
                        dmRed_Btn.setVisibility(View.GONE);
                        dmRedFull_Btn.setVisibility(View.VISIBLE);

                    } else {
                        Log.d(TAG, "fragment : else");

                        dm_Btn.setVisibility(View.GONE);
                        dmFull_Btn.setVisibility(View.GONE);
                        dmRedFull_Btn.setVisibility(View.GONE);
                        dmRed_Btn.setVisibility(View.VISIBLE);

                    }
                }
            }

        }

    } // processIntent()


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate() 호출");

        setVariable();
        setView();

        networkStatus = NetworkStatus.getConnectivityStatus(this);

    } // onCreate()


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() 호출");

        getNoti(currentNickname);

    } // onStart()


    /*
    시스템 뒤로가기 버튼 클릭 인터페이스
     */
    public interface OnBackPressedListener {
        void onBack();
    } // OnBackPressedListener

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mBackListener = listener;
    } // setOnBackPressedListener

    /*
    시스템 뒤로가기 버튼 클릭 리스너
     */
    @Override
    public void onBackPressed() {

        if (mBackListener != null) {
            mBackListener.onBack();
        }

        backBtn.onBackPressed();

    } // onBackPressed()



    // --------------------------------------------------------------------------------------------


    /*
    변수 초기화
     */
    public void setVariable() {

        home_Btn = findViewById(R.id.home_Btn);
        heart_Btn = findViewById(R.id.heart_Btn);
        add_Btn = findViewById(R.id.add_Btn);
        dm_Btn = findViewById(R.id.dm_Btn);
        myProfile_Btn = findViewById(R.id.myProfile_Btn);
        dmRed_Btn = findViewById(R.id.dmred_Btn);
        dmRedFull_Btn = findViewById(R.id.dmredfull_Btn);

        homeFull_Btn = findViewById(R.id.homefull_Btn);
        heartFull_Btn = findViewById(R.id.heartfull_Btn);
        addFull_Btn = findViewById(R.id.addfull_Btn);
        dmFull_Btn = findViewById(R.id.dmfull_Btn);
        myProfileFull_Btn = findViewById(R.id.myProfilefull_Btn);

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {

                Log.d(TAG, "토큰 확인 성공 : " + token);
                editor.putString("fcmToken", token);
                editor.commit();

                updateFcmToken(currentNickname, token);

            }
        });

        sharedInfo = sharedPreferences.getString("id","");
        currentNickname = sharedPreferences.getString("nickname","");
        fcmToken = sharedPreferences.getString("fcmToken","");

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

        backBtn = new BackBtn(this, mBackListener);

        handler = new Handler();

    } // setvariable()


    /*
    뷰 초기화
     */
    public void setView() {

        home_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(0);

                    if (messageStatus) {
                        dmRed_Btn.setVisibility(View.VISIBLE);
                        dmRedFull_Btn.setVisibility(View.GONE);
                        dm_Btn.setVisibility(View.GONE);
                    } else {
                        dm_Btn.setVisibility(View.VISIBLE);
                        dmRed_Btn.setVisibility(View.GONE);
                        dmRedFull_Btn.setVisibility(View.GONE);
                    }

                    home_Btn.setVisibility(View.GONE);
                    homeFull_Btn.setVisibility(View.VISIBLE);

                    heart_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    heartFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);
                    dmFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                    ButtonThread thread = new ButtonThread();
                    thread.start();

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

                    if (messageStatus) {
                        dmRed_Btn.setVisibility(View.VISIBLE);
                        dm_Btn.setVisibility(View.GONE);
                        dmRedFull_Btn.setVisibility(View.GONE);
                    } else {
                        dm_Btn.setVisibility(View.VISIBLE);
                        dmRed_Btn.setVisibility(View.GONE);
                        dmRedFull_Btn.setVisibility(View.GONE);
                    }

                    heart_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);
                    dmFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                    ButtonThread thread = new ButtonThread();
                    thread.start();

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

                    if (messageStatus) {
                        dmRed_Btn.setVisibility(View.VISIBLE);
                        dm_Btn.setVisibility(View.GONE);
                        dmRedFull_Btn.setVisibility(View.GONE);

                    } else {
                        dm_Btn.setVisibility(View.VISIBLE);
                        dmRed_Btn.setVisibility(View.GONE);
                        dmRedFull_Btn.setVisibility(View.GONE);
                    }

                    add_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    heart_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.GONE);
                    dmFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                    ButtonThread thread = new ButtonThread();
                    thread.start();

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

                    ButtonThread thread = new ButtonThread();
                    thread.start();

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dmRed_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    fragmentChange(3);

                    dm_Btn.setVisibility(View.GONE);
                    dmRed_Btn.setVisibility(View.GONE);
                    dmRedFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    heart_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);
                    myProfile_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.GONE);

                    ButtonThread thread = new ButtonThread();
                    thread.start();

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

                    if (messageStatus) {
                        dmRed_Btn.setVisibility(View.VISIBLE);
                        dm_Btn.setVisibility(View.GONE);
                        dmRedFull_Btn.setVisibility(View.GONE);
                    } else {
                        dm_Btn.setVisibility(View.VISIBLE);
                        dmRed_Btn.setVisibility(View.GONE);
                        dmRedFull_Btn.setVisibility(View.GONE);
                    }

                    myProfile_Btn.setVisibility(View.GONE);
                    myProfileFull_Btn.setVisibility(View.VISIBLE);

                    home_Btn.setVisibility(View.VISIBLE);
                    heart_Btn.setVisibility(View.VISIBLE);
                    add_Btn.setVisibility(View.VISIBLE);

                    homeFull_Btn.setVisibility(View.GONE);
                    heartFull_Btn.setVisibility(View.GONE);
                    addFull_Btn.setVisibility(View.GONE);
                    dmFull_Btn.setVisibility(View.GONE);

                    ButtonThread thread = new ButtonThread();
                    thread.start();

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    } // setView()


    // ----------------------------------------------------------------------------------------


    /*
    일정 시간동안 버튼 안눌림 처리 스레드
     */
    public class ButtonThread extends Thread {

        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    home_Btn.setEnabled(false);
                    heart_Btn.setEnabled(false);
                    add_Btn.setEnabled(false);
                    dm_Btn.setEnabled(false);
                    myProfile_Btn.setEnabled(false);
                    dmRed_Btn.setEnabled(false);

                }
            });

            try {
                sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    home_Btn.setEnabled(true);
                    heart_Btn.setEnabled(true);
                    add_Btn.setEnabled(true);
                    dm_Btn.setEnabled(true);
                    myProfile_Btn.setEnabled(true);
                    dmRed_Btn.setEnabled(true);

                }
            });

        }

    } // ButtonThread


    /*
    프래그먼트 이동
     */
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

    } // fragmentChange()


    /*
    홈프래그먼트로 이동 + 번들 전달
     */
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

    } // goHomeFragment()


    /*
    FCM 토큰 업데이트
     */
    public void updateFcmToken(String nickname, String fcmToken) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.updateFcmToken(nickname, fcmToken);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "updateFcmToken - onResponse isSuccessful");

                    String rpCode = response.body();
                    Log.d(TAG, "rpCode: " + rpCode);

                } else {
                    Log.d(TAG, "updateFcmToken - onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "updateFcmToken - onFailure: " + t);
            }
        });

    } // updateFcmToken()


    /*
    안 읽은 메시지 확인
     */
    public void getNoti(String currentNickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getNoti(currentNickname);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "getNoti - onResponse : isSuccessful");

                    String rpCode = response.body().toString();
                    Log.d(TAG, "rpCode : " + rpCode);



                    if (rpCode.equals("is")) {

                        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                            if (fragment.isVisible()) {
                                if (fragment instanceof Fragment_Dm) {
                                    Log.d(TAG, "fragment : Fragment_Dm");
                                    dm_Btn.setVisibility(View.GONE);
                                    dmRed_Btn.setVisibility(View.GONE);
                                    dmFull_Btn.setVisibility(View.GONE);
                                    dmRedFull_Btn.setVisibility(View.VISIBLE);
                                } else {
                                    Log.d(TAG, "fragment : else");
                                    dmRed_Btn.setVisibility(View.VISIBLE);
                                    dmFull_Btn.setVisibility(View.GONE);
                                    dm_Btn.setVisibility(View.GONE);
                                    dmRedFull_Btn.setVisibility(View.GONE);
                                }
                            }
                        }

                        messageStatus = true;

                    } else if (rpCode.equals("isNot")) {

                        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                            if (fragment.isVisible()) {
                                if (fragment instanceof Fragment_Dm) {
                                    Log.d(TAG, "fragment : Fragment_Dm");
                                    dm_Btn.setVisibility(View.GONE);
                                    dmRed_Btn.setVisibility(View.GONE);
                                    dmRedFull_Btn.setVisibility(View.GONE);
                                    dmFull_Btn.setVisibility(View.VISIBLE);
                                } else {
                                    Log.d(TAG, "fragment : else");
                                    dmRed_Btn.setVisibility(View.GONE);
                                    dmFull_Btn.setVisibility(View.GONE);
                                    dmRedFull_Btn.setVisibility(View.GONE);
                                    dm_Btn.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        messageStatus = false;

                    }

                } else {
                    Log.d(TAG, "getNoti - onResponse : isFailure");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "getNoti - onFailure");
            }
        });

    } // getNoti()


}