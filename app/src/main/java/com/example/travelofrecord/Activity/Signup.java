package com.example.travelofrecord.Activity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.travelofrecord.Function.BackBtn;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {

    String TAG = "회원가입 액티비티";
    String backText = "\'뒤로\' 버튼을 한번 더 누르면 로그인 페이지로 이동합니다.";
    String edit_id, edit_pw, edit_pwCheck, edit_phone, edit_phoneCheck, edit_nickname, idRule, pwRule, phoneRule, nicknameRule,
            id_code, nickname_code, login_Type, kakaoId, kakaoImage, googleId, naverId, setTime, imagePath, smsCode, imageFileName;
    int phoneCount, smsTime, smsTime_min, smsTime_sec, smsCheckNumber;
    boolean idStatus, pwStatus, pwCheckStatus, nicknameStatus;
    SharedPreferences userShared, authShared;
    SharedPreferences.Editor userEditor, authEditor;
    ActivityResultLauncher<Intent> launcher;
    Uri uri;
    FirebaseAuth auth;
    Animation left_out, left_in, right_out, right_in, appear, disappear;
    Pattern pattern_id, pattern_pw, pattern_ph, pattern_nick;
    Matcher matcher_id, matcher_pw, matcher_ph, matcher_nick;
    TextView phoneCountText, id_Using, id_Error, id_Useable, pw_Error, pw_Useable, pwCheck_Error, pwCheck_Useable, phone_Send,
            phone_SmsTime, phone_SmsOk, phone_SmsError, phone_SmsEmpty, phone_SmsTimeout, phone_NumberError, nickname_Using, nickname_Useable, nickname_Error;
    FrameLayout frameLayout_1, frameLayout_2, frameLayout_3, id_Info, pw_Info, nickname_Info;
    EditText signup_id, signup_pw, signup_pwCheck, signup_phone, signup_phoneCheck, signup_nickname;
    Button nextBtn_1, nextBtn_2, submitBtn, nextBlock_1, nextBlock_2, submitBlock, smsSend_Btn, smsCheck_Btn, smsSend_Block, smsCheck_Block;
    ImageButton backBtn_1, backBtn_2, backBtn_3, id_Btn, pw_Btn, nickname_Btn;
    ImageView photo_Btn;
    BackBtn backBtn = new BackBtn(this);
    Handler handler;
    IdinfoThread idThread;
    PwinfoThread pwThread;
    NicknameinfoThread nicknameThread;
    SmsTimeThread smsTimeThread;


    /*
    갤러리 접근 권한 관련 상수 세팅
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Log.d(TAG, "onCreate() 호출");

        setVariable();
        checkLoginType();

    } // onCreate()


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() 호출");

        idStatus = false;
        pwStatus = false;
        pwCheckStatus = false;
        nicknameStatus = false;

        setListener();

    } // onStart()


    /*
    시스템 뒤로가기 버튼 클릭 이벤트
     */
    @Override
    public void onBackPressed() {

        backBtn.onBackPressed(backText);

    } // onBackPressed()


    /*
    뷰 세팅
     */
    public void setVariable() {

        frameLayout_1 = findViewById(R.id.signup_frameLayout1);
        frameLayout_2 = findViewById(R.id.signup_frameLayout2);
        frameLayout_3 = findViewById(R.id.signup_frameLayout3);
        id_Info = findViewById(R.id.signup_idInfo);
        pw_Info = findViewById(R.id.signup_pwInfo);
        nickname_Info = findViewById(R.id.signup_nicknameInfo);

        signup_id = findViewById(R.id.signup_id);
        signup_pw = findViewById(R.id.signup_pw);
        signup_pwCheck = findViewById(R.id.signup_pwCheck);
        signup_phone = findViewById(R.id.signup_phone);
        signup_phoneCheck = findViewById(R.id.signup_CheckNum);
        signup_nickname = findViewById(R.id.signup_nickname);

        nextBtn_1 = findViewById(R.id.signupNext1_button);
        nextBtn_2 = findViewById(R.id.signupNext2_button);
        submitBtn = findViewById(R.id.signupSubmit_button);

        nextBlock_1 = findViewById(R.id.signupNext1_Block);
        nextBlock_2 = findViewById(R.id.signupNext2_Block);
        submitBlock = findViewById(R.id.signupSubmit_Block);

        backBtn_1 = findViewById(R.id.signupBack_Btn);
        backBtn_2 = findViewById(R.id.signupBack_Btn2);
        backBtn_3 = findViewById(R.id.signupBack_Btn3);
        id_Btn = findViewById(R.id.signup_Idbtn);
        pw_Btn = findViewById(R.id.signup_Pwbtn);
        nickname_Btn = findViewById(R.id.signup_Nicknamebtn);
        photo_Btn = findViewById(R.id.signup_Photo);

        userShared = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        userEditor = userShared.edit();

        authShared = getSharedPreferences("휴대폰 인증", MODE_PRIVATE);
        authEditor = authShared.edit();

        phoneCountText = findViewById(R.id.signUp_sendPhoneText);
        phoneCount = authShared.getInt("남은 횟수", 0);
        phoneCountText.setText("남은 인증 횟수 : " + String.valueOf(phoneCount));
        if (phoneCount == 0) {
            smsSend_Btn.setVisibility(View.INVISIBLE);
        }

        left_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.frame_leftout);
        left_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.frame_leftin);
        right_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.frame_rightout);
        right_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.frame_rightin);
        appear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.frame_appear);
        disappear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.frame_disappear);

        id_Using = findViewById(R.id.id_Using);
        id_Error = findViewById(R.id.id_Error);
        id_Useable = findViewById(R.id.id_Useable);
        pw_Error = findViewById(R.id.pw_Error);
        pw_Useable = findViewById(R.id.pw_Useable);
        pwCheck_Error = findViewById(R.id.pwCheck_Error);
        pwCheck_Useable = findViewById(R.id.pwCheck_Useable);
        phone_Send = findViewById(R.id.smsSend_Text);
        phone_SmsOk = findViewById(R.id.smsOk_Text);
        phone_SmsTime = findViewById(R.id.smsTime_Text);
        phone_SmsError = findViewById(R.id.smsError_Text);
        phone_NumberError = findViewById(R.id.numberError_Text);
        phone_SmsTimeout = findViewById(R.id.smsTimeout_Text);
        smsSend_Btn = findViewById(R.id.smsSend_button);
        smsCheck_Btn = findViewById(R.id.smsCheck_button);
        smsSend_Block = findViewById(R.id.smsSend_Block);
        smsCheck_Block = findViewById(R.id.smsCheck_Block);
        phone_SmsEmpty = findViewById(R.id.smsEmpty_Text);
        nickname_Using = findViewById(R.id.nickname_Using);
        nickname_Useable = findViewById(R.id.nickname_Useable);
        nickname_Error = findViewById(R.id.nickname_Error);

        idThread = new IdinfoThread();
        pwThread = new PwinfoThread();
        nicknameThread = new NicknameinfoThread();

        handler = new Handler();

    }  // setVariable()


    /*
    클릭 리스너 세팅
     */
    public void setListener() {

        // ▼ 아이디 텍스트 변경 시 이벤트 처리 ▼
        signup_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: " + editable.toString());
                Log.d(TAG, "afterTextChanged: " + idRuleCheck2());
                if (!idRuleCheck2()) {
                    id_Useable.setVisibility(View.INVISIBLE);
                    id_Using.setVisibility(View.INVISIBLE);
                    id_Error.setVisibility(View.VISIBLE);
                } else {
                    idCheck(editable.toString());
                }

            }
        });


        // ▼ 닉네임 텍스트 변경 시 이벤트 처리 ▼
        signup_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: " + editable.toString());

                if (!nicknameRuleCheck()) {
                    nickname_Useable.setVisibility(View.INVISIBLE);
                    nickname_Using.setVisibility(View.INVISIBLE);
                    nickname_Error.setVisibility(View.VISIBLE);
                } else {
                    nicknameCheck(editable.toString());
                }

                if (nicknameStatus) {
                    Log.d(TAG, "텍스트 체인지 nickname Status : " + nicknameStatus + " 버튼 활성화!");
                    submitBtn.setVisibility(View.VISIBLE);
                    submitBlock.setVisibility(View.INVISIBLE);
                } else {
                    Log.d(TAG, "텍스트 체인지 nickname Status : " + nicknameStatus + " 버튼 비활성화!");
                    submitBtn.setVisibility(View.INVISIBLE);
                    submitBlock.setVisibility(View.VISIBLE);
                }

            }
        });


        // ▼ 닉네임 텍스트 포커스 이벤트 처리 ▼
        signup_nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {

                    Log.d(TAG, "포커스 온?! : " + b);
                    if (!nicknameThread.isAlive()) {
                        Log.d(TAG, "onFocusChange: 스레드 죽어있음. 실행 가능");
                        nicknameThread = new NicknameinfoThread();
                        nicknameThread.start();
                    } else {
                        Log.d(TAG, "onFocusChange: 스레드 살아있음. 실행 불가");
                    }

                } else {

                    Log.d(TAG, "포커스 온?! : " + b);

                    nickname_Info.setVisibility(View.GONE);

                }

            }
        });


        // ▼ 아이디 텍스트 포커스 이벤트 처리 ▼
        signup_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {

                    Log.d(TAG, "포커스 온?! : " + b);
                    if (!idThread.isAlive()) {
                        Log.d(TAG, "onFocusChange: 스레드 죽어있음. 실행 가능");
                        idThread = new IdinfoThread();
                        idThread.start();
                    } else {
                        Log.d(TAG, "onFocusChange: 스레드 살아있음. 실행 불가");
                    }

                } else {

                    Log.d(TAG, "포커스 온?! : " + b);

                    id_Info.setVisibility(View.GONE);

                }

            }
        });


        // ▼ 비밀번호 텍스트 변경 시 이벤트 처리 ▼
        signup_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!pwRuleCheck()) {
                    pw_Error.setVisibility(View.VISIBLE);
                    pw_Useable.setVisibility(View.INVISIBLE);
                    pwStatus = false;
                } else {
                    pw_Useable.setVisibility(View.VISIBLE);
                    pw_Error.setVisibility(View.INVISIBLE);
                    pwStatus = true;
                }

                if (idStatus & pwStatus & pwCheckStatus) {
                    Log.d(TAG, "id / pw / pwCheck Status : " + idStatus + " " + pwStatus + " " + pwCheckStatus + " 버튼 활성화!");
                    nextBtn_1.setVisibility(View.VISIBLE);
                    nextBlock_1.setVisibility(View.INVISIBLE);
                } else {
                    Log.d(TAG, "id / pw / pwCheck Status : " + idStatus + " " + pwStatus + " " + pwCheckStatus + " 버튼 비활성화!");
                    nextBtn_1.setVisibility(View.INVISIBLE);
                    nextBlock_1.setVisibility(View.VISIBLE);
                }

            }

        });


        // ▼ 비밀번호 텍스트 포커스 이벤트 처리 ▼
        signup_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {

                    Log.d(TAG, "포커스 온?! : " + b);

                    if (!pwThread.isAlive()) {
                        Log.d(TAG, "onFocusChange: 스레드 죽어있음. 실행 가능");
                        pwThread = new PwinfoThread();
                        pwThread.start();
                    } else {
                        Log.d(TAG, "onFocusChange: 스레드 살아있음. 실행 불가");
                    }

                } else {

                    Log.d(TAG, "포커스 온?! : " + b);

                    pw_Info.setVisibility(View.GONE);

                }

            }
        });


        // ▼ 비밀번호확인 텍스트 변경 시 이벤트 처리 ▼
        signup_pwCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.d(TAG, "afterTextChanged: " + editable);

                if (!pwCheckRuleCheck()) {
                    pwCheck_Error.setVisibility(View.VISIBLE);
                    pwCheck_Useable.setVisibility(View.INVISIBLE);
                    pwCheckStatus = false;
                } else if (editable.equals("")) {
                    pwCheck_Error.setVisibility(View.VISIBLE);
                    pwCheck_Useable.setVisibility(View.INVISIBLE);
                    pwCheckStatus = false;

                } else {
                    pwCheck_Error.setVisibility(View.INVISIBLE);
                    pwCheck_Useable.setVisibility(View.VISIBLE);
                    pwCheckStatus = true;
                }

                if (idStatus & pwStatus & pwCheckStatus) {
                    Log.d(TAG, "id / pw / pwCheck Status : " + idStatus + " " + pwStatus + " " + pwCheckStatus + " 버튼 활성화!");
                    nextBtn_1.setVisibility(View.VISIBLE);
                    nextBlock_1.setVisibility(View.INVISIBLE);
                } else {
                    Log.d(TAG, "id / pw / pwCheck Status : " + idStatus + " " + pwStatus + " " + pwCheckStatus + " 버튼 비활성화!");
                    nextBtn_1.setVisibility(View.INVISIBLE);
                    nextBlock_1.setVisibility(View.VISIBLE);
                }

            }
        });


        // ▼ 아이디 입력 안내 버튼 ▼
        id_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!idThread.isAlive()) {
                    Log.d(TAG, "onFocusChange: 스레드 죽어있음. 실행 가능");
                    idThread = new IdinfoThread();
                    idThread.start();
                } else {
                    Log.d(TAG, "onFocusChange: 스레드 살아있음. 실행 불가");
                }

            }
        });


        // ▼ 비밀번호 입력 안내 버튼 ▼
        pw_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!pwThread.isAlive()) {
                    Log.d(TAG, "onFocusChange: 스레드 죽어있음. 실행 가능");
                    pwThread = new PwinfoThread();
                    pwThread.start();
                } else {
                    Log.d(TAG, "onFocusChange: 스레드 살아있음. 실행 불가");
                }

            }
        });


        // ▼ 닉네임 입력 안내 버튼 ▼
        nickname_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!nicknameThread.isAlive()) {
                    Log.d(TAG, "onFocusChange: 스레드 죽어있음. 실행 가능");
                    nicknameThread = new NicknameinfoThread();
                    nicknameThread.start();
                } else {

                    Log.d(TAG, "onFocusChange: 스레드 살아있음. 실행 불가");
                }

            }
        });


        // ▼ 1페이지 다음 버튼 ▼
        nextBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout_1.startAnimation(left_out);
                frameLayout_1.setVisibility(View.INVISIBLE);
                frameLayout_2.startAnimation(left_in);
                frameLayout_2.setVisibility(View.VISIBLE);
            }
        });


        // ▼ 2페이지 다음 버튼 ▼
        nextBtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout_2.startAnimation(left_out);
                frameLayout_2.setVisibility(View.INVISIBLE);
                frameLayout_3.startAnimation(left_in);
                frameLayout_3.setVisibility(View.VISIBLE);

                verifyStoragePermissions(Signup.this);

            }
        });


        // ▼ 3페이지 완료 버튼 ▼
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                Log.d(TAG, "NetworkStatus : " + status);
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    edit_id = signup_id.getText().toString();
                    edit_pw = signup_pw.getText().toString();
                    edit_pwCheck = signup_pwCheck.getText().toString();
                    edit_phone = signup_phone.getText().toString();
                    edit_nickname = signup_nickname.getText().toString();

                    String fcmToken = userShared.getString("fcmToken", "");

                    if (kakaoId != null) {
                        if (signupCheck2()) {
                            login_Type = "Kakao";
                            edit_id = kakaoId;
                        }
                    } else if (googleId != null) {
                        if (signupCheck2()) {
                            login_Type = "Google";
                            edit_id = googleId;
                        }
                    } else if (naverId != null) {
                        if (signupCheck2()) {
                            login_Type = "Naver";
                            edit_id = naverId;
                        }
                    } else {
                        if (signupCheck()) {
                            login_Type = "Basic";
                        }
                    }

                    String systemTime = String.valueOf(System.currentTimeMillis());
                    imageFileName = systemTime + ".jpg";


                    RequestBody loginType = RequestBody.create(MediaType.parse("text/plain"), login_Type);
                    RequestBody id = RequestBody.create(MediaType.parse("text/plain"), edit_id);
                    RequestBody pw = RequestBody.create(MediaType.parse("text/plain"), edit_pw);
                    RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), edit_phone);
                    RequestBody nickname = RequestBody.create(MediaType.parse("text/plain"), edit_nickname);
                    RequestBody image = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
                    RequestBody fcm = RequestBody.create(MediaType.parse("text/plain"), fcmToken);
                    HashMap map = new HashMap();
                    map.put("loginType", loginType);
                    map.put("id", id);
                    map.put("pw", pw);
                    map.put("phone", phone);
                    map.put("nickname", nickname);
                    map.put("imagePath", image);
                    map.put("fcmToken", fcm);

                    File imageFile = new File(imagePath);

                    Log.d(TAG, "데이터 : \nid-" + edit_id + "\npw-" + edit_pw + "\nphone-" + edit_phone + "\nnickname-" + edit_nickname + "\nimagePath-" + imagePath + "\nimageFileName-" + imageFileName + "\nfcmToken-" + fcmToken);

                    Signup(imageFile, map);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        // ▼ 1페이지 뒤로가기 버튼 ▼
        backBtn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // ▼ 2페이지 뒤로가기 버튼 ▼
        backBtn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout_2.startAnimation(right_out);
                frameLayout_2.setVisibility(View.INVISIBLE);
                frameLayout_1.startAnimation(right_in);
                frameLayout_1.setVisibility(View.VISIBLE);
            }
        });


        // ▼ 3페이지 뒤로가기 버튼 ▼
        backBtn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout_3.startAnimation(right_out);
                frameLayout_3.setVisibility(View.INVISIBLE);
                frameLayout_2.startAnimation(right_in);
                frameLayout_2.setVisibility(View.VISIBLE);
            }
        });


        // ▼ SMS 전송 버튼 ▼
        smsSend_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                Log.d(TAG, "NetworkStatus : " + status);
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    edit_phone = signup_phone.getText().toString();

                    signup_phoneCheck.setText("");

                    if (edit_phone.isEmpty()) {
                        phone_NumberError.setVisibility(View.INVISIBLE);
                        phone_SmsEmpty.setVisibility(View.VISIBLE);
                    } else if (!phoneRuleCheck()) {
                        phone_SmsEmpty.setVisibility(View.INVISIBLE);
                        phone_NumberError.setVisibility(View.VISIBLE);
                    } else {

                        phoneCheck(edit_phone);

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        // ▼ SMS 인증번호 확인 버튼 ▼
        smsCheck_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                Log.d(TAG, "NetworkStatus : " + status);
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    edit_phoneCheck = signup_phoneCheck.getText().toString();

                    if (edit_phoneCheck.equals("")) {

                        Toast.makeText(getApplicationContext(), "인증 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();

                    } else {

                        smsCheckNumber = 2;
                        smsTime = 0;

                        Log.d(TAG, "smsCheckNumber : " + smsCheckNumber);

                        phone_Send.setVisibility(View.INVISIBLE);
                        phone_SmsTime.setVisibility(View.INVISIBLE);
                        smsCheck_Block.setVisibility(View.VISIBLE);
                        smsCheck_Btn.setVisibility(View.INVISIBLE);

                        if (edit_phoneCheck.equals(smsCode)) {
//                        if (edit_phoneCheck.equals("7777")) {
                            phone_SmsOk.setVisibility(View.VISIBLE);
                            nextBlock_2.setVisibility(View.INVISIBLE);
                            nextBtn_2.setVisibility(View.VISIBLE);
                            auth.signOut();
                        } else {

                            if (phoneCount == 0) {

                                phone_SmsError.setVisibility(View.VISIBLE);
                                smsSend_Btn.setVisibility(View.INVISIBLE);
                                smsSend_Block.setVisibility(View.INVISIBLE);
                                auth.signOut();

                            } else {

                                phone_SmsError.setVisibility(View.VISIBLE);
                                smsSend_Btn.setVisibility(View.VISIBLE);
                                smsSend_Block.setVisibility(View.INVISIBLE);
                                auth.signOut();

                            }

                        }

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });


//        ▼ 프로필 사진 업로드 버튼 ▼
        photo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: 프로필 사진 버튼");

                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                Log.d(TAG, "NetworkStatus : " + status);
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    if (ActivityCompat.checkSelfPermission(Signup.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        Log.d(TAG, "퍼미션 허용");

                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_PICK);
                        launcher.launch(i);

                    } else {
                        Log.d(TAG, "퍼미션 거부");

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == RESULT_OK) {

                            Intent intent = result.getData();
                            uri = intent.getData();

                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .transition(withCrossFade(factory))
                                    .placeholder(R.drawable.loading2)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(photo_Btn);

                            imagePath = getRealPathFromUri(uri);

                        }
                    }
                });

    } // setListener()


    /*
    권한 확인
     */
    public static void verifyStoragePermissions(Activity activity) {

        int WRITE_PERMISSION = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int READ_PERMISSION = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (WRITE_PERMISSION != PackageManager.PERMISSION_GRANTED
                || READ_PERMISSION != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

    } // verifyStoragePermissions()


    /*
    휴대폰 인증 문자 전송
     */
    public void sendSms(String phoneNumber) {

        auth = FirebaseAuth.getInstance();

        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // 인증 성공 시 처리
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential.getSmsCode());
                smsCode = phoneAuthCredential.getSmsCode();

            }

            // 인증 실패 시 처리
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Log.d(TAG, "onVerificationFailed: " + e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.d(TAG, "onVerificationFailed : 잘못된 요청");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.d(TAG, "onVerificationFailed : sms 할당량 초과");
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    Log.d(TAG, "onVerificationFailed : 확인된 reCAPTCHA 없음");
                }

            }

            // 인증 번호 전송 시 처리
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                String mResendToken = token.toString();

                Log.d(TAG, "onCodeSent - token : " + mResendToken);
            }

        };

        auth = FirebaseAuth.getInstance();
        auth.setLanguageCode("ko");

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(120L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    } // smsSend()


    /*
    인증 번호 클릭 시 동작
     */
    public void phoneCheckOnClick() {

        smsTimeThread = new SmsTimeThread();
        smsTimeThread.start();

        phone_SmsEmpty.setVisibility(View.INVISIBLE);
        phone_NumberError.setVisibility(View.INVISIBLE);
        phone_Send.setVisibility(View.VISIBLE);
        phone_SmsTime.setVisibility(View.VISIBLE);
        phone_SmsOk.setVisibility(View.INVISIBLE);
        phone_SmsError.setVisibility(View.INVISIBLE);
        phone_SmsTimeout.setVisibility(View.INVISIBLE);

        smsSend_Block.setVisibility(View.VISIBLE);
        smsSend_Btn.setVisibility(View.INVISIBLE);
        smsCheck_Block.setVisibility(View.INVISIBLE);
        smsCheck_Btn.setVisibility(View.VISIBLE);

        smsCheckNumber = 1;

        phoneCount -= 1;
        phoneCountText.setText("남은 인증 횟수 : " + phoneCount);
        authEditor.putInt("남은 횟수", phoneCount);
        authEditor.commit();

        String phoneNum = "+82" + edit_phone.substring(1, edit_phone.length());
        Log.d(TAG, "전송할 핸드폰 번호 : " + phoneNum);

        sendSms(phoneNum);

    } // phoneCheckOnClick()


    /*
    Uri -- > 절대경로로 바꿔서 리턴
     */
    String getRealPathFromUri(Uri uri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();

        return result;

    } // getRealPathFromUri()


    /*
    소셜 로그인으로 진입 시 안내 문구 다이얼로그
     */
    public void infoDlg() {

        AlertDialog.Builder reset = new AlertDialog.Builder(Signup.this);
        reset.setTitle("추가 정보 수집");
        reset.setMessage("본 앱에서는 한 사용자의 다중 계정 사용 방지를 위해 휴대폰 인증을 진행합니다.");
        reset.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog resetDlg = reset.create();
        resetDlg.show();

    } // infoDlg()


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// 스 레 드 //////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    SMS 인증 남은 시간 스레드
     */
    public class SmsTimeThread extends Thread {

        public void run() {
            smsTime = 120;

            while (smsTime >= 0) {
                smsTime_min = smsTime / 60;
                smsTime_sec = smsTime % 60;
                Log.d(TAG, "남은 시간 : " + smsTime);
                Log.d(TAG, "표시 : " + setTime);

                if (smsTime_sec < 10) {
                    setTime = "남은시간 : 0" + smsTime_min + ":0" + smsTime_sec;
                } else {
                    setTime = "남은시간 : 0" + smsTime_min + ":" + smsTime_sec;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        phone_SmsTime.setText(setTime);

                        if (smsCheckNumber == 1) {

                            if (smsTime == 0) {
                                Log.d(TAG, "스레드 종료 1");

                                phone_SmsTime.setVisibility(View.INVISIBLE);
                                phone_SmsTimeout.setVisibility(View.VISIBLE);
                                smsCheck_Block.setVisibility(View.VISIBLE);
                                smsCheck_Btn.setVisibility(View.INVISIBLE);
                                smsSend_Block.setVisibility(View.INVISIBLE);
                                smsSend_Btn.setVisibility(View.VISIBLE);
                                phone_SmsOk.setVisibility(View.INVISIBLE);

                            }

                        }

                    }
                });

                smsTime -= 1;

            }

        }

    } // SmsTimeThread


    /*
    아이디 입력 안내 스레드
     */
    private class IdinfoThread extends Thread {

        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    id_Info.startAnimation(appear);
                    id_Info.setVisibility(View.VISIBLE);
                    Log.d(TAG, "아이디 애니메이션 시작");
                }
            });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    id_Info.startAnimation(disappear);
                    id_Info.setVisibility(View.GONE);
                    Log.d(TAG, "아이디 애니메이션 종료");

                }
            });

        }

    } // IdinfoThread


    /*
    비밀번호 입력 안내 스레드
     */
    public class PwinfoThread extends Thread {

        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    pw_Info.startAnimation(appear);
                    pw_Info.setVisibility(View.VISIBLE);
                    Log.d(TAG, "비밀번호 애니메이션 시작");
                }
            });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    pw_Info.startAnimation(disappear);
                    pw_Info.setVisibility(View.GONE);
                    Log.d(TAG, "비밀번호 애니메이션 종료");

                }
            });

        }

    } // PwinfoThread


    /*
    닉네임 입력 안내 스레드
     */
    public class NicknameinfoThread extends Thread {

        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    nickname_Info.startAnimation(appear);
                    nickname_Info.setVisibility(View.VISIBLE);
                    Log.d(TAG, "닉네임 애니메이션 시작");
                }
            });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    nickname_Info.startAnimation(disappear);
                    nickname_Info.setVisibility(View.GONE);
                    Log.d(TAG, "닉네임 애니메이션 종료");

                }
            });

        }

    } // NicknameinfoThread


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// 입력 값 DB조회 중복 검사 ///////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    회원가입 완료 버튼 클릭 시 동작
     */
    public void Signup(File file, HashMap map) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", imageFileName, requestFile);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.insertInfo(body, map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Signup - onResponse isSuccessful");

                    String rpCode = response.body();

                    if (rpCode.equals("usingId")) {
                        Toast.makeText(getApplicationContext(), "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                    } else if (rpCode.equals("usingNick")) {
                        Toast.makeText(getApplicationContext(), "이미 사용중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                    } else if (rpCode.equals("uploadOk")) {
                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        userEditor.putString("loginType", login_Type);
                        userEditor.putString("id", edit_id);
                        userEditor.putString("nickname", edit_nickname);
                        userEditor.putString("image", imageFileName);
                        userEditor.commit();

                        Intent intent = new Intent(Signup.this, Home.class);
                        startActivity(intent);
                        finish();


                        ((Login) Login.context).finish();
                        ((Start) Start.context).finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "회원가입 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d(TAG, "Signup - onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Signup - onFailure" + t);
            }
        });

    } // Signup()


    /*
    아이디 중복 검사
     */
    public void idCheck(String id) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getIdCheck(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                id_code = response.body().toString();
                Log.d(TAG, "onResponse: " + id_code);

                if (id_code.equals("usingId")) {

                    id_Useable.setVisibility(View.INVISIBLE);
                    id_Using.setVisibility(View.VISIBLE);
                    id_Error.setVisibility(View.INVISIBLE);
                    idStatus = false;
                    Log.d(TAG, "id / pw / pwCheck Status : " + idStatus + " " + pwStatus + " " + pwCheckStatus);

                } else {

                    id_Useable.setVisibility(View.VISIBLE);
                    id_Using.setVisibility(View.INVISIBLE);
                    id_Error.setVisibility(View.INVISIBLE);

                    idStatus = true;
                    Log.d(TAG, "id / pw / pwCheck Status : " + idStatus + " " + pwStatus + " " + pwCheckStatus);

                }

                if (idStatus & pwStatus & pwCheckStatus) {
                    Log.d(TAG, "id / pw / pwCheck Status : " + idStatus + " " + pwStatus + " " + pwCheckStatus + " 버튼 활성화!");
                    nextBtn_1.setVisibility(View.VISIBLE);
                    nextBlock_1.setVisibility(View.INVISIBLE);
                } else {
                    Log.d(TAG, "id / pw / pwCheck Status : " + idStatus + " " + pwStatus + " " + pwCheckStatus + " 버튼 비활성화!");
                    nextBtn_1.setVisibility(View.INVISIBLE);
                    nextBlock_1.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }
        });

    }  // idCheck()


    /*
    닉네임 중복 검사
     */
    public void nicknameCheck(String nickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getNicknameCheck(nickname);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                nickname_code = response.body().toString();
                Log.d(TAG, "onResponse: " + nickname_code);

                if (nickname_code.equals("usingNickname")) {

                    nickname_Useable.setVisibility(View.INVISIBLE);
                    nickname_Using.setVisibility(View.VISIBLE);
                    nickname_Error.setVisibility(View.INVISIBLE);
                    nicknameStatus = false;
                    Log.d(TAG, "닉네임 중복검사 nickname Status : " + nicknameStatus + " 버튼 비활성화!");

                } else {

                    nickname_Useable.setVisibility(View.VISIBLE);
                    nickname_Using.setVisibility(View.INVISIBLE);
                    nickname_Error.setVisibility(View.INVISIBLE);
                    nicknameStatus = true;

                    Log.d(TAG, "닉네임 중복검사 nickname Status : " + nicknameStatus + " 버튼 활성화!");
                    submitBtn.setVisibility(View.VISIBLE);
                    submitBlock.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }
        });

    }  // nicknameCheck()


    /*
    핸드폰 중복 검사
     */
    public void phoneCheck(String phone) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getPhoneCheck(phone);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "phoneCheck - onResponse : isSuccessful");

                    String rpCode = response.body();
                    Log.d(TAG, "onResponse : " + rpCode);

                    if (rpCode.equals("Basic")) {

                        phoneDlg("");

                    } else if (rpCode.equals("Kakao")) {

                        phoneDlg("카카오");

                    } else if (rpCode.equals("Google")) {

                        phoneDlg("구글");

                    } else if (rpCode.equals("Naver")) {

                        phoneDlg("네이버");

                    } else if (rpCode.equals("noPhone")) {

                        phoneCheckOnClick();

                    }

                } else {
                    Log.d(TAG, "phoneCheck - onResponse : isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "phoneCheck - onFailure");
            }
        });

    } // phoneCheck()


    /*
    휴대폰 번호 중복 시 안내 다이얼로그
     */
    public void phoneDlg(String loginType) {

        String message;

        if (loginType.equals("")) {
            message = "\n앱 내 회원가입 기록이 존재합니다.\n해당 계정으로 로그인하시기 바랍니다.";
        } else {
            message = "\n" + loginType + " 계정을 통해 회원가입된 사용자입니다.\n해당 계정으로 로그인하시기 바랍니다.";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
        builder.setTitle("이미 가입된 사용자입니다.")
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();

    } // phoneDlg()


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////// 입력 값 정규식 검사 //////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    3페이지의 submit 시, 마지막 정규식 점검
     */
    private boolean signupCheck() {

        idRule = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$";
        pwRule = "^.*(?=^.{8,12}$)(\\w)(?=.*[!@#$%^&+=]).*$";
        phoneRule = "^\\d{10,11}$";
        nicknameRule = "^\\w{2,8}$";

        pattern_id = Pattern.compile(idRule);
        matcher_id = pattern_id.matcher(edit_id);

        pattern_pw = Pattern.compile(pwRule);
        matcher_pw = pattern_pw.matcher(edit_pw);

        pattern_ph = Pattern.compile(phoneRule);
        matcher_ph = pattern_ph.matcher(edit_phone);

        pattern_nick = Pattern.compile(nicknameRule);
        matcher_nick = pattern_nick.matcher(edit_nickname);

        if (!matcher_id.find()) {
            Toast t = Toast.makeText(Signup.this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else if (!matcher_pw.find()) {
            Toast t = Toast.makeText(Signup.this, "비밀번호 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else if (!edit_pw.equals(edit_pwCheck)) {
            Toast t = Toast.makeText(Signup.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
            t.show();

            Log.d(TAG, "비밀번호 : " + edit_pw);
            Log.d(TAG, "비밀번호 확인 : " + edit_pwCheck);

            return false;
        } else if (!matcher_ph.find()) {
            Toast t = Toast.makeText(Signup.this, "휴대폰 번호가 올바르지 않습니다.", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else if (!matcher_nick.find()) {
            Toast t = Toast.makeText(Signup.this, "닉네임 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else {
            return true;
        }

    }  // signupCheck()


    /*
    소셜 로그인 회원가입 절차의 정규식 점검
     */
    private boolean signupCheck2() {

        phoneRule = "^\\d{10,11}$";
        nicknameRule = "^\\w{2,8}$";

        pattern_ph = Pattern.compile(phoneRule);
        matcher_ph = pattern_ph.matcher(edit_phone);

        pattern_nick = Pattern.compile(nicknameRule);
        matcher_nick = pattern_nick.matcher(edit_nickname);

        if (!matcher_ph.find()) {
            Toast t = Toast.makeText(Signup.this, "휴대폰 번호가 올바르지 않습니다.", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else if (!matcher_nick.find()) {
            Toast t = Toast.makeText(Signup.this, "닉네임 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else {
            return true;
        }

    } // signupCheck2()


    /*
    아이디 정규식 점검
     */
    private boolean idRuleCheck2() {
        idRule = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$";
        pattern_id = Pattern.compile(idRule);
//        pattern_id = Patterns.EMAIL_ADDRESS;
//        id_Pattern = Patterns.EMAIL_ADDRESS;

        edit_id = signup_id.getText().toString();
        matcher_id = pattern_id.matcher(edit_id);

        if (!matcher_id.find()) {

            idStatus = false;
            nextBtn_1.setVisibility(View.INVISIBLE);
            nextBlock_1.setVisibility(View.VISIBLE);

            return false;

        } else {
            return true;
        }

    } // idRuleCheck2()


    /*
    비밀번호 정규식 점검
     */
    private boolean pwRuleCheck() {
        pwRule = "^.*(?=^.{8,12}$)(\\w)(?=.*[!@#$%^&+=]).*$";
        pattern_pw = Pattern.compile(pwRule);

        edit_pw = signup_pw.getText().toString();
        matcher_pw = pattern_pw.matcher(edit_pw);

        if (!matcher_pw.find()) {

            pwStatus = false;
            nextBtn_1.setVisibility(View.INVISIBLE);
            nextBlock_1.setVisibility(View.VISIBLE);

            return false;

        } else {
            return true;
        }

    } // pwRuleCheck()


    /*
    비밀번호확인 점검
     */
    private boolean pwCheckRuleCheck() {
        edit_pw = signup_pw.getText().toString();
        edit_pwCheck = signup_pwCheck.getText().toString();

        if (!edit_pw.equals(edit_pwCheck)) {
            pwCheckStatus = false;
            nextBtn_1.setVisibility(View.INVISIBLE);
            nextBlock_1.setVisibility(View.VISIBLE);

            return false;

        } else {
            return true;
        }

    } // pwCheckRuleCheck()


    /*
    닉네임 정규식 점검
     */
    private boolean nicknameRuleCheck() {
        nicknameRule = "^\\w{2,12}$";
        pattern_nick = Pattern.compile(nicknameRule);

        edit_nickname = signup_nickname.getText().toString();
        matcher_nick = pattern_nick.matcher(edit_nickname);

        if (!matcher_nick.find()) {

            nicknameStatus = false;
            nextBtn_1.setVisibility(View.INVISIBLE);
            nextBlock_1.setVisibility(View.VISIBLE);
            Log.d(TAG, "닉네임 정규식 체크 nickname Status : " + nicknameStatus + " 버튼 비활성화!");

            return false;

        } else {
            return true;
        }

    } // nicknameRuleCheck()


    /*
    휴대폰 번호 정규식 점검
     */
    private boolean phoneRuleCheck() {

        phoneRule = "^\\d{10,11}$";
        pattern_ph = Pattern.compile(phoneRule);
        matcher_ph = pattern_ph.matcher(edit_phone);

        if (!matcher_ph.find()) {
            return false;
        } else {
            return true;
        }

    } // phoneRuleCheck()


    /*
    로그인 타입 확인
     */
    public void checkLoginType() {

        // ▼ 일반 회원가입 or 소셜 로그인 경로의 회원가입 구분 ▼
        Intent i = getIntent();
        kakaoId = i.getStringExtra("kakaoId");
        kakaoImage = i.getStringExtra("kakaoImage");
        googleId = i.getStringExtra("googleId");
        naverId = i.getStringExtra("naverId");

        if (kakaoId != null) {
            Log.d(TAG, "카카오 로그인 : " + kakaoId);

            backBtn_2.setVisibility(View.INVISIBLE);
            frameLayout_1.setVisibility(View.INVISIBLE);
            frameLayout_2.setVisibility(View.VISIBLE);

            Glide.with(getApplicationContext())
                    .load(kakaoImage)
                    .into(photo_Btn);

            imagePath = kakaoImage;
            edit_id = kakaoId;

            infoDlg();

            login_Type = "Kakao";

        } else if (googleId != null) {
            Log.d(TAG, "구글 로그인 : " + googleId);

            backBtn_2.setVisibility(View.GONE);
            frameLayout_1.setVisibility(View.GONE);
            frameLayout_2.setVisibility(View.VISIBLE);

            infoDlg();
            edit_id = googleId;

            login_Type = "Google";

        } else if (naverId != null) {
            Log.d(TAG, "네이버 로그인 : " + naverId);

            backBtn_2.setVisibility(View.GONE);
            frameLayout_1.setVisibility(View.GONE);
            frameLayout_2.setVisibility(View.VISIBLE);

            infoDlg();
            edit_id = naverId;

            login_Type = "Naver";

        } else {
            Log.d(TAG, "확인된 로그인 타입 없음 : " + kakaoId + " " + googleId + " " + naverId);
            login_Type = "Basic";
        }

    } // checkLoginType()


}