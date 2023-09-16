package com.example.travelofrecord.Activity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.travelofrecord.EventBus.CommentAddEventBus;
import com.example.travelofrecord.EventBus.CommentDeleteEventBus;
import com.example.travelofrecord.EventBus.CommentNumAddEventBus;
import com.example.travelofrecord.EventBus.CommentNumDeleteEventBus;
import com.example.travelofrecord.EventBus.HeartEventBus;
import com.example.travelofrecord.EventBus.PostDeleteEventBusPost;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Adapter.Comment_Adapter;
import com.example.travelofrecord.Function.GetAdress;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.R;
import com.google.android.gms.common.api.Api;

import org.greenrobot.eventbus.EventBus;

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
    ImageButton post_Heart_Iv;
    ImageButton post_HeartFull_Iv;
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
    LinearLayout comment_Layout;

    ApiInterface apiInterface;
    LinearLayout post_TopLayout;

    int networkStatus;

    String[] heartEventArray;
    String[] commentNumArray;

    HeartEventBus heartEventBus;
    CommentNumAddEventBus commentNumAddEventBus;
    CommentAddEventBus commentAddEventBus;
    CommentNumDeleteEventBus commentNumDeleteEventBus;
    PostDeleteEventBusPost postDeleteEventBusPost;

    EventBus eventBusHeart;
    EventBus eventBusCommentNum;
    EventBus eventBusCommentAdd;
    EventBus eventBusCommentNumDelete;
    EventBus eventBusPostDeletePost;

    boolean eventBusAddStatus = false;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Log.d(TAG, "onStart() 호출됨");
        context = this;

        setVariable();
        setView();
        getPost(accessNickname, post_Num);
        getComment(post_Num);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() 호출됨");

        Log.d(TAG, "게시글삭제 확인됨2 : " + post_Menu_Btn.getVisibility());

        if (post_Menu_Btn.getVisibility() == View.GONE) {
            Log.d(TAG, "게시글삭제 확인됨3 : " + post_Menu_Btn.getVisibility());
            checkPostDelete(post_Num);
        }

        if (eventBusHeart.isRegistered(heartEventBus)) {
            eventBusHeart.unregister(heartEventBus);
        }
        if (eventBusCommentNum.isRegistered(commentNumAddEventBus)) {
            eventBusCommentNum.unregister(commentNumAddEventBus);
        }
        if (eventBusCommentAdd.isRegistered(commentAddEventBus)) {
            eventBusCommentAdd.unregister(commentAddEventBus);
        }
        if (eventBusCommentNumDelete.isRegistered(commentNumDeleteEventBus)) {
            eventBusCommentNumDelete.unregister(commentNumDeleteEventBus);
        }
        if (eventBusPostDeletePost.isRegistered(postDeleteEventBusPost)) {
            eventBusPostDeletePost.unregister(postDeleteEventBusPost);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() 호출됨");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() 호출됨");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() 호출됨");

        if (!eventBusHeart.isRegistered(heartEventBus)) {
            eventBusHeart.register(heartEventBus);
        }
        if (!eventBusCommentNum.isRegistered(commentNumAddEventBus)) {
            eventBusCommentNum.register(commentNumAddEventBus);
        }
        if (!eventBusCommentAdd.isRegistered(commentAddEventBus)) {
            eventBusCommentAdd.register(commentAddEventBus);
        }
        if (!eventBusCommentNumDelete.isRegistered(commentNumDeleteEventBus)) {
            eventBusCommentNumDelete.register(commentNumDeleteEventBus);
        }
        if (!eventBusPostDeletePost.isRegistered(postDeleteEventBusPost)) {
            eventBusPostDeletePost.register(postDeleteEventBusPost);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() 호출됨");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() 호출됨");


        if (eventBusHeart.isRegistered(heartEventBus)) {
            eventBusHeart.unregister(heartEventBus);
        }
        if (eventBusCommentNum.isRegistered(commentNumAddEventBus)) {
            eventBusCommentNum.unregister(commentNumAddEventBus);
        }
        if (eventBusCommentAdd.isRegistered(commentAddEventBus)) {
            eventBusCommentAdd.unregister(commentAddEventBus);
        }
        if (eventBusCommentNumDelete.isRegistered(commentNumDeleteEventBus)) {
            eventBusCommentNumDelete.unregister(commentNumDeleteEventBus);
        }
        if (eventBusPostDeletePost.isRegistered(postDeleteEventBusPost)) {
            eventBusPostDeletePost.unregister(postDeleteEventBusPost);
        }

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

        Log.d(TAG, "commentNumText1 : " + post_CommentNum_Text);
        Intent i = getIntent();

        post_Num = i.getIntExtra("num", 0);
        getPosition = i.getIntExtra("position", 0);

        home = new Home();

        sharedPreferences = getSharedPreferences("로그인 정보", Context.MODE_PRIVATE);
        accessNickname = sharedPreferences.getString("nickname", "");
        accessProfileImage = sharedPreferences.getString("image", "");

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        comment_Layout = findViewById(R.id.post_commentLayout);
        scrollView = findViewById(R.id.post_ScrollView);
        recyclerView = findViewById(R.id.comment_recyclerView);
        adapter = new Comment_Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postDataArrayList = new ArrayList<>();
        adapter.setItemComment(postDataArrayList, post_CommentNum_Text);

        post_TopLayout = findViewById(R.id.post_TopLayout);

        // 좋아요 이미지 변경 & 좋아요 개수 변경
        eventBusHeart = EventBus.getDefault();
        heartEventBus = new HeartEventBus(post_HeartNum_Text, post_Heart_Iv, post_HeartFull_Iv, post_Num);

        // 댓글 개수 변경
        eventBusCommentNum = EventBus.getDefault();
        commentNumAddEventBus = new CommentNumAddEventBus(post_CommentNum_Text, post_Num);

        // 댓글 추가 적용
        eventBusCommentAdd = EventBus.getDefault();
        commentAddEventBus = new CommentAddEventBus(postDataArrayList, adapter, recyclerView, post_Num);

        // 댓글 삭제 적용
        eventBusCommentNumDelete = EventBus.getDefault();
        commentNumDeleteEventBus = new CommentNumDeleteEventBus(post_CommentNum_Text, post_Num);

        // 게시글 삭제 적용 - Post Activity
        eventBusPostDeletePost = EventBus.getDefault();
        postDeleteEventBusPost = new PostDeleteEventBusPost(post_Num, post_Menu_Btn);

        // 게시글 삭제 적용 - Home Fragment


        // 게시글 삭제 적용 - Heart Fragment


    } // setVariable()

    public void setView() {

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

                if (networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    Intent i = new Intent(getApplicationContext(), Profile.class);
                    i.putExtra("nickname", post_Nickname);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        post_Nickname_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    Intent i = new Intent(getApplicationContext(), Profile.class);
                    i.putExtra("nickname", post_Nickname);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        post_CommentAdd_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    addComment = post_Comment_Edit.getText().toString();
                    addDateComment = getTime.getTime().toString();

                    if (!addComment.equals("")) {

                        post_CommentNum += 1;
                        post_CommentNum_Text.setText(String.valueOf(post_CommentNum));

                        addComment(post_Num, accessProfileImage, accessNickname, addDateComment, addComment, post_CommentNum);

                        post_Comment_Edit.setText("");

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

        post_PostImage_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    Intent i = new Intent(Post.this, PhotoView.class);
                    i.putExtra("image", ApiClient.serverPostImagePath + post_PostImage);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        post_Menu_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(Post.this, view);
                popup.getMenuInflater().inflate(R.menu.post_delete, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.menu_PostDelete) {

                            deleteDlg();

                        }

                        return false;
                    }
                });

            }
        });

        setHeartOnClick();
        setHeartFullOnClick();


    } // setView()


    public void setHeartOnClick() {

        post_Heart_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                post_HeartFull_Iv.setClickable(false);

                if (networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    post_Heart_Iv.setVisibility(View.GONE);
                    post_HeartFull_Iv.setVisibility(View.VISIBLE);
                    post_Heart += 1;
                    post_HeartNum_Text.setText(String.valueOf(post_Heart));


                    insertWhoLike(post_Num, accessNickname, post_Heart, String.valueOf(System.currentTimeMillis()));

                    heartEventArray = new String[3];
                    heartEventArray[0] = String.valueOf(post_Heart);
                    heartEventArray[1] = "true";
                    heartEventArray[2] = String.valueOf(post_Num);

                    eventBusHeart.post(heartEventArray);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    } // setHeartOnClick()

    public void setHeartFullOnClick() {

        post_HeartFull_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                post_Heart_Iv.setClickable(false);

                if (networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    post_HeartFull_Iv.setVisibility(View.GONE);
                    post_Heart_Iv.setVisibility(View.VISIBLE);
                    post_Heart -= 1;
                    post_HeartNum_Text.setText(String.valueOf(post_Heart));

                    deleteWhoLike(post_Num, accessNickname, post_Heart);

                    heartEventArray = new String[3];
                    heartEventArray[0] = String.valueOf(post_Heart);
                    heartEventArray[1] = "false";
                    heartEventArray[2] = String.valueOf(post_Num);

                    eventBusHeart.post(heartEventArray);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    } // setHeartFullOnClick()


    public void deleteDlg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);
        builder.setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int[] array = new int[1];
                        array[0] = post_Num;
                        eventBusPostDeletePost.post(array);
                        deletePost(post_Num);

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setCancelable(false)
                .create()
                .show();

    }


    public void noDataDlg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);
        builder.setTitle("삭제된 게시물입니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();

    } // noDataDlg()


    // -------------------------------------------------------------------------------------------


    public void insertWhoLike(int postNum, String whoLike, int heart, String time) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.insertWhoLike(postNum, whoLike, heart, time);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "scar3insertWhoLike_Response 성공");

                    String rpCode = response.body();

                    if (rpCode.equals("noData")) {

                        noDataDlg();

                    } else {
                        post_HeartFull_Iv.setClickable(true);
                        Log.d(TAG, "onResponse : 좋아요 추가 완료");
                    }

                } else {
                    Log.d(TAG, "insertWhoLike_Response 실패");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: 실패 " + t);
            }
        });

    } // insertWhoLike

    public void deleteWhoLike(int postNum, String whoLike, int heart) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.deleteWhoLike(postNum, whoLike, heart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "scar3deleteWhoLike_Response 성공");

                    String rpCode = response.body();

                    if (rpCode.equals("noData")) {

                        noDataDlg();

                    } else {
                        post_Heart_Iv.setClickable(true);
                        Log.d(TAG, "onResponse : 좋아요 삭제 완료");
                    }

                } else {
                    Log.d(TAG, "deleteWhoLike_Response 실패");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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

                    if (response.body().equals("noData")) {

                        noDataDlg();

                    } else {

                        commentNumArray = new String[2];
                        commentNumArray[0] = String.valueOf(post_CommentNum);
                        commentNumArray[1] = String.valueOf(post_Num);

                        eventBusCommentNum.post(commentNumArray);
                        eventBusAddStatus = true;

                        postDataArrayList.clear();
                        getComment(post_Num);

                    }

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

                    if (data.size() > 0) {

                        PostData postData = null;

                        for (int i = 0; i < data.size(); i++) {

                            String profileImage = data.get(i).getCommentProfileImage();
                            String whoComment = data.get(i).getWhoComment();
                            String dateComment = data.get(i).getDateComment();
                            String comment = data.get(i).getComment();
                            int commentNumber = data.get(i).getCommentNumber();
                            Log.d(TAG, "commentNumber : " + commentNumber);

                            postData = new PostData(commentNumber, profileImage, whoComment, dateComment, comment, post_Num, post_CommentNum);

                            postDataArrayList.add(postData);

                        }

                        listSize = postDataArrayList.size();
                        adapter.notifyDataSetChanged();

                        if (eventBusAddStatus) {
                            eventBusCommentAdd.post(postData);
                            eventBusAddStatus = false;
                            recyclerView.scrollToPosition(postDataArrayList.size() - 1);
                        }

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

    public void checkPostDelete(int postNum) {

        Log.d(TAG, "게시글삭제 확인됨4 : " + post_Menu_Btn.getVisibility());

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.checkPostDelete(postNum);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "checkPostDelete - onResponse : isSuccessful");

                    String rpCode = response.body();

                    if (rpCode.equals("fail")) {
                        noDataDlg();
                    }

                } else {
                    Log.d(TAG, "checkPostDelete - onResponse : isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "checkPostDelete - onFailure");
            }
        });

    } // checkPostDelete()

    public void getPost(String currentNickname, int postNum) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getPost(currentNickname, postNum);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "getPost - onResponse : isSuccessful");
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

                        String currentLocation = getAddress.getAddress(getApplicationContext(), latitude, longitude);

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

                        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

                        Glide.with(getApplicationContext())
                                .load(ApiClient.serverProfileImagePath + post_ProfileImage)
//                                .transition(withCrossFade(factory))
//                                .placeholder(R.drawable.loading2)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(post_ProfileImage_Iv);

                        Glide.with(getApplicationContext())
                                .load(ApiClient.serverPostImagePath + post_PostImage)
                                .transition(withCrossFade(factory))
                                .placeholder(R.drawable.loading2)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(post_PostImage_Iv);

                    } else {
                        scrollView.setVisibility(View.GONE);
                        comment_Layout.setVisibility(View.GONE);
                        noDataDlg();
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

    public void deletePost(int postNum) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.deletePost(postNum);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "deletePost - onResponse : isSuccessful");
                    Log.d(TAG, "onResponse : " + response.body());
                    Toast.makeText(Post.this, "게시글 삭제 완료", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.d(TAG, "deletePost - onResponse : isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "deletePost - onFailure");
            }
        });

    }


}