package com.example.travelofrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

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

    TextView findId_sendText;
    TextView findId_smsTimeText;
    TextView findId_smsErrorText;
    TextView findId_smsTimeoutText;
    TextView findId_idText;
    TextView findPw_sendText;
    TextView findPw_smsTimeText;
    TextView findPw_smsErrorText;
    TextView findPw_smsTimeoutText;

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
                findIdFrame1.setVisibility(View.VISIBLE);
            }
        });

        findPw_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findInfoFrame.setVisibility(View.INVISIBLE);
                findPwFrame1.setVisibility(View.VISIBLE);
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
                    findIdFrame2.setVisibility(View.VISIBLE);
                    //                findId_idText.setText();

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
            }
        });

        findId_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findInfoFrame.setVisibility(View.VISIBLE);
                findIdFrame1.setVisibility(View.INVISIBLE);

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

                    findPwFrame1.setVisibility(View.INVISIBLE);
                    findPwFrame2.setVisibility(View.VISIBLE);

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

        findPw_submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Find_UserInfo.this,Login.class);
                startActivity(i);
            }
        });

        findPw_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findInfoFrame.setVisibility(View.VISIBLE);
                findPwFrame1.setVisibility(View.INVISIBLE);

                findPw_email.setText("");
                findPw_checkNum.setText("");
            }
        });



    } // onStart()


    // ▼ SMS 인증 남은 시간 스레드 ▼
    public class SmsTimeThread extends Thread {

        public void run() {
            smsTime = 10;

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

        findId_sendText = findViewById(R.id.findId_sendText);
        findId_smsTimeText = findViewById(R.id.findId_smsTimeText);
        findId_smsErrorText = findViewById(R.id.findId_smsErrorText);
        findId_smsTimeoutText = findViewById(R.id.findId_smsTimeoutText);
        findId_idText = findViewById(R.id.findid_Text);
        findPw_sendText = findViewById(R.id.findPw_sendText);
        findPw_smsTimeText = findViewById(R.id.findPw_timeText);
        findPw_smsErrorText = findViewById(R.id.findPw_errorText);
        findPw_smsTimeoutText = findViewById(R.id.findPw_timeoutText);

        findId_phone = findViewById(R.id.findId_phone);
        findId_checkNum = findViewById(R.id.findId_checkNum);
        findPw_email = findViewById(R.id.findPw_email);
        findPw_checkNum = findViewById(R.id.findPw_checkNum);
        findPw_newPw = findViewById(R.id.findPw_newPw);

        handler = new Handler();

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