package com.example.travelofrecord.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.travelofrecord.Adapter.Chat_Adapter;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
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

    Handler handler;
    Socket socket;
    PrintWriter printWriter;
    String ip = "3.34.246.77";
    int port = 8888;

    String sendMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm);
        Log.d(TAG, "onCreate() 호출됨");

        setVariable();
        setView();

        SocketThread thread = new SocketThread();
        thread.start();

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

        handler = new Handler();

    } // setVariable()


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

                sendMessage = chatEdit.getText().toString();
                PrintWriterThread thread = new PrintWriterThread();
                thread.start();

            }
        });


    } // setView()


    // ========================================================================================

    class SocketThread extends Thread {

        public void run() {

            try{

                InetAddress inetAddress = InetAddress.getByName(ip);
                socket = new Socket(inetAddress, port);
                printWriter = new PrintWriter(socket.getOutputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (true) {

                    String readLine = bufferedReader.readLine();
                    Log.d(TAG, "readLine : " + readLine);

                    if (readLine != null) {
                        handler.post(new messageUpdate(readLine));
                    }

                }

            } catch (IOException e) {
                Log.d(TAG, "exception : " + e);
                e.printStackTrace();
            }

        }

    } // SocketThread


    class PrintWriterThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                printWriter.println(currentNickname + "↖" + sendMessage);
                printWriter.flush();
                chatEdit.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class messageUpdate implements Runnable {

        String message;

        public messageUpdate(String msg) {
            this.message = msg;
        }

        @Override
        public void run() {

            String[] array = message.split("↖");
            String nickname = array[0];
            String message = array[1];
            String time = String.valueOf(getTime.getFormatTime(getTime.getTime()));
            int viewType = 0;
            if(nickname.equals(currentNickname)) {
                viewType = 1;
            }

            PostData postData = new PostData(currentImage, currentNickname, message, time, viewType);

            arrayList.add(postData);
            adapter.notifyDataSetChanged();

        }

    } // messageUpdate


}