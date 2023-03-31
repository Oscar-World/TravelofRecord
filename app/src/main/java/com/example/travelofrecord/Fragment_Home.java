package com.example.travelofrecord;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class Fragment_Home extends Fragment {

    String TAG = "홈 프래그먼트";

    View v;

    private Button photo_Btn;
    private Button map_Btn;
    private Button photo_Block;
    private Button map_Block;

    RecyclerView recyclerView;

    Context context = getActivity();
    ArrayList<Item_Post> itemPost;
    int itemSize;
    Home_Adapter adapter;

    Bundle getData;
    String image;


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

        itemSize = itemPost.size();
        Log.d(TAG, "아이템 사이즈 1 : " + itemSize);

        if (getData != null) {

            Item_Post item = new Item_Post(image);

            Log.d(TAG, "아이템 사이즈 2 : " + itemSize);
            Log.d(TAG, "아이템 : " + item);

            itemPost.add(itemSize,item);

            adapter.notifyDataSetChanged();

            Log.d(TAG, "아이템 사이즈 2 : " + itemSize);
            Log.d(TAG, "아이템 : " + item);

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



    }


    public void setView() {

        photo_Btn = v.findViewById(R.id.homePhoto_Btn);
        map_Btn = v.findViewById(R.id.homeMap_Btn);
        photo_Block = v.findViewById(R.id.homePhoto_Block);
        map_Block = v.findViewById(R.id.homeMap_Block);

        itemPost = new ArrayList<>();
        adapter = new Home_Adapter();

        recyclerView = v.findViewById(R.id.home_RecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        adapter.setItemPost(itemPost);

        getData = getArguments();

        Log.d(TAG, "받은 번들 데이터 : " + getData);

        if (getData != null) {
//            image = getData.getString("image");
            image = getData.getString("image");
            Log.d(TAG, "받아서 변환시킨 번들 데이터 : " + image);

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