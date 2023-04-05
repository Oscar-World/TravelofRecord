package com.example.travelofrecord;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {

    String TAG = "홈 프래그먼트";

    View v;

    private Button photo_Btn;
    private Button map_Btn;
    private Button photo_Block;
    private Button map_Block;

    RecyclerView recyclerView;

    ArrayList<Item_Post> itemPost_ArrayList;
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

        getPost();

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
    }
    @Override public void onStart() {
        Log.d(TAG, "onStart() 호출됨");
        super.onStart();


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
        Call<Post> call = apiInterface.getPost();
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {

                    Log.d(TAG, "getPost : " + response.body().getResponse());

                    Log.d(TAG, "getInfo : " + response.body().getNickname());

                    Post rp_code = response.body();
                    Log.d(TAG, "onResponse: " + rp_code);

                    nickname = response.body().getNickname();
                    profileImage = response.body().getProfileImage();
                    heart = response.body().getHeart();
                    location = response.body().getLocation();
                    postImage = response.body().getPostImage();
                    writing = response.body().getWriting();
                    dateCreated = response.body().getDateCreated();

                    itemSize = itemPost_ArrayList.size();
                    Log.d(TAG, "itemSize : " + itemSize);

                        Item_Post itemPost = new Item_Post(nickname, profileImage, heart, location, postImage, writing, dateCreated);
//                    Item_Post itemPost = new Item_Post();
                        itemPost_ArrayList.add(itemPost);
                        adapter.notifyDataSetChanged();



//                    // add에서 넘어왔을 때만.
//                    if (getData != null) {
//
//                        Item_Post itemPost = new Item_Post(nickname, profileImage, heart, location, postImage, writing, dateCreated);
//                        itemPost_ArrayList.add(itemPost);
//                        adapter.notifyDataSetChanged();
//
//                    }


                } else {
                    Log.d(TAG, "getPost : 실패");
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d(TAG, "onFailure: 실패 " + t);
            }
        });

    }


    public void setView() {

        photo_Btn = v.findViewById(R.id.homePhoto_Btn);
        map_Btn = v.findViewById(R.id.homeMap_Btn);
        photo_Block = v.findViewById(R.id.homePhoto_Block);
        map_Block = v.findViewById(R.id.homeMap_Block);

        recyclerView = v.findViewById(R.id.home_RecyclerView);
        adapter = new Home_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        itemPost_ArrayList = new ArrayList<>();

        adapter.setItemPost(itemPost_ArrayList);

//        itemSize = itemPost_ArrayList.size();

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