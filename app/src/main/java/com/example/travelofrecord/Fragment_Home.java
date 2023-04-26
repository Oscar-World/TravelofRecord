package com.example.travelofrecord;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {

    String TAG = "홈 프래그먼트";

    View v;

    SwipeRefreshLayout swipeRefreshLayout;

//    private Button photo_Btn;
//    private Button map_Btn;
//    private Button photo_Block;
//    private Button map_Block;

    RecyclerView recyclerView;

    ArrayList<PostData> post_Data_ArrayList;
    int itemSize;
    Home_Adapter adapter;

    Bundle getData;

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

    String nowAddr; // 전체 주소


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

        getPost();

    }
    @Override public void onStart() {
        Log.d(TAG, "onStart() 호출됨");
        super.onStart();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                post_Data_ArrayList = new ArrayList<>();
                adapter.setItemPost(post_Data_ArrayList);
                getPost();

                swipeRefreshLayout.setRefreshing(false);

            }
        });

//        photo_Btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                photo_Btn.setVisibility(View.GONE);
//                photo_Block.setVisibility(View.VISIBLE);
//                map_Btn.setVisibility(View.VISIBLE);
//                map_Block.setVisibility(View.GONE);
//
//            }
//        });
//
//        map_Btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                map_Btn.setVisibility(View.GONE);
//                map_Block.setVisibility(View.VISIBLE);
//                photo_Btn.setVisibility(View.VISIBLE);
//                photo_Block.setVisibility(View.GONE);
//
//            }
//        });

    } // onStart()



    public void getPost() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getPost();
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {
                if (response.isSuccessful()) {

                    ArrayList<PostData> data = response.body();

                    if (data.size() > 0) {

                        Log.d(TAG, "data.size : " + data.size());

                        for (int i = 0; i < data.size(); i++) {
                            num = data.get(i).getNum();
                            nickname = data.get(i).getNickname();
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

                            String currentLocation = getAddress(getContext(),latitude,longitude);

                            String datePost = lastTime(dateCreated);
                            String addressPost = editAddress(currentLocation);
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

    public String lastTime(String dateCreated) {

        String msg = null;

        long datePosted = Long.parseLong(dateCreated);
        long currentTime = System.currentTimeMillis();
        long lastTime = (currentTime - datePosted) / 1000;

        if (lastTime < 60) {
            msg = "방금 전";
        } else if ((lastTime /= 60) < 60) {
            msg = lastTime + "분 전";
        } else if ((lastTime /= 60) < 24) {
            msg = lastTime + "시간 전";
        } else if ((lastTime /= 24) < 7) {
            msg = lastTime + "일 전";
        } else if (lastTime < 14) {
            msg = "1주 전";
        } else if (lastTime < 21) {
            msg = "2주 전";
        } else if (lastTime < 28) {
            msg = "3주 전";
        } else if ((lastTime / 30) < 12) {
            msg = lastTime + "달 전";
        } else {
            msg = (lastTime / 365) + "년 전";
        }

        return msg;
    }

    // Geocoder - 위도, 경도 사용해서 주소 구하기.
    public String getAddress(Context mContext, double lat, double lng) {
        nowAddr ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;

        try
        {
            if (geocoder != null)
            {
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0)
                {
                    nowAddr = address.get(0).getAddressLine(0).toString();
                    Log.d(TAG, "전체 주소 : " + nowAddr);

                }
            }
        }
        catch (IOException e)
        {
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return nowAddr;
    } // getAddress


    public String editAddress(String location) {

        String address = null;

        String[] addressArray = location.split(" ");

        address = addressArray[1] + " " + addressArray[2] + " " + addressArray[3] + " " + addressArray[4];

        return address;

    }


    public void setView() {

        swipeRefreshLayout = v.findViewById(R.id.home_SwipeRefreshLayout);

//        photo_Btn = v.findViewById(R.id.homePhoto_Btn);
//        map_Btn = v.findViewById(R.id.homeMap_Btn);
//        photo_Block = v.findViewById(R.id.homePhoto_Block);
//        map_Block = v.findViewById(R.id.homeMap_Block);

        recyclerView = v.findViewById(R.id.home_RecyclerView);
        adapter = new Home_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        post_Data_ArrayList = new ArrayList<>();

        adapter.setItemPost(post_Data_ArrayList);

        getData = getArguments();

        Log.d(TAG, "getData : " + getData);

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보", Context.MODE_PRIVATE);
        loginNickname = sharedPreferences.getString("nickname","");

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

        getData = null;

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