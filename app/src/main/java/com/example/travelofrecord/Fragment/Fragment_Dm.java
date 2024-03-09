package com.example.travelofrecord.Fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelofrecord.Adapter.ChatRoom_Adapter;
import com.example.travelofrecord.Data.Chat;
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

public class Fragment_Dm extends Fragment {

    String TAG = "채팅 프래그먼트";
    String currentNickname, roomName, message, dateMessage;
    int notReadMessage;
    String ip = "3.34.246.77";
    int port = 8888;
    View v;
    TextView noChatRoomText;
    ImageView chatRoomLoadingIv;
    Animation rotate;
    RecyclerView recyclerView;
    ImageButton addChatBtn;
    ChatRoom_Adapter adapter;
    ArrayList<Chat> arrayList;
    GetTime getTime;
    SharedPreferences sharedPreferences;
    Handler handler;
    Socket socket;
    PrintWriter printWriter;
    Chat chat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView() 호출");
        v = inflater.inflate(R.layout.fragment_dm, container, false);
        return v;

    } // onCreateView()


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated() 호출");

        setVariable();
        setView();

    } // onViewCreated()


    @Override
    public void onStart() {
        Log.d(TAG, "onStart() 호출");
        super.onStart();

        getRoom(currentNickname);
        SocketThread thread = new SocketThread();
        thread.start();
        deleteNoti();

    } // onStart()(


    @Override
    public void onStop() {
        Log.d(TAG, "onStop() 호출");
        super.onStop();

        LogoutPrintWriterThread thread = new LogoutPrintWriterThread();
        thread.start();

    } // onStop()


    /*
    변수 초기화
     */
    public void setVariable() {

        noChatRoomText = v.findViewById(R.id.noChatRoom_Text);
        chatRoomLoadingIv = v.findViewById(R.id.chatRoom_Loading);
        rotate = AnimationUtils.loadAnimation(getActivity(),R.anim.loading);
        chatRoomLoadingIv.startAnimation(rotate);
        recyclerView = v.findViewById(R.id.chatRoom_RecyclerView);
        addChatBtn = v.findViewById(R.id.chatRoomAdd_Btn);
        adapter = new ChatRoom_Adapter();
        arrayList = new ArrayList<>();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setItemChatRoom(arrayList);

        getTime = new GetTime();

        sharedPreferences = getActivity().getSharedPreferences("로그인 정보", Context.MODE_PRIVATE);
        currentNickname = sharedPreferences.getString("nickname", "");

        handler = new Handler();


    } // setVariable()


    /*
    뷰 초기화
     */
    public void setView() {

        addChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();

            }
        });


    } // setView()


    // --------------------------------------------------------------------------------------


    /*
    소켓 통신 스레드
     */
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


    /*
    채팅 응답 시 관련 뷰 업데이트
     */
    class messageUpdate implements Runnable {

        String message;

        public messageUpdate(String msg) {
            this.message = msg;
        }

        public void run() {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            getRoom(currentNickname);

        }

    } // messageUpdate


    /*
    채팅방 목록 진입 내역 입력
     */
    class LoginPrintWriterThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                printWriter.println(currentNickname + "↖" + "ⓐloginRoomⓐ");
                printWriter.flush();
                Log.d(TAG, "LoginPrintWriter : 실행 완료");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    } // LoginPrintWriterThread


    /*
    채팅방 목록 퇴장 내역 입력
     */
    class LogoutPrintWriterThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {

                printWriter.println(currentNickname + "↖" + "ⓐlogoutRoomⓐ");
                printWriter.flush();
                Log.d(TAG, "LogoutPrintWriter : 실행 완료");

                socket.close();
                Log.d(TAG, "onStop : logout & socket.close()");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    } // LogoutPrintWriterThread


    // --------------------------------------------------------------------------------------


    /*
    채팅방 목록 데이터 불러오기
     */
    public void getRoom(String nickname) {

        arrayList.clear();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Chat>> call = apiInterface.getRoom(nickname);
        call.enqueue(new Callback<ArrayList<Chat>>() {
            @Override
            public void onResponse(Call<ArrayList<Chat>> call, Response<ArrayList<Chat>> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "getRoom - onResponse isSuccessful");

                    ArrayList<Chat> data = response.body();

                    if (data.size() > 0) {

                        for (int i = 0; i < data.size(); i ++) {

                        roomName = data.get(i).getRoomName();
                        message = data.get(i).getLastMessage();
                        dateMessage = data.get(i).getLastDate();
                        notReadMessage = data.get(i).getNotReadMessage();
                        String senderImage = data.get(i).getSenderImage();

                        Log.d(TAG, "채팅방 정보 : " + roomName + " / " + message + " / " + dateMessage + " / " + notReadMessage + " / " + senderImage);

                        String[] array = roomName.split("↘");
                        String user1 = array[0];
                        String user2 = array[1];

                        if (currentNickname.equals(user1)) {
                            roomName = user2;
                        } else {
                            roomName = user1;
                        }

                        chat = new Chat(roomName, message, dateMessage, notReadMessage, senderImage);

                        arrayList.add(chat);

                        } // for()

                        noChatRoomText.setVisibility(View.GONE);
                        chatRoomLoadingIv.setVisibility(View.GONE);
                        chatRoomLoadingIv.clearAnimation();
                        recyclerView.setVisibility(View.VISIBLE);

                        adapter.notifyDataSetChanged();

                    } else {

                        noChatRoomText.setVisibility(View.VISIBLE);
                        chatRoomLoadingIv.setVisibility(View.GONE);
                        chatRoomLoadingIv.clearAnimation();
                        recyclerView.setVisibility(View.GONE);

                    }

                } else {
                    Log.d(TAG, "getRoom - onResponse isFailure");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Chat>> call, Throwable t) {
                Log.d(TAG, "getRoom - onFailure");
            }
        });

    } // getRoom()


    /*
    채팅방 알림 삭제
     */
    public void deleteNoti() {

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    } // deleteNoti()


}