package com.example.travelofrecord.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.travelofrecord.R;

public class Fragment_Dm extends Fragment {

    String TAG = "Dm 프래그먼트";
    View v;

    FrameLayout noChatRoomFrameLayout;
    RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() 호출");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() 호출");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() 호출");
        v = inflater.inflate(R.layout.fragment_dm, container, false);
        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated() 호출");
    }
    @Override
    public void onStart() {
        Log.d(TAG, "onStart() 호출");
        super.onStart();
    }
    @Override
    public void onResume() {
        Log.d(TAG, "onResume() 호출");
        super.onResume();
    }
    @Override
    public void onPause() {
        Log.d(TAG, "onPause() 호출");
        super.onPause();
    }
    @Override
    public void onStop() {
        Log.d(TAG, "onStop() 호출");
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView() 호출");
        super.onDestroyView();
    }
    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach() 호출");
        super.onDetach();
    }

    public void setVariable() {

        noChatRoomFrameLayout = v.findViewById(R.id.noChatRoom_FrameLayout);
        recyclerView = v.findViewById(R.id.chatRoom_RecyclerView);

    }

    public void setView() {

        int a = 1;
        if (a == 1) {
            noChatRoomFrameLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noChatRoomFrameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }


}