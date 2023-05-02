package com.example.travelofrecord;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Icon;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ted.gun0912.clustering.clustering.TedClusterItem;
import ted.gun0912.clustering.geometry.TedLatLng;
import ted.gun0912.clustering.naver.TedNaverClustering;

public class Fragment_Heart extends Fragment implements OnMapReadyCallback {

    String TAG = "하트 프래그먼트";

    View v;

    private Button photo_Btn;
    private Button map_Btn;
    private Button photo_Block;
    private Button map_Block;

    RecyclerView recyclerView;
    ArrayList<PostData> post_Data_ArrayList;
    Heart_Adapter adapter;
    ArrayList<PostData> data;

    int itemSize;

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
    boolean heartStatus;

    String nowAddr;
    String addressHeart;

    double latitude;
    double longitude;

    SharedPreferences sharedPreferences;
    String nickname;

    MapView mapView;
    NaverMap naverMap;
//    InfoWindow infoWindow;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() 호출");
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() 호출");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() 호출");
        v = inflater.inflate(R.layout.fragment_heart, container, false);
        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated() 호출");
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }
    @Override
    public void onStart() {
        Log.d(TAG, "onStart() 호출");
        super.onStart();
        mapView.onStart();

        setVariable();
        setView();
        getHeart(nickname);

    }
    @Override public void onResume() {
        Log.d(TAG, "onResume() 호출");
        super.onResume();
        mapView.onResume();
    }
    @Override public void onPause() {
        Log.d(TAG, "onPause() 호출");
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override public void onStop() {
        Log.d(TAG, "onStop() 호출");
        super.onStop();
        mapView.onStop();
    }
    @Override public void onDestroyView() {
        Log.d(TAG, "onDestroyView() 호출");
        super.onDestroyView();
        mapView.onDestroy();
    }
    @Override public void onDetach() {
        Log.d(TAG, "onDetach() 호출");
        super.onDetach();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    // ----------------------------------------------------------------------------------------------------------


    public void setVariable() {

        photo_Btn = v.findViewById(R.id.heartPhoto_Btn);
        map_Btn = v.findViewById(R.id.heartMap_Btn);
        photo_Block = v.findViewById(R.id.heartPhoto_Block);
        map_Block = v.findViewById(R.id.heartMap_Block);

        recyclerView = v.findViewById(R.id.heart_RecyclerView);
        adapter = new Heart_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        post_Data_ArrayList = new ArrayList<>();

        adapter.setItemHeart(post_Data_ArrayList);

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보",Context.MODE_PRIVATE);
        nickname = sharedPreferences.getString("nickname","");

    }

    public void setView() {

        photo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photo_Btn.setVisibility(View.GONE);
                photo_Block.setVisibility(View.VISIBLE);
                map_Btn.setVisibility(View.VISIBLE);
                map_Block.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.GONE);
            }
        });

        map_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                map_Btn.setVisibility(View.GONE);
                map_Block.setVisibility(View.VISIBLE);
                photo_Btn.setVisibility(View.VISIBLE);
                photo_Block.setVisibility(View.GONE);

                recyclerView.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);

            }
        });

    } //setView()


    // -------------------------------------------------------------------------------------------------------


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Log.d(TAG, "onMapReady() 호출");

        this.naverMap = naverMap;

        naverMap.setMaxZoom(17);
        naverMap.setMinZoom(5);

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

                post_Location = data.get(i).getLocation();

                String[] arrayLocation = post_Location.split(" ");
                latitude = Double.parseDouble(arrayLocation[0]);
                longitude = Double.parseDouble(arrayLocation[1]);

                String currentLocation = getAddress(getContext(),latitude,longitude);
                addressHeart = editAddress2(currentLocation);

                setMarker(latitude, longitude, i, addressHeart);
            }
        }

    }

    public void setMarker(double lat, double lng, int i, String addressHeart) {

        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat,lng));
        marker.setTag(addressHeart);
//        marker.setCaptionText(addressHeart);
//        marker.setCaptionAligns(Align.Top);
//        marker.setCaptionMinZoom(11);
//        marker.setCaptionOffset(10);

        marker.setMap(naverMap);

        setInfoWindow(marker);

    }

    public void setInfoWindow(Marker marker) {

        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getActivity()) {
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
                return true;
            }
        });

    }


    public void getHeart(String nickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getHeart(nickname);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse 호출");
                    data = response.body();

                    Log.d(TAG, "data.size : " + data.size());

                    if (data.size() > 0) {

                        for (int i = 0; i < data.size(); i++) {

                            post_Num = data.get(i).getNum();
                            post_Nickname = data.get(i).getNickname();
                            post_ProfileImage = data.get(i).getProfileImage();
                            post_Heart = data.get(i).getHeart();
                            post_CommentNum = data.get(i).getCommentNum();
                            post_Location = data.get(i).getLocation();
                            post_PostImage = data.get(i).getPostImage();
                            post_Writing = data.get(i).getWriting();
                            post_DateCreated = data.get(i).getDateCreated();
                            post_WhoLike = data.get(i).getWhoLike();

                            heartStatus = false;

                            if (nickname.equals(post_WhoLike)) {
                                heartStatus = true;
                            }

                            String[] arrayLocation = post_Location.split(" ");
                            latitude = Double.parseDouble(arrayLocation[0]);
                            longitude = Double.parseDouble(arrayLocation[1]);

                            Log.d(TAG, "getHeart - latitude : " + latitude);
                            Log.d(TAG, "getHeart - longitude : " + longitude);

                            String currentLocation = getAddress(getContext(),latitude,longitude);
                            addressHeart = editAddress4(currentLocation);

                            PostData postData = new PostData(post_Num, post_Nickname, post_ProfileImage, post_Heart, post_CommentNum,
                                    addressHeart, post_PostImage, post_Writing, post_DateCreated, post_Num, post_WhoLike, heartStatus);

                            post_Data_ArrayList.add(0, postData);

                        }

                        itemSize = post_Data_ArrayList.size();
                        Log.d(TAG, "itemSize : " + itemSize);

                        adapter.notifyDataSetChanged();

                        Log.d(TAG, "latitude : " + latitude);
                        Log.d(TAG, "longitude : " + longitude);

                    }

                } else {
                    Log.d(TAG, "onResponse 실패");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<PostData>> call, Throwable t) {
                Log.d(TAG, "onFailure 실패");
            }
        });

    } // getHeart()


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


    public String editAddress4(String location) {

        String address = null;
        String[] addressArray = location.split(" ");
        address = addressArray[1] + " " + addressArray[2] + " " + addressArray[3] + " " + addressArray[4];
//        address = addressArray[2] + " " + addressArray[4];

        return address;

    } // editAddress()

    public String editAddress2(String location) {

        String address = null;
        String[] addressArray = location.split(" ");
//        address = addressArray[1] + " " + addressArray[2] + " " + addressArray[3] + " " + addressArray[4];
        address = addressArray[2] + " " + addressArray[4];

        return address;

    } // editAddress()

}