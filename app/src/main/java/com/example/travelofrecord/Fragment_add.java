package com.example.travelofrecord;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_add extends Fragment {

    String TAG = "추가 프래그먼트";

    View v;

    Button addUpload_Btn;
    EditText addWrote_Edit;
    ImageView addImage;

    File file;

    String image;
    String wroteContent;

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


    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");

        homeActivity = (Home) getActivity();

    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == RESULT_OK) {

                            getCamera = result.getData().getExtras();

                            imageBitmap = (Bitmap) getCamera.get("data");

                            Log.d(TAG, "result : " + result);
                            Log.d(TAG, "bundle : " + getCamera);
                            Log.d(TAG, "bitmap : " + imageBitmap);

//                            image = imageBitmap.toString();
                            String imageString = bitmapConverter.BitmapToString(imageBitmap);
                            byte[] imageByte = bitmapConverter.BitmapToByteArray(imageBitmap);
                            Bitmap bitmap = bitmapConverter.StringToBitmap(imageString);

                            Log.d(TAG, "imageBitmap 데이터: " + imageBitmap + "\nimageString 데이터 : " + imageString + "\nimageByte 데이터 : " + imageByte + "\n다시 변환시킨 bitmap 데이터 : " + bitmap);

                            Glide.with(getActivity())
                                    .load(imageBitmap)
                                    .into(addImage);

                        }

                    }
                });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add, container, false);

        setView();

        return v;
    }


    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
    }
    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");
    }
    @Override public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                launcher.launch(i);


            }
        });


        addUpload_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wroteContent = addWrote_Edit.getText().toString();

                Log.d(TAG, "서버로 보낼 데이터 :\n아이디-" + sharedInfo + "\n텍스트-" + wroteContent + "\n비트맵이미지-" + imageBitmap);

                insertFeed(sharedInfo,wroteContent,imageBitmap);

            }
        });


        limitText(addWrote_Edit);



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
    }
    @Override public void onDestroyView() {
        Log.d(TAG, "onDestroyView()");
        super.onDestroyView();
    }
    @Override public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();

        homeActivity = null;

    }

    public void setView() {

        addUpload_Btn = v.findViewById(R.id.addUpload_Btn);
        addWrote_Edit = v.findViewById(R.id.add_Wrote);
        addImage = v.findViewById(R.id.addImage_Btn);

        sendData = new Bundle();
        fragment_home = new Fragment_Home();

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences_Kakao = this.getActivity().getSharedPreferences("a5636c0dc6cb43c4ea8f52134f0f1337", MODE_PRIVATE);
        editor_Kakao = sharedPreferences_Kakao.edit();

        sharedInfo = sharedPreferences.getString("로그인","");

        bitmapConverter = new BitmapConverter();

    }


    // 서버에서 데이터 받아옴
    public void insertFeed(String id, String text, Bitmap image) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.insertFeed(id, text, image);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    String rpId = response.body().getId();
                    String rpText = response.body().getTextContent();
                    Bitmap rpImg = response.body().getBitmapImage();

                    Log.d(TAG, "추가된 데이터 : " + rpId + "\n" + rpText + "\n" + rpImg);

                    sendData.putParcelable("image",imageBitmap);

                    homeActivity.goHomeFragment(sendData);
                    Log.d(TAG, "보낸 번들 데이터 : " + sendData);


                } else {
                    Log.d(TAG, "onResponse: 리스폰스 실패");
                }

            }   // onResponse

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!! " + t.getMessage());
            }

        });

    }  // getInfo()


    // EditText 라인 수 제한
    public void limitText(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {

            String previousString = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previousString = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editText.getLineCount() > 7) {

                    editText.setText(previousString);
                    editText.setSelection(editText.length());

                }

            }
        });

    }

}