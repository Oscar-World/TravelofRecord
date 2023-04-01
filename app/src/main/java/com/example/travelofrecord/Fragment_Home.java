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

    Context context = getActivity();
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
        Log.d(TAG, "onAttach()");
    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        setView();

        return v;
    }


    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
    }
    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
    }
    @Override public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();

        itemSize = itemPost_ArrayList.size();
        Log.d(TAG, "아이템 사이즈 1 : " + itemSize);

        if (getData != null) {

            Item_Post item_post = new Item_Post(nickname,profileImage,heart,location,postImage,writing,dateCreated);

            Log.d(TAG, "아이템 사이즈 2 : " + itemSize);
            Log.d(TAG, "아이템 : " + item_post);

            itemPost_ArrayList.add(itemSize,item_post);

            adapter.notifyDataSetChanged();

            Log.d(TAG, "아이템 사이즈 2 : " + itemSize);
            Log.d(TAG, "아이템 : " + item_post);

        }

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


    public void getPost(String nickName) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Post> call = apiInterface.getPost(nickName);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {

                    nickname = response.body().getNickname();
                    profileImage = response.body().getProfileImage();
                    heart = response.body().getHeart();
                    location = response.body().getLocation();
                    postImage = response.body().getPostImage();
                    writing = response.body().getWriting();
                    dateCreated = response.body().getDateCreated();




                } else {
                    Log.d(TAG, "onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d(TAG, "onFailure: 실패");
            }
        });

    }


    public void setView() {

        photo_Btn = v.findViewById(R.id.homePhoto_Btn);
        map_Btn = v.findViewById(R.id.homeMap_Btn);
        photo_Block = v.findViewById(R.id.homePhoto_Block);
        map_Block = v.findViewById(R.id.homeMap_Block);

        itemPost_ArrayList = new ArrayList<>();
        adapter = new Home_Adapter();

        recyclerView = v.findViewById(R.id.home_RecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.setItemPost(itemPost_ArrayList);

        getData = getArguments();

        Log.d(TAG, "받은 번들 데이터 : " + getData);

        if (getData != null) {

            nickname = getData.getString("nickname");
            profileImage = getData.getString("profileImage");
            heart = getData.getInt("heart");
            location = getData.getString("location");
            postImage = getData.getString("postImage");
            writing = getData.getString("writing");
            dateCreated = getData.getString("dateCreated");

            Log.d(TAG, "받아서 변환시킨 번들 데이터 : " + nickname + " " + profileImage + " " + heart + " " + location + " " + postImage + " " + writing + " " + dateCreated);

        }


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