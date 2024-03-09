package com.example.travelofrecord.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelofrecord.Adapter.Chat_Adapter;
import com.example.travelofrecord.Data.Chat;
import com.example.travelofrecord.Function.BackBtn;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectMessage extends AppCompatActivity {

    String TAG = "채팅액티비티";
    String currentNickname, currentImage, sendMessage, roomNum, getNickname, nicknameSum1, nicknameSum2, otherFcmToken, messageStatus, baseActivity;
    int lastPosition, totalCount;
    boolean userStatus;
    String ip = "3.34.246.77";
    String newChatDate = "";
    int port = 8888;
    boolean roomCheck = false;
    boolean chatRoomStatus = false;
    ImageButton backBtn, sendBtn;
    EditText chatEdit;
    TextView chatRoomText;
    LinearLayout newMessageLayout;
    RecyclerView chatRecyclerView;
    ArrayList<Chat> arrayList;
    Chat_Adapter adapter;
    SharedPreferences sharedPreferences;
    HashMap<String, String> map;
    GetTime getTime;
    Handler handler;
    Socket socket;
    PrintWriter printWriter;
    BackBtn back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm);
        Log.d(TAG, "onCreate() 호출됨");

        setVariable();
        setView();
        getRoomNum(nicknameSum1, nicknameSum2);
        getFcmToken(getNickname);

    } // onCreate()


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() 호출됨");
        LogoutPrintWriterThread thread = new LogoutPrintWriterThread();
        thread.start();

    } // onDestroy()


    /*
    시스템 뒤로가기 클릭 이벤트
     */
    @Override
    public void onBackPressed() {

        if (baseActivity.equals(".Activity.Home")) {
            Log.d(TAG, "onBackPressed : 내부 진입");
            finish();
        } else {
            Log.d(TAG, "onBackPressed : 외부 진입");
            back.onBackPressedAtDm();
        }

    } // onBackPressed()


    // -------------------------------------------------------------------------------------------


    /*
    변수 초기화
     */
    public void setVariable() {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> info = manager.getAppTasks();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            baseActivity = info.get(0).getTaskInfo().baseActivity.getShortClassName();
        }

        back = new BackBtn(this);

        backBtn = findViewById(R.id.chatBack_Btn);
        sendBtn = findViewById(R.id.chatSend_Btn);
        chatEdit = findViewById(R.id.chatMessage_Edit);
        chatRoomText = findViewById(R.id.chat_RoomText);
        chatRecyclerView = findViewById(R.id.chat_RecyclerView);
        newMessageLayout = findViewById(R.id.chat_newMessageText);

        Intent i = getIntent();
        getNickname = i.getStringExtra("postNickname");
        userStatus = i.getBooleanExtra("userStatus", true);

        if (userStatus) {
            chatRoomText.setText(getNickname);
        } else {
            chatRoomText.setText(getNickname + " (탈퇴한 사용자)");
        }

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        currentNickname = sharedPreferences.getString("nickname", "");
        currentImage = sharedPreferences.getString("image", "");

        Log.d(TAG, "currentNickname : " + currentNickname + " / getNickname : " + getNickname);
        nicknameSum1 = currentNickname + "↘" + getNickname;
        nicknameSum2 = getNickname + "↘" + currentNickname;

        arrayList = new ArrayList<>();
        adapter = new Chat_Adapter();
        chatRecyclerView.setAdapter(adapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setItemChat(arrayList);


        getTime = new GetTime();

        handler = new Handler();

    } // setVariable()


    /*
    뷰 초기화
     */
    public void setView() {

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (baseActivity.equals(".Activity.Home")) {
                    Log.d(TAG, "onBackPressed : 내부 진입");
                    finish();
                } else {
                    Log.d(TAG, "onBackPressed : 외부 진입");
                    back.onBackPressedAtDm();
                }

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

                }

            }
        });

        chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                totalCount = recyclerView.getAdapter().getItemCount();

                if (lastPosition == totalCount - 1) {

                    newMessageLayout.setVisibility(View.GONE);

                }

            }
        });

        newMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chatRecyclerView.smoothScrollToPosition(arrayList.size() - 1);
                newMessageLayout.setVisibility(View.GONE);

            }
        });


    } // setView()


    // ========================================================================================


    /*
    소켓 통신 스레드
     */
    class SocketThread extends Thread {

        public void run() {

            try {

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


    /*
    채팅 입력 스레드
     */
    class PrintWriterThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                printWriter.println(roomNum + "↖" + currentNickname + "↖" + getNickname + "↖" + currentImage + "↖" + sendMessage + "↖" + otherFcmToken);
                printWriter.flush();

                chatEdit.setText("");

                Log.d(TAG, "MessagePrintWriter : 실행 완료");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    } // PrintWriterThread


    /*
    채팅방 입장 알림 입력 스레드
     */
    class LoginPrintWriterThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                printWriter.println(roomNum + "↖" + currentNickname + "↖" + getNickname + "↖" + currentImage + "↖" + "ⓐloginⓐ" + "↖" + otherFcmToken);
                printWriter.flush();
                Log.d(TAG, "LoginPrintWriter : 실행 완료");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    } // LoginPrintWriterThread


    /*
    채팅방 퇴장 알림 입력 스레드
     */
    class LogoutPrintWriterThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {

                printWriter.println(roomNum + "↖" + currentNickname + "↖" + getNickname + "↖" + currentImage + "↖" + "ⓐlogoutⓐ" + "↖" + otherFcmToken);
                printWriter.flush();
                Log.d(TAG, "LogoutPrintWriter : 실행 완료");

                socket.close();
                Log.d(TAG, "onStop : logout & socket.close()");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    } // LogoutPrintWriterThread


    /*
    채팅 응답 시 관련 뷰 업데이트
     */
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
            String senderImage = array[3];
            String message = array[4];
            messageStatus = array[6];
            String time = String.valueOf(getTime.getFormatTime1(getTime.getTime()));
            int viewType = 0;

            String formatTime = String.valueOf(getTime.getFormatTime5(getTime.getTime()));
            String dayOfWeek = getTime.getDayOfWeek(formatTime);
            String date = getTime.getFormatTime6(getTime.getTime()) + dayOfWeek;

            if (nickname.equals(currentNickname) & !message.equals("ⓐloginⓐ") & !message.equals("ⓐlogoutⓐ")) {
                viewType = 1;

                insertChat(roomNum, currentNickname, getNickname, currentImage, sendMessage, String.valueOf(System.currentTimeMillis()), messageStatus, otherFcmToken);
                if (!chatRoomStatus) {

                    insertChatRoom(roomNum, currentNickname, getNickname, sendMessage, String.valueOf(System.currentTimeMillis()));
                    chatRoomStatus = true;
                }

            }

            // login & logout 응답 받을 때는 예외처리.
            // 상대방 status == true 응답 시 전체 리스트 status 수정.

            if (message.equals("ⓐloginⓐ")) {

                String beforeTime = "";
                String beforeSender = "";

                if (!currentNickname.equals(nickname) & messageStatus.equals("true")) {
                    for (int i = 0; i < arrayList.size(); i++) {

                            arrayList.set(i, new Chat(arrayList.get(i).getRoomNum(), arrayList.get(i).getSender(), arrayList.get(i).getSenderImage(), arrayList.get(i).getMessage(),
                                    arrayList.get(i).getDateMessage(), arrayList.get(i).getViewType(), "true", arrayList.get(i).getDate()));

                    }

                }

            } else if (!message.equals("ⓐlogoutⓐ")) {

                if (!newChatDate.equals("")) {

                    if (!formatTime.equals(getTime.getFormatTime5(Long.valueOf(newChatDate)))) {

                        arrayList.add(new Chat("", "", "", "", "", 2, "", date));

                        newChatDate = String.valueOf(getTime.getTime());

                    }

                }

                if (arrayList.size() > 0) {
                    if (time.equals(arrayList.get(arrayList.size() - 1).getDateMessage()) & nickname.equals(arrayList.get(arrayList.size() - 1).getSender())) {
                        arrayList.set(arrayList.size() - 1, new Chat(array[0], arrayList.get(arrayList.size() - 1).getSender(), arrayList.get(arrayList.size() - 1).getSenderImage(),
                                arrayList.get(arrayList.size() - 1).getMessage(), "", arrayList.get(arrayList.size() - 1).getViewType(), arrayList.get(arrayList.size() - 1).getMessageStatus()));
                    }
                }

                Chat chat = new Chat(array[0], nickname, senderImage, message, time, viewType, messageStatus);
                arrayList.add(chat);

                if (arrayList.size() > 0) {

                    if (lastPosition < totalCount - 5 & !nickname.equals(currentNickname)) {

                        newMessageLayout.setVisibility(View.VISIBLE);

                    } else {

                        chatRecyclerView.scrollToPosition(arrayList.size() - 1);

                    }

                }

            }

            adapter.notifyDataSetChanged();


        } // run()

    } // messageUpdate


    /*
    채팅방 번호 불러오기
     */
    public void getRoomNum(String roomNum1, String roomNum2) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Chat> call = apiInterface.getRoomNum(roomNum1, roomNum2);
        call.enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Call<Chat> call, Response<Chat> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "getRoomNum - onResponse isSuccessful");

                    roomCheck = response.body().getRoomCheck();
                    roomNum = response.body().getRoomNum();
                    String sender = response.body().getSender();
                    Log.d(TAG, "roomCheck : " + roomCheck + " roomNum : " + roomNum + " sender : " + sender);


                    if (roomCheck) {
                        Log.d(TAG, "이미 만들어진 채팅방 있음");
                        chatRoomStatus = true;
                        getChatting(roomNum, getNickname);
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


    /*
    기존 채팅 내역 불러오기
     */
    public void getChatting(String roomNum, String sender) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Chat>> call = apiInterface.getChatting(roomNum, sender);
        call.enqueue(new Callback<ArrayList<Chat>>() {
            @Override
            public void onResponse(Call<ArrayList<Chat>> call, Response<ArrayList<Chat>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "getChatting - onResponse isSuccessful");

                    ArrayList<Chat> list = response.body();
                    String formatday = "";
                    String dayOfWeek = "";
                    String date = "";
                    int viewType = 0;

                    String roomNumber = "";
                    String sender = "";
                    String senderImage = "";
                    String message = "";
                    String dateMessage = "";
                    String messageStatus = "";
                    String time = "";

                    int dateNum = 0;

                    if (list.size() > 0) {

                        for (int i = 0; i < list.size(); i++) {

                            Log.d(TAG, "beforeTime : " + time + " / time : " + getTime.getFormatTime1(Long.valueOf(list.get(i).getDateMessage())));
                            Log.d(TAG, "beforeSender : " + sender + " / sender : " + list.get(i).getSender());

                            if (arrayList.size() > 0) {

                                if (time.equals(String.valueOf(getTime.getFormatTime1(Long.valueOf(list.get(i).getDateMessage())))) &&
                                        sender.equals(list.get(i).getSender())) {
                                    Log.d(TAG, "before 들어옴");
                                    Log.d(TAG, "oscar - index : " + i);
                                    Log.d(TAG, "oscar - time1 : " + time + " / time2 : " + String.valueOf(getTime.getFormatTime1(Long.valueOf(list.get(i).getDateMessage()))));
                                    Log.d(TAG, "oscar - sender1 : " + sender + " / sender2 : " + list.get(i).getSender());

                                    Chat chat = new Chat(arrayList.get(i-1+dateNum).getRoomNum(), arrayList.get(i-1+dateNum).getSender(), arrayList.get(i-1+dateNum).getSenderImage(),
                                            arrayList.get(i-1+dateNum).getMessage(), "", arrayList.get(i-1+dateNum).getViewType(), arrayList.get(i-1+dateNum).getMessageStatus(), arrayList.get(i-1+dateNum).getDate());
                                    arrayList.set(i - 1+dateNum, chat);

                                }

                            }

                            roomNumber = list.get(i).getRoomNum();
                            sender = list.get(i).getSender();
                            senderImage = list.get(i).getSenderImage();
                            message = list.get(i).getMessage();
                            dateMessage = list.get(i).getDateMessage();
                            messageStatus = list.get(i).getMessageStatus();
                            newChatDate = dateMessage;

                            time = String.valueOf(getTime.getFormatTime1(Long.valueOf(dateMessage)));

                            Log.d(TAG, "formatday : " + formatday + "\n" + getTime.getFormatTime5(Long.valueOf(dateMessage)));

                            if (!formatday.equals(String.valueOf(getTime.getFormatTime5(Long.valueOf(dateMessage))))) {

                                viewType = 2;
                                dateNum += 1;

                                formatday = String.valueOf(getTime.getFormatTime5(Long.valueOf(dateMessage)));
                                dayOfWeek = getTime.getDayOfWeek(formatday);
                                date = getTime.getFormatTime6(Long.valueOf(dateMessage)) + dayOfWeek;

                                Chat chat = new Chat("", "", "", "", "", viewType, "", date);
                                arrayList.add(chat);

                            }

                            viewType = 0;
                            if (sender.equals(currentNickname)) {
                                viewType = 1;
                            }

                            Chat chat = new Chat(roomNumber, sender, senderImage, message, time, viewType, messageStatus);
                            arrayList.add(chat);

                        }

                        adapter.notifyDataSetChanged();
                        chatRecyclerView.scrollToPosition(arrayList.size() - 1);

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


    /*
    채팅 내역 DB에 추가
     */
    public void insertChat(String roomNum, String sender, String receiver, String senderImage, String message, String dateMessage, String messageStatus, String fcmToken) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.insertChatting(roomNum, sender, receiver, senderImage, message, dateMessage, messageStatus, fcmToken);
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


    /*
    FCM 토큰 불러오기
     */
    public void getFcmToken(String nickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.getFcmToken(nickname);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "getFcmToken onResponse isSuccessful");
                    Log.d(TAG, "getFcmToken onResponse: " + response.body());
                    otherFcmToken = response.body();

                } else {
                    Log.d(TAG, "getFcmToken onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "getFcmToken onFailure");
            }
        });

    } // getFcmToken()


    /*
    채팅방 새로 생성
     */
    public void insertChatRoom(String chatRoomNum, String chatRoomUser1, String chatRoomUser2, String chatRoomMessage, String chatRoomDateMessage) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.insertChatRoom(chatRoomNum, chatRoomUser1, chatRoomUser2, chatRoomMessage, chatRoomDateMessage);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "insertChatRoom onResponse isSuccessful");
                    Log.d(TAG, "insertChatRoom onResponse : " + response.body());
                } else {
                    Log.d(TAG, "insertChatRoom onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "insertChatRoom onFailure");
            }
        });

    } // insertChatRoom()


}