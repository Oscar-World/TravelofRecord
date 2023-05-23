package com.example.travelofrecord.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.travelofrecord.Adapter.Chat_Adapter;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.R;

import java.util.ArrayList;

public class DirectMessage extends AppCompatActivity {

    String TAG = "채팅";

    ImageButton backBtn;
    ImageButton sendBtn;
    EditText chatEdit;
    RecyclerView chatRecyclerView;
    ArrayList<PostData> arrayList;
    Chat_Adapter adapter;
    SharedPreferences sharedPreferences;
    String currentNickname;
    String currentImage;

    GetTime getTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm);
        Log.d(TAG, "onCreate() 호출됨");

        setVariable();
        setView();
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


    // -------------------------------------------------------------------------------------------

    public void setVariable() {

        backBtn = findViewById(R.id.chatBack_Btn);
        sendBtn = findViewById(R.id.chatSend_Btn);
        chatEdit = findViewById(R.id.chatMessage_Edit);
        chatRecyclerView = findViewById(R.id.chat_RecyclerView);

        arrayList = new ArrayList<>();
        adapter = new Chat_Adapter();
        chatRecyclerView.setAdapter(adapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setItemChat(arrayList);

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        currentNickname = sharedPreferences.getString("nickname","");
        currentImage = sharedPreferences.getString("image", "");

        getTime = new GetTime();



    }

    public void setView() {

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = chatEdit.getText().toString();
                Log.d(TAG, "onClick: " + message);
                chatEdit.setText("");

            }
        });



        arrayList.add(new PostData(null, "testNickname", "hi Oscar ~.~", String.valueOf(getTime.getFormatTime(getTime.getTime())), 0));
        arrayList.add(new PostData(null, "testNickname", "hello Oscar ~.~", String.valueOf(getTime.getFormatTime(getTime.getTime())), 1));
        adapter.notifyDataSetChanged();

    }



}