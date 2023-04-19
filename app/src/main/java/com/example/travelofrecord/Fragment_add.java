package com.example.travelofrecord;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_add extends Fragment {

    String TAG = "추가 프래그먼트";

    View v;

    Button addUpload_Btn;
    EditText writing_Edit;
    ImageView postImage_Iv;
    FrameLayout writing_Layout;

    File file;

    String nickname;
    String profileImage;
    int heart = 0;
    String currentLocation;
    String postImage;
    String writing;
    String dataCreated;

    ActivityResultLauncher<Intent> launcher;

    Bundle getCamera;
    Bitmap imageBitmap;

    Bundle sendData;

    Fragment_Home fragment_home;
    Home homeActivity;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_Kakao;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_Kakao;
    String sharedInfo;

    BitmapConverter bitmapConverter;

    Uri photoUri;
    InputMethodManager imm;
    InputMethodManager immhide;

    // 위치 정보 권한
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private FusedLocationProviderClient fusedLocationClient;

    String adminArea; // 광역시, 도
    String locality; // 시
    String subLocality; // 구
    String thoroughfare; // 동

    String latitude; // 위도
    String longitude; // 경도



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");

        homeActivity = (Home) getActivity();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == RESULT_OK) {

                            Log.d(TAG, "경로! : " + photoUri);
                            Log.d(TAG, "절대경로! : " + postImage);


                            Glide.with(getActivity())
                                    .load(postImage)
                                    .into(postImage_Iv);

                        }

                    }
                });

    }


    // ImageFile 생성 후, 경로를 가져올 메서드 선언
    private File createImageFile() throws IOException {
        // 파일이름을 세팅 및 저장경로 세팅
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        postImage = image.getAbsolutePath();

        return image;
    }


    // 비트맵 > uri 변환 메서드 (기기 앨범에 사진 저장)
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    // uri 절대경로 리턴 메서드
    String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add, container, false);

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

        postImage = null;

    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();

        setView();

        Log.d(TAG, "postImage : " + postImage);
        if (postImage == null) {
            writing_Edit.setText("");
        }


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
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d(TAG, "onSuccess: location - " + location + "\n위도 - " + location.getLatitude() + "\n경도 - " + location.getLongitude());
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                    currentLocation = latitude + " " + longitude;

                    Log.d(TAG, "location : " + currentLocation);

                } else {
                    Log.d(TAG, "location == null");
                }
            }
        });


        postImage_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.d(TAG, "파일 생성 실패");
                }
                if (photoFile != null) {

                    photoUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getPackageName() + ".fileprovider", photoFile);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                    Log.d(TAG, "사진 찍었을 때 uri : " + photoUri);

                    launcher.launch(i);

                }


            }
        });


        addUpload_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentLocation == null) {
                    currentLocation = "someWhere";
                }
                writing = writing_Edit.getText().toString();
                dataCreated = getTime().toString();

                Log.d(TAG, "서버로 보낼 데이터 : 닉네임 : " + nickname + "\n프로필사진 : " + profileImage + "\n주소 : " + currentLocation +
                        "\n업로드할사진 : " + postImage + "\n작성한글 : " + writing + "\n오늘날짜 : " + dataCreated);

                if (postImage == null) {
                    Toast.makeText(getActivity(),"사진을 촬영해주세요",Toast.LENGTH_SHORT).show();
                } else if (writing.equals("")) {
                    Toast.makeText(getActivity(),"내용을 기록해주세요",Toast.LENGTH_SHORT).show();
                } else {
                    insertFeed(nickname, profileImage, heart, currentLocation, postImage, writing, dataCreated);
                }

            }
        });

        writing_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                writing_Edit.requestFocus();

            }
        });


    } // onStart()


    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
//        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();

        homeActivity = null;

    }

    public void setView() {

        addUpload_Btn = v.findViewById(R.id.addUpload_Btn);
        writing_Edit = v.findViewById(R.id.writing_Edit);
        postImage_Iv = v.findViewById(R.id.postImage_Iv);
        writing_Layout = v.findViewById(R.id.writing_FrameLayout);

        sendData = new Bundle();
        fragment_home = new Fragment_Home();

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences_Kakao = this.getActivity().getSharedPreferences("a5636c0dc6cb43c4ea8f52134f0f1337", MODE_PRIVATE);
        editor_Kakao = sharedPreferences_Kakao.edit();

        nickname = sharedPreferences.getString("nickname", "");
        profileImage = sharedPreferences.getString("image", "");

        bitmapConverter = new BitmapConverter();

        imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        immhide = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);

        writing_Edit.setFocusableInTouchMode(true);

    }



    public Long getTime() {

        long currentTime = System.currentTimeMillis();
//        Date date = new Date(currentTime);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
//
//        String today = format.format(date);
//
//        return today;

        return currentTime;

    }

    // 서버에 게시글 데이터 추가
    public void insertFeed(String nickname, String profileImage, int heart, String location, String postImage, String writing, String dateCreated) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Post> call = apiInterface.insertFeed(nickname, profileImage, heart, location, postImage, writing, dateCreated);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (response.isSuccessful()) {

                    Log.d(TAG, "onResponse: 리스폰스 성공");

                    String rp_code = response.body().getResponse();
                    Log.d(TAG, "서버에 게시글 추가 응답 : " + rp_code);

                    sendData.putString("nickname", nickname);
                    sendData.putString("profileImage", profileImage);
                    sendData.putInt("heart", heart);
                    sendData.putString("location", location);
                    sendData.putString("postImage", postImage);
                    sendData.putString("writing", writing);
                    sendData.putString("dateCreated", dateCreated);

                    homeActivity.goHomeFragment(sendData);
                    Log.d(TAG, "보낸 번들 데이터 : " + sendData);

                } else {
                    Log.d(TAG, "onResponse: 리스폰스 실패");
                }

            }   // onResponse

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }

        });

    }  // insertFeed()



}