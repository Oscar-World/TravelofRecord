package com.example.travelofrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user_info);

        setView();

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onResume() 호출됨");


        // 회원 정보 찾기
        findId_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findInfoFrame.setVisibility(View.INVISIBLE);
                findInfoFrame.startAnimation(left_out);
                findIdFrame1.setVisibility(View.VISIBLE);
                findIdFrame1.startAnimation(left_in);
            }
        });

        findPw_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findInfoFrame.setVisibility(View.INVISIBLE);
                findInfoFrame.startAnimation(left_out);
                findPwFrame1.setVisibility(View.VISIBLE);
                findPwFrame1.startAnimation(left_in);
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

            }
        });

        findId_checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idCheckNum_value = findId_checkNum.getText().toString();

                smsCheckNumber = 2;
                smsTime = 0;

                if (idCheckNum_value.equals("7777")) {

                    findIdFrame1.setVisibility(View.INVISIBLE);
                    findIdFrame1.startAnimation(left_out);
                    findIdFrame2.setVisibility(View.VISIBLE);
                    findIdFrame2.startAnimation(left_in);

                    phoneCheck(user_phoneNum);

                } else {

                    findId_smsTimeText.setVisibility(View.INVISIBLE);
                    findId_smsErrorText.setVisibility(View.VISIBLE);
                    findId_sendText.setVisibility(View.INVISIBLE);
                    findId_checkBlock.setVisibility(View.VISIBLE);
                    findId_checkBtn.setVisibility(View.INVISIBLE);
                    findId_sendBtn.setVisibility(View.VISIBLE);
                    findId_sendBlock.setVisibility(View.INVISIBLE);

                    findId_checkNum.setText("");

                }

            }
        });

        findId_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Find_UserInfo.this,Login.class);
                startActivity(i);
                finish();
            }
        });

        findId_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findInfoFrame.setVisibility(View.VISIBLE);
                findInfoFrame.startAnimation(right_in);
                findIdFrame1.setVisibility(View.INVISIBLE);
                findIdFrame1.startAnimation(right_out);

                findId_phone.setText("");
                findId_checkNum.setText("");
            }
        });



        // 비밀번호 찾기
        findPw_sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

            }
        });


        findPw_checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                getNewPw(user_email,new_Pw);

            }
        });

        findPw_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findInfoFrame.setVisibility(View.VISIBLE);
                findInfoFrame.startAnimation(right_in);
                findPwFrame1.setVisibility(View.INVISIBLE);
                findPwFrame1.startAnimation(right_out);

                findPw_email.setText("");
                findPw_checkNum.setText("");
            }
        });



    } // onStart()


    // ▼ SMS 인증 남은 시간 스레드 ▼
    public class SmsTimeThread extends Thread {

        public void run() {
            smsTime = 180;

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

                Intent i = new Intent(Find_UserInfo.this,Login.class);
                startActivity(i);
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

                    Log.d(TAG, "onResponse: 가입 정보 없음" + phone_Code);

                } else {

                    Log.d(TAG, "onResponse: 가입 정보 있음" + phone_Code);

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



    public void setView() {

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