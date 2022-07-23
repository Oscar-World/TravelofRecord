package com.example.travelofrecord;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

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

                            image = imageBitmap.toString();

                            Log.d(TAG, "imageBitmap 데이터: " + imageBitmap);
                            Log.d(TAG, "image 데이터: " + image);


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

                sendData.putString("image", image);

                homeActivity.goHomeFragment(sendData);
                Log.d(TAG, "보낸 번들 데이터 : " + sendData);

            }
        });




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

    }

}