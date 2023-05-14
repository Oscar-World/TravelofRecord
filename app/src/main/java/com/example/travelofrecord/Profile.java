package com.example.travelofrecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements OnMapReadyCallback {

    String TAG = "프로필 액티비티";
    GetAdress getAddress = new GetAdress();
    GetTime getTime = new GetTime();

    MapView mapView;
    ScrollView profileScrollView;
    TextView profileNicknameText;
    TextView profileMemoText;
    Button profileInfoBtn;
    Button profileInfoBlock;
    Button profileMapBtn;
    Button profileMapBlock;
    ImageView profileImage;
    ImageButton profileDmBtn;
    ImageButton profileBackBtn;

    String getNickname;
    ApiInterface apiInterface;

    RecyclerView profileRecyclerView;
    Profile_Adapter adapter;
    ArrayList<PostData> postData_ArrayList;
    ArrayList<PostData> data;

    String user_Nickname;
    String user_ImagePath;
    String user_Memo;

    NaverMap naverMap;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() 호출됨");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mapView = findViewById(R.id.profile_MapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출됨");
        mapView.onStart();

        setVariable();
        setView();
        getProfile(getNickname);
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() 호출됨");
        mapView.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() 호출됨");
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() 호출됨");
        mapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() 호출됨");
        mapView.onStop();
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
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory() 호출됨");
        mapView.onLowMemory();
    }


    // ---------------------------------------------------------------------------------------------------


    public void setVariable() {

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

    } // setVariable()


    public void setView() {

        profileBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profileInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profileInfoBtn.setVisibility(View.GONE);
                profileInfoBlock.setVisibility(View.VISIBLE);
                profileMapBtn.setVisibility(View.VISIBLE);
                profileMapBlock.setVisibility(View.GONE);

                mapView.setVisibility(View.GONE);
                profileScrollView.setVisibility(View.VISIBLE);

            }
        });

        profileMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profileMapBtn.setVisibility(View.GONE);
                profileMapBlock.setVisibility(View.VISIBLE);
                profileInfoBtn.setVisibility(View.VISIBLE);
                profileInfoBlock.setVisibility(View.GONE);

                profileScrollView.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);

            }
        });

    } // setView()


    // ---------------------------------------------------------------------------------------------


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Log.d(TAG, "onMapReady() 호출");

        this.naverMap = naverMap;
        naverMap.setExtent(new LatLngBounds(new LatLng(31.43, 122.37), new LatLng(44.35, 132)));

        naverMap.setMaxZoom(17);
        naverMap.setMinZoom(5);

        Log.d(TAG, "onMapReady: " + latitude + " " + longitude);
        CameraPosition cameraPosition = new CameraPosition(new LatLng(latitude, longitude), 9);
        naverMap.setCameraPosition(cameraPosition);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setScaleBarEnabled(false);
        uiSettings.setZoomControlEnabled(false);
        uiSettings.setLogoClickEnabled(false);

        addMarker();

    }


    public void addMarker() {

        if (data.size() > 0) {
            for (int i=0; i < data.size(); i++) {
                Log.d(TAG, "datasize() : " + data.size());
                String location = data.get(i).getLocation();

                String[] arrayLocation = location.split(" ");
                double latitude = Double.parseDouble(arrayLocation[0]);
                double longitude = Double.parseDouble(arrayLocation[1]);

                String currentLocation = getAddress.getAddress(this,latitude,longitude);
                String shortLocation = getAddress.editAddress24(currentLocation);

                setMarker(latitude, longitude, shortLocation);
            }
        }

    } // addMarker()


    public void setMarker(double lat, double lng, String addressHeart) {

        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat,lng));
        marker.setTag(addressHeart);

        marker.setMap(naverMap);

        setInfoWindow(marker);

    } // setMarker()


    public void setInfoWindow(Marker marker) {

        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return marker.getTag().toString();
            }
        });

        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                if (marker.getInfoWindow() == null) {
                    // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                    infoWindow.open(marker);
                } else {
                    // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                    infoWindow.close();
                }

                CameraPosition cameraPosition = new CameraPosition(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude), 12);
                CameraUpdate cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition).animate(CameraAnimation.Easing,2000);
                naverMap.moveCamera(cameraUpdate);

                return true;
            }
        });
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {

            }
        });

    } // setInfoWindow()


    public void getProfile(String nickname) {

        Call<ArrayList<PostData>> call = apiInterface.getProfile(nickname);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "getProfile onResponse");

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

                    Glide.with(getApplicationContext())
                            .load(user_ImagePath)
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

}