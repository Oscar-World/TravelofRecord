package com.example.travelofrecord;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

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

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraPosition;
import net.daum.mf.map.api.CameraUpdate;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.CancelableCallback;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Heart extends Fragment implements MapView.MapViewEventListener, MapView.POIItemEventListener {

    String TAG = "하트 프래그먼트";

    View v;

    private Button photo_Btn;
    private Button map_Btn;
    private Button photo_Block;
    private Button map_Block;

    RecyclerView recyclerView;
    ArrayList<PostData> post_Data_ArrayList;
    Heart_Adapter adapter;

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

    FrameLayout map_FrameLayout;
    MapView mapView;
    ViewGroup mapViewContainer;
    MapView.POIItemEventListener poiItemEventListener;



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

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
        Log.d(TAG, "onViewCreated: " + nickname);

    }


    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();

        setView();
        setMapView();
        getHeart(nickname);

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

    } // onStart()


    public void getHeart(String nickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getHeart(nickname);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {

                if (response.isSuccessful()) {

                    ArrayList<PostData> data = response.body();

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
                            addressHeart = editAddress(currentLocation);

                            pickMarker(latitude, longitude, i, addressHeart);

                            PostData postData = new PostData(post_Num, post_Nickname, post_ProfileImage, post_Heart, post_CommentNum,
                                    addressHeart, post_PostImage, post_Writing, post_DateCreated, post_Num, post_WhoLike, heartStatus);

                            post_Data_ArrayList.add(0, postData);

                        }

                        itemSize = post_Data_ArrayList.size();
                        Log.d(TAG, "itemSize : " + itemSize);

                        adapter.notifyDataSetChanged();

                        Log.d(TAG, "latitude : " + latitude);
                        Log.d(TAG, "longitude : " + longitude);

                        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude),true);
                        mapView.setZoomLevel(10,true);

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


    public String editAddress(String location) {

        String address = null;

        String[] addressArray = location.split(" ");

        address = addressArray[1] + " " + addressArray[2] + " " + addressArray[3] + " " + addressArray[4];

//        address = addressArray[2] + " " + addressArray[4];

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

        post_Data_ArrayList = new ArrayList<>();

        adapter.setItemHeart(post_Data_ArrayList);

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보",Context.MODE_PRIVATE);
        nickname = sharedPreferences.getString("nickname","");

        map_FrameLayout = v.findViewById(R.id.heart_MapView);

    }

    class CustomBalloonAdapter implements CalloutBalloonAdapter {
        private final View calloutBalloon;

        public CustomBalloonAdapter() {
            calloutBalloon = getLayoutInflater().inflate(R.layout.custom_balloon,null);

        }

        @Override
        public View getCalloutBalloon(MapPOIItem mapPOIItem) {
            ((ImageView) calloutBalloon.findViewById(R.id.ballon_Image)).setImageURI(Uri.parse(post_Data_ArrayList.get(2).postImage));

            return calloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
            return null;
        }
    } // CustomBalloonAdapter

    public void setMapView() {
        mapView = new MapView(this.getActivity());
        mapViewContainer = (ViewGroup) v.findViewById(R.id.heart_MapView);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        mapView.setCalloutBalloonAdapter(new CustomBalloonAdapter());

    }

    public void pickMarker(double lat, double log, int i, String address) {
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(lat, log);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(address);
        marker.setTag(i);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);


    } // pickMaker()

    // MapView.MapViewEventListener
    @Override
    public void onMapViewInitialized(MapView mapView) {
        int a = mapView.getPOIItems().length;
        Toast.makeText(getActivity(),String.valueOf(a),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        Toast.makeText(getActivity(), "zoomLevel : " + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
    }


    // MapView.POIItemEventListener
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        CameraPosition cameraPosition = new CameraPosition(mapPOIItem.getMapPoint(), 3);
        mapView.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, new CancelableCallback() {
            @Override
            public void onFinish() {
                Toast.makeText(getActivity(),"이동 완료", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
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