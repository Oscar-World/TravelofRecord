package com.example.travelofrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    String TAG = "로그인 액티비티";

    Button login_Btn;
    ImageButton back_Btn;
    EditText login_id;
    EditText login_pw;
    ImageButton kakao_Btn;
    ImageButton google_Btn;
    ImageButton naver_Btn;
    Button signup_Btn;
    Button findInfo_Btn;

    String rp_code;
    String edit_id;
    String edit_pw;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate() 호출");

        setView();

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출");

        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit_id = login_id.getText().toString();
                edit_pw = login_pw.getText().toString();

                if (edit_id.isEmpty()) {
                    Toast t = Toast.makeText(login_Btn.getContext(),"아이디를 입력해주세요.",Toast.LENGTH_SHORT);
                    t.show();
                } else if (edit_pw.isEmpty()) {
                    Toast t = Toast.makeText(login_Btn.getContext(),"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    getLogin(edit_id,edit_pw);
                }

            }
        });

        findInfo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        signup_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(signup_Btn.getContext(),Signup.class);
                startActivity(i);

            }
        });

        kakao_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserApiClient.getInstance().loginWithKakaoTalk(Login.this,(oAuthToken, error) -> {
                    if (error != null) {
                        Log.d(TAG, "로그인 실패", error);
                    } else if (oAuthToken != null) {
                        Log.d(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());

                        // 사용자의 가입 정보가 있으면 홈으로 이동
                        // 사용자의 가입 정보가 없으면 회원가입 3페이지로 이동

                        UserApiClient.getInstance().me((user, meError) -> {
                            if (meError != null) {
                                Log.d(TAG, "사용자 정보 요청 실패", meError);
                            } else {
                                System.out.println("로그인 완료");
                                Log.d(TAG, user.toString());
                                {
                                    Log.d(TAG, "사용자 정보 요청 성공" +
                                            "\n회원번호: "+user.getId() +
                                            "\n이메일: "+user.getKakaoAccount().getEmail());
                                }
                                Account user1 = user.getKakaoAccount();
                                System.out.println("사용자 계정" + user1);

                                getKakaoTest(user.getKakaoAccount().getEmail(),"1");

                                if(rp_code != null) {

                                    if (rp_code.equals("NOID")) {

                                        Intent i = new Intent(Login.this, Signup.class);
                                        i.putExtra("kakao", user.getKakaoAccount().getEmail());
                                        startActivity(i);
//                                        finish();

                                    } else if (rp_code.equals("NOPW")) {

                                        Intent i = new Intent(Login.this, Home.class);
                                        startActivity(i);

                                        editor.putString("로그인",user.getKakaoAccount().getEmail());
                                        editor.commit();

                                        finish();

                                    }
                                }

                            }
                            return null;
                        });
                    }
                    return null;
                });

            }
        });




        google_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        naver_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }  // onStart()

    public void getLogin(String id, String pw) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getLoginInfo(id,pw);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                rp_code = response.body().toString();

                Log.d(TAG, "onResponse: " + rp_code);

                if (rp_code.equals("NOID")) {

                    Toast t = Toast.makeText(login_Btn.getContext(),"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT);
                    t.show();

                } else if (rp_code.equals("NOPW")) {
                    Toast t = Toast.makeText(login_Btn.getContext(),"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT);
                    t.show();

                } else if (rp_code.equals("ok")) {
                    Toast t = Toast.makeText(login_Btn.getContext(),"환영합니다!",Toast.LENGTH_SHORT);
                    t.show();

                    editor.putString("로그인", edit_id);
                    editor.commit();

                    Intent i = new Intent(Login.this,Home.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }
        });

    }  // getLogin()


    public void getKakaoTest(String id, String pw) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getLoginInfo(id,pw);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                rp_code = response.body().toString();

                Log.d(TAG, "onResponse: " + rp_code);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }
        });

    }  // getLogin()


    public void setView() {

        login_Btn = findViewById(R.id.login_Btn);
        back_Btn = findViewById(R.id.loginBack_Btn);
        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);
        kakao_Btn = findViewById(R.id.Kakao_button);
        google_Btn = findViewById(R.id.Google_button);
        naver_Btn = findViewById(R.id.Naver_button);
        signup_Btn = findViewById(R.id.Signup_button);
        findInfo_Btn = findViewById(R.id.find_Info);

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }  // setView()

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