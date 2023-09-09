package com.example.travelofrecord.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelofrecord.Activity.Home;
import com.example.travelofrecord.Adapter.HomeHeartList_Adapter;
import com.example.travelofrecord.Data.User;
import com.example.travelofrecord.Function.BackBtn;
import com.example.travelofrecord.Function.RandomResult;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Function.GetAdress;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Adapter.Home_Adapter;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment implements Home.OnBackPressedListener{

    String TAG = "홈 프래그먼트";
    View v;
    GetAdress getAdress = new GetAdress();
    GetTime getTime = new GetTime();
    RandomResult randomResult = new RandomResult();

    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    TextView internetText;

    FrameLayout heartLayout;
    RecyclerView heartRecyclerView;
    ArrayList<User> heart_ArrayList;
    HomeHeartList_Adapter heartAdapter;
    ImageView heartListClose_Iv;


    ImageView loading_Iv;
    Animation rotate;
    Animation appear;
    Animation disappear;

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
    SharedPreferences.Editor editor;
    String loginNickname;
    String writeCount;

    int networkStatus;

    IntentFilter heartFilter;
    IntentFilter commentFilter;
    BroadcastReceiver deleteReceiver;
    IntentFilter deleteFilter;

    boolean receiverStatus;

    int pageNum;
    String pagingStatus;

    int lastPosition;
    int totalCount;

    LinearLayout loadingLayout;
    ImageView loadingImage;
    Handler handler;
    int listSize = 0;
    boolean requestStatus = true;

    ArrayList<Integer> adList;
    int adListIndex;
    int adListValue;
    int dataSize;
    int[] randomValueArray;

    @Override
    public void onBack() {
        Log.d(TAG, "onBack: ");
        recyclerView.smoothScrollToPosition(0);
    }


    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() 호출됨");
        ((Home)context).setOnBackPressedListener(this);

    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() 호출됨");
        receiverStatus = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() 호출됨");

        v = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d(TAG, "receiverStatus : " + receiverStatus);
        return v;

    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated() 호출됨");

        setVariable();
        setView();
        getWriteCount(loginNickname);
        if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
            internetText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            requestStatus = true;
            getPost(loginNickname, pageNum);
        }else {
            internetText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }
    @Override public void onStart() {
        Log.d(TAG, "onStart() 호출됨");
        super.onStart();

//        Log.d(TAG, "onStart receiverStatus : " + receiverStatus);
//        if (receiverStatus) {
//            getActivity().unregisterReceiver(deleteReceiver);
//            getActivity().unregisterReceiver(adapter.heartReceiver);
//            getActivity().unregisterReceiver(adapter.commentReceiver);
//        }


    } // onStart()

    public void getHeartList(int postNum) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<User>> call = apiInterface.getHeartList(postNum);
        call.enqueue(new Callback<ArrayList<User>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "getHeartList - onResponse isSuccessful");

                    ArrayList<User> data = response.body();
                    Log.d(TAG, "data.size() : " + data.size());

                    if (data.size() > 0) {

                        String image;
                        String nickname;


                        for (int i = 0; i < data.size(); i++) {

                            image = data.get(i).getImage();
                            nickname = data.get(i).getNickname();

                            User heartData = new User(image, nickname);

                            heart_ArrayList.add(heartData);

                            Log.d(TAG, "image : " + image + " nickname : " + nickname + " list.size() : " + heart_ArrayList.size());

                        }

                        heartAdapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "getHeartList 없음");
                    }

                } else {
                    Log.d(TAG, "getHeartList - onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.d(TAG, "getHeartList - onFailure");
            }
        });

    }


    public void getPost(String currentNickname, int pageNumber) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getPosts(currentNickname, pageNumber);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {

                if (response.isSuccessful()) {
                    loading_Iv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    loading_Iv.clearAnimation();

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
                            pagingStatus = data.get(i).getPagingStatus();

                            heartStatus = false;

                            if (loginNickname.equals(whoLike)) {
                                heartStatus = true;
                            }

                            String[] arrayLocation = location.split(" ");
                            double latitude = Double.parseDouble(arrayLocation[0]);
                            double longitude = Double.parseDouble(arrayLocation[1]);

                            String currentLocation = getAdress.getAddress(getContext(),latitude,longitude);
                            Log.d(TAG, "currentLocation : " + currentLocation);
                            String datePost = getTime.lastTime(dateCreated);
                            String addressPost = getAdress.editAddress1234(currentLocation);
                            Log.d(TAG, "i : " + i);

                            Log.d(TAG, "num = " + num + "\nnickname = " + nickname + "\npostNum : " + postNum
                             + "\nwhoLike : " + whoLike + "\nheartStatus : " + heartStatus + "\npagingStatus : " + pagingStatus);

                            PostData postData = new PostData(num, nickname, profileImage, heart, commentNum, addressPost, postImage, writing, datePost, postNum, whoLike, heartStatus, 0);

                            listSize = post_Data_ArrayList.size();
                            post_Data_ArrayList.add(post_Data_ArrayList.size(), postData);
                            listSize = post_Data_ArrayList.size() - listSize;

                            dataSize ++;

                            if (dataSize % 7 == 0) {

                                randomValueArray = randomResult.getRandomAdIndex(adList);
                                adListValue = randomValueArray[0];
                                adListIndex = randomValueArray[1];
                                Log.d(TAG, "adListIndex : " + adListValue);
                                Log.d(TAG, "adList : " + adList);

                                post_Data_ArrayList.add(post_Data_ArrayList.size(), randomResult.randomAd(adListValue));
                                adList.remove(adListIndex);
                                Log.d(TAG, "adListDeleted : " + adList);
                                if (adList.size() == 0) {
                                    Log.d(TAG, "initArrayList getPost");
                                    initArrayList();
                                }

                            }

                        }

                        itemSize = post_Data_ArrayList.size();
                        Log.d(TAG, "itemSize : " + itemSize);

                        if (requestStatus) {
                            adapter.setItemPost(post_Data_ArrayList);
                        } else {
                            adapter.notifyItemRangeChanged(post_Data_ArrayList.size()-listSize,listSize);
                        }



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


    } // getPost()

    public void initArrayList() {

        adList = new ArrayList<>();
        for (int j = 0; j < 4; j++) {
            adList.add(j);
        }
        dataSize = 0;

    }


    public void deletePost(int num) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.deletePost(num);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "deletePost : onResponse");
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse : " + response.body());
                } else {
                    Log.d(TAG, "onResponse : fail");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "deletePost : onFailure");
            }
        });

    } // deletePost()

    public void getWriteCount(String nickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getWriteCount(nickname);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "getWriteCount - onResponse : isSuccessful");
                    writeCount = response.body().getWriteCount();
                    Log.d(TAG, "onResponse : " + writeCount);
                    editor.putString("writeCount", writeCount);
                    editor.commit();

                } else {
                    Log.d(TAG, "getWriteCount - onResponse : isFailure");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "getWriteCount - onFailure");
            }
        });

    } // getWriteCount()

    public void setVariable() {

        loading_Iv = v.findViewById(R.id.home_Loading);
        rotate = AnimationUtils.loadAnimation(getActivity(),R.anim.loading);
        appear = AnimationUtils.loadAnimation(getActivity(),R.anim.loading_appear);
        disappear = AnimationUtils.loadAnimation(getActivity(),R.anim.loading_disappear);

        networkStatus = NetworkStatus.getConnectivityStatus(getActivity());
        swipeRefreshLayout = v.findViewById(R.id.home_SwipeRefreshLayout);
        internetText = v.findViewById(R.id.internetCheck_Text);

        recyclerView = v.findViewById(R.id.home_RecyclerView);
        adapter = new Home_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        post_Data_ArrayList = new ArrayList<>();
        adapter.setItemPost(post_Data_ArrayList);

        heartRecyclerView = v.findViewById(R.id.home_HeartRecyclerView);
        heartAdapter = new HomeHeartList_Adapter();
        heart_ArrayList = new ArrayList<>();

        heartRecyclerView.setAdapter(heartAdapter);
        heartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        heartAdapter.setItem(heart_ArrayList);

        heartLayout = v.findViewById(R.id.home_HeartLayout);
        heartListClose_Iv = v.findViewById(R.id.heartListClose_Image);

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loginNickname = sharedPreferences.getString("nickname","");
        writeCount = sharedPreferences.getString("writeCount", "");

        pageNum = 1;
        pagingStatus = "false";

        loadingLayout = v.findViewById(R.id.postLoading_Layout);
        loadingImage = v.findViewById(R.id.postLoading_Image);
        handler = new Handler();

        adList = new ArrayList<>();
        Log.d(TAG, "initArrayList setVariable");
        initArrayList();
        dataSize = 0;
        randomValueArray = new int[2];

    }

    public void setView() {

//        deleteReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d(TAG, "onReceiveDelete");
//
//                int position = intent.getIntExtra("position", 0);
//                int deleteNum = post_Data_ArrayList.get(position).num;
//                Log.d(TAG, "postion : " + position + "\nnum : " + deleteNum);
//                post_Data_ArrayList.remove(position);
//                adapter.notifyDataSetChanged();
//
//                deletePost(deleteNum);
//
//            }
//        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    pageNum = 1;

                    internetText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    post_Data_ArrayList.clear();
                    post_Data_ArrayList = new ArrayList<>();

                    requestStatus = true;

                    Log.d(TAG, "initArrayList swipe");
                    initArrayList();

                    getPost(loginNickname, pageNum);
                }else {
                    internetText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.setVisibility(View.GONE);
        loading_Iv.setVisibility(View.VISIBLE);
        loading_Iv.startAnimation(rotate);


//        this.heartReceiver = adapter.heartReceiver;
//        this.commentReceiver = adapter.commentReceiver;
        deleteFilter = new IntentFilter("deletePostSync");
        heartFilter = new IntentFilter("homeHeartSync");
        commentFilter = new IntentFilter("homeCommentSync");

        adapter.setOnItemLongClickListener(new Home_Adapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int postNum) {
                Log.d(TAG, "onItemLongClick : " + postNum);
                heartLayout.setVisibility(View.VISIBLE);

                getHeartList(postNum);

            }
        });

        heartListClose_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                heartLayout.setVisibility(View.GONE);

                heart_ArrayList.clear();
                heartAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setEnabled(true);

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                totalCount = recyclerView.getAdapter().getItemCount();

                if (lastPosition == totalCount -1 & pagingStatus.equals("true")) {

                    recyclerView.scrollToPosition(post_Data_ArrayList.size()-1);
                    loadingLayout.setVisibility(View.VISIBLE);
                    loadingLayout.startAnimation(appear);
                    loadingImage.startAnimation(rotate);

                    WaitPagingThread pagingThread = new WaitPagingThread();
                    pagingThread.start();

                }

            }
        });



        heartLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                swipeRefreshLayout.setEnabled(false);
                return true;
            }
        });

    } // setView()

    public class WaitPagingThread extends Thread {

        public void run() {
            Log.d(TAG, "WaitPagingTheard.start()");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    loadingImage.startAnimation(disappear);
                    loadingLayout.startAnimation(disappear);
                    loadingLayout.setVisibility(View.GONE);

                }
            });

            if (lastPosition == totalCount -1) {

                pageNum += 1;
                Log.d(TAG, "최하단 도착. pageNum : " + pageNum);
                requestStatus = false;
                getPost(loginNickname, pageNum);

            }

        }

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
        Log.d(TAG, "onStop receiverStatus : " + receiverStatus);

//        getActivity().registerReceiver(deleteReceiver, deleteFilter);
//        getActivity().registerReceiver(adapter.heartReceiver, heartFilter);
//        getActivity().registerReceiver(adapter.commentReceiver, commentFilter);
//        receiverStatus = true;
//        Log.d(TAG, "onStop receiverStatus : " + receiverStatus);


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