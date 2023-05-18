package com.example.travelofrecord.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Adapter.Comment_Adapter;
import com.example.travelofrecord.Function.GetAdress;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Post extends AppCompatActivity {

    String TAG = "게시글 액티비티";
    GetAdress getAddress = new GetAdress();
    GetTime getTime = new GetTime();

    ImageButton back_Btn;
    TextView post_Nickname_Text;
    ImageView post_ProfileImage_Iv;
    TextView post_Location_Text;
    ImageView post_PostImage_Iv;
    TextView post_DateCreated_Text;
    TextView post_Writing_Text;
    ImageView post_Heart_Iv;
    ImageView post_HeartFull_Iv;
    TextView post_HeartNum_Text;
    ImageView post_Comment_Iv;
    TextView post_CommentNum_Text;
    EditText post_Comment_Edit;
    ImageButton post_CommentAdd_Btn;
    ImageButton post_Menu_Btn;

    int getPosition;

    int post_Num;
    String post_Nickname;
    String post_ProfileImage;
    int post_Heart;
    int post_CommentNum;
    String post_Location;
    String post_PostImage;
    String post_Writing;
    String post_DateCreated;
    String post_WhoLike;
    boolean post_HeartStatus;

    String post_EditDate;
    String post_EditLocation;

    String addComment;
    String accessNickname;
    String accessProfileImage;
    String addDateComment;
    SharedPreferences sharedPreferences;

    Home home;

    NestedScrollView scrollView;
    RecyclerView recyclerView;
    Comment_Adapter adapter;
    ArrayList<PostData> postDataArrayList;
    int listSize;

    ApiInterface apiInterface;
    LinearLayout post_TopLayout;

    int networkStatus;

    BroadcastReceiver heartReceiver;
    IntentFilter heartFilter;
    BroadcastReceiver commentReceiver;
    IntentFilter commentFilter;

    BroadcastReceiver commentReceiver2;
    IntentFilter commentFilter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Log.d(TAG, "onStart() 호출됨");

        setVariable();
        setView();
        getPost(accessNickname, post_Num);
        getComment(post_Num);

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출됨");
        registerReceiver(commentReceiver2, commentFilter2);
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
        unregisterReceiver(commentReceiver2);
        registerReceiver(heartReceiver, heartFilter);
        registerReceiver(commentReceiver, commentFilter);

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
        unregisterReceiver(heartReceiver);
        unregisterReceiver(commentReceiver);
    }


    // ---------------------------------------------------------------------------------------------


    public void setVariable() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        networkStatus = NetworkStatus.getConnectivityStatus(this);

        back_Btn = findViewById(R.id.postBack_Btn);
        post_Nickname_Text = findViewById(R.id.post_nickname);
        post_ProfileImage_Iv = findViewById(R.id.post_profileImage);
        post_PostImage_Iv = findViewById(R.id.post_postImage);
        post_Writing_Text = findViewById(R.id.post_writing);
        post_HeartNum_Text = findViewById(R.id.post_heartNumber);
        post_CommentNum_Text = findViewById(R.id.post_commentNumber);
        post_DateCreated_Text = findViewById(R.id.post_dateCreated);
        post_Heart_Iv = findViewById(R.id.post_heart);
        post_HeartFull_Iv = findViewById(R.id.post_heartFull);
        post_Comment_Iv = findViewById(R.id.post_comment);
        post_Location_Text = findViewById(R.id.post_location);
        post_Comment_Edit = findViewById(R.id.post_commentEdit);
        post_CommentAdd_Btn = findViewById(R.id.post_commentAdd_Btn);
        post_Menu_Btn = findViewById(R.id.postMenu_Btn);

        Intent i = getIntent();

        post_Num = i.getIntExtra("num", 0);
        getPosition = i.getIntExtra("position", 0);

        home = new Home();

        sharedPreferences = getSharedPreferences("로그인 정보", Context.MODE_PRIVATE);
        accessNickname = sharedPreferences.getString("nickname","");
        accessProfileImage = sharedPreferences.getString("image", "");

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        scrollView = findViewById(R.id.post_ScrollView);
        recyclerView = findViewById(R.id.comment_recyclerView);
        adapter = new Comment_Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postDataArrayList = new ArrayList<>();
        adapter.setItemComment(postDataArrayList);

        post_TopLayout = findViewById(R.id.post_TopLayout);

    } // setVariable()

    public void setView() {



        heartReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int heartNum = intent.getIntExtra("heartNum",0);
                boolean heartStatus = intent.getBooleanExtra("heartStatus", false);
                Log.d(TAG, "onReceive: " + heartNum + " " + heartStatus);

                post_HeartNum_Text.setText(String.valueOf(heartNum));

                if (heartStatus) {
                    post_Heart_Iv.setVisibility(View.GONE);
                    post_HeartFull_Iv.setVisibility(View.VISIBLE);
                } else {
                    post_HeartFull_Iv.setVisibility(View.GONE);
                    post_Heart_Iv.setVisibility(View.VISIBLE);
                }

            }
        };

        commentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int commentNum = intent.getIntExtra("commentNum", 0);
                Log.d(TAG, "받은개수 : " + commentNum);
                post_CommentNum_Text.setText(String.valueOf(commentNum));

            }
        };

        commentReceiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int commentNum = intent.getIntExtra("commentNum2", 0);
                Log.d(TAG, "받은개수2 : " + commentNum);
                post_CommentNum_Text.setText(String.valueOf(commentNum));

            }
        };

        heartFilter = new IntentFilter("heartSync");
        commentFilter = new IntentFilter("commentSync");
        commentFilter2 = new IntentFilter("commentSync2");


        post_Comment_Iv.setFocusableInTouchMode(true);
        recyclerView.setFocusableInTouchMode(true);


        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        post_ProfileImage_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    Intent i = new Intent(getApplicationContext(), Profile.class);
                    i.putExtra("nickname",post_Nickname);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        post_Nickname_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    Intent i = new Intent(getApplicationContext(),Profile.class);
                    i.putExtra("nickname",post_Nickname);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        post_CommentAdd_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    addComment = post_Comment_Edit.getText().toString();
                    addDateComment = getTime.getTime().toString();

                    if (!addComment.equals("")) {

                        post_CommentNum += 1;
                        post_CommentNum_Text.setText(String.valueOf(post_CommentNum));

                        addComment(post_Num, accessProfileImage, accessNickname, addDateComment, addComment, post_CommentNum);

                        post_Comment_Edit.setText("");

                        Intent i = new Intent("commentSync");
                        i.putExtra("commentNum", post_CommentNum);
                        sendBroadcast(i);

                        Intent i2 = new Intent("homeCommentSync");
                        i2.putExtra("commentNum", post_CommentNum);
                        sendBroadcast(i2);

                    } else {
                        Toast.makeText(Post.this, "댓글을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }



                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        post_TopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        post_Comment_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.requestFocus();
            }
        });

        post_Heart_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    post_Heart_Iv.setVisibility(View.GONE);
                    post_HeartFull_Iv.setVisibility(View.VISIBLE);
                    post_Heart += 1;
                    post_HeartNum_Text.setText(String.valueOf(post_Heart));

                    insertWhoLike(post_Num, accessNickname, post_Heart);

                    Intent i = new Intent("heartSync");
                    i.putExtra("heartNum", post_Heart);
                    i.putExtra("heartStatus", true);
                    sendBroadcast(i);

                    Intent i2 = new Intent("homeHeartSync");
                    i2.putExtra("heartNum", post_Heart);
                    i2.putExtra("heartStatus", true);
                    i2.putExtra("postNum", post_Num);
                    sendBroadcast(i2);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        post_HeartFull_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    post_HeartFull_Iv.setVisibility(View.GONE);
                    post_Heart_Iv.setVisibility(View.VISIBLE);
                    post_Heart -= 1;
                    post_HeartNum_Text.setText(String.valueOf(post_Heart));

                    deleteWhoLike(post_Num, accessNickname, post_Heart);

                    Intent i = new Intent("heartSync");
                    i.putExtra("heartNum", post_Heart);
                    i.putExtra("heartStatus", false);
                    sendBroadcast(i);

                    Intent i2 = new Intent("homeHeartSync");
                    i2.putExtra("heartNum", post_Heart);
                    i2.putExtra("heartStatus", false);
                    i2.putExtra("postNum", post_Num);
                    sendBroadcast(i2);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        post_PostImage_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    Intent i = new Intent(Post.this, PhotoView.class);
                    i.putExtra("image",post_PostImage);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        post_Menu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(Post.this,view);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.menu_Delete) {
                            Intent i = new Intent("deletePostSync");
                            i.putExtra("position", getPosition);
                            sendBroadcast(i);
                            finish();

                        }

                        return false;
                    }
                });

                popup.show();

            }
        });


    } // setView()


    // -------------------------------------------------------------------------------------------


    public void insertWhoLike(int postNum, String whoLike, int heart) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<PostData> call = apiInterface.insertWhoLike(postNum, whoLike, heart);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "insertWhoLike_Response 성공");

                    int rp_postNum = response.body().getPostNum();
                    String rp_whoLike = response.body().getWhoLike();

                    Log.d(TAG, "저장된 데이터 -\nrp_num : " + rp_postNum + "\nrp_heart : " + rp_whoLike);

                } else {
                    Log.d(TAG, "insertWhoLike_Response 실패");
                }

            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Log.d(TAG, "onFailure: 실패 " + t);
            }
        });

    } // insertWhoLike

    public void deleteWhoLike(int postNum, String whoLike, int heart) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<PostData> call = apiInterface.deleteWhoLike(postNum, whoLike, heart);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "deleteWhoLike_Response 성공");
                } else {
                    Log.d(TAG, "deleteWhoLike_Response 실패");
                }

            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Log.d(TAG, "onFailure: 실패 " + t);
            }
        });

    } // deleteWhoLike()


    public void addComment(int postNum, String profileImage, String whoComment, String dateComment, String comment, int commentNum) {

        Call<String> call = apiInterface.insertComment(postNum, profileImage, whoComment, dateComment, comment, commentNum);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "insertComment onResponse");
                if (response.isSuccessful()) {

                    postDataArrayList.clear();
                    getComment(post_Num);

                } else {
                    Log.d(TAG, "responseFail");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "insertComent onFailure : " + t);
            }
        });

    } // addComment()


    public void getComment(int num) {

        Call<ArrayList<PostData>> call = apiInterface.getComment(num);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {
                Log.d(TAG, "getComment onResponse");
                if (response.isSuccessful()) {

                    ArrayList<PostData> data = response.body();
//                    Log.d(TAG, "data.size() : " + data.size());

                    if (data.size() > 0) {

                        for (int i = 0; i < data.size(); i++) {

                            String profileImage = data.get(i).getCommentProfileImage();
                            String whoComment = data.get(i).getWhoComment();
                            String dateComment = data.get(i).getDateComment();
                            String comment = data.get(i).getComment();

                            Log.d(TAG, "dateComment : " + dateComment);


//                            Log.d(TAG, "profileImage : " + profileImage + "\nwhoComment : " + whoComment + "\ndateComment : " + commentTime + "\ncomment : " + comment);

                            PostData postData = new PostData(profileImage, whoComment, dateComment, comment, post_Num, post_CommentNum);

                            postDataArrayList.add(postData);

                        }

                        listSize = postDataArrayList.size();
                        adapter.notifyDataSetChanged();

                    }

                } else {
                    Log.d(TAG, "responseFail");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<PostData>> call, Throwable t) {
                Log.d(TAG, "getComent onFailure : " + t);
            }
        });

    } // getComment()


    public void getPost(String currentNickname, int postNum) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getPost(currentNickname, postNum);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {
                if (response.isSuccessful()) {

                    ArrayList<PostData> data = response.body();

                    if (data.size() > 0) {
//                        Log.d(TAG, "data.size : " + data.size());

                            post_Num = data.get(0).getNum();
                            post_Nickname = data.get(0).getPostNickname();
                            post_ProfileImage = data.get(0).getProfileImage();
                            post_Heart = data.get(0).getHeart();
                            post_CommentNum = data.get(0).getCommentNum();
                            post_Location = data.get(0).getLocation();
                            post_PostImage = data.get(0).getPostImage();
                            post_Writing = data.get(0).getWriting();
                            post_DateCreated = data.get(0).getDateCreated();
                            post_WhoLike = data.get(0).getWhoLike();
                            post_HeartStatus = false;

                            if (currentNickname.equals(post_WhoLike)) {
                                post_HeartStatus = true;
                            }

                            String[] arrayLocation = post_Location.split(" ");
                            double latitude = Double.parseDouble(arrayLocation[0]);
                            double longitude = Double.parseDouble(arrayLocation[1]);

                            String currentLocation = getAddress.getAddress(getApplicationContext(),latitude,longitude);

                            post_EditDate = getTime.lastTime(post_DateCreated);
                            post_EditLocation = getAddress.editAddress1234(currentLocation);

                        post_Nickname_Text.setText(post_Nickname);
                        post_Location_Text.setText(post_EditLocation);
                        post_HeartNum_Text.setText(String.valueOf(post_Heart));
                        post_CommentNum_Text.setText(String.valueOf(post_CommentNum));
                        post_DateCreated_Text.setText(post_EditDate);
                        post_Writing_Text.setText(post_Writing);

                        if (!accessNickname.equals(post_Nickname)) {
                            post_Menu_Btn.setVisibility(View.GONE);
                        } else {
                            post_Menu_Btn.setVisibility(View.VISIBLE);
                        }

                        if (post_HeartStatus) {
                            post_Heart_Iv.setVisibility(View.GONE);
                            post_HeartFull_Iv.setVisibility(View.VISIBLE);
                        }

                        Glide.with(getApplicationContext())
                                .load(post_ProfileImage)
                                .into(post_ProfileImage_Iv);

                        Glide.with(getApplicationContext())
                                .load(post_PostImage)
                                .into(post_PostImage_Iv);

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




}