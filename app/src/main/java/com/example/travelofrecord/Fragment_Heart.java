package com.example.travelofrecord;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Heart extends Fragment {

    String TAG = "하트 프래그먼트";

    View v;

    private Button photo_Btn;
    private Button map_Btn;
    private Button photo_Block;
    private Button map_Block;

    RecyclerView recyclerView;
    ArrayList<Post> post_ArrayList;
    Heart_Adapter adapter;

    int itemSize;

    String location;
    String postImage;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_heart, container, false);

        setView();

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");

        getHeart();

    }




    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
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


    }


    public void getHeart() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Post>> call = apiInterface.getHeart();
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {

                if (response.isSuccessful()) {

                    ArrayList<Post> data = response.body();
                    Log.d(TAG, "response.body : " + data);

                    if (data.size() > 0) {

                        for (int i = 0; i < data.size(); i++) {

                            postImage = data.get(i).getPostImage();
                            location = data.get(i).getLocation();

                            String addressHeart = getAddress(location);

                            Post post = new Post(addressHeart,postImage);

                            post_ArrayList.add(0, post);

                        }

                        itemSize = post_ArrayList.size();
                        Log.d(TAG, "itemSize : " + itemSize);

                        adapter.notifyDataSetChanged();

                    }

                } else {
                    Log.d(TAG, "onResponse 실패");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.d(TAG, "onFailure 실패");
            }
        });

    } // getHeart()

    public String getAddress(String location) {

        String address = null;

        String[] addressArray = location.split(" ");

//        address = addressArray[1] + " " + addressArray[2] + " " + addressArray[3] + " " + addressArray[4];

        address = addressArray[2] + " " + addressArray[4];

        return address;

    }

    public void setView() {

        photo_Btn = v.findViewById(R.id.heartPhoto_Btn);
        map_Btn = v.findViewById(R.id.heartMap_Btn);
        photo_Block = v.findViewById(R.id.heartPhoto_Block);
        map_Block = v.findViewById(R.id.heartMap_Block);

        recyclerView = v.findViewById(R.id.heart_RecyclerView);
        adapter = new Heart_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        post_ArrayList = new ArrayList<>();

        adapter.setItemHeart(post_ArrayList);

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