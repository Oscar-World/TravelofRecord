package com.example.travelofrecord.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.travelofrecord.Activity.DirectMessage;
import com.example.travelofrecord.Adapter.ChatRoom_Adapter;
import com.example.travelofrecord.Data.Chat;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Dm extends Fragment {

    String TAG = "DirectMessage 프래그먼트";
    View v;

    FrameLayout noChatRoomFrameLayout;
    RecyclerView recyclerView;
    ImageButton addChatBtn;
    ChatRoom_Adapter adapter;
    ArrayList<Chat> arrayList;

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

        setVariable();
        setView();

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
        addChatBtn = v.findViewById(R.id.chatRoomAdd_Btn);
        adapter = new ChatRoom_Adapter();
        arrayList = new ArrayList<>();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setItemChatRoom(arrayList);

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

// 채팅 상대 검색하는 액티비티 추가 예정.
//        addChatBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(getActivity(), DirectMessage.class);
//                startActivity(i);
//            }
//        });


    }


    // --------------------------------------------------------------------------------------


    public void getRoom() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Chat>> call = apiInterface.getRoom();
        call.enqueue(new Callback<ArrayList<Chat>>() {
            @Override
            public void onResponse(Call<ArrayList<Chat>> call, Response<ArrayList<Chat>> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "getRoom - onResponse isSuccessful");



                } else {
                    Log.d(TAG, "getRoom - onResponse isFailure");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Chat>> call, Throwable t) {
                Log.d(TAG, "getRoom - onFailure");
            }
        });

    }


}