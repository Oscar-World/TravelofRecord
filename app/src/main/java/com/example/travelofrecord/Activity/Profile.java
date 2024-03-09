package com.example.travelofrecord.Activity;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.travelofrecord.Data.Markers;
import com.example.travelofrecord.EventBus.PostDeleteEventBusHome;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Function.GetAddress;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Adapter.Profile_Adapter;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ted.gun0912.clustering.clustering.Cluster;
import ted.gun0912.clustering.clustering.TedClusterItem;
import ted.gun0912.clustering.naver.TedNaverClustering;

public class Profile extends AppCompatActivity implements OnMapReadyCallback {

    String TAG = "프로필 액티비티";
    String getNickname, user_Nickname, user_ImagePath, user_Memo, post_Location, post_PostImage, post_Writing, post_DateCreated;
    double latitude, longitude, currentLatitude, currentLongitude;
    int networkStatus, post_Num, putNum;
    int[] clusterBucket = {10, 20, 50, 100, 200, 500, 1000};
    GetAddress getAddress = new GetAddress();
    GetTime getTime = new GetTime();
    MapView mapView;
    NestedScrollView profileScrollView;
    TextView profileNicknameText, profileMemoText, mapDrawerText, checkPositionText;
    Button profileInfoBtn, profileInfoBlock, profileMapBtn, profileMapBlock;
    ImageView profileImage, loading_Iv, mapDrawerImage;
    ImageButton profileDmBtn, profileBackBtn;
    FrameLayout loadingFrame, mapDrawer;
    LinearLayout mapDrawerDown;
    ApiInterface apiInterface;
    RecyclerView profileRecyclerView;
    Profile_Adapter adapter;
    ArrayList<PostData> postData_ArrayList, data;
    NaverMap naverMap;
    ArrayList<Markers> markerList;
    Markers markers;
    Marker marker;
    Animation appear, disappear, rotate;
    EventBus eventBusPostDeleteProfile;
    PostDeleteEventBusHome postDeleteEventBusProfile;

    /*
    권한 상수 초기화
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() 호출됨");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mapView = findViewById(R.id.profile_MapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        setVariable();
        setView();
        checkUser(getNickname);

    } // onCreate()


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() 호출됨");
        mapView.onStart();

        if (!checkPositionText.getText().toString().equals("")) {
            Log.d(TAG, "checkPosition 들어옴 : " + checkPositionText.getText().toString());
            postData_ArrayList.remove(Integer.parseInt(checkPositionText.getText().toString()));
            adapter.notifyItemRemoved(Integer.parseInt(checkPositionText.getText().toString()));

            checkPositionText.setText("");

        }

        if (eventBusPostDeleteProfile.isRegistered(postDeleteEventBusProfile)) {
            eventBusPostDeleteProfile.unregister(postDeleteEventBusProfile);
        }

    } // onStart()


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() 호출됨");
        mapView.onResume();
    } // onResume()


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() 호출됨");
        mapView.onPause();
    } // onPause()


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() 호출됨");
        mapView.onSaveInstanceState(outState);
    } // onSaveInstanceState()


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() 호출됨");
        mapView.onStop();

        if (!eventBusPostDeleteProfile.isRegistered(postDeleteEventBusProfile)) {
            eventBusPostDeleteProfile.register(postDeleteEventBusProfile);
        }

    } // onStop()


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() 호출됨");
    } // onRestart()


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() 호출됨");

        mapView.onDestroy();

        if (eventBusPostDeleteProfile.isRegistered(postDeleteEventBusProfile)) {
            eventBusPostDeleteProfile.unregister(postDeleteEventBusProfile);
        }

    } // onDestroy()


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory() 호출됨");
        mapView.onLowMemory();
    } // onLowMemory()


    // ---------------------------------------------------------------------------------------------------


    /*
    변수 초기화
     */
    public void setVariable() {

        loadingFrame = findViewById(R.id.profileLoading_Frame);
        loading_Iv = findViewById(R.id.profile_Loading);
        rotate = AnimationUtils.loadAnimation(this, R.anim.loading);

        networkStatus = NetworkStatus.getConnectivityStatus(this);

        profileScrollView = findViewById(R.id.profile_ScrollView);
        profileNicknameText = findViewById(R.id.profile_nickname);
        profileMemoText = findViewById(R.id.profile_memo);
        profileInfoBtn = findViewById(R.id.profileInfo_Btn);
        profileInfoBlock = findViewById(R.id.profileInfo_Block);
        profileMapBtn = findViewById(R.id.profileMap_Btn);
        profileMapBlock = findViewById(R.id.profileMap_Block);
        profileDmBtn = findViewById(R.id.profileDM_Btn);
        profileImage = findViewById(R.id.profile_Image);
        profileBackBtn = findViewById(R.id.profileBack_Btn);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Intent intent = getIntent();
        getNickname = intent.getStringExtra("nickname");

        profileRecyclerView = findViewById(R.id.profile_RecyclerView);
        adapter = new Profile_Adapter();
        profileRecyclerView.setAdapter(adapter);
        profileRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        postData_ArrayList = new ArrayList<>();
        adapter.setItemProfile(postData_ArrayList);

        markerList = new ArrayList<>();

        mapDrawer = findViewById(R.id.profile_mapDrawer);
        mapDrawerDown = findViewById(R.id.profile_MapDrawerDropDown);
        mapDrawerImage = findViewById(R.id.profile_MapDrawerImage);
        mapDrawerText = findViewById(R.id.profile_MapDrawerText);
        appear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mapdrawer_appear);
        disappear = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mapdrawer_disappear);
        checkPositionText = findViewById(R.id.profileCheckPosition_Text);

        eventBusPostDeleteProfile = EventBus.getDefault();
        postDeleteEventBusProfile = new PostDeleteEventBusHome(postData_ArrayList, checkPositionText);

    } // setVariable()


    /*
    뷰 초기화
     */
    public void setView() {

        loadingFrame.setVisibility(View.VISIBLE);
        loading_Iv.startAnimation(rotate);

        profileBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profileInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    profileInfoBtn.setVisibility(View.GONE);
                    profileInfoBlock.setVisibility(View.VISIBLE);
                    profileMapBtn.setVisibility(View.VISIBLE);
                    profileMapBlock.setVisibility(View.GONE);

                    mapView.setVisibility(View.GONE);
                    profileScrollView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        profileMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    profileMapBtn.setVisibility(View.GONE);
                    profileMapBlock.setVisibility(View.VISIBLE);
                    profileInfoBtn.setVisibility(View.VISIBLE);
                    profileInfoBlock.setVisibility(View.GONE);

                    profileScrollView.setVisibility(View.GONE);
                    loadingFrame.setVisibility(View.GONE);
                    mapView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    Intent i = new Intent(Profile.this, PhotoView.class);
                    i.putExtra("image", user_ImagePath);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        profileDmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    Intent i = new Intent(Profile.this, DirectMessage.class);
                    i.putExtra("postNickname", user_Nickname);
                    startActivity(i);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        int LOCATION_PERMISSION = ActivityCompat.checkSelfPermission(Profile.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int COARSE_PERMISSION = ActivityCompat.checkSelfPermission(Profile.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (LOCATION_PERMISSION != PackageManager.PERMISSION_GRANTED && COARSE_PERMISSION != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "위치 권한 없음");

            ActivityCompat.requestPermissions(
                    Profile.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return;
        } else {
            Log.d(TAG, "위치 권한 있음");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(Profile.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    Log.d(TAG, "onSuccess: location - " + location + "\n위도 - " + location.getLatitude() + "\n경도 - " + location.getLongitude());
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();

                } else {
                    Log.d(TAG, "location == null");
                }
            }
        });

        mapDrawerDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mapDrawer.startAnimation(disappear);
                mapDrawer.setVisibility(View.GONE);
                return true;
            }
        });

//        mapDrawerDown.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        mapDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

    } // setView()



    // ---------------------------------------------------------------------------------------------


    /*
    네이버 지도 세팅 완료 시
     */
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Log.d(TAG, "onMapReady() 호출");

        this.naverMap = naverMap;
        naverMap.setExtent(new LatLngBounds(new LatLng(31.43, 122.37), new LatLng(44.35, 132)));

        naverMap.setMaxZoom(17);
        naverMap.setMinZoom(5);

        CameraPosition cameraPosition;

        if (latitude == 0.0) {
            cameraPosition = new CameraPosition(new LatLng(currentLatitude, currentLongitude), 6);
            naverMap.setCameraPosition(cameraPosition);
        } else {
            cameraPosition = new CameraPosition(new LatLng(latitude, longitude), 4);
            naverMap.setCameraPosition(cameraPosition);
        }

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setScaleBarEnabled(false);
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setLogoClickEnabled(false);

        addMarker();

        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mapDrawer.getVisibility() == View.VISIBLE) {
                    mapDrawer.startAnimation(disappear);
                    mapDrawer.setVisibility(View.GONE);
                }

                return false;
            }
        });

    } // onMapReady()


    /*
    마커 추가 작업
     */
    public void addMarker() {

        if (data.size() > 0) {
            for (int i=0; i < data.size(); i++) {
                Log.d(TAG, "datasize() : " + data.size());
                String location = data.get(i).getLocation();

                post_Location = data.get(i).getLocation();
                post_PostImage = data.get(i).getPostImage();
                post_Writing = data.get(i).getWriting();
                post_DateCreated = data.get(i).getDateCreated();
                post_Num = data.get(i).getPostNum();

                String[] arrayLocation = location.split(" ");
                double latitude = Double.parseDouble(arrayLocation[0]);
                double longitude = Double.parseDouble(arrayLocation[1]);

                markers = new Markers(latitude, longitude, post_Location, post_PostImage, post_Writing, post_DateCreated, post_Num);
                markerList.add(markers);

            }

            TedNaverClustering.with(getApplicationContext(), naverMap)
                    .items(markerList)
                    .customMarker(new Function1<TedClusterItem, Marker>() {
                        @Override
                        public Marker invoke(TedClusterItem tedClusterItem) {
                            marker = new Marker();
                            marker.setWidth(75);
                            marker.setHeight(100);

                            return marker;
                        }
                    })
                    .markerClickListener(new Function1<TedClusterItem, Unit>() {
                        @Override
                        public Unit invoke(TedClusterItem tedClusterItem) {

                            String image = "";
                            String text = "";
                            String tedLat = String.valueOf(tedClusterItem.getTedLatLng().getLatitude());
                            String tedLng = String.valueOf(tedClusterItem.getTedLatLng().getLongitude());

                            if (String.valueOf(tedClusterItem.getTedLatLng().getLatitude()).length() == 8) {
                                tedLat = tedClusterItem.getTedLatLng().getLatitude() + "0";
                            }

                            if (String.valueOf(tedClusterItem.getTedLatLng().getLongitude()).length() == 9) {
                                tedLng = tedClusterItem.getTedLatLng().getLongitude() + "0";
                            }

                            String tedLocation = tedLat + " " + tedLng;

                            for (int i = 0; i < markerList.size(); i++) {

                                if (markerList.get(i).getLocation().equals(tedLocation)) {
                                    image = markerList.get(i).getPostImage();
                                    text = markerList.get(i).getWriting();
                                    putNum = markerList.get(i).getNum();

                                    break;
                                }

                            }

                            Glide.with(getApplicationContext())
                                    .load(ApiClient.serverPostImagePath + image)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .placeholder(R.drawable.loading2)
                                    .into(mapDrawerImage);

                            mapDrawerText.setText(text);

                            mapDrawer.setVisibility(View.VISIBLE);
                            mapDrawer.startAnimation(appear);

                            return null;
                        }
                    })
                    .clusterClickListener(new Function1<Cluster<TedClusterItem>, Unit>() {
                        @Override
                        public Unit invoke(Cluster<TedClusterItem> tedClusterItemCluster) {
                            CameraPosition cameraPosition = new CameraPosition(new LatLng(tedClusterItemCluster.getPosition().getLatitude(),
                                    tedClusterItemCluster.getPosition().getLongitude()), 9);

                            CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition).animate(CameraAnimation.Easing,1500);
                            naverMap.moveCamera(cameraUpdate);

                            Log.d(TAG, "clusterClick : " + tedClusterItemCluster.getItems());

                            return null;
                        }
                    })
                    .minClusterSize(2)
                    .clusterBuckets(clusterBucket)
                    .make();

        }

    } // addMarker()


    /*
    탈퇴한 사용자 처리
     */
    public void noDataDlg() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("존재하지 않는 사용자입니다.")
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


    /*
    프로필 불러오기
     */
    public void getProfile(String nickname) {

        Call<ArrayList<PostData>> call = apiInterface.getProfile(nickname);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "getProfile onResponse");

                    loadingFrame.setVisibility(View.GONE);
                    profileScrollView.setVisibility(View.VISIBLE);
                    loading_Iv.clearAnimation();

                    data = response.body();
                    Log.d(TAG, "onResponse: " + data + " " + data.toString());

                    for (int i = 0; i < data.size(); i++) {

                        user_Nickname = data.get(i).getNickname();
                        user_ImagePath = data.get(i).getImagePath();
                        user_Memo = data.get(i).getMemo();
                        int num = data.get(i).getNum();
                        String postNickname = data.get(i).getPostNickname();
                        String profileImage = data.get(i).getProfileImage();
                        int heart = data.get(i).getHeart();
                        int commentNum = data.get(i).getCommentNum();
                        String location = data.get(i).getLocation();
                        String postImage = data.get(i).getPostImage();
                        String writing = data.get(i).getWriting();
                        String dateCreated = data.get(i).getDateCreated();

                        String[] arrayLocation = location.split(" ");
                        latitude = Double.parseDouble(arrayLocation[0]);
                        longitude = Double.parseDouble(arrayLocation[1]);

                        String currentLocation = getAddress.getAddress(getApplicationContext(),latitude,longitude);
                        String addressPost = getAddress.editAddress1234(currentLocation);

                        String datePost = getTime.lastTime(dateCreated);

                        PostData postData = new PostData(user_Nickname, user_ImagePath, user_Memo, num, postNickname, profileImage, heart, commentNum, addressPost, postImage, writing, datePost);

                        postData_ArrayList.add(0, postData);

                    }
                    Log.d(TAG, "nickname : " + user_Nickname + " memo : " + user_Memo);
                    profileNicknameText.setText(user_Nickname);
                    profileMemoText.setText(user_Memo);

                    DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

                    Glide.with(getApplicationContext())
                            .load(ApiClient.serverProfileImagePath + user_ImagePath)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(profileImage);

                    adapter.notifyDataSetChanged();

                } else {
                    Log.d(TAG, "responseFail");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PostData>> call, Throwable t) {
                Log.d(TAG, "getProfile onFailure");
            }
        });

    } // getProfile()


    /*
    유효한 사용자 체크
     */
    public void checkUser(String nickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.checkUser(nickname);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "checkUser - onResponse : ok");

                    String rpCode = response.body();

                    if (rpCode.equals("ok")) {

                        getProfile(getNickname);

                    } else {

                        noDataDlg();

                    }

                } else {
                    Log.d(TAG, "checkUser - onResponse : fail");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "checkUser - onFailure");
            }
        });

    } // checkUser()


}