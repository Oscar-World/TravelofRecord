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
import com.example.travelofrecord.Adapter.Ranking_Adapter;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ranking extends AppCompatActivity {

    String TAG = "랭킹 액티비티";
    String currentNickname;
    boolean userDuplicate = false;
    int currentLocation = 0;
    int clickNum = 0;
    ImageButton backBtn;
    Button rankDayBtn, rankDayBlock, rankMonthBtn, rankMonthBlock, rankYearBtn, rankYearBlock;
    RecyclerView recyclerView;
    ImageView loadingImage, userImage;
    TextView userRankText, userNicknameText, heartNumText, dateText, leftBtn, rightBtn;
    LinearLayout userRankLayout, noRankLayout;
    FrameLayout noDataLayout, loadingLayout;
    ArrayList<PostData> arrayList, data, monthList, yearList, dateList;
    Ranking_Adapter adapter;
    GetTime getTime;
    Animation loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        setVariable();
        setListener();
        getRanking();

    } // onCreate()


    /*
    변수 초기화
     */
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

        leftBtn = findViewById(R.id.ranking_LeftText);
        rightBtn = findViewById(R.id.ranking_RightText);

        userRankLayout = findViewById(R.id.ranking_UserRankLayout);
        noRankLayout = findViewById(R.id.ranking_NoRankTextLayout);
        noDataLayout = findViewById(R.id.rankingNoData_Layout);
        dateText = findViewById(R.id.ranking_DateText);

        arrayList = new ArrayList<>();
        monthList = new ArrayList<>();
        yearList = new ArrayList<>();
        dateList = new ArrayList<>();
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


    /*
    클릭 리스너 세팅
     */
    public void setListener() {

        dateText.setText(getTime.getFormatTime44(getTime.getTime()));
        loadingImage.startAnimation(loading);

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

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    leftOnClick();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    rightOnClick();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    } // setListener()


    /*
    일자 클릭
     */
    public void dayOnClick() {

        currentLocation = 0;
        clickNum = 0;
        dateText.setText(getTime.getFormatTime44(getTime.getTime()));
        leftBtn.setVisibility(View.VISIBLE);

        rankDayBtn.setVisibility(View.GONE);
        rankDayBlock.setVisibility(View.VISIBLE);
        rankMonthBtn.setVisibility(View.VISIBLE);
        rankMonthBlock.setVisibility(View.GONE);
        rankYearBtn.setVisibility(View.VISIBLE);
        rankYearBlock.setVisibility(View.GONE);

        adapter.setRankItem(arrayList);
        listDataCheck(arrayList);

        if (userStatus(arrayList)) {
            noRankLayout.setVisibility(View.GONE);
            userRankLayout.setVisibility(View.VISIBLE);
        } else {
            userRankLayout.setVisibility(View.GONE);
            noRankLayout.setVisibility(View.VISIBLE);
        }

        checkRightBtn();

    } // dayOnClick()


    /*
    월 클릭
     */
    public void monthOnClick() {

        currentLocation = 1;
        clickNum = 0;
        dateText.setText(getTime.getFormatTime7(getTime.getTime()));
        leftBtn.setVisibility(View.VISIBLE);

        rankDayBtn.setVisibility(View.VISIBLE);
        rankDayBlock.setVisibility(View.GONE);
        rankMonthBtn.setVisibility(View.GONE);
        rankMonthBlock.setVisibility(View.VISIBLE);
        rankYearBtn.setVisibility(View.VISIBLE);
        rankYearBlock.setVisibility(View.GONE);

        adapter.setRankItem(monthList);
        listDataCheck(monthList);

        if (userStatus(monthList)) {
            noRankLayout.setVisibility(View.GONE);
            userRankLayout.setVisibility(View.VISIBLE);
        } else {
            userRankLayout.setVisibility(View.GONE);
            noRankLayout.setVisibility(View.VISIBLE);
        }

        checkRightBtn();

    } // monthOnClick()


    /*
    년 클릭
     */
    public void yearOnClick() {

        currentLocation = 2;
        clickNum = 0;
        String[] array = getTime.getFormatTime7(getTime.getTime()).split(" ");
        dateText.setText(array[0]);
        leftBtn.setVisibility(View.VISIBLE);

        rankDayBtn.setVisibility(View.VISIBLE);
        rankDayBlock.setVisibility(View.GONE);
        rankMonthBtn.setVisibility(View.VISIBLE);
        rankMonthBlock.setVisibility(View.GONE);
        rankYearBtn.setVisibility(View.GONE);
        rankYearBlock.setVisibility(View.VISIBLE);

        adapter.setRankItem(yearList);
        listDataCheck(yearList);

        if (userStatus(yearList)) {
            noRankLayout.setVisibility(View.GONE);
            userRankLayout.setVisibility(View.VISIBLE);
        } else {
            userRankLayout.setVisibility(View.GONE);
            noRankLayout.setVisibility(View.VISIBLE);
        }

        checkRightBtn();

    } // yearOnClick()


    /*
    왼쪽 화살표 클릭
     */
    public void leftOnClick() throws ParseException {

        dateList = new ArrayList<>();
        rightBtn.setVisibility(View.VISIBLE);
        String date = dateText.getText().toString();

        if (currentLocation == 0) {

            clickNum ++;
            date = getTime.decreaseDay(date);
            dateText.setText(date);

        } else if (currentLocation == 1) {

            clickNum ++;
            date = getTime.decreaseMonth(date);
            dateText.setText(date);

        } else {

            clickNum ++;
            date = getTime.decreaseYear(date);
            dateText.setText(date);

        }

        if (data.size() > 0) {

            int rank = 0;
            int heartNum = 1;
            String profileImage = "";
            String postNickname = "";
            String dateLiked = "";

            for (int i = 0; i < data.size(); i++) {

                profileImage = data.get(i).getProfileImage();
                postNickname = data.get(i).getPostNickname();
                dateLiked = data.get(i).getDateLiked();
                rank = 0;

                if (currentLocation == 0) {

                    if (date.equals(getTime.getFormatTime44(Long.parseLong(dateLiked)))) {

                        setArrayList(dateList, rank, profileImage, postNickname, heartNum);

                    }

                } else if (currentLocation == 1) {

                    if (date.equals(getTime.getFormatTime7(Long.parseLong(dateLiked)))) {

                        setArrayList(dateList, rank, profileImage, postNickname, heartNum);

                    }

                } else {

                    String[] array = getTime.getFormatTime7(getTime.getTime()).split(" ");

                    if (date.equals(array[0])) {

                        setArrayList(dateList, rank, profileImage, postNickname, heartNum);

                    }

                }

            }

            sortList(dateList);

            if (userStatus(arrayList)) {
                noRankLayout.setVisibility(View.GONE);
                userRankLayout.setVisibility(View.VISIBLE);
            } else {
                userRankLayout.setVisibility(View.GONE);
                noRankLayout.setVisibility(View.VISIBLE);
            }

            adapter.setRankItem(dateList);
            listDataCheck(dateList);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();

        } else {

            recyclerView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);

        }

        if (userStatus(dateList)) {
            noRankLayout.setVisibility(View.GONE);
            userRankLayout.setVisibility(View.VISIBLE);
        } else {
            userRankLayout.setVisibility(View.GONE);
            noRankLayout.setVisibility(View.VISIBLE);
        }

        if (clickNum > 6) {
            leftBtn.setVisibility(View.INVISIBLE);
        }

    } // leftOnClick()


    /*
    오른쪽 화살표 클릭
     */
    public void rightOnClick() throws ParseException {

        dateList = new ArrayList<>();
        String date = dateText.getText().toString();

        if (currentLocation == 0) {

            clickNum --;
            date = getTime.increaseDay(date);
            dateText.setText(date);

            if (date.equals(getTime.getFormatTime44(getTime.getTime()))) {
                rightBtn.setVisibility(View.INVISIBLE);
            }

        } else if (currentLocation == 1) {

            clickNum --;
            date = getTime.increaseMonth(date);
            dateText.setText(date);

            if (date.equals(getTime.getFormatTime7(getTime.getTime()))) {
                rightBtn.setVisibility(View.INVISIBLE);
            }

        } else {

            clickNum --;
            date = getTime.increaseYear(date);
            dateText.setText(date);

            String[] array = getTime.getFormatTime7(getTime.getTime()).split(" ");
            if (date.equals(array[0])) {
                rightBtn.setVisibility(View.INVISIBLE);
            }

        }


        if (data.size() > 0) {

            int rank = 0;
            int heartNum = 1;
            String profileImage = "";
            String postNickname = "";
            String dateLiked = "";

            for (int i = 0; i < data.size(); i++) {

                profileImage = data.get(i).getProfileImage();
                postNickname = data.get(i).getPostNickname();
                dateLiked = data.get(i).getDateLiked();
                rank = 0;

                if (currentLocation == 0) {

                    if (date.equals(getTime.getFormatTime44(Long.parseLong(dateLiked)))) {

                        setArrayList(dateList, rank, profileImage, postNickname, heartNum);

                    }

                } else if (currentLocation == 1) {

                    if (date.equals(getTime.getFormatTime7(Long.parseLong(dateLiked)))) {

                        setArrayList(dateList, rank, profileImage, postNickname, heartNum);

                    }

                } else {

                    String[] array = getTime.getFormatTime7(getTime.getTime()).split(" ");

                    if (date.equals(array[0])) {

                        setArrayList(dateList, rank, profileImage, postNickname, heartNum);

                    }

                }

            }

            sortList(dateList);

            if (userStatus(arrayList)) {
                noRankLayout.setVisibility(View.GONE);
                userRankLayout.setVisibility(View.VISIBLE);
            } else {
                userRankLayout.setVisibility(View.GONE);
                noRankLayout.setVisibility(View.VISIBLE);
            }

            adapter.setRankItem(dateList);
            listDataCheck(dateList);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();

        } else {

            recyclerView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);

        }

        if (userStatus(dateList)) {
            noRankLayout.setVisibility(View.GONE);
            userRankLayout.setVisibility(View.VISIBLE);
        } else {
            userRankLayout.setVisibility(View.GONE);
            noRankLayout.setVisibility(View.VISIBLE);
        }


        if (clickNum < 7) {
            leftBtn.setVisibility(View.VISIBLE);
        }

    } // rightOnClick()


    /*
    클래스 정렬 함수
     */
    Comparator<PostData> sortByHeartNum = new Comparator<PostData>() {
        @Override
        public int compare(PostData postData, PostData t1) {
            return Integer.compare(postData.heartNum, t1.heartNum);
        }
    };


    /*
    랭킹 데이터 불러오기
     */
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
                    String profileImage = "";
                    String postNickname = "";
                    String dateLiked = "";

                    if (data.size() > 0) {
                        Log.d(TAG, "getRanking - data.size() " + data.size());

                        loadingImage.clearAnimation();
                        loadingLayout.setVisibility(View.GONE);

                        for (int i = 0; i < data.size(); i++) {

                            profileImage = data.get(i).getProfileImage();
                            postNickname = data.get(i).getPostNickname();
                            dateLiked = data.get(i).getDateLiked();
                            rank = 0;

                            if (getTime.isSameDay(Long.parseLong(dateLiked))) {

                                setArrayList(arrayList, rank, profileImage, postNickname, heartNum);

                            }

                            if (getTime.isSameMonth(Long.parseLong(dateLiked))) {

                                setArrayList(monthList, rank, profileImage, postNickname, heartNum);

                            }

                            if (getTime.isSameYear(Long.parseLong(dateLiked))) {

                                setArrayList(yearList, rank, profileImage, postNickname, heartNum);

                            }

                        }

                        sortList(arrayList);
                        sortList(monthList);
                        sortList(yearList);

                        if (userStatus(arrayList)) {
                            noRankLayout.setVisibility(View.GONE);
                            userRankLayout.setVisibility(View.VISIBLE);
                        } else {
                            userRankLayout.setVisibility(View.GONE);
                            noRankLayout.setVisibility(View.VISIBLE);
                        }

                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();

                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);
                    }

                    listDataCheck(arrayList);

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


    /*
    리스트 정렬
     */
    public void sortList(ArrayList<PostData> list) {

        Collections.sort(list, sortByHeartNum);
        Collections.reverse(list);
        for (int i = 0; i < list.size(); i++) {
            list.set(i, new PostData(i + 1, list.get(i).getProfileImage(), list.get(i).getPostNickname(), list.get(i).getHeartNum()));
        }

    } // sortList()


    /*
    사용자 랭킹 존재 여부
     */
    public boolean userStatus(ArrayList<PostData> list) {

        for (int p = 0; p < list.size(); p++) {

            if (list.get(p).getPostNickname().equals(currentNickname)) {
                Log.d(TAG, "getRanking - 해당 사용자의 데이터 확인되어 출력");

                Glide.with(Ranking.this)
                        .load(ApiClient.serverProfileImagePath + list.get(p).getProfileImage())
//                        .skipMemoryCache(true)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(userImage);

                userRankText.setText(String.valueOf(list.get(p).getRank()));
                userNicknameText.setText(list.get(p).getPostNickname());
                heartNumText.setText(String.valueOf(list.get(p).getHeartNum()));

                return true;
            }

        }
        return false;

    } // userStatus()


    /*
    랭킹 리스트 세팅
     */
    public void setArrayList(ArrayList<PostData> list, int rank, String profileImage, String postNickname, int heartNum) {

        userDuplicate = false;
        int j = 0;

        if (list.size() > 0) {
            for (j = 0; j < list.size(); j++) {
                if (postNickname.equals(list.get(j).getPostNickname())) {
                    userDuplicate = true;
                    break;
                }
            }
        }

        if (userDuplicate) {
            Log.d(TAG, "getRanking - 중복 데이터 확인되어 수정 완료");
            list.set(j, new PostData(rank, profileImage, postNickname, list.get(j).getHeartNum() + 1));
        } else {
            Log.d(TAG, "getRanking - 중복 데이터 없어서 추가 완료");
            list.add(new PostData(rank, profileImage, postNickname, heartNum));
        }

    } // setArrayList()


    /*
    랭킹 데이터 존재 여부
     */
    public void listDataCheck(ArrayList<PostData> list) {

        if (list.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        } else {
            noDataLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    } // listDataCheck()


    /*
    랭킹 확인 버튼 클릭
     */
    public void checkRightBtn() {

        String date = dateText.getText().toString();

        if (currentLocation == 0) {

            if (date.equals(getTime.getFormatTime4(getTime.getTime()))) {
                rightBtn.setVisibility(View.INVISIBLE);
            }

        } else if (currentLocation == 1) {

            if (date.equals(getTime.getFormatTime7(getTime.getTime()))) {
                rightBtn.setVisibility(View.INVISIBLE);
            }

        } else {

            String[] array = getTime.getFormatTime7(getTime.getTime()).split(" ");
            if (date.equals(array[0])) {
                rightBtn.setVisibility(View.INVISIBLE);
            }

        }

    } // checkRightBtn()


}