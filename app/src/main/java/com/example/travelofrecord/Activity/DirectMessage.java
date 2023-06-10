package com.example.travelofrecord.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import java.util.HashMap;

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
    HashMap<String, String> map;

    GetTime getTime;

    Handler handler;
    Socket socket;
    PrintWriter printWriter;
    String ip = "3.34.246.77";
    int port = 8888;

    String sendMessage;
    String roomNum;
    boolean roomCheck = false;
    String getNickname;
    String nicknameSum1;
    String nicknameSum2;


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
        getRoomNum(nicknameSum1, nicknameSum2);
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

        LogoutPrintWriterThread thread = new LogoutPrintWriterThread();
        thread.start();

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

        Intent i = getIntent();
        getNickname = i.getStringExtra("postNickname");

        nicknameSum1 = currentNickname + "↘" + getNickname;
        nicknameSum2 = getNickname + "↘" + currentNickname;


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

                if (sendMessage.equals("") | sendMessage.equals(" ")) {
                    Toast.makeText(DirectMessage.this, "메시지를 입력하세요", Toast.LENGTH_SHORT).show();
                } else {

                    PrintWriterThread thread = new PrintWriterThread();
                    thread.start();

                    insertChat(roomNum, currentNickname, getNickname, currentImage, sendMessage, String.valueOf(getTime.getTime()));

                }

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

                LoginPrintWriterThread thread = new LoginPrintWriterThread();
                thread.start();

                while (true) {

                    String readLine = bufferedReader.readLine();

                    if (readLine != null) {
                        Log.d(TAG, "readLine : " + readLine);
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
                printWriter.println(roomNum + "↖" + currentNickname + "↖" + currentImage + "↖" + sendMessage);
                printWriter.flush();

                chatEdit.setText("");

                Log.d(TAG, "MessagePrintWriter : 실행 완료");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class LoginPrintWriterThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {

                printWriter.println(roomNum + "↖" + currentNickname + "↖" + "ⓐloginⓐ");
                printWriter.flush();
                Log.d(TAG, "LoginPrintWriter : 실행 완료");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    class LogoutPrintWriterThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {

                printWriter.println(roomNum + "↖" + currentNickname + "↖" + "ⓐlogoutⓐ");
                printWriter.flush();
                Log.d(TAG, "LogoutPrintWriter : 실행 완료");

                socket.close();
                Log.d(TAG, "onStop : logout & socket.close()");

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
            Log.d(TAG, "message : " + message);
            String[] array = message.split("↖");
            String nickname = array[1];
            String senderImage = array[2];
            String message = array[3];
            String time = String.valueOf(getTime.getFormatTime(getTime.getTime()));
            int viewType = 0;
            if(nickname.equals(currentNickname)) {
                viewType = 1;
            }

            Chat chat = new Chat(array[0], nickname, senderImage, message, time, viewType);

            arrayList.add(chat);
            adapter.notifyDataSetChanged();

            chatRecyclerView.smoothScrollToPosition(arrayList.size()-1);

//            if (roomNum.equals(array[0])) {
//                arrayList.add(chat);
//                adapter.notifyDataSetChanged();
//            }

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

                    roomCheck = response.body().getRoomCheck();
                    roomNum = response.body().getRoomNum();
                    Log.d(TAG, "roomCheck : " + roomCheck + " roomNum : " + roomNum);

                    if (roomCheck) {
                        Log.d(TAG, "이미 만들어진 채팅방 있음");
                        getChatting(roomNum);
                    } else {

                        SocketThread thread = new SocketThread();
                        thread.start();

                        Log.d(TAG, "이미 만들어진 채팅방 없음");
                    }

                } else {
                    Log.d(TAG, "getRoomNum - onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<Chat> call, Throwable t) {
                Log.d(TAG, "getRoomNum - onFailure : " + t);
            }
        });

    } // getRoomNum()


    public void getChatting(String roomNum) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Chat>> call = apiInterface.getChatting(roomNum);
        call.enqueue(new Callback<ArrayList<Chat>>() {
            @Override
            public void onResponse(Call<ArrayList<Chat>> call, Response<ArrayList<Chat>> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "getChatting - onResponse isSuccessful");

                    ArrayList<Chat> list = response.body();

                    if (list.size() > 0) {

                        for (int i = 0; i < list.size(); i++) {

                            String roomNumber = response.body().get(i).getRoomNum();
                            String sender = response.body().get(i).getSender();
                            String senderImage = response.body().get(i).getSenderImage();
                            String message = response.body().get(i).getMessage();
                            String dateMessage = response.body().get(i).getDateMessage();
                            String time = String.valueOf(getTime.getFormatTime(Long.valueOf(dateMessage)));

                            int viewType = 0;
                            if(sender.equals(currentNickname)) {
                                viewType = 1;
                            }

                            Chat chat = new Chat(roomNumber, sender, senderImage, message, time, viewType);
                            arrayList.add(chat);

                        }

                        adapter.notifyDataSetChanged();
                        chatRecyclerView.smoothScrollToPosition(arrayList.size()-1);

                    }

                    SocketThread thread = new SocketThread();
                    thread.start();

                } else {
                    Log.d(TAG, "getChatting - onResponse isFailure");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Chat>> call, Throwable t) {
                Log.d(TAG, "getChatting - onFailure : " + t);
            }
        });

    } // getChatting()


    public void insertChat(String roomNum, String sender, String receiver, String senderImage, String message, String dateMessage) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.insertChatting(roomNum, sender, receiver, senderImage, message, dateMessage);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "insertChat - onResponse isSuccessful");

                    String rpCode = response.body();

                    Log.d(TAG, "insertChat - onResponse : " + rpCode);

                } else {
                    Log.d(TAG, "insertChat - onResponse isfailure");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "insertChat - onFailure : " + t);
            }
        });

    } // insertChat()


}