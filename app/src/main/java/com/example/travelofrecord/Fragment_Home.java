package com.example.travelofrecord;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {

    String TAG = "홈 프래그먼트";

    View v;

    SwipeRefreshLayout swipeRefreshLayout;

    private Button photo_Btn;
    private Button map_Btn;
    private Button photo_Block;
    private Button map_Block;

    RecyclerView recyclerView;

    ArrayList<Post> post_ArrayList;
    int itemSize;
    Home_Adapter adapter;

    Bundle getData;

    String nickname;
    String profileImage;
    int heart;
    String location;
    String postImage;
    String writing;
    String dateCreated;
    int num;


    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() 호출됨");
    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() 호출됨");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() 호출됨");

        v = inflater.inflate(R.layout.fragment_home, container, false);

        setView();

        return v;
    }


    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated() 호출됨");
    }
    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated() 호출됨");

        getPost();

    }
    @Override public void onStart() {
        Log.d(TAG, "onStart() 호출됨");
        super.onStart();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                post_ArrayList = new ArrayList<>();
                adapter.setItemPost(post_ArrayList);
                getPost();

                swipeRefreshLayout.setRefreshing(false);

            }
        });

        photo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photo_Btn.setVisibility(View.GONE);
                photo_Block.setVisibility(View.VISIBLE);
                map_Btn.setVisibility(View.VISIBLE);
                map_Block.setVisibility(View.GONE);

            }
        });

        map_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                map_Btn.setVisibility(View.GONE);
                map_Block.setVisibility(View.VISIBLE);
                photo_Btn.setVisibility(View.VISIBLE);
                photo_Block.setVisibility(View.GONE);

            }
        });

    } // onStart()



    public void getPost() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Post>> call = apiInterface.getPost();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {
                if (response.isSuccessful()) {

                    ArrayList<Post> data = response.body();

                    if (data.size() > 0) {
                        Log.d(TAG, "onResponse: " + data);
                        Log.d(TAG, "data : " + data.get(0).getNickname());

                        Log.d(TAG, "data.size : " + data.size());

                        for (int i = 0; i < data.size(); i++) {
                            num = data.get(i).getNum();
                            nickname = data.get(i).getNickname();
                            profileImage = data.get(i).getProfileImage();
                            heart = data.get(i).getHeart();
                            location = data.get(i).getLocation();
                            postImage = data.get(i).getPostImage();
                            writing = data.get(i).getWriting();
                            dateCreated = data.get(i).getDateCreated();

                            String datePost = lastTime(dateCreated);
                            String addressPost = getAddress(location);

                            Log.d(TAG, "onResponse: num = " + num + " nickname = " + nickname + " profileImage = " + profileImage + "\n writing : " + writing);


                            Post post = new Post(num, nickname, profileImage, heart, addressPost, postImage, writing, datePost);

                            post_ArrayList.add(0,post);


                        }

                        itemSize = post_ArrayList.size();
                        Log.d(TAG, "itemSize : " + itemSize);

                        adapter.notifyDataSetChanged();

                    }


                } else {
                    Log.d(TAG, "getPost : 실패");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.d(TAG, "onFailure: 실패 " + t);
            }
        });

    }

    public String lastTime(String dateCreated) {

        String msg = null;

        long datePosted = Long.parseLong(dateCreated);
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

    public String getAddress(String location) {

        String address = null;

        String[] addressArray = location.split(" ");

        address = addressArray[1] + " " + addressArray[2] + " " + addressArray[3] + " " + addressArray[4];

        return address;

    }


    public void setView() {

        swipeRefreshLayout = v.findViewById(R.id.home_SwipeRefreshLayout);

        photo_Btn = v.findViewById(R.id.homePhoto_Btn);
        map_Btn = v.findViewById(R.id.homeMap_Btn);
        photo_Block = v.findViewById(R.id.homePhoto_Block);
        map_Block = v.findViewById(R.id.homeMap_Block);

        recyclerView = v.findViewById(R.id.home_RecyclerView);
        adapter = new Home_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        post_ArrayList = new ArrayList<>();

        adapter.setItemPost(post_ArrayList);

        getData = getArguments();

        Log.d(TAG, "getData : " + getData);

    }


    @Override public void onResume() {
        Log.d(TAG, "onResume() 호출됨");
        super.onResume();
    }
    @Override public void onPause() {
        Log.d(TAG, "onPause() 호출됨");
        super.onPause();
    }
    @Override public void onStop() {
        Log.d(TAG, "onStop() 호출됨");
        super.onStop();

        getData = null;

    }
    @Override public void onDestroyView() {
        Log.d(TAG, "onDestroyView() 호출됨");
        super.onDestroyView();
    }
    @Override public void onDetach() {
        Log.d(TAG, "onDetach() 호출됨");
        super.onDetach();
    }

}