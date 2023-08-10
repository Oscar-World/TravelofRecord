package com.example.travelofrecord.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.travelofrecord.Activity.Post;
import com.example.travelofrecord.Data.Markers;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Function.GetAdress;
import com.example.travelofrecord.Adapter.Heart_Adapter;
import com.example.travelofrecord.Data.PostData;
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
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ted.gun0912.clustering.clustering.Cluster;
import ted.gun0912.clustering.clustering.TedClusterItem;
import ted.gun0912.clustering.naver.TedNaverClustering;
import ted.gun0912.clustering.naver.TedNaverMarker;

public class Fragment_Heart extends Fragment implements OnMapReadyCallback {

    String TAG = "하트 프래그먼트";
    View v;
    GetAdress getAdress = new GetAdress();

    private Button photo_Btn;
    private Button map_Btn;
    private Button photo_Block;
    private Button map_Block;

    RecyclerView recyclerView;
    TextView noHeartText;
    ImageView loadingIv;
    Animation rotate;

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

    // 위치 정보 권한 상수
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    double latitude;
    double longitude;
    double currentLatitude;
    double currentLongitude;

    SharedPreferences sharedPreferences;
    String nickname;

    MapView mapView;
    NaverMap naverMap;
    private FusedLocationProviderClient fusedLocationClient;
    int networkStatus;

    ArrayList<Markers> markerList;
    Markers markers;

    FrameLayout mapDrawer;
    LinearLayout mapDrawerDown;
    ImageView mapDrawerImage;
    TextView mapDrawerText;

    Animation appear;
    Animation disappear;

    int putNum;


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

        networkStatus = NetworkStatus.getConnectivityStatus(getActivity());
        photo_Btn = v.findViewById(R.id.heartPhoto_Btn);
        map_Btn = v.findViewById(R.id.heartMap_Btn);
        photo_Block = v.findViewById(R.id.heartPhoto_Block);
        map_Block = v.findViewById(R.id.heartMap_Block);

        noHeartText = v.findViewById(R.id.noHeart_Text);
        loadingIv = v.findViewById(R.id.heart_Loading);
        rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.loading);

        recyclerView = v.findViewById(R.id.heart_RecyclerView);
        adapter = new Heart_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        post_Data_ArrayList = new ArrayList<>();

        adapter.setItemHeart(post_Data_ArrayList);

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보",Context.MODE_PRIVATE);
        nickname = sharedPreferences.getString("nickname","");

        markerList = new ArrayList<>();

        mapDrawer = v.findViewById(R.id.heart_mapDrawer);
        mapDrawerDown = v.findViewById(R.id.heart_MapDrawerDropDown);
        mapDrawerImage = v.findViewById(R.id.heart_MapDrawerImage);
        mapDrawerText = v.findViewById(R.id.heart_MapDrawerText);
        appear = AnimationUtils.loadAnimation(getActivity(), R.anim.mapdrawer_appear);
        disappear = AnimationUtils.loadAnimation(getActivity(), R.anim.mapdrawer_disappear);

    }

    public void setView() {

        loadingIv.setVisibility(View.VISIBLE);
        loadingIv.startAnimation(rotate);

        photo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    photo_Btn.setVisibility(View.GONE);
                    photo_Block.setVisibility(View.VISIBLE);
                    map_Btn.setVisibility(View.VISIBLE);
                    map_Block.setVisibility(View.GONE);

                    if (post_Data_ArrayList.size() == 0) {
                        noHeartText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        noHeartText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    mapView.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        map_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {
                    map_Btn.setVisibility(View.GONE);
                    map_Block.setVisibility(View.VISIBLE);
                    photo_Btn.setVisibility(View.VISIBLE);
                    photo_Block.setVisibility(View.GONE);

                    noHeartText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    loadingIv.setVisibility(View.GONE);
                    mapView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        // 위치 권한 확인
        int LOCATION_PERMISSION = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int COARSE_PERMISSION = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if (LOCATION_PERMISSION != PackageManager.PERMISSION_GRANTED && COARSE_PERMISSION != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "위치 권한 없음");

            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return;
        } else {
            Log.d(TAG, "위치 권한 있음");
        }

        // 현재 위치의 위도, 경도 확인
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
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

        mapDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


    } //setView()


    // -------------------------------------------------------------------------------------------------------

    int[] clusterBucket = {10, 20, 50, 100, 200, 500, 1000};
    Marker marker;

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

        mapDrawerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Post.class);
                i.putExtra("num", putNum);
                startActivity(i);
            }
        });

        mapDrawerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Post.class);
                i.putExtra("num", putNum);
                startActivity(i);
            }
        });

    }

    public void addMarker() {

        if (data.size() > 0) {
            for (int i=0; i < data.size(); i++) {

                post_Location = data.get(i).getLocation();
                post_PostImage = data.get(i).getPostImage();
                post_Writing = data.get(i).getWriting();
                post_DateCreated = data.get(i).getDateCreated();
                post_Num = data.get(i).getPostNum();
                post_Nickname = data.get(i).getPostNickname();

                String[] arrayLocation = post_Location.split(" ");
                latitude = Double.parseDouble(arrayLocation[0]);
                longitude = Double.parseDouble(arrayLocation[1]);

                String currentLocation = getAdress.getAddress(getContext(),latitude,longitude);
                addressHeart = getAdress.editAddress24(currentLocation);

                markers = new Markers(latitude, longitude, post_Location, post_PostImage, post_Writing, post_DateCreated, post_Num, post_Nickname);
                markerList.add(markers);

            }



            TedNaverClustering.with(getActivity(), naverMap)
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
                    .clusterBackground(new Function1<Integer, Integer>() {
                        @Override
                        public Integer invoke(Integer integer) {
                            return R.color.lightGreen;
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

                            Glide.with(requireActivity())
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
                                    tedClusterItemCluster.getPosition().getLongitude()), 7);

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



    public void getHeart(String nickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getHeart(nickname);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse 호출");

                    recyclerView.setVisibility(View.VISIBLE);
                    loadingIv.setVisibility(View.GONE);
                    loadingIv.clearAnimation();

                    data = response.body();

                    Log.d(TAG, "data.size : " + data.size());

                    if (data.size() > 0) {

                        noHeartText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        for (int i = 0; i < data.size(); i++) {

                            post_Num = data.get(i).getNum();
                            post_Nickname = data.get(i).getPostNickname();
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

                            Log.d(TAG, "onResponseImage : " + post_PostImage);
                            Log.d(TAG, "getHeart - latitude : " + latitude);
                            Log.d(TAG, "getHeart - longitude : " + longitude);

                            String currentLocation = getAdress.getAddress(getContext(),latitude,longitude);
                            addressHeart = getAdress.editAddress1234(currentLocation);

                            PostData postData = new PostData(post_Num, post_Nickname, post_ProfileImage, post_Heart, post_CommentNum,
                                    addressHeart, post_PostImage, post_Writing, post_DateCreated, post_Num, post_WhoLike, heartStatus);

                            post_Data_ArrayList.add(0, postData);

                        }

                        itemSize = post_Data_ArrayList.size();
                        Log.d(TAG, "itemSize : " + itemSize);

                        adapter.notifyDataSetChanged();

                    } else {

                        noHeartText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

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


}