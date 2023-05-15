package com.example.travelofrecord.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Function.GetAdress;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Adapter.Home_Adapter;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {

    String TAG = "홈 프래그먼트";
    View v;
    GetAdress getAdress = new GetAdress();
    GetTime getTime = new GetTime();

    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    TextView internetText;

    ArrayList<PostData> post_Data_ArrayList;
    int itemSize;
    Home_Adapter adapter;

    String nickname;
    String profileImage;
    int heart;
    int commentNum;
    String location;
    String postImage;
    String writing;
    String dateCreated;
    int num;
    int postNum;
    String whoLike;
    boolean heartStatus;

    SharedPreferences sharedPreferences;
    String loginNickname;

    int networkStatus = NetworkStatus.getConnectivityStatus(getActivity());


    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() 호출됨");
    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() 호출됨");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() 호출됨");

        v = inflater.inflate(R.layout.fragment_home, container, false);

        setView();

        return v;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated() 호출됨");

        if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
            internetText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            getPost(loginNickname);
        }else {
            internetText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }
    @Override public void onStart() {
        Log.d(TAG, "onStart() 호출됨");
        super.onStart();



    } // onStart()



    public void getPost(String currentNickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getPost(currentNickname);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {
                if (response.isSuccessful()) {

                    ArrayList<PostData> data = response.body();

                    if (data.size() > 0) {

                        Log.d(TAG, "data.size : " + data.size());

                        for (int i = 0; i < data.size(); i++) {
                            num = data.get(i).getNum();
                            nickname = data.get(i).getPostNickname();
                            profileImage = data.get(i).getProfileImage();
                            heart = data.get(i).getHeart();
                            commentNum = data.get(i).getCommentNum();
                            location = data.get(i).getLocation();
                            postImage = data.get(i).getPostImage();
                            writing = data.get(i).getWriting();
                            dateCreated = data.get(i).getDateCreated();
                            postNum = data.get(i).getPostNum();
                            whoLike = data.get(i).getWhoLike();

                            heartStatus = false;

                            if (loginNickname.equals(whoLike)) {
                                heartStatus = true;
                            }

                            String[] arrayLocation = location.split(" ");
                            double latitude = Double.parseDouble(arrayLocation[0]);
                            double longitude = Double.parseDouble(arrayLocation[1]);

                            String currentLocation = getAdress.getAddress(getContext(),latitude,longitude);

                            String datePost = getTime.lastTime(dateCreated);
                            String addressPost = getAdress.editAddress1234(currentLocation);
                            Log.d(TAG, "i : " + i);

                            Log.d(TAG, "num = " + num + "\nnickname = " + nickname + "\npostNum : " + postNum
                             + "\nwhoLike : " + whoLike + "\nheartStatus : " + heartStatus);


                            PostData postData = new PostData(num, nickname, profileImage, heart, commentNum, addressPost, postImage, writing, datePost, postNum, whoLike, heartStatus);

                            post_Data_ArrayList.add(0, postData);

                        }

                        itemSize = post_Data_ArrayList.size();
                        Log.d(TAG, "itemSize : " + itemSize);

                        adapter.notifyDataSetChanged();

                    }

                } else {
                    Log.d(TAG, "getPost : 실패");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PostData>> call, Throwable t) {
                Log.d(TAG, "onFailure: 실패 " + t);
            }
        });

    }


    public void setView() {

        swipeRefreshLayout = v.findViewById(R.id.home_SwipeRefreshLayout);

        internetText = v.findViewById(R.id.internetCheck_Text);
        recyclerView = v.findViewById(R.id.home_RecyclerView);
        adapter = new Home_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        post_Data_ArrayList = new ArrayList<>();

        adapter.setItemPost(post_Data_ArrayList);


        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보", Context.MODE_PRIVATE);
        loginNickname = sharedPreferences.getString("nickname","");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    internetText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    post_Data_ArrayList = new ArrayList<>();
                    adapter.setItemPost(post_Data_ArrayList);
                    getPost(loginNickname);
                }else {
                    internetText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    @Override public void onResume() {
        Log.d(TAG, "onResume() 호출됨");
        super.onResume();
    }
    @Override public void onPause() {
        Log.d(TAG, "onPause() 호출됨");
        super.onPause();
    }
    @Override public void onStop() {
        Log.d(TAG, "onStop() 호출됨");
        super.onStop();
    }
    @Override public void onDestroyView() {
        Log.d(TAG, "onDestroyView() 호출됨");
        super.onDestroyView();
    }
    @Override public void onDetach() {
        Log.d(TAG, "onDetach() 호출됨");
        super.onDetach();
    }

}