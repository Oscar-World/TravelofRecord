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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Fragment_Heart extends Fragment {

    String TAG = "하트 프래그먼트";

    private Button photo_Btn;
    private Button map_Btn;
    private Button photo_Block;
    private Button map_Block;



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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_heart, container, false);

        photo_Btn = v.findViewById(R.id.heartPhoto_Btn);
        map_Btn = v.findViewById(R.id.heartMap_Btn);
        photo_Block = v.findViewById(R.id.heartPhoto_Block);
        map_Block = v.findViewById(R.id.heartMap_Block);

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
    }




    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();


        // 현재 날짜 시간 받아오기 테스트 =========================================================================

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //Date 객체 사용
        Date date = new Date();
        String time1 = simpleDateFormat.format(date);

        //Calendar 클래스의 getTime()함수 사용
        Calendar calendar = Calendar.getInstance();
        String time2 = simpleDateFormat.format(calendar.getTime());

        //System 클래스의 currentTimeMillis()함수 사용
        String time3 = simpleDateFormat.format(System.currentTimeMillis());

        Log.d(TAG, "현재 시간 :\n" + time1 + "\n" + time2 + "\n" + time3);

        Log.d(TAG, "System.currentTimeMillis : " + System.currentTimeMillis());
        Log.d(TAG, "파싱 전 date.getTime() : " + date.getTime());
        try {
            date = simpleDateFormat.parse("2023-04-09 15:57:50");
        } catch (ParseException e) {
            Log.d(TAG, "파싱 실패");
            e.printStackTrace();
        }
        Log.d(TAG, "파싱 후 date.getTime() : " + date.getTime());
        Log.d(TAG, "지난 시간 : " + (System.currentTimeMillis() - date.getTime()));

        long time = System.currentTimeMillis() - date.getTime();
        String min = String.valueOf(time/60000);
        String hour = String.valueOf(time/3600000);
        String day = String.valueOf(time/86400000);
        String week = String.valueOf(time/604800000);
        String month = String.valueOf(time/2592000000L);
        String year = String.valueOf(time/31536000000L);
        Log.d(TAG, "year : " + year + "\nmonth : " + month + "\nweek : " + week + "\nday : " + day
         + "\nhour : " + hour + "\nmin : " + min);

        // ==================================================================================================

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