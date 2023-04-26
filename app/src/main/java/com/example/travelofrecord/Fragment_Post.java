package com.example.travelofrecord;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Post extends Fragment {

    String TAG = "게시물 프래그먼트";

    View v;

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
    TextView post_CommentNum_Text;
    EditText post_Comment_Edit;
    ImageButton post_CommentAdd_Btn;

    int backPosition;
    int post_Num;
    String post_Nickname;
    String post_ProfileImage;
    int post_Heart;
    String post_Location;
    String post_PostImage;
    String post_Writing;
    String post_DateCreated;

    String addComment;
    String accessNickname;
    String accessProfileImage;
    String dateComment;
    SharedPreferences sharedPreferences;

    Home home;

    RecyclerView recyclerView;
    Comment_Adapter adapter;
    ArrayList<PostData> postDataArrayList;
    int listSize;

    ApiInterface apiInterface;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() 호출됨");
        v = inflater.inflate(R.layout.fragment_post, container, false);

        setView();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");

        getComment(post_Num);

    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();

        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.fragmentChange(backPosition);

            }
        });

        post_Nickname_Text.setText(post_Nickname);
        post_Location_Text.setText(post_Location);
        post_HeartNum_Text.setText(String.valueOf(post_Heart));
        post_DateCreated_Text.setText(post_DateCreated);
        post_Writing_Text.setText(post_Writing);

        Glide.with(getActivity())
                .load(post_ProfileImage)
                .into(post_ProfileImage_Iv);

        Glide.with(getActivity())
                .load(post_PostImage)
                .into(post_PostImage_Iv);

        post_CommentAdd_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addComment = post_Comment_Edit.getText().toString();
                dateComment = getTime().toString();

                Log.d(TAG, "postNum : " + post_Num + "\naccessNickname : " + accessNickname + "\ndateComment : " + dateComment + "\naddComment : " + addComment);

                addComment(post_Num, accessProfileImage, accessNickname, dateComment, addComment);

            }
        });

    }

    // 시스템 시간 불러오기
    public Long getTime() {

        long currentTime = System.currentTimeMillis();

        return currentTime;

    }

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
    }

    public void addComment(int postNum, String profileImage, String whoComment, String dateComment, String comment) {

        Call<String> call = apiInterface.insertComment(postNum, profileImage, whoComment, dateComment, comment);
        call.enqueue(new Callback<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "insertComment onResponse");
                if (response.isSuccessful()) {
                    Log.d(TAG, "response : " + response.body().toString());

                    postDataArrayList.clear();
                    adapter.setItemComment(postDataArrayList);
                    adapter.notifyDataSetChanged();

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

    }

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

    }

    public void setView() {

        back_Btn = v.findViewById(R.id.postBack_Btn);
        post_Nickname_Text = v.findViewById(R.id.post_nickname);
        post_ProfileImage_Iv = v.findViewById(R.id.post_profileImage);
        post_PostImage_Iv = v.findViewById(R.id.post_postImage);
        post_Writing_Text = v.findViewById(R.id.post_writing);
        post_HeartNum_Text = v.findViewById(R.id.post_heartNumber);
        post_CommentNum_Text = v.findViewById(R.id.post_commentNumber);
        post_DateCreated_Text = v.findViewById(R.id.post_dateCreated);
        post_Heart_Iv = v.findViewById(R.id.post_heart);
        post_HeartFull_Iv = v.findViewById(R.id.post_heartFull);
        post_Location_Text = v.findViewById(R.id.post_location);
        post_Comment_Edit = v.findViewById(R.id.post_commentEdit);
        post_CommentAdd_Btn = v.findViewById(R.id.post_commentAdd_Btn);

        backPosition = getArguments().getInt("backPosition");
        post_Num = getArguments().getInt("num");
        post_Nickname = getArguments().getString("nickname");
        post_ProfileImage = getArguments().getString("profileImage");
        post_Heart = getArguments().getInt("heart");
        post_Location = getArguments().getString("location");
        post_PostImage = getArguments().getString("postImage");
        post_Writing = getArguments().getString("writing");
        post_DateCreated = getArguments().getString("dateCreated");

        Log.d(TAG, "getArguments : " + getArguments());

        home = (Home) getActivity();

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보",Context.MODE_PRIVATE);
        accessNickname = sharedPreferences.getString("nickname","");
        accessProfileImage = sharedPreferences.getString("image", "");

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        recyclerView = v.findViewById(R.id.comment_recyclerView);
        adapter = new Comment_Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postDataArrayList = new ArrayList<>();
        adapter.setItemComment(postDataArrayList);




    }

    @Override public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }
    @Override public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }
    @Override public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }
    @Override public void onDestroyView() {
        Log.d(TAG, "onDestroyView()");
        super.onDestroyView();
    }
    @Override public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
    }

}