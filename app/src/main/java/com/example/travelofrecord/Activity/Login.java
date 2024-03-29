package com.example.travelofrecord.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.R;
import com.example.travelofrecord.Data.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.sdk.user.UserApiClient;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.profile.NidProfileCallback;
import com.navercorp.nid.profile.data.NidProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    public static Context context;
    String TAG = "로그인 액티비티";
    String user_type, user_id, user_pw, user_phone, user_nickname, user_image, user_memo, user_writeCount,
            rp_code, edit_id, edit_pw, kakaoId, kakaoImage, googleEmail, naverEmail, savedTime, currentTime;
    int networkStatus, socialLoginBtnStatus, phoneCount;
    Button login_Btn, signup_Btn, findInfo_Btn;
    EditText login_id, login_pw;
    ImageButton kakao_Btn, naver_Btn, google_Btn, back_Btn;
    LinearLayout socialLogin_Layout, socialLoginBtn_Layout;
    ImageView dropDown_Btn, dropUp_Btn;
    SharedPreferences userShared, authShared;
    SharedPreferences.Editor editor, authEditor;
    ActivityResultLauncher<Intent> launcher_Google;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount account;
    Task<GoogleSignInAccount> task;
    ActivityResultLauncher<Intent> launcher_Naver;
    NaverIdLoginSDK naverLoginSDK;
    NidOAuthLogin nidOAuthLogin;
    GetTime getTime;
    Animation appearAnim, disappearAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate() 호출");

        context = this;

        setVariable();
        setListener();
        setLauncher();


    } // onCreate()


    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출");

        socialLoginBtnStatus = 0;

    }  // onStart()


    /*
    초기 변수 세팅
     */
    public void setVariable() {

        networkStatus = NetworkStatus.getConnectivityStatus(getApplicationContext());

        login_Btn = findViewById(R.id.login_Btn);
        back_Btn = findViewById(R.id.loginBack_Btn);
        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);
        kakao_Btn = findViewById(R.id.Kakao_button);
        google_Btn = findViewById(R.id.Google_button);
        naver_Btn = findViewById(R.id.Naver_button);
        signup_Btn = findViewById(R.id.Signup_button);
        findInfo_Btn = findViewById(R.id.find_Info);

        socialLogin_Layout = findViewById(R.id.socialLogin_Layout);
        socialLoginBtn_Layout = findViewById(R.id.socialLoginBtn_Layout);
        dropDown_Btn = findViewById(R.id.loginDropDown_Btn);
        dropUp_Btn = findViewById(R.id.loginDropUp_Btn);

        userShared = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = userShared.edit();

        authShared = getSharedPreferences("휴대폰 인증", MODE_PRIVATE);
        authEditor = authShared.edit();

        phoneCount = authShared.getInt("남은 횟수", 99);
        savedTime = authShared.getString("해당 날짜", "");
        getTime = new GetTime();
        currentTime = getTime.getFormatTime2(getTime.getTime());

        if (phoneCount == 99) {
            authEditor.putInt("남은 횟수", 3);
            authEditor.putString("해당 날짜", currentTime);
            authEditor.commit();
        } else {

            if (!currentTime.equals(savedTime)) {
                authEditor.putInt("남은 횟수", 3);
                authEditor.putString("해당 날짜", currentTime);
                authEditor.commit();
            } else {
                Log.d(TAG, "날짜가 바뀌지 않았음");
            }

        }

        naverLoginSDK = NaverIdLoginSDK.INSTANCE;
        naverLoginSDK.initialize(getApplicationContext(),
                getString(R.string.naver_client_id),
                getString(R.string.naver_client_secret),
                getString(R.string.naver_client_name));
        nidOAuthLogin = new NidOAuthLogin();

        appearAnim = AnimationUtils.loadAnimation(Login.this, R.anim.socialloginappear);
        disappearAnim = AnimationUtils.loadAnimation(Login.this, R.anim.sociallogindisappear);

    }  // setVariable()


    /*
    리스너 세팅
     */
    public void setListener() {

        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

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

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findInfo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    Intent i = new Intent(Login.this, Find_UserInfo.class);
                    startActivity(i);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        signup_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    Intent i = new Intent(signup_Btn.getContext(), Signup.class);
                    startActivity(i);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        kakao_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    kakao_Dialog();

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        google_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();

                    googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

                    Intent i = googleSignInClient.getSignInIntent();
                    launcher_Google.launch(i);

                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        naver_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    naverLoginSDK.authenticate(getApplicationContext(), launcher_Naver);

                    Log.d(TAG, "onClick");
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        socialLoginBtn_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                socialLoginBtnStatus += 1;
                Log.d(TAG, "socialLoginBtnStatus : " + socialLoginBtnStatus);

                if (socialLoginBtnStatus % 2 == 0) {
                    dropDown_Btn.setVisibility(View.VISIBLE);
                    dropUp_Btn.setVisibility(View.GONE);
                    socialLogin_Layout.setVisibility(View.GONE);
                    socialLogin_Layout.startAnimation(disappearAnim);
                } else {
                    dropDown_Btn.setVisibility(View.GONE);
                    dropUp_Btn.setVisibility(View.VISIBLE);
                    socialLogin_Layout.setVisibility(View.VISIBLE);
                    socialLogin_Layout.startAnimation(appearAnim);
                }

            }
        });

    } // setLisenter()


    /*
    ActivityResultLauncher 세팅
     */
    public void setLauncher() {

        launcher_Google = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.d(TAG, "googleLogin - onActivityResult : ok");
                    task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                    try {

                        account = task.getResult(ApiException.class);
                        googleEmail = account.getEmail();

                        googleLogin(googleEmail, "");

                    } catch (ApiException e) {
                        Log.d(TAG, "googleLogin - onActivityResult Exception : " + e);
                    }

                } else {
                    Log.d(TAG, "googleLogin - onActivityResult : fail");
                }

            }
        });

        launcher_Naver = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.d(TAG, "naverLogin - onActivityResult: ok");

                    String naverToken = naverLoginSDK.getAccessToken();
                    Log.d(TAG, "launcher naverToken : " + naverToken);

                    nidOAuthLogin.callProfileApi(new NidProfileCallback<NidProfileResponse>() {
                        @Override
                        public void onSuccess(NidProfileResponse nidProfileResponse) {

                            String email = nidProfileResponse.getProfile().getEmail();
                            Log.d(TAG, "onSuccess : " + nidProfileResponse.getProfile().toString());

                            naverEmail = email;

                            naverLogin(naverEmail, "");

                        }
                        @Override
                        public void onFailure(int i, @NonNull String s) {
                            Log.d(TAG, "onFailure : " + i);
                            Log.d(TAG, "onFailure : " + s);
                        }
                        @Override
                        public void onError(int i, @NonNull String s) {
                            Log.d(TAG, "onError : " + i);
                            Log.d(TAG, "onError : " + s);
                            onFailure(i, s);
                        }
                    });


                } else {
                    Log.d(TAG, "naverLogin - onActivityResult: fail");
                    Log.d(TAG, "Error Code: " + naverLoginSDK.getLastErrorCode().toString());
                    Log.d(TAG, "Error Description : " + naverLoginSDK.getLastErrorDescription());
                }


            }
        });

    } // setLauncher()


    /*
    카카오 로그인 방식 선택 다이얼로그
     */
    public void kakao_Dialog() {

        AlertDialog.Builder dlg = new AlertDialog.Builder(Login.this);
        dlg.setTitle("로그인 방식을 선택하세요\n"); //제목
        final String[] kakaoLogin_Array = new String[] {"카카오톡","카카오계정"};

        dlg.setItems(kakaoLogin_Array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) { // 카카오톡 로그인

                    UserApiClient.getInstance().loginWithKakaoTalk(Login.this,(oAuthToken, error) -> {
                        if (error != null) {
                            Log.d(TAG, "로그인 실패", error);
                        } else if (oAuthToken != null) {
                            Log.d(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());

                            UserApiClient.getInstance().me((user, meError) -> {
                                if (meError != null) {
                                    Log.d(TAG, "사용자 정보 요청 실패", meError);
                                } else {
                                    System.out.println("로그인 완료");
                                    Log.d(TAG, user.toString());

                                    Log.d(TAG, "사용자 정보 요청 성공" +
                                                    "\n회원번호: " + user.getId() +
                                                    "\n이메일: " + user.getKakaoAccount().getEmail() +
                                        "\n프로필 사진: " + user.getKakaoAccount().getProfile().getProfileImageUrl()
                                    );

                                    kakaoId = user.getKakaoAccount().getEmail();
                                    kakaoImage = user.getKakaoAccount().getProfile().getProfileImageUrl();

                                    getKakaoTest(kakaoId, "");

                                }
                                return null;
                            });
                        }
                        return null;
                    });

                }
                else if (which == 1) { // 카카오계정 로그인

                    UserApiClient.getInstance().loginWithKakaoAccount(Login.this,(oAuthToken, error) -> {
                        if (error != null) {
                            Log.d(TAG, "로그인 실패", error);
                        } else if (oAuthToken != null) {
                            Log.d(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());

                            UserApiClient.getInstance().me((user, meError) -> {
                                if (meError != null) {
                                    Log.d(TAG, "사용자 정보 요청 실패", meError);
                                } else {
                                    System.out.println("로그인 완료");
                                    Log.d(TAG, user.toString());

                                    Log.d(TAG, "사용자 정보 요청 성공" +
                                                    "\n회원번호: " + user.getId() +
                                                    "\n이메일: " + user.getKakaoAccount().getEmail() +
                                        "\n프로필 사진: " + user.getKakaoAccount().getProfile().getProfileImageUrl()
                                    );

                                    kakaoId = user.getKakaoAccount().getEmail();
                                    kakaoImage = user.getKakaoAccount().getProfile().getProfileImageUrl();

                                    getKakaoTest(kakaoId, "");

                                }
                                return null;
                            });
                        }
                        return null;
                    });


                }

            }
        });

        dlg.setNegativeButton("취소",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dlg.show();

    } // kakaoDialog()


    /*
    구글 로그인 진행
     */
    public void googleLogin(String id, String pw) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getLoginInfo(id,pw);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "googleLogin - onResponse isSuccessful");

                    String rpCode = response.body().getResponse();
                    String loginType = response.body().getType();

                    if (rpCode.equals("noId")) {

                        Intent i = new Intent(getApplicationContext(), Signup.class);
                        i.putExtra("googleId", googleEmail);
                        startActivity(i);

                    } else if (rpCode.equals("ok")) {

                        if (loginType.equals("Google")) {

                            Toast.makeText(getApplicationContext(),"환영합니다!",Toast.LENGTH_SHORT).show();

                            user_type = response.body().getType();
                            user_id = response.body().getId();
                            user_pw = response.body().getPw();
                            user_phone = response.body().getPhone();
                            user_nickname = response.body().getNickname();
                            user_image = response.body().getImage();
                            user_memo = response.body().getMemo();
                            user_writeCount = response.body().getWriteCount();

                            editor.putString("loginType", user_type);
                            editor.putString("id", user_id);
                            editor.putString("nickname", user_nickname);
                            editor.putString("image", user_image);
                            editor.putString("memo", user_memo);
                            editor.putString("writeCount", user_writeCount);
                            editor.commit();
                            Intent i = new Intent(Login.this, Home.class);
                            startActivity(i);
                            ((Start)Start.context).finish();
                            finish();

                        } else if (loginType.equals("Kakao")) {



                        } else if (loginType.equals("Naver")) {



                        } else if (loginType.equals("Basic")) {



                        }



                    }

                } else {
                    Log.d(TAG, "googleLogin - onResponse isFailure");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "googleLogin - onFailure");
            }
        });

    } // googleLogin()


    /*
    네이버 로그인 진행
     */
    public void naverLogin(String id, String pw) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getLoginInfo(id,pw);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "naverLogin - onResponse isSuccessful");

                    String rpCode = response.body().getResponse();
                    String loginType = response.body().getType();
                    Log.d(TAG, "naverLogin - onResponse : " + rpCode);

                    if (rpCode.equals("noId")) {

                        Intent i = new Intent(getApplicationContext(), Signup.class);
                        i.putExtra("naverId", naverEmail);
                        startActivity(i);

                    } else if (rpCode.equals("ok")) {

                        if (loginType.equals("Naver")) {

                            Toast.makeText(getApplicationContext(),"환영합니다!",Toast.LENGTH_SHORT).show();

                            user_type = response.body().getType();
                            user_id = response.body().getId();
                            user_pw = response.body().getPw();
                            user_phone = response.body().getPhone();
                            user_nickname = response.body().getNickname();
                            user_image = response.body().getImage();
                            user_memo = response.body().getMemo();
                            user_writeCount = response.body().getWriteCount();

                            editor.putString("loginType", user_type);
                            editor.putString("id", user_id);
                            editor.putString("nickname", user_nickname);
                            editor.putString("image", user_image);
                            editor.putString("memo", user_memo);
                            editor.putString("writeCount", user_writeCount);
                            editor.commit();
                            Intent i = new Intent(Login.this, Home.class);
                            startActivity(i);
                            ((Start)Start.context).finish();
                            finish();

                        } else if (loginType.equals("Kakao")) {



                        } else if (loginType.equals("Google")) {



                        } else if (loginType.equals("Basic")) {



                        }



                    }

                } else {
                    Log.d(TAG, "naverLogin - onResponse isFailure");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "naverLogin - onFailure");
            }
        });

    } // naverLogin()


    /*
    DB 로그인 정보 확인
     */
    public void getLogin(String id, String pw) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getLoginInfo(id,pw);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                rp_code = response.body().getResponse();
                Log.d(TAG, "응답 : " + rp_code);

                if (rp_code.equals("noId")) {

                    Toast t = Toast.makeText(login_Btn.getContext(),"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT);
                    t.show();

                } else if (rp_code.equals("noPw")) {
                    Toast t = Toast.makeText(login_Btn.getContext(),"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT);
                    t.show();

                } else {
                    Toast t = Toast.makeText(login_Btn.getContext(),"환영합니다!",Toast.LENGTH_SHORT);
                    t.show();

                    user_type = response.body().getType();
                    user_id = response.body().getId();
                    user_pw = response.body().getPw();
                    user_phone = response.body().getPhone();
                    user_nickname = response.body().getNickname();
                    user_image = response.body().getImage();
                    user_memo = response.body().getMemo();
                    user_writeCount = response.body().getWriteCount();

                    Log.d(TAG, "서버에서 전달 받은 코드 - 타입 : " + user_type + "\n아이디 : " + user_id + "\n비번 : "
                            + user_pw + "\n전화번호 : " + user_phone + "\n닉네임 : " + user_nickname + "\n이미지 : " + user_image);

                    editor.putString("loginType", user_type);
                    editor.putString("id", user_id);
                    editor.putString("nickname", user_nickname);
                    editor.putString("image", user_image);
                    editor.putString("memo", user_memo);
                    editor.putString("writeCount", user_writeCount);
                    editor.commit();
                    Intent i = new Intent(Login.this, Home.class);
                    startActivity(i);
                    ((Start)Start.context).finish();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }
        });

    }  // getLogin()


    /*
    DB 카카오 계정 로그인 기록 확인
     */
    public void getKakaoTest(String id, String pw) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getLoginInfo(id,pw);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                rp_code = response.body().getResponse();

                Log.d(TAG, "카카오 - 아이디 조회 : " + rp_code);

                if (response.isSuccessful()) {

                    // 사용자의 가입 정보가 있으면 홈으로 이동
                    // 사용자의 가입 정보가 없으면 회원가입 3페이지로 이동

                    if (rp_code.equals("noId")) {

                        Intent i = new Intent(Login.this, Signup.class);
                        i.putExtra("kakaoId", kakaoId);
                        i.putExtra("kakaoImage", kakaoImage);
                        startActivity(i);

                    } else {

                        user_type = response.body().getType();
                        user_id = response.body().getId();
                        user_pw = response.body().getPw();
                        user_phone = response.body().getPhone();
                        user_nickname = response.body().getNickname();
                        user_image = response.body().getImage();
                        user_memo = response.body().getMemo();
                        user_writeCount = response.body().getWriteCount();

                        Log.d(TAG, "서버에서 전달 받은 코드 : " + user_type + "\n" + user_id + "\n" + user_pw + "\n" + user_phone + "\n" + user_nickname + "\n" + user_image);

                        editor.putString("loginType", user_type);
                        editor.putString("id", kakaoId);
                        editor.putString("nickname", user_nickname);
                        editor.putString("image", kakaoImage);
                        editor.putString("memo", user_memo);
                        editor.putString("writeCount", user_writeCount);
                        editor.commit();

                        Intent i = new Intent(Login.this, Home.class);
                        startActivity(i);
                        ((Start)Start.context).finish();
                        finish();

                    }
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }
        });

    }  // getKakaoTest()


}