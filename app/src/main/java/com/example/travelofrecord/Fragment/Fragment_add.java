package com.example.travelofrecord.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Other.BitmapConverter;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Activity.Home;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_add extends Fragment {

    String TAG = "추가 프래그먼트";
    View v;
    GetTime getTime = new GetTime();

    Button addUpload_Btn;
    EditText writing_Edit;
    ImageView postImage_Iv;
    FrameLayout writing_Layout;
    TextView writingCount_Text;

    File file;

    String nickname;
    String profileImage;
    int heart = 0;
    int commentNum = 0;
    String currentLocation;
    String postImage;
    String writing;
    String dateCreated;

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

    SharedPreferences writeShared;
    SharedPreferences.Editor writeEditor;

    // 위치 정보 권한 상수
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private FusedLocationProviderClient fusedLocationClient;

    String latitude; // 위도
    String longitude; // 경도

    String imageFileName;
    String tempWrite;
    String tempImage;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() 호출");

        homeActivity = (Home) getActivity();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() 호출");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == RESULT_OK) {

                            Glide.with(getActivity())
                                    .load(postImage)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(postImage_Iv);

                        }

                    }
                });


    } // onCreate()



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() 호출");
        v = inflater.inflate(R.layout.fragment_add, container, false);
        setVariable();
        setView();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated() 호출");

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored() 호출");

        postImage = null;
        writing_Edit.setText("");
        
        String tempWriteCheck = writeShared.getString("write", "");
        String tempImageCheck = writeShared.getString("image", "");

        Log.d(TAG, "onViewCreated : " + tempWriteCheck + " / " + tempImageCheck);

        if (!tempWriteCheck.equals("") | !tempImageCheck.equals("")) {
            tempDialog();
        }

    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart() 호출");
        super.onStart();

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




    } // onStart()


    @Override
    public void onResume() {
        Log.d(TAG, "onResume() 호출");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() 호출");
        super.onPause();
//        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop() 호출");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView() 호출");
        super.onDestroyView();

        String tempImage = postImage;
        String tempWrite = writing_Edit.getText().toString();

        writeEditor.putString("image", tempImage);
        writeEditor.putString("write", tempWrite);
        writeEditor.commit();

    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach() 호출");
        super.onDetach();

        homeActivity = null;

    }


    // --------------------------------------------------------------------------------------------


    public void setVariable() {

        addUpload_Btn = v.findViewById(R.id.addUpload_Btn);
        writing_Edit = v.findViewById(R.id.writing_Edit);
        postImage_Iv = v.findViewById(R.id.postImage_Iv);
        writing_Layout = v.findViewById(R.id.writing_FrameLayout);
        writingCount_Text = v.findViewById(R.id.writingCount_Text);


        sendData = new Bundle();
        fragment_home = new Fragment_Home();

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences_Kakao = this.getActivity().getSharedPreferences("a5636c0dc6cb43c4ea8f52134f0f1337", MODE_PRIVATE);
        editor_Kakao = sharedPreferences_Kakao.edit();
        writeShared = this.getActivity().getSharedPreferences("임시저장", MODE_PRIVATE);
        writeEditor = writeShared.edit();

        nickname = sharedPreferences.getString("nickname", "");
        profileImage = sharedPreferences.getString("image", "");

        bitmapConverter = new BitmapConverter();

        imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        immhide = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);

        writing_Edit.setFocusableInTouchMode(true);

    } // setVariable()

    public void setView() {

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
                dateCreated = getTime.getTime().toString();

                if (postImage == null) {
                    Toast.makeText(getActivity(),"사진을 촬영해주세요",Toast.LENGTH_SHORT).show();
                } else if (writing.equals("")) {
                    Toast.makeText(getActivity(),"내용을 기록해주세요",Toast.LENGTH_SHORT).show();
                } else {
                    writeEditor.clear();
                    writeEditor.commit();

                    String systemTime = String.valueOf(System.currentTimeMillis());
                    imageFileName = systemTime + ".jpg";

                    RequestBody addNickname = RequestBody.create(MediaType.parse("text/plain"), nickname);
                    RequestBody addProfileImage = RequestBody.create(MediaType.parse("text/plain"), profileImage);
                    RequestBody addHeart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(heart));
                    RequestBody addCommentNum = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(commentNum));
                    RequestBody addLocation = RequestBody.create(MediaType.parse("text/plain"), currentLocation);
                    RequestBody addPostImage = RequestBody.create(MediaType.parse("text/plain"), imageFileName);
                    RequestBody addWriting = RequestBody.create(MediaType.parse("text/plain"), writing);
                    RequestBody addDateCreated = RequestBody.create(MediaType.parse("text/plain"), dateCreated);

                    HashMap map = new HashMap();
                    map.put("nickname", addNickname);
                    map.put("profileImage", addProfileImage);
                    map.put("heart", addHeart);
                    map.put("commentNum", addCommentNum);
                    map.put("location", addLocation);
                    map.put("postImage", addPostImage);
                    map.put("writing", addWriting);
                    map.put("dateCreated", addDateCreated);

                    Log.d(TAG, "onClick : " + postImage);
                    Log.d(TAG, "onClick: " + imageFileName);
                    File file = new File(postImage);
                    insertFeed(file, map);

//                    insertFeed(nickname, profileImage, heart, commentNum, currentLocation, postImage, writing, dateCreated);
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



        writing_Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {

                int writingCount = writing_Edit.getText().toString().length();
                Log.d(TAG, "writingLength : " + writingCount);
                writingCount_Text.setText(String.valueOf(writingCount) + "/100");

            }
        });

    } // setView()


    // --------------------------------------------------------------------------------------------


    public void tempDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("작성중인 게시글이 존재합니다.")
                .setMessage("이어서 작성하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        tempWrite = writeShared.getString("write","");
                        tempImage = writeShared.getString("image", "");
                        postImage = tempImage;

                        if (!tempWrite.equals("")) {
                            writing_Edit.setText(tempWrite);
                        }
                        if (!tempImage.equals("")) {
                            Glide.with(getActivity())
                                    .load(tempImage)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(postImage_Iv);
                        }


                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        writeEditor.clear();
                        writeEditor.commit();

                    }
                })
                .create()
                .show();

    } // tempDialog()


    public void insertFeed(File file, HashMap map) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", imageFileName, requestFile);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.insertFeed(body, map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String rpCode = response.body();
                    Log.d(TAG, "insertFeed - onResponse isSuccessful : " + rpCode);

                    if (rpCode.equals("uploadOk")) {

                        sendData.putString("nickname", nickname);
                        sendData.putString("profileImage", profileImage);
                        sendData.putInt("heart", heart);
                        sendData.putString("location", currentLocation);
                        sendData.putString("postImage", postImage);
                        sendData.putString("writing", writing);
                        sendData.putString("dateCreated", dateCreated);

                        homeActivity.goHomeFragment(sendData);
                        Log.d(TAG, "보낸 번들 데이터 : " + sendData);

                    } else {
                        Toast.makeText(getActivity(), "업로드 실패", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Log.d(TAG, "insertFeed - onResponse isFailure");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "insertFeed - onFailure");
            }
        });

    }


    // 서버에 게시글 데이터 추가
    public void insertFeed(String nickname, String profileImage, int heart, int commentNum, String location, String postImage, String writing, String dateCreated) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<PostData> call = apiInterface.insertFeed(nickname, profileImage, heart, commentNum, location, postImage, writing, dateCreated);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {

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
            public void onFailure(Call<PostData> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }

        });

    }  // insertFeed()


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


}