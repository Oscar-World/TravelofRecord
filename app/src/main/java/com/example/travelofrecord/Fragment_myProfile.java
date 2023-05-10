package com.example.travelofrecord;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.sdk.user.UserApiClient;
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

public class Fragment_myProfile extends Fragment implements OnMapReadyCallback {

    String TAG = "내 프로필 프래그먼트";

    View v;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_Kakao;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_Kakao;

    LinearLayout profileSelect_Layout;
    ImageButton drawer_Btn;
    Button logout_Btn;
    Button userQuit_Btn;
    Button editProfile_Btn;
    Button editProfileSubmit_Btn;

    ImageView profile_Image;
    ImageView editProfile_Image;
    ImageView touchImage_Image;

    TextView profile_Text;
    TextView profile_nickname;
    TextView profile_memo;

    EditText profile_Edit;

    Button photo_Btn;
    Button photo_Block;
    Button map_Btn;
    Button map_Block;

    ScrollView scrollView;

    String edit_memo;

    String user_id;
    String user_nickname;
    String user_memo;
    String user_image;

    DrawerLayout drawerLayout;
    View drawerView;

    Uri uri;

    ActivityResultLauncher<Intent> launcher;

    RecyclerView recyclerView;
    ArrayList<PostData> post_Data_ArrayList;
    MyProfile_Adapter adapter;
    int itemSize;
    ArrayList<PostData> data;

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

    double latitude;
    double longitude;

    Bundle bundle;

    MapView mapView;
    NaverMap naverMap;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() 호출");
    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() 호출");

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == RESULT_OK) {

                            Intent intent = result.getData();
                            uri = intent.getData();

                            Log.d(TAG, "onActivityResult: " + result);
                            Log.d(TAG, "onActivityResult: " + intent);
                            Log.d(TAG, "onActivityResult: " + uri);

                            Glide.with(getActivity())
                                    .load(uri)
                                    .into(profile_Image);

                            Glide.with(getActivity())
                                    .load(uri)
                                    .into(editProfile_Image);

                            user_image = getRealPathFromUri(uri);

                            Log.d(TAG, "uri : " + uri + "\nuri.toString : " + uri.toString() + "\nimagePath : " + user_image);

                            editor.putString("image",user_image);
                            editor.commit();

                        }

                    }
                });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        Log.d(TAG, "onCreateView() 호출");

        return v;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated() 호출");
        mapView = view.findViewById(R.id.myProfile_MapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }
    @Override public void onStart() {
        Log.d(TAG, "onStart() 호출");
        super.onStart();
        mapView.onStart();
        setView();
        getMyPost(user_nickname);

        // 햄버거 버튼
        drawer_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        // 프로필 수정 버튼
        editProfile_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getActivity());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    profile_Text.setText("프로필 수정");

                    profile_memo.setVisibility(View.GONE);
                    profile_Edit.setVisibility(View.VISIBLE);
                    profile_Edit.setText(user_memo);
                    drawer_Btn.setVisibility(View.GONE);
                    editProfileSubmit_Btn.setVisibility(View.VISIBLE);
                    profile_Image.setVisibility(View.GONE);
                    editProfile_Image.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    touchImage_Image.setVisibility(View.VISIBLE);
                    map_Btn.setVisibility(View.GONE);
                    map_Block.setVisibility(View.GONE);
                    photo_Btn.setVisibility(View.GONE);
                    photo_Block.setVisibility(View.GONE);

                    drawerLayout.closeDrawer(drawerView);

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 프로필 수정 완료 버튼
        editProfileSubmit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getActivity());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    profile_Text.setText("프로필");
                    edit_memo = profile_Edit.getText().toString();
                    Log.d(TAG, "수정된 메시지 : " + edit_memo);

                    drawer_Btn.setVisibility(View.VISIBLE);
                    editProfileSubmit_Btn.setVisibility(View.GONE);
                    profile_Image.setVisibility(View.VISIBLE);
                    editProfile_Image.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    touchImage_Image.setVisibility(View.GONE);
                    map_Btn.setVisibility(View.VISIBLE);
                    map_Block.setVisibility(View.GONE);
                    photo_Btn.setVisibility(View.GONE);
                    photo_Block.setVisibility(View.VISIBLE);

                    updateProfile(user_nickname,edit_memo, user_image);

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 로그아웃 버튼
        logout_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getActivity());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    UserApiClient.getInstance().unlink(error -> {
                        if (error != null) {
                            Log.d(TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error);
                        }else{
                            Log.d(TAG, "로그아웃 성공, SDK에서 토큰 삭제됨");
                        }
                        return null;
                    });

                    editor_Kakao.clear();
                    editor_Kakao.commit();

                    editor.clear();
                    editor.commit();

                    Intent i = new Intent(getActivity(),Start.class);
                    startActivity(i);

                    getActivity().finish();

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 회원 탈퇴 버튼
        userQuit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getActivity());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    deleteUser(user_id);

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        // 정보 버튼
        photo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getActivity());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    photo_Btn.setVisibility(View.GONE);
                    photo_Block.setVisibility(View.VISIBLE);
                    map_Btn.setVisibility(View.VISIBLE);
                    map_Block.setVisibility(View.GONE);
                    drawer_Btn.setVisibility(View.VISIBLE);

                    mapView.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        // 지도 버튼
        map_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getActivity());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    map_Btn.setVisibility(View.GONE);
                    map_Block.setVisibility(View.VISIBLE);
                    photo_Btn.setVisibility(View.VISIBLE);
                    photo_Block.setVisibility(View.GONE);
                    drawer_Btn.setVisibility(View.INVISIBLE);

                    scrollView.setVisibility(View.GONE);
                    mapView.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 프로필 사진 수정 버튼
        editProfile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = NetworkStatus.getConnectivityStatus(getActivity());
                Log.d(TAG, "NetworkStatus : " + status);
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_PICK);
                    launcher.launch(i);

                    Log.d(TAG, "DB로 전송할 데이터 : " + user_nickname + " " + user_image);

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });


    } // onStart()

    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(getActivity(), uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }


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

                String currentLocation = getAddress(getActivity(),latitude,longitude);
                String shortLocation = editAddress2(currentLocation);

                setMarker(latitude, longitude, shortLocation);
            }
        }

    } // addMarker()

    public void setMarker(double lat, double lng, String addressHeart) {

        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat,lng));
        marker.setTag(addressHeart);
//        marker.setCaptionText(addressHeart);
//        marker.setCaptionAligns(Align.Top);
//        marker.setCaptionMinZoom(11);
//        marker.setCaptionOffset(10);

        marker.setMap(naverMap);

        setInfoWindow(marker);

    } // setMarker()

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


    public void getMyPost(String nickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getMyPost(nickname);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {

                if (response.isSuccessful()) {

                    data = response.body();

                    Log.d(TAG, "data.size : " + data.size());

                    if (data.size() > 0) {

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

                            if (user_nickname.equals(post_WhoLike)) {
                                heartStatus = true;
                            }

                            String[] arrayLocation = post_Location.split(" ");
                            latitude = Double.parseDouble(arrayLocation[0]);
                            longitude = Double.parseDouble(arrayLocation[1]);

                            String currentLocation = getAddress(getContext(),latitude,longitude);
                            String addressPost = editAddress4(currentLocation);

                            String datePost = lastTime(post_DateCreated);

                            PostData postData = new PostData(post_Num, post_Nickname, post_ProfileImage, post_Heart, post_CommentNum,
                                    addressPost, post_PostImage, post_Writing, datePost, post_Num, post_WhoLike, heartStatus);
                            post_Data_ArrayList.add(0, postData);

                        }

                        itemSize = post_Data_ArrayList.size();
                        Log.d(TAG, "itemSize : " + itemSize);

                        adapter.notifyDataSetChanged();



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

    }


    // 상태 메시지, 프로필 사진 변경
    public void updateProfile(String nickname, String memo, String image) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.updateProfile(nickname, memo, image);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    user_memo = response.body().getMemo();
                    user_image = response.body().getImage();

                    Log.d(TAG, "수정된 메모 : " + user_memo);
                    Log.d(TAG, "수정된 이미지 : " + user_image);

                    editor.putString("memo", user_memo);
                    editor.commit();


                    if (user_memo == null | "".equals(user_memo)) {
                        profile_Edit.setVisibility(View.GONE);
                        profile_memo.setVisibility(View.VISIBLE);
                    } else {
                        profile_memo.setVisibility(View.VISIBLE);
                        profile_memo.setText(edit_memo);
                        profile_Edit.setVisibility(View.GONE);
                    }


                } else {
                    Log.d(TAG, "onResponse: 리스폰스 실패");
                }

            }   // onResponse

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }

        });

    }  // updateMemo()

    // 회원 탈퇴
    public void deleteUser(String id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.deleteUser(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String rp_code = response.body();
                    Log.d(TAG, "삭제 response : " + rp_code);

                    if (rp_code.equals("Ok")) {
                        Toast.makeText(getActivity().getApplicationContext(),"회원 탈퇴 완료",Toast.LENGTH_SHORT).show();


                        UserApiClient.getInstance().unlink(error -> {
                            if (error != null) {
                                Log.d(TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error);
                            }else{
                                Log.d(TAG, "로그아웃 성공, SDK에서 토큰 삭제됨");
                            }
                            return null;
                        });

                        editor_Kakao.clear();
                        editor_Kakao.commit();

                        editor.clear();
                        editor.commit();

                        Intent i = new Intent(getActivity(),Start.class);
                        startActivity(i);

                        getActivity().finish();


                    } else {
                        Log.d(TAG, "onResponse: 회원 탈퇴 실패");
                    }

                } else {
                    Log.d(TAG, "onResponse: 리스폰스 실패");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!!! " + t.getMessage());
            }
        });
    }  // deleteUser()


    // Geocoder - 위도, 경도 사용해서 주소 구하기.
    public String getAddress(Context mContext, double lat, double lng) {
        String nowAddr ="현재 위치를 확인 할 수 없습니다.";
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
            Toast.makeText(mContext, "주소를 가져올 수 없습니다.", Toast.LENGTH_LONG).show();
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

    } // editAddress4()

    public String editAddress2(String location) {

        String address = null;
        String[] addressArray = location.split(" ");
//        address = addressArray[1] + " " + addressArray[2] + " " + addressArray[3] + " " + addressArray[4];
        address = addressArray[2] + " " + addressArray[4];

        return address;

    } // editAddress2()



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

    } // lastTime()


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

    public void setView() {

        scrollView = v.findViewById(R.id.myProfile_ScrollView);
//        profileSelect_Layout = v.findViewById(R.id.myProfileSelect_Layout);
        drawer_Btn = v.findViewById(R.id.myProfile_drawerBtn);
        logout_Btn = v.findViewById(R.id.logout_Btn);
        userQuit_Btn = v.findViewById(R.id.userQuit_Btn);
        editProfile_Btn = v.findViewById(R.id.editProfile_Btn);
        editProfileSubmit_Btn = v.findViewById(R.id.editProfileSubmit_Btn);

        profile_Image = v.findViewById(R.id.myProfile_image);
        editProfile_Image = v.findViewById(R.id.myProfileEdit_image);
        touchImage_Image = v.findViewById(R.id.myProfileTouchImage_Image);

        profile_Text = v.findViewById(R.id.myProfile_Text);
        profile_nickname = v.findViewById(R.id.myProfile_nickname);
        profile_memo = v.findViewById(R.id.myProfile_memo);
        profile_Edit = v.findViewById(R.id.myProfile_Edit);

        photo_Btn = v.findViewById(R.id.myProfilePhoto_Btn);
        photo_Block = v.findViewById(R.id.myProfilePhoto_Block);
        map_Btn = v.findViewById(R.id.myProfileMap_Btn);
        map_Block = v.findViewById(R.id.myProfileMap_Block);

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences_Kakao = this.getActivity().getSharedPreferences("a5636c0dc6cb43c4ea8f52134f0f1337", MODE_PRIVATE);
        editor_Kakao = sharedPreferences_Kakao.edit();

        user_id = sharedPreferences.getString("id","");
        user_nickname = sharedPreferences.getString("nickname", "");
        user_image = sharedPreferences.getString("image", "");
        user_memo = sharedPreferences.getString("memo", "");

        drawerLayout = v.findViewById(R.id.myProfile_drawerLayout);
        drawerView = v.findViewById(R.id.myProfile_drawer);


        if (user_memo == null | "".equals(user_memo)) {
            profile_Edit.setVisibility(View.GONE);
            profile_memo.setVisibility(View.VISIBLE);
        } else {
            profile_memo.setText(user_memo);
            profile_memo.setVisibility(View.VISIBLE);
            profile_Edit.setVisibility(View.GONE);
        }

        profile_nickname.setText(user_nickname);
        Glide.with(getActivity())
                .load(user_image)
                .into(profile_Image);
        Glide.with(getActivity())
                .load(user_image)
                .into(editProfile_Image);

        recyclerView = v.findViewById(R.id.myProfile_RecyclerView);
        adapter = new MyProfile_Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        post_Data_ArrayList = new ArrayList<>();
        adapter.setItemMyProfile(post_Data_ArrayList);

        bundle = new Bundle();

    } // setView()

}