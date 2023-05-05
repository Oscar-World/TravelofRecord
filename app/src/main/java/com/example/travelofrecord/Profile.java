package com.example.travelofrecord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    String TAG = "프로필 액티비티";

    TextView profileNicknameText;
    TextView profileMemoText;
    Button profileInfoBtn;
    Button profileInfoBlock;
    Button profileMapBtn;
    Button profileMapBlock;
    Button profileDmBtn;
    ImageView profileImage;
    ImageButton profileBackBtn;
    RecyclerView profileRecyclerView;

    SharedPreferences sharedPreferences;

    String getNickname;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() 호출됨");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setVariable();
        setView();

    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출됨");
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

    public void getProfile(String nickname) {

        Call<ArrayList<PostData>> call = apiInterface.getProfile(nickname);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "getProfile onResponse");



                } else {
                    Log.d(TAG, "responseFail");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<PostData>> call, Throwable t) {
                Log.d(TAG, "getProfile onFailure");
            }
        });

    }

    public void setVariable() {

        profileNicknameText = findViewById(R.id.profile_nickname);
        profileMemoText = findViewById(R.id.profile_memo);
        profileInfoBtn = findViewById(R.id.profileInfo_Btn);
        profileInfoBlock = findViewById(R.id.profileInfo_Block);
        profileMapBtn = findViewById(R.id.profileMap_Btn);
        profileMapBlock = findViewById(R.id.profileMap_Block);
        profileDmBtn = findViewById(R.id.profileDM_Btn);
        profileImage = findViewById(R.id.profile_Image);
        profileBackBtn = findViewById(R.id.profileBack_Btn);
        profileRecyclerView = findViewById(R.id.profile_RecyclerView);

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        getNickname = sharedPreferences.getString("nickname","");

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }

    public void setView() {



    }

}