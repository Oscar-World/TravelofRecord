package com.example.travelofrecord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Post extends AppCompatActivity {

    String TAG = "게시글 액티비티";

    ImageButton back_Btn;
    TextView post_Nickname_Text;
    ImageView post_ProfileImage_Iv;
    TextView post_Location_Text;
    ImageView post_PostImage_Iv;
    TextView post_DateCreated_Text;
    TextView post_Writing_Text;
    ImageView post_Heart_Iv;
    ImageView post_HeartFull_Iv;
    TextView post_HeartNum_Text;
    ImageView post_Comment_Iv;
    TextView post_CommentNum_Text;
    EditText post_Comment_Edit;
    ImageButton post_CommentAdd_Btn;

    int post_Num;
    String post_Nickname;
    String post_ProfileImage;
    int post_Heart;
    int post_CommentNum;
    String post_Location;
    String post_PostImage;
    String post_Writing;
    String post_DateCreated;
    String post_WhoLike;
    boolean post_HeartStatus;

    String addComment;
    String accessNickname;
    String accessProfileImage;
    String dateComment;
    SharedPreferences sharedPreferences;

    Home home;

    ScrollView scrollView;
    RecyclerView recyclerView;
    Comment_Adapter adapter;
    ArrayList<PostData> postDataArrayList;
    int listSize;

    ApiInterface apiInterface;
    LinearLayout post_TopLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Log.d(TAG, "onStart() 호출됨");

        setVariable();
        setView();

        getComment(post_Num);

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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        back_Btn = findViewById(R.id.postBack_Btn);
        post_Nickname_Text = findViewById(R.id.post_nickname);
        post_ProfileImage_Iv = findViewById(R.id.post_profileImage);
        post_PostImage_Iv = findViewById(R.id.post_postImage);
        post_Writing_Text = findViewById(R.id.post_writing);
        post_HeartNum_Text = findViewById(R.id.post_heartNumber);
        post_CommentNum_Text = findViewById(R.id.post_commentNumber);
        post_DateCreated_Text = findViewById(R.id.post_dateCreated);
        post_Heart_Iv = findViewById(R.id.post_heart);
        post_HeartFull_Iv = findViewById(R.id.post_heartFull);
        post_Comment_Iv = findViewById(R.id.post_comment);
        post_Location_Text = findViewById(R.id.post_location);
        post_Comment_Edit = findViewById(R.id.post_commentEdit);
        post_CommentAdd_Btn = findViewById(R.id.post_commentAdd_Btn);

        Intent i = getIntent();

        Log.d(TAG, "getIntent : " + i);

        post_Num = i.getIntExtra("num", 0);
        post_Nickname = i.getStringExtra("nickname");
        post_ProfileImage = i.getStringExtra("profileImage");
        post_Heart = i.getIntExtra("heart", 0);
        post_CommentNum = i.getIntExtra("commentNum", 0);
        post_Location = i.getStringExtra("location");
        post_PostImage = i.getStringExtra("postImage");
        post_Writing = i.getStringExtra("writing");
        post_DateCreated = i.getStringExtra("dateCreated");
        post_WhoLike = i.getStringExtra("whoLike");
        post_HeartStatus = i.getBooleanExtra("heartStatus", false);
        Log.d(TAG, "heartStatus : " + post_HeartStatus);

        home = new Home();

        sharedPreferences = getSharedPreferences("로그인 정보", Context.MODE_PRIVATE);
        accessNickname = sharedPreferences.getString("nickname","");
        accessProfileImage = sharedPreferences.getString("image", "");

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        scrollView = findViewById(R.id.post_ScrollView);
        recyclerView = findViewById(R.id.comment_recyclerView);
        adapter = new Comment_Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postDataArrayList = new ArrayList<>();
        adapter.setItemComment(postDataArrayList);

        post_TopLayout = findViewById(R.id.post_TopLayout);

    } // setVariable()

    public void setView() {

        post_Nickname_Text.setText(post_Nickname);
        post_Location_Text.setText(post_Location);
        post_HeartNum_Text.setText(String.valueOf(post_Heart));
        post_CommentNum_Text.setText(String.valueOf(post_CommentNum));
        post_DateCreated_Text.setText(post_DateCreated);
        post_Writing_Text.setText(post_Writing);

        if (post_HeartStatus) {
            post_Heart_Iv.setVisibility(View.GONE);
            post_HeartFull_Iv.setVisibility(View.VISIBLE);
        }

        Glide.with(this)
                .load(post_ProfileImage)
                .into(post_ProfileImage_Iv);

        Glide.with(this)
                .load(post_PostImage)
                .into(post_PostImage_Iv);

        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        post_CommentAdd_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addComment = post_Comment_Edit.getText().toString();
                dateComment = getTime().toString();

                Log.d(TAG, "postNum : " + post_Num + "\naccessProfileImage : " + accessProfileImage + "\naccessNickname : " + accessNickname +
                        "\ndateComment : " + dateComment + "\naddComment : " + addComment + "\ncommentNum : " + post_CommentNum);

                post_CommentNum += 1;
                post_CommentNum_Text.setText(String.valueOf(post_CommentNum));

                addComment(post_Num, accessProfileImage, accessNickname, dateComment, addComment, post_CommentNum);

                post_Comment_Edit.setText("");

            }
        });

        post_TopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scrollView.fullScroll(ScrollView.FOCUS_UP);

            }
        });


    } // setView()



    // 시스템 시간 불러오기
    public Long getTime() {

        long currentTime = System.currentTimeMillis();

        return currentTime;

    } // getTime()

    // DB에 저장되어있는 시스템 시간 불러와서 지난 시간 계산
    public String lastTime(String dateComment) {

        String msg = null;

        long datePosted = Long.parseLong(dateComment);
        long currentTime = System.currentTimeMillis();
        long lastTime = (currentTime - datePosted) / 1000;

        if (lastTime < 60) {
            msg = "방금 전";
        } else if ((lastTime /= 60) < 60) {
            msg = lastTime + "분 전";
        } else if ((lastTime /= 60) < 24) {
            msg = lastTime + "시간 전";
        } else if ((lastTime /= 24) < 7) {
            msg = lastTime + "일 전";
        } else if (lastTime < 14) {
            msg = "1주 전";
        } else if (lastTime < 21) {
            msg = "2주 전";
        } else if (lastTime < 28) {
            msg = "3주 전";
        } else if ((lastTime / 30) < 12) {
            msg = lastTime + "달 전";
        } else {
            msg = (lastTime / 365) + "년 전";
        }

        return msg;

    } // lastTime()


    public void addComment(int postNum, String profileImage, String whoComment, String dateComment, String comment, int commentNum) {

        Call<String> call = apiInterface.insertComment(postNum, profileImage, whoComment, dateComment, comment, commentNum);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "insertComment onResponse");
                if (response.isSuccessful()) {
                    Log.d(TAG, "response : " + response.body().toString());

                    postDataArrayList.clear();
//                    adapter.setItemComment(postDataArrayList);
                    getComment(post_Num);

                } else {
                    Log.d(TAG, "responseFail");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "insertComent onFailure : " + t);
            }
        });

    } // addComment()



    public void getComment(int num) {

        Call<ArrayList<PostData>> call = apiInterface.getComment(num);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {
                Log.d(TAG, "getComment onResponse");
                if (response.isSuccessful()) {
                    Log.d(TAG, "response : " + response.body().toString());

                    ArrayList<PostData> data = response.body();
                    Log.d(TAG, "data.size() : " + data.size());

                    if (data.size() > 0) {

                        for (int i = 0; i < data.size(); i++) {

                            String profileImage = data.get(i).getCommentProfileImage();
                            String whoComment = data.get(i).getWhoComment();
                            String dateComment = data.get(i).getDateComment();
                            String comment = data.get(i).getComment();

                            Log.d(TAG, "dateComment : " + dateComment);
                            String commentTime = lastTime(dateComment);

                            Log.d(TAG, "profileImage : " + profileImage + "\nwhoComment : " + whoComment + "\ndateComment : " + commentTime + "\ncomment : " + comment);

                            PostData postData = new PostData(profileImage, whoComment, commentTime, comment);

                            postDataArrayList.add(postData);

                        }

                        listSize = postDataArrayList.size();
                        adapter.notifyDataSetChanged();

                    }


                } else {
                    Log.d(TAG, "responseFail");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<PostData>> call, Throwable t) {
                Log.d(TAG, "getComent onFailure : " + t);
            }
        });

    } // getComment()




}