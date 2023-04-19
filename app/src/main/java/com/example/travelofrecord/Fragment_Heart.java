package com.example.travelofrecord;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Heart extends Fragment {

    String TAG = "하트 프래그먼트";

    View v;

    private Button photo_Btn;
    private Button map_Btn;
    private Button photo_Block;
    private Button map_Block;

    RecyclerView recyclerView;
    ArrayList<Post> post_ArrayList;
    Heart_Adapter adapter;

    int itemSize;

    String location;
    String postImage;
    String profileImage;
    String nowAddr;
    String addressHeart;

    double latitude;
    double longitude;

    SharedPreferences sharedPreferences;
    String nickname;

    FrameLayout map_FrameLayout;
    MapView mapView;
    ViewGroup mapViewContainer;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_heart, container, false);

        setView();

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");

        Log.d(TAG, "onViewCreated: " + nickname);
        getHeart(nickname);

    }




    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();

        mapView.setMapViewEventListener(new MapViewEventListener() {
            @Override
            public void onLoadMapView() {

            }
        });

        photo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photo_Btn.setVisibility(View.GONE);
                photo_Block.setVisibility(View.VISIBLE);
                map_Btn.setVisibility(View.VISIBLE);
                map_Block.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);
                map_FrameLayout.setVisibility(View.GONE);

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
                map_FrameLayout.setVisibility(View.VISIBLE);

            }
        });


    }

    public void pickMarker(double lat, double log, int i) {

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(lat, log);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(addressHeart);
        marker.setTag(i);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);

    } // pickMaker()


    public void getHeart(String nickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<Post>> call = apiInterface.getHeart(nickname);
        call.enqueue(new Callback<ArrayList<Post>>() {
            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {

                if (response.isSuccessful()) {

                    ArrayList<Post> data = response.body();

                    Log.d(TAG, "data.size : " + data.size());

                    if (data.size() > 0) {

                        for (int i = 0; i < data.size(); i++) {

                            postImage = data.get(i).getPostImage();
                            location = data.get(i).getLocation();
                            profileImage = data.get(i).getProfileImage();

                            String[] arrayLocation = location.split(" ");
                            latitude = Double.parseDouble(arrayLocation[0]);
                            longitude = Double.parseDouble(arrayLocation[1]);

                            Log.d(TAG, "getHeart - latitude : " + latitude);
                            Log.d(TAG, "getHeart - longitude : " + longitude);

//                            mapView.setCalloutBalloonAdapter();

                            pickMarker(latitude, longitude, i);

                            String currentLocation = getAddress(getContext(),latitude,longitude);
                            addressHeart = editAddress(currentLocation);

                            Post post = new Post(addressHeart,postImage);

                            post_ArrayList.add(0, post);

                        }

                        itemSize = post_ArrayList.size();
                        Log.d(TAG, "itemSize : " + itemSize);

                        adapter.notifyDataSetChanged();

                        Log.d(TAG, "latitude : " + latitude);
                        Log.d(TAG, "longitude : " + longitude);

                        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude),true);
                        mapView.setZoomLevel(10,true);
                        mapView.zoomIn(true);
                        mapView.zoomOut(true);

                    }

                } else {
                    Log.d(TAG, "onResponse 실패");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Log.d(TAG, "onFailure 실패");
            }
        });

    } // getHeart()


    public class CustomBalloonAdapter {



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

//        address = addressArray[1] + " " + addressArray[2] + " " + addressArray[3] + " " + addressArray[4];

        address = addressArray[2] + " " + addressArray[4];

        return address;

    }

    @SuppressLint("CutPasteId")
    public void setView() {

        photo_Btn = v.findViewById(R.id.heartPhoto_Btn);
        map_Btn = v.findViewById(R.id.heartMap_Btn);
        photo_Block = v.findViewById(R.id.heartPhoto_Block);
        map_Block = v.findViewById(R.id.heartMap_Block);

        recyclerView = v.findViewById(R.id.heart_RecyclerView);
        adapter = new Heart_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        post_ArrayList = new ArrayList<>();

        adapter.setItemHeart(post_ArrayList);

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보",Context.MODE_PRIVATE);
        nickname = sharedPreferences.getString("nickname","");

        map_FrameLayout = v.findViewById(R.id.heart_MapView);
        mapView = new MapView(this.getActivity());
        mapViewContainer = (ViewGroup) v.findViewById(R.id.heart_MapView);
        mapViewContainer.addView(mapView);

    }

    @Override public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }
    @Override public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }
    @Override public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
        mapViewContainer.removeView(mapView);
    }
    @Override public void onDestroyView() {
        Log.d(TAG, "onDestroyView()");
        super.onDestroyView();
    }
    @Override public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
    }

}