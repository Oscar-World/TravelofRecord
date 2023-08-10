package com.example.travelofrecord.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.travelofrecord.Activity.PhotoView;
import com.example.travelofrecord.Activity.Post;
import com.example.travelofrecord.Activity.Start;
import com.example.travelofrecord.Data.Markers;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Function.GetAdress;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Adapter.MyProfile_Adapter;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;
import com.example.travelofrecord.Data.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.OAuthLoginCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ted.gun0912.clustering.clustering.Cluster;
import ted.gun0912.clustering.clustering.TedClusterItem;
import ted.gun0912.clustering.naver.TedNaverClustering;

public class Fragment_myProfile extends Fragment implements OnMapReadyCallback {

    String TAG = "내 프로필 프래그먼트";
    View v;
    GetAdress getAdress = new GetAdress();
    GetTime getTime = new GetTime();

    int networkStatus;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_Kakao;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_Kakao;

    ImageButton drawer_Btn;
    Button logout_Btn;
    Button userQuit_Btn;
    Button editProfile_Btn;
    Button editProfileSubmit_Btn;

    ImageView profile_Image;
    ImageView editProfile_Image;
    ImageView touchImage_Image;
    ImageView loading_Iv;
    Animation rotate;

    TextView profile_Text;
    TextView profile_nickname;
    TextView profile_memo;

    EditText profile_Edit;

    Button photo_Btn;
    Button photo_Block;
    Button map_Btn;
    Button map_Block;

    NestedScrollView scrollView;

    String edit_memo;

    String loginType;
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

    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;

    String imageFileName;
    boolean imageStatus;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private FusedLocationProviderClient fusedLocationClient;
    double currentLatitude;
    double currentLongitude;

    ArrayList<Markers> markerList;
    Markers markers;

    FrameLayout mapDrawer;
    LinearLayout mapDrawerDown;
    ImageView mapDrawerImage;
    TextView mapDrawerText;

    Animation appear;
    Animation disappear;

    int putNum;


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
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(profile_Image);

                            Glide.with(getActivity())
                                    .load(uri)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(editProfile_Image);

                            user_image = getRealPathFromUri(uri);
                            imageStatus = true;

                            Log.d(TAG, "uri : " + uri + "\nuri.toString : " + uri.toString() + "\nimagePath : " + user_image);

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

        setVariable();
        setView();
        getMyPost(user_nickname);

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
        imageStatus = false;


    } // onStart()

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

    // ----------------------------------------------------------------------------------------------------------------


    public void setVariable() {

        loading_Iv = v.findViewById(R.id.myProfile_Loading);
        rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.loading);

        networkStatus = NetworkStatus.getConnectivityStatus(getActivity());

        scrollView = v.findViewById(R.id.myProfile_ScrollView);
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

        loginType = sharedPreferences.getString("loginType", "");
        user_id = sharedPreferences.getString("id","");
        user_nickname = sharedPreferences.getString("nickname", "");
        user_image = sharedPreferences.getString("image", "");
        user_memo = sharedPreferences.getString("memo", "");
        Log.d(TAG, "setVariable: " + user_image);

        drawerLayout = v.findViewById(R.id.myProfile_drawerLayout);
        drawerView = v.findViewById(R.id.myProfile_drawer);

        recyclerView = v.findViewById(R.id.myProfile_RecyclerView);
        adapter = new MyProfile_Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        post_Data_ArrayList = new ArrayList<>();
        adapter.setItemMyProfile(post_Data_ArrayList);

        bundle = new Bundle();

        markerList = new ArrayList<>();

        mapDrawer = v.findViewById(R.id.myProfile_mapDrawer);
        mapDrawerDown = v.findViewById(R.id.myProfile_MapDrawerDropDown);
        mapDrawerImage = v.findViewById(R.id.myProfile_MapDrawerImage);
        mapDrawerText = v.findViewById(R.id.myProfile_MapDrawerText);
        appear = AnimationUtils.loadAnimation(getActivity(), R.anim.mapdrawer_appear);
        disappear = AnimationUtils.loadAnimation(getActivity(), R.anim.mapdrawer_disappear);

    } // setVariable()


    public void setView() {

        loading_Iv.setVisibility(View.VISIBLE);
        loading_Iv.startAnimation(rotate);

        if (user_memo == null | "".equals(user_memo)) {
            profile_Edit.setVisibility(View.GONE);
            profile_memo.setVisibility(View.VISIBLE);
        } else {
            profile_memo.setText(user_memo);
            profile_memo.setVisibility(View.VISIBLE);
            profile_Edit.setVisibility(View.GONE);
        }

        profile_nickname.setText(user_nickname);
        Log.d(TAG, "setView userImage : " + user_image);

        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        Glide.with(requireActivity())
                .load(ApiClient.serverProfileImagePath + user_image)
//                .transition(withCrossFade(factory))
//                .placeholder(R.drawable.loading2)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(profile_Image);

        Glide.with(requireActivity())
                .load(ApiClient.serverProfileImagePath + user_image)
//                .transition(withCrossFade(factory))
//                .placeholder(R.drawable.loading2)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(editProfile_Image);


        // 햄버거 버튼
        drawer_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    drawerLayout.openDrawer(drawerView);

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 프로필 수정 버튼
        editProfile_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

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

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

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
                    profile_Edit.setVisibility(View.GONE);

                    if (edit_memo == null | "".equals(edit_memo)) {
                        profile_Edit.setVisibility(View.GONE);
                        profile_memo.setVisibility(View.VISIBLE);
                    } else {
                        profile_memo.setText(edit_memo);
                        profile_memo.setVisibility(View.VISIBLE);
                        profile_Edit.setVisibility(View.GONE);
                    }

                    String systemTime = String.valueOf(System.currentTimeMillis());
                    imageFileName = systemTime + ".jpg";

                    Log.d(TAG, "업로드 시 imageStatus : " + imageStatus);

                    RequestBody image = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
                    RequestBody nickname = RequestBody.create(MediaType.parse("text/plain"), user_nickname);
                    RequestBody memo = RequestBody.create(MediaType.parse("text/plain"), edit_memo);
                    RequestBody beforeImage = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getString("image", ""));

                    HashMap map = new HashMap();
                    map.put("nickname", nickname);
                    map.put("memo", memo);
                    map.put("imagePath", image);
                    map.put("beforeImage", beforeImage);

                    if (imageStatus) {
                        File file = new File(user_image);
                        updateProfile(file, map);
                    } else {
                        updateMemo(map);
                    }

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 로그아웃 버튼
        logout_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    Log.d(TAG, "로그아웃 : " + loginType);

                    if (loginType.equals("Kakao")) {

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

                    } else if (loginType.equals("Google")) {

                        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();

                        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

                        googleSignInClient.signOut().addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "구글 로그아웃 완료", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if (loginType.equals("Naver")) {

                        NaverIdLoginSDK naverLoginSDK = NaverIdLoginSDK.INSTANCE;
                        naverLoginSDK.initialize(requireActivity(),
                                getString(R.string.naver_client_id),
                                getString(R.string.naver_client_secret),
                                getString(R.string.naver_client_name));

                        NidOAuthLogin nidOAuthLogin = new NidOAuthLogin();

                        nidOAuthLogin.callDeleteTokenApi(requireActivity(), new OAuthLoginCallback() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "onSuccess: DeleteToken");
                            }

                            @Override
                            public void onFailure(int i, @NonNull String s) {
                                Log.d(TAG, "onFailure: DeleteToken");
                            }

                            @Override
                            public void onError(int i, @NonNull String s) {
                                Log.d(TAG, "onError: DeleteToken");
                            }
                        });


                    }

                    editor.clear();
                    editor.commit();

                    updateToken(user_nickname, "");

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 회원 탈퇴 버튼
        userQuit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                    quitDialog(user_nickname);

                }else {
                    Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        // 정보 버튼
        photo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

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

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

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

                if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

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

        // 프로필 사진 보기
        profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), PhotoView.class);
                i.putExtra("image",ApiClient.serverProfileImagePath + user_image);
                startActivity(i);

            }
        });

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

    } // setView()


    // ---------------------------------------------------------------------------------------------

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
                                    tedClusterItemCluster.getPosition().getLongitude()), 8);

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


    public void getMyPost(String nickname) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ArrayList<PostData>> call = apiInterface.getMyPost(nickname);
        call.enqueue(new Callback<ArrayList<PostData>>() {
            @Override
            public void onResponse(Call<ArrayList<PostData>> call, Response<ArrayList<PostData>> response) {

                if (response.isSuccessful()) {

                    loading_Iv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    loading_Iv.clearAnimation();

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

                            String currentLocation = getAdress.getAddress(getContext(),latitude,longitude);
                            String addressPost = getAdress.editAddress1234(currentLocation);

                            String datePost = getTime.lastTime(post_DateCreated);

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

    } // getMyPost()

    public void updateMemo(HashMap map) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.updateMemo(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String rpCode = response.body();
                    Log.d(TAG, "updateMemo - onResponse isSuccessful" + rpCode);

                    if (rpCode.equals("uploadOk1")) {
                        editor.putString("memo", edit_memo);
                        editor.commit();
                    }

                } else {
                    Log.d(TAG, "updateMemo - onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "updateMemo - onFailure");
            }
        });

    }

    // 상태 메시지, 프로필 사진 변경
    public void updateProfile(File file, HashMap map) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", imageFileName, requestFile);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.updateProfile(body, map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String rpCode = response.body();
                    Log.d(TAG, "updateProfile - onResponse isSuccessful : " + rpCode);

                    if (rpCode.equals("uploadOk2")) {

                        editor.putString("memo", edit_memo);
                        editor.putString("image", imageFileName);
                        editor.commit();

                    }
                    else {
                        Toast.makeText(getActivity(), "프로필 수정 실패", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Log.d(TAG, "updateProfile - onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "updateProfile - onFailure : " + t);
            }
        });

    } //updateProfile()


    // 회원 탈퇴
    public void deleteUser(String nickname) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.deleteUser(nickname);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    String rp_code = response.body();
                    Log.d(TAG, "삭제 response : " + rp_code);

                    if (rp_code.equals("Ok")) {
                        Toast.makeText(getActivity().getApplicationContext(),"회원 탈퇴 완료",Toast.LENGTH_SHORT).show();


                        if (loginType.equals("Kakao")) {

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

                        } else if (loginType.equals("Google")) {

                            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();

                            googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

                            googleSignInClient.signOut().addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getActivity(), "구글 로그아웃 완료", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else if (loginType.equals("Naver")) {

                            NaverIdLoginSDK naverLoginSDK = NaverIdLoginSDK.INSTANCE;
                            naverLoginSDK.initialize(requireActivity(),
                                    getString(R.string.naver_client_id),
                                    getString(R.string.naver_client_secret),
                                    getString(R.string.naver_client_name));

                            NidOAuthLogin nidOAuthLogin = new NidOAuthLogin();

                            nidOAuthLogin.callDeleteTokenApi(requireActivity(), new OAuthLoginCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "onSuccess: DeleteToken");
                                }

                                @Override
                                public void onFailure(int i, @NonNull String s) {
                                    Log.d(TAG, "onFailure: DeleteToken");
                                }

                                @Override
                                public void onError(int i, @NonNull String s) {
                                    Log.d(TAG, "onError: DeleteToken");
                                }
                            });


                        }

                        editor.clear();
                        editor.commit();

                        updateToken(user_nickname, "");

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


    public void updateToken(String nickname, String fcmToken) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.updateFcmToken(nickname, fcmToken);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "updateFcmToken - onResponse isSuccessful");

                    String rpCode = response.body();
                    Log.d(TAG, "rpCode: " + rpCode);

                    Intent i = new Intent(getActivity(), Start.class);
                    startActivity(i);

                    getActivity().finish();

                } else {
                    Log.d(TAG, "updateFcmToken - onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "updateFcmToken - onFailure: " + t);
            }
        });

    } // updateToken()


    public void quitDialog(String user_Nickname) {

        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());

                dlg.setTitle("회원 탈퇴 하시겠습니까?")
                .setMessage("회원님의 모든 데이터가 삭제됩니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteUser(user_Nickname);

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();

    }


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


}