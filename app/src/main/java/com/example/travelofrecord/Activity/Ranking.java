package com.example.travelofrecord.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.travelofrecord.Adapter.Ranking_Adapter;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ranking extends AppCompatActivity {

    String TAG = "랭킹 액티비티";

    ImageButton backBtn;
    Button rankDayBtn;
    Button rankDayBlock;
    Button rankMonthBtn;
    Button rankMonthBlock;
    Button rankYearBtn;
    Button rankYearBlock;

    RecyclerView recyclerView;
    ImageView loadingImage;

    TextView userRankText;
    ImageView userImage;
    TextView userNicknameText;
    TextView heartNumText;

    LinearLayout userRankLayout;
    LinearLayout noRankLayout;
    FrameLayout noDataLayout;
    TextView dateText;

    ArrayList<PostData> arrayList;
    Ranking_Adapter adapter;
    ArrayList<PostData> data;
    String currentNickname;

    GetTime getTime;
    Animation loading;
    FrameLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        setVariable();
        setView();
        getRanking();

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

    public void setVariable() {

        backBtn = findViewById(R.id.rankingBack_Btn);
        rankDayBtn = findViewById(R.id.rankingDay_Btn);
        rankDayBlock = findViewById(R.id.rankingDay_Block);
        rankMonthBtn = findViewById(R.id.rankingMonth_Btn);
        rankMonthBlock = findViewById(R.id.rankingMonth_Block);
        rankYearBtn = findViewById(R.id.rankingYear_Btn);
        rankYearBlock = findViewById(R.id.rankingYear_Block);

        recyclerView = findViewById(R.id.ranking_RecyclerView);
        loadingImage = findViewById(R.id.rankingLoading_Image);

        userRankText = findViewById(R.id.ranking_UserRankText);
        userImage = findViewById(R.id.ranking_UserImage);
        userNicknameText = findViewById(R.id.ranking_UserNicknameText);
        heartNumText = findViewById(R.id.ranking_HeartNumText);

        userRankLayout = findViewById(R.id.ranking_UserRankLayout);
        noRankLayout = findViewById(R.id.ranking_NoRankTextLayout);
        noDataLayout = findViewById(R.id.rankingNoData_Layout);
        dateText = findViewById(R.id.ranking_DateText);

        arrayList = new ArrayList<>();
        adapter = new Ranking_Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Ranking.this));
        adapter.setRankItem(arrayList);

        Intent i = getIntent();
        currentNickname = i.getStringExtra("nickname");

        getTime = new GetTime();
        loading = AnimationUtils.loadAnimation(Ranking.this, R.anim.loading);
        loadingLayout = findViewById(R.id.rankingLoading_Layout);

    } // setVariable()

    public void setView() {

        dateText.setText(getTime.getFormatTime4(getTime.getTime()));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rankDayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dayOnClick();

            }
        });

        rankMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                monthOnClick();

            }
        });

        rankYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                yearOnClick();

            }
        });

        loadingImage.startAnimation(loading);


    } // setView()

    public void dayOnClick() {

        dateText.setText(getTime.getFormatTime4(getTime.getTime()));

        rankDayBtn.setVisibility(View.GONE);
        rankDayBlock.setVisibility(View.VISIBLE);
        rankMonthBtn.setVisibility(View.VISIBLE);
        rankMonthBlock.setVisibility(View.GONE);
        rankYearBtn.setVisibility(View.VISIBLE);
        rankYearBlock.setVisibility(View.GONE);

    }

    public void monthOnClick() {

        dateText.setText(getTime.getFormatTime7(getTime.getTime()));

        rankDayBtn.setVisibility(View.VISIBLE);
        rankDayBlock.setVisibility(View.GONE);
        rankMonthBtn.setVisibility(View.GONE);
        rankMonthBlock.setVisibility(View.VISIBLE);
        rankYearBtn.setVisibility(View.VISIBLE);
        rankYearBlock.setVisibility(View.GONE);

    }

    public void yearOnClick() {

        String[] array = getTime.getFormatTime7(getTime.getTime()).split(" ");
        dateText.setText(array[0]);

        rankDayBtn.setVisibility(View.VISIBLE);
        rankDayBlock.setVisibility(View.GONE);
        rankMonthBtn.setVisibility(View.VISIBLE);
        rankMonthBlock.setVisibility(View.GONE);
        rankYearBtn.setVisibility(View.GONE);
        rankYearBlock.setVisibility(View.VISIBLE);

    }

    Comparator<PostData> sortByHeartNum = new Comparator<PostData>() {
        @Override
        public int compare(PostData postData, PostData t1) {
            return Integer.compare(postData.heartNum, t1.heartNum);
        }
    };

    public void getRanking() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getRanking();
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "getRanking - onResponse isSuccessful");

                    data = response.body();
                    int rank = 0;
                    int heartNum = 1;
                    int j = 0;
                    String profileImage = "";
                    String postNickname = "";
                    String dateLiked = "";
                    boolean userStatus = false;
                    boolean userDuplicate;

                    if (data.size() > 0) {
                        Log.d(TAG, "getRanking - data.size() " + data.size());

                        for (int i = 0; i < data.size(); i++) {

                            profileImage = data.get(i).getProfileImage();
                            postNickname = data.get(i).getPostNickname();
                            dateLiked = data.get(i).getDateLiked();
                            rank = 0;
                            userDuplicate = false;

                            Log.d(TAG, "getRanking - postNickname : " + postNickname + " / dateLiked : " + dateLiked);

                            if (getTime.isSameDay(Long.parseLong(dateLiked))) {

                            if (arrayList.size() > 0) {
                                Log.d(TAG, "getRanking - arrayList.size() : " + arrayList.size());

                                for (j = 0; j < arrayList.size(); j++) {

                                    if (postNickname.equals(arrayList.get(j).getPostNickname())) {

                                        userDuplicate = true;
                                        break;

                                    }

                                }

                                Log.d(TAG, "getRanking - j : " + j);

                            }

                            if (userDuplicate) {
                                Log.d(TAG, "getRanking - 중복 데이터 확인되어 수정 완료");

                                arrayList.set(j, new PostData(rank, profileImage, postNickname, arrayList.get(j).getHeartNum() + 1));

                            } else {
                                Log.d(TAG, "getRanking - 중복 데이터 없어서 추가 완료");
                                arrayList.add(new PostData(rank, profileImage, postNickname, heartNum));
                            }

                        }

                        }

                        Collections.sort(arrayList, sortByHeartNum);
                        Collections.reverse(arrayList);

                        for (int i = 0; i < arrayList.size(); i++) {
                            arrayList.set(i, new PostData(i+1,arrayList.get(i).getProfileImage(), arrayList.get(i).getPostNickname(), arrayList.get(i).getHeartNum()));
                        }

                        for (int p = 0; p < arrayList.size(); p++) {

                            if (arrayList.get(p).getPostNickname().equals(currentNickname)) {
                                Log.d(TAG, "getRanking - 해당 사용자의 데이터 확인되어 출력");

                                Glide.with(Ranking.this)
                                        .load(ApiClient.serverProfileImagePath + arrayList.get(p).getProfileImage())
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .into(userImage);

                                userRankText.setText(String.valueOf(arrayList.get(p).getRank()));
                                userNicknameText.setText(arrayList.get(p).getPostNickname());
                                heartNumText.setText(String.valueOf(arrayList.get(p).getHeartNum()));
                                userStatus = true;

                            }

                        }

                        if (userStatus) {
                            userRankLayout.setVisibility(View.VISIBLE);
                        } else {
                            noRankLayout.setVisibility(View.VISIBLE);
                        }

                        loadingImage.clearAnimation();
                        loadingLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        adapter.notifyDataSetChanged();

                    } else {
                        loadingImage.clearAnimation();
                        loadingLayout.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);
                    }

                } else {
                    Log.d(TAG, "getRanking - onResponse isFailure");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<PostData>> call, Throwable t) {
                Log.d(TAG, "getRanking - onFailure");
            }
        });

    } // getRanking()


}