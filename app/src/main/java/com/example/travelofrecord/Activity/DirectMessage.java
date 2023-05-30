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
import com.example.travelofrecord.Data.Chat;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectMessage extends AppCompatActivity {

    String TAG = "채팅";

    ImageButton backBtn;
    ImageButton sendBtn;
    EditText chatEdit;
    RecyclerView chatRecyclerView;
    ArrayList<Chat> arrayList;
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
    String roomNum;


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
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        // DB에 저장된 채팅 내용 불러와서 ArrayList.add 해주고 리사이클러뷰에 반영

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

                // DB에 채팅 내용 저장

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

            Chat chat = new Chat(roomNum, nickname, currentImage, message, time, viewType);

            arrayList.add(chat);
            adapter.notifyDataSetChanged();

        }

    } // messageUpdate

    public void getRoomNum(String roomNum1, String roomNum2) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Chat> call = apiInterface.getRoomNum(roomNum1, roomNum2);
        call.enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "getRoomNum - onResponse isSuccessful");

                    roomNum = response.body().getRoomNum();

                } else {
                    Log.d(TAG, "getRoomNum - onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                Log.d(TAG, "onFailure : " + t);
            }
        });

    }


}