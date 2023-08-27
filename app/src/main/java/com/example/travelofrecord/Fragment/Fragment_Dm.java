package com.example.travelofrecord.Fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelofrecord.Activity.DirectMessage;
import com.example.travelofrecord.Activity.Home;
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
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Dm extends Fragment {

    String TAG = "채팅 프래그먼트";
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
    String currentNickname;

    Bundle bundle;
    String postNickname;

    Handler handler;
    Socket socket;
    PrintWriter printWriter;
    String ip = "3.34.246.77";
    int port = 8888;

    String roomName;
    String message;
    String dateMessage;
    int notReadMessage;
    Chat chat;


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
        getRoom(currentNickname);
        SocketThread thread = new SocketThread();
        thread.start();
        deleteNoti();
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

        LogoutPrintWriterThread thread = new LogoutPrintWriterThread();
        thread.start();

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

//        bundle = getArguments();

//        try {
//
//            if (bundle.getString("postNickname") != null) {
//                Log.d(TAG, "getArguments : " + bundle.getString("postNickname"));
//                postNickname = bundle.getString("postNickname");
//            } else {
//                Log.d(TAG, "getArguments : null");
//            }
//
//        } catch (NullPointerException e) {
//            Log.d(TAG, "NullPointerException : " + e);
//            Log.d(TAG, "getArguments : null");
//        }

        handler = new Handler();


    } // setVariable()



    public void setView() {

// 채팅 상대 검색하는 액티비티 추가 예정.
        addChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();

            }
        });


    } // setView()


    // --------------------------------------------------------------------------------------

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
    }

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

    }

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

    }


    // --------------------------------------------------------------------------------------


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

//                    if (postNickname != null) {
//                        Log.d(TAG, "postNickname : " + postNickname);
//
//                        Intent i = new Intent(getActivity(), DirectMessage.class);
//                        i.putExtra("postNickname", postNickname);
//                        startActivity(i);
//
//                        bundle = null;
//
//                    } else {
//                        Log.d(TAG, "postNickname is null");
//                    }



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

    public void deleteNoti() {

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
//        notificationManager.cancel(0);

    }


}