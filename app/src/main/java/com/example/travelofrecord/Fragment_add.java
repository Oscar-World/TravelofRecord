package com.example.travelofrecord;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_add extends Fragment {

    String TAG = "추가 프래그먼트";

    View v;

    Button addUpload_Btn;
    EditText writing_Edit;
    ImageView postImage_Iv;

    File file;

    String nickname;
    String profileImage;
    int heart =0;
    String location;
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


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == RESULT_OK) {

//                            getCamera = result.getData().getExtras();
//
//                            imageBitmap = (Bitmap) getCamera.get("data");
//
//                            Log.d(TAG, "result : " + result);
//                            Log.d(TAG, "bundle : " + getCamera);
//                            Log.d(TAG, "bitmap : " + imageBitmap);
//
////                            image = imageBitmap.toString();
//                            String imageString = bitmapConverter.BitmapToString(imageBitmap);
//                            byte[] imageByte = bitmapConverter.BitmapToByteArray(imageBitmap);
//                            Bitmap bitmap = bitmapConverter.StringToBitmap(imageString);
//
//                            Log.d(TAG, "imageBitmap 데이터: " + imageBitmap + "\nimageString 데이터 : " + imageString + "\nimageByte 데이터 : " + imageByte + "\n다시 변환시킨 bitmap 데이터 : " + bitmap);
//                            Log.d(TAG, "이미지스트링의 길이 : " + imageString.length());
//
//
//                            Uri uri = getImageUri(getActivity(),imageBitmap);
//
//                            Log.d(TAG, "uri 데이터 : " + uri);
//
//                            String realPath = getRealPathFromUri(photoUri);
//
//                            Log.d(TAG, "절대경로 : " + realPath);

//                            Intent i = result.getData();

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
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();


        postImage_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {

                }
                if (photoFile != null) {

                    photoUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getPackageName() + ".fileprovider", photoFile);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                    launcher.launch(i);

                }


            }
        });


        addUpload_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                writing = writing_Edit.getText().toString();

                Log.d(TAG, "서버로 보낼 데이터 :\n아이디-" + sharedInfo + "\n텍스트-" + writing + "\n비트맵이미지-" + imageBitmap);

                insertFeed(nickname, profileImage, heart, location, postImage, writing, dataCreated);

            }
        });


    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
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

        sendData = new Bundle();
        fragment_home = new Fragment_Home();

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences_Kakao = this.getActivity().getSharedPreferences("a5636c0dc6cb43c4ea8f52134f0f1337", MODE_PRIVATE);
        editor_Kakao = sharedPreferences_Kakao.edit();

        sharedInfo = sharedPreferences.getString("로그인", "");

        bitmapConverter = new BitmapConverter();

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