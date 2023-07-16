package com.example.travelofrecord.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Find_UserInfo extends AppCompatActivity {

    String TAG = "회원 정보 찾기";

    FrameLayout findInfoFrame;
    FrameLayout findIdFrame1;
    FrameLayout findIdFrame2;
    FrameLayout findPwFrame1;
    FrameLayout findPwFrame2;

    ImageButton findInfo_backBtn;
    ImageButton findId_backBtn;
    ImageButton findPw_backBtn;

    Button findId_Btn;
    Button findPw_Btn;
    Button findId_sendBtn;
    Button findId_sendBlock;
    Button findPw_sendBtn;
    Button findPw_sendBlock;
    Button findId_checkBtn;
    Button findId_checkBlock;
    Button findPw_checkBtn;
    Button findPw_checkBlock;
    Button findId_submitBtn;
    Button findPw_submitBtn;
    Button findPw_submitBlock;

    TextView findId_sendText;
    TextView findId_smsTimeText;
    TextView findId_smsErrorText;
    TextView findId_smsTimeoutText;
    TextView findId_idText;
    TextView findPw_sendText;
    TextView findPw_smsTimeText;
    TextView findPw_smsErrorText;
    TextView findPw_smsTimeoutText;
    TextView findPw_Ok;
    TextView findPw_RuleError;
    TextView findPw_pwInfoText;

    EditText findId_phone;
    EditText findId_checkNum;
    EditText findPw_email;
    EditText findPw_checkNum;
    EditText findPw_newPw;

    Handler handler;
    String setTime;
    int smsTime;
    int smsTime_min;
    int smsTime_sec;
    int smsCheckNumber;
    int findCode;
    SmsTimeThread smsTimeThread;

    String user_phoneNum;
    String user_email;
    String idCheckNum_value;
    String pwCheckNum_value;
    String id_Code;
    String pw_Code;
    String phone_Code;
    String pwRule;
    String new_Pw;

    Pattern pattern_pw;
    Matcher matcher_pw;

    Animation left_out;
    Animation left_in;
    Animation right_out;
    Animation right_in;

    int networkStatus;

    Button emailTestBtn;

    FirebaseAuth auth;
    String smsCode;
    String emailCode;

    // 갤러리 접근 권한
    private static final int SMS_SEND_PERMISSION = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS
    };

    public static void verifySmsSendPermissions(Activity activity){

        int SMS_PERMISSION = ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS);

        if (SMS_PERMISSION != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, SMS_SEND_PERMISSION);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user_info);

        setView();
//        verifySmsSendPermissions(Find_UserInfo.this);

    }

    public void sendSms(String phoneNumber) {

//        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, Find_UserInfo.class), PendingIntent.FLAG_MUTABLE);
//
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(phoneNumber, null, smsMessage, pi, null);
//
//        Toast.makeText(this, "전송 완료", Toast.LENGTH_SHORT).show();
        auth = FirebaseAuth.getInstance();

        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential.getSmsCode());
                smsCode = phoneAuthCredential.getSmsCode();

            }
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

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);
                String mVerificationId = verificationId;
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

    }



    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onResume() 호출됨");

        findId_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    findInfoFrame.setVisibility(View.INVISIBLE);
                    findInfoFrame.startAnimation(left_out);
                    findIdFrame1.setVisibility(View.VISIBLE);
                    findIdFrame1.startAnimation(left_in);
            }else {
                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }

            }
        });

        findPw_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    findInfoFrame.setVisibility(View.INVISIBLE);
                    findInfoFrame.startAnimation(left_out);
                    findPwFrame1.setVisibility(View.VISIBLE);
                    findPwFrame1.startAnimation(left_in);
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findInfo_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 아이디 찾기
        findId_sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    findCode = 1;
                    user_phoneNum = findId_phone.getText().toString();

                    smsTimeThread = new SmsTimeThread();
                    smsTimeThread.start();

                    findId_sendText.setVisibility(View.VISIBLE);
                    findId_sendBtn.setVisibility(View.INVISIBLE);
                    findId_sendBlock.setVisibility(View.VISIBLE);
                    findId_checkBtn.setVisibility(View.VISIBLE);
                    findId_checkBlock.setVisibility(View.INVISIBLE);
                    findId_smsTimeText.setVisibility(View.VISIBLE);
                    findId_smsErrorText.setVisibility(View.INVISIBLE);
                    findId_smsTimeoutText.setVisibility(View.INVISIBLE);

                    smsCheckNumber = 1;

                    String phoneNum = "+82" + user_phoneNum.substring(1,user_phoneNum.length());
                    Log.d(TAG, "전송할 핸드폰 번호 : " + phoneNum);
                    sendSms(phoneNum);



                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findId_checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    idCheckNum_value = findId_checkNum.getText().toString();

                    smsCheckNumber = 2;
                    smsTime = 0;

                    Log.d(TAG, "입력한 값 : " + idCheckNum_value + " / 인증번호 : " + smsCode);

                    if (idCheckNum_value.equals(smsCode)) {
                        findIdFrame1.setVisibility(View.INVISIBLE);
                        findIdFrame1.startAnimation(left_out);
                        findIdFrame2.setVisibility(View.VISIBLE);
                        findIdFrame2.startAnimation(left_in);

                        phoneCheck(user_phoneNum);
                        auth.signOut();
                    } else {
                        findId_smsTimeText.setVisibility(View.INVISIBLE);
                        findId_smsErrorText.setVisibility(View.VISIBLE);
                        findId_sendText.setVisibility(View.INVISIBLE);
                        findId_checkBlock.setVisibility(View.VISIBLE);
                        findId_checkBtn.setVisibility(View.INVISIBLE);
                        findId_sendBtn.setVisibility(View.VISIBLE);
                        findId_sendBlock.setVisibility(View.INVISIBLE);

                        findId_checkNum.setText("");

                        auth.signOut();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findId_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findId_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    findInfoFrame.setVisibility(View.VISIBLE);
                    findInfoFrame.startAnimation(right_in);
                    findIdFrame1.setVisibility(View.INVISIBLE);
                    findIdFrame1.startAnimation(right_out);

                    findId_phone.setText("");
                    findId_checkNum.setText("");
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });



        // 비밀번호 찾기
        findPw_sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    findCode = 2;
                    user_email = findPw_email.getText().toString();

                    smsTimeThread = new SmsTimeThread();
                    smsTimeThread.start();

                    findPw_sendText.setVisibility(View.VISIBLE);
                    findPw_smsTimeText.setVisibility(View.VISIBLE);
                    findPw_sendBtn.setVisibility(View.INVISIBLE);
                    findPw_sendBlock.setVisibility(View.VISIBLE);
                    findPw_checkBtn.setVisibility(View.VISIBLE);
                    findPw_checkBlock.setVisibility(View.INVISIBLE);
                    findPw_smsErrorText.setVisibility(View.INVISIBLE);
                    findPw_smsTimeoutText.setVisibility(View.INVISIBLE);

                    smsCheckNumber = 1;
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        findPw_checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    pwCheckNum_value = findPw_checkNum.getText().toString();

                    smsCheckNumber = 2;
                    smsTime = 0;

                    if (pwCheckNum_value.equals("7777")) {

                        idCheck(user_email);

                        findPwFrame1.setVisibility(View.INVISIBLE);
                        findPwFrame1.startAnimation(left_out);
                        findPwFrame2.setVisibility(View.VISIBLE);
                        findPwFrame2.startAnimation(left_in);

                    } else {

                        findPw_smsTimeText.setVisibility(View.INVISIBLE);
                        findPw_smsErrorText.setVisibility(View.VISIBLE);
                        findPw_sendText.setVisibility(View.INVISIBLE);
                        findPw_sendBlock.setVisibility(View.INVISIBLE);
                        findPw_sendBtn.setVisibility(View.VISIBLE);
                        findPw_checkBlock.setVisibility(View.VISIBLE);
                        findPw_checkBtn.setVisibility(View.INVISIBLE);

                        findPw_checkNum.setText("");

                    }
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        // 비밀번호 입력 이벤트 처리
        findPw_newPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {

                if (pwRuleCheck()) {
                    findPw_Ok.setVisibility(View.VISIBLE);
                    findPw_RuleError.setVisibility(View.INVISIBLE);
                    findPw_submitBtn.setVisibility(View.VISIBLE);
                    findPw_submitBlock.setVisibility(View.INVISIBLE);

                } else {

                    findPw_Ok.setVisibility(View.INVISIBLE);
                    findPw_RuleError.setVisibility(View.VISIBLE);
                    findPw_submitBtn.setVisibility(View.INVISIBLE);
                    findPw_submitBlock.setVisibility(View.VISIBLE);
                }

            }
        });



        findPw_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    if (id_Code.equals("usingId")) {
                        getNewPw(user_email, new_Pw);
                    } else {
                        finish();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findPw_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    findInfoFrame.setVisibility(View.VISIBLE);
                    findInfoFrame.startAnimation(right_in);
                    findPwFrame1.setVisibility(View.INVISIBLE);
                    findPwFrame1.startAnimation(right_out);

                    findPw_email.setText("");
                    findPw_checkNum.setText("");
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        emailTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
//                        .setUrl("https://console.firebase.google.com")
//                        .setHandleCodeInApp(true)
//                        .setAndroidPackageName("com.example.travelofrecord", true, "12")
//                        .build();
//
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                auth.sendSignInLinkToEmail("rkdgjdl@naver.com", actionCodeSettings)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                Log.d(TAG, "onComplete : Email sent");
//
//                                Intent i = getIntent();
//                                String emailLink = i.getData().toString();
//
//                                if (auth.isSignInWithEmailLink(emailLink)) {
//
//                                    String email = "rkdgdjl@naver.com";
//
//                                    auth.signInWithEmailLink(email, emailLink)
//                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                                    Log.d(TAG, "onComplete: success emailLink");
//
//                                                }
//                                            });
//
//                                }
//
//                            }
//                        });

//                sendSms("+821082435284");



            }
        });


    } // onStart()


    // ▼ SMS 인증 남은 시간 스레드 ▼
    public class SmsTimeThread extends Thread {

        public void run() {
            smsTime = 120;

            while (smsTime >= 0) {
                smsTime_min = smsTime / 60;
                smsTime_sec = smsTime % 60;
//                Log.d(TAG, "남은 시간 : " + smsTime);
//                Log.d(TAG, "표시 : " + setTime);

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

                        findId_smsTimeText.setText(setTime);
                        findPw_smsTimeText.setText(setTime);

                        if (smsCheckNumber == 1) {

                            if (smsTime == 0) {
                                Log.d(TAG, "스레드 종료 1");

                                if (findCode == 1) {

                                    findId_smsTimeText.setVisibility(View.INVISIBLE);
                                    findId_smsTimeoutText.setVisibility(View.VISIBLE);
                                    findId_checkBlock.setVisibility(View.VISIBLE);
                                    findId_checkBtn.setVisibility(View.INVISIBLE);
                                    findId_sendBlock.setVisibility(View.INVISIBLE);
                                    findId_sendBtn.setVisibility(View.VISIBLE);
                                    findId_sendText.setVisibility(View.INVISIBLE);

                                } else if (findCode == 2) {

                                    findPw_smsTimeText.setVisibility(View.INVISIBLE);
                                    findPw_smsTimeoutText.setVisibility(View.VISIBLE);
                                    findPw_checkBlock.setVisibility(View.VISIBLE);
                                    findPw_checkBtn.setVisibility(View.INVISIBLE);
                                    findPw_sendBlock.setVisibility(View.INVISIBLE);
                                    findPw_sendBtn.setVisibility(View.VISIBLE);
                                    findPw_sendText.setVisibility(View.INVISIBLE);

                                }

                            }

                        }

                    }
                });

                smsTime -= 1;

            }

        }

    }


    // ▼ 아이디 중복 검사 ▼
    public void idCheck(String id) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getIdCheck(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                id_Code = response.body().toString();
                Log.d(TAG, "onResponse: " + id_Code);

                if (!id_Code.equals("usingId")) {

                    findPw_newPw.setVisibility(View.INVISIBLE);
                    findPw_pwInfoText.setText("가입된 이메일 정보가 없습니다.");
                    findPw_submitBlock.setVisibility(View.INVISIBLE);
                    findPw_submitBtn.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }
        });

    }  // idCheck()


    // ▼ PW 변경 ▼
    public void getNewPw(String id, String pw) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getNewPw(id,pw);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                pw_Code = response.body().toString();

                Log.d(TAG, "onResponse: " + pw_Code);

                Toast t = Toast.makeText(Find_UserInfo.this, "비밀번호 재설정 완료", Toast.LENGTH_SHORT);
                t.show();

//                Intent i = new Intent(Find_UserInfo.this,Login.class);
//                startActivity(i);
                finish();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }
        });

    }  // getNewPw()

    // ▼ 휴대폰 번호 중복 검사 ▼
    public void phoneCheck(String phone) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getPhoneCheck(phone);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                phone_Code = response.body().toString();
                Log.d(TAG, "onResponse: " + phone_Code);

                if (phone_Code.equals("NOID")) {

                    Log.d(TAG, "onResponse: 가입 정보 없음 " + phone_Code);

                } else {

                    Log.d(TAG, "onResponse: 가입 정보 있음 " + phone_Code);

                    findId_idText.setText(phone_Code);

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    } // phoneCheck()


    // ▼ 비밀번호 정규식 점검 ▼
    private boolean pwRuleCheck() {

        pwRule = "^.*(?=^.{8,12}$)(\\w)(?=.*[!@#$%^&+=]).*$";
        pattern_pw = Pattern.compile(pwRule);

        new_Pw = findPw_newPw.getText().toString();
        matcher_pw = pattern_pw.matcher(new_Pw);

        if(!matcher_pw.find()) {
            return false;
        } else {
            return true;
        }

    }

    // 랜덤 문자열 생성
    public String getRandomResult() {

        Random ran = new Random();
        String result = "";
        int type;
        char c;
        int i;

        for (int j = 0; j < 6; j++) {

            type = ran.nextInt(2);

            if (type == 0) {

                i = 0;

                while(i < 64) {

                    i = ran.nextInt(91);

                }

                c = (char) i;
                result += c;

            } else {

                i = ran.nextInt(9);
                result += String.valueOf(i+1);

            }
        }

        return result;

    } // getRandomResult()



    public void setView() {

        networkStatus = NetworkStatus.getConnectivityStatus(getApplicationContext());

        findInfoFrame = findViewById(R.id.findInfo_Frame);
        findIdFrame1 = findViewById(R.id.findInfo_idFrame1);
        findIdFrame2 = findViewById(R.id.findInfo_idFrame2);
        findPwFrame1 = findViewById(R.id.findInfo_pwFrame1);
        findPwFrame2 = findViewById(R.id.findInfo_pwFrame2);

        findInfo_backBtn = findViewById(R.id.findInfo_backBtn);
        findId_backBtn = findViewById(R.id.findId_backBtn);
        findPw_backBtn = findViewById(R.id.findPw_backBtn);
        findId_Btn = findViewById(R.id.findInfo_idBtn);
        findPw_Btn = findViewById(R.id.findInfo_pwBtn);
        findId_sendBtn = findViewById(R.id.findId_sendBtn);
        findId_sendBlock = findViewById(R.id.findId_sendBlock);
        findPw_sendBtn = findViewById(R.id.findPw_sendBtn);
        findPw_sendBlock = findViewById(R.id.findPw_sendBlock);
        findId_checkBtn = findViewById(R.id.findId_CheckBtn);
        findId_checkBlock = findViewById(R.id.findId_CheckBlock);
        findPw_checkBtn = findViewById(R.id.findPw_checkBtn);
        findPw_checkBlock = findViewById(R.id.findPw_checkBlock);
        findId_submitBtn = findViewById(R.id.findId_submitBtn);
        findPw_submitBtn = findViewById(R.id.findPw_submitBtn);
        findPw_submitBlock = findViewById(R.id.findPw_submitBlock);

        findId_sendText = findViewById(R.id.findId_sendText);
        findId_smsTimeText = findViewById(R.id.findId_smsTimeText);
        findId_smsErrorText = findViewById(R.id.findId_smsErrorText);
        findId_smsTimeoutText = findViewById(R.id.findId_smsTimeoutText);
        findId_idText = findViewById(R.id.findid_Text);
        findPw_sendText = findViewById(R.id.findPw_sendText);
        findPw_smsTimeText = findViewById(R.id.findPw_timeText);
        findPw_smsErrorText = findViewById(R.id.findPw_errorText);
        findPw_smsTimeoutText = findViewById(R.id.findPw_timeoutText);
        findPw_Ok = findViewById(R.id.findPw_Ok);
        findPw_RuleError = findViewById(R.id.findPw_RuleError);
        findPw_pwInfoText = findViewById(R.id.findPw_pwInfoText);

        findId_phone = findViewById(R.id.findId_phone);
        findId_checkNum = findViewById(R.id.findId_checkNum);
        findPw_email = findViewById(R.id.findPw_email);
        findPw_checkNum = findViewById(R.id.findPw_checkNum);
        findPw_newPw = findViewById(R.id.findPw_newPw);

        handler = new Handler();

        left_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.frame_leftout);
        left_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.frame_leftin);
        right_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.frame_rightout);
        right_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.frame_rightin);

        emailTestBtn = findViewById(R.id.emailTest_Btn);


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