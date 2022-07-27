package com.example.travelofrecord;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.sdk.user.UserApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_myProfile extends Fragment {

    String TAG = "내 프로필 프래그먼트";

    View v;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_Kakao;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor_Kakao;

    String sharedInfo;

    ImageButton drawer_Btn;
    Button logout_Btn;

    ImageView profile_image;

    TextView profile_nickname;
    TextView profile_memo;

    EditText profile_Edit;

    Button photo_Btn;
    Button photo_Block;
    Button map_Btn;
    Button map_Block;

    Button profile_editBtn;
    String edit_memo;

    String user_type;
    String user_id;
    String user_pw;
    String user_phone;
    String user_nickname;
    String user_memo;
    String user_image;

    DrawerLayout drawerLayout;
    View drawerView;

    Uri uri;
    String imagePath;

    ActivityResultLauncher<Intent> launcher;


    @Override public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");
    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

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
                                    .into(profile_image);

                            imagePath = getRealPathFromUri(uri);

                            updateImage(user_nickname,imagePath);

                            Log.d(TAG, "uri : " + uri + "\nuri.toString : " + uri.toString() + "\nimagePath : " + imagePath);

                        }

                    }
                });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        setView();

        getInfo(sharedInfo);

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

        drawer_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        logout_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            }
        });


        photo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photo_Btn.setVisibility(View.GONE);
                photo_Block.setVisibility(View.VISIBLE);
                map_Btn.setVisibility(View.VISIBLE);
                map_Block.setVisibility(View.GONE);

            }
        });


        map_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                map_Btn.setVisibility(View.GONE);
                map_Block.setVisibility(View.VISIBLE);
                photo_Btn.setVisibility(View.VISIBLE);
                photo_Block.setVisibility(View.GONE);

            }
        });


        profile_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profile_memo.setVisibility(View.GONE);
                profile_Edit.setVisibility(View.VISIBLE);
                profile_editBtn.setVisibility(View.VISIBLE);
                profile_Edit.setText(user_memo);

            }
        });

        profile_editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit_memo = profile_Edit.getText().toString();

                updateMemo(user_nickname,edit_memo);

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_PICK);
                launcher.launch(i);

                Log.d(TAG, "DB로 전송할 데이터 : " + user_nickname + " " + imagePath);


            }
        });




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


    public void getInfo(String id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getInfo(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    user_type = response.body().getType();
                    user_id = response.body().getId();
                    user_pw = response.body().getPw();
                    user_phone = response.body().getPhone();
                    user_nickname = response.body().getNickname();
                    user_memo = response.body().getMemo();
                    user_image = response.body().getImage();

                    Log.d(TAG, "서버에서 전달 받은 코드 : " + user_type + "\n" + user_id + "\n" + user_pw + "\n" + user_phone + "\n" + user_nickname + "\n" + user_memo + "\n" + user_image);


                    if (user_memo == null | "".equals(user_memo)) {
                        profile_Edit.setVisibility(View.VISIBLE);
                        profile_editBtn.setVisibility(View.VISIBLE);
                        profile_memo.setVisibility(View.GONE);
                    } else {
                        profile_memo.setText(user_memo);
                        profile_memo.setVisibility(View.VISIBLE);
                        profile_Edit.setVisibility(View.GONE);
                        profile_editBtn.setVisibility(View.GONE);
                    }

                    profile_nickname.setText(user_nickname);
                    Glide.with(getActivity())
                            .load(user_image)
                            .into(profile_image);


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


    public void updateMemo(String nickname, String memo) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.updateMemo(nickname,memo);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    user_memo = response.body().getMemo();

                    Log.d(TAG, "수정된 메모 : " + user_memo);


                    if (user_memo == null | "".equals(user_memo)) {
                        profile_Edit.setVisibility(View.VISIBLE);
                        profile_editBtn.setVisibility(View.VISIBLE);
                        profile_memo.setVisibility(View.GONE);
                    } else {
                        profile_memo.setVisibility(View.VISIBLE);
                        profile_memo.setText(edit_memo);
                        profile_Edit.setVisibility(View.GONE);
                        profile_editBtn.setVisibility(View.GONE);
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



    public void updateImage(String nickname, String image) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.updateImage(nickname,image);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    user_image = response.body().getImage();

                    Log.d(TAG, "수정된 이미지 데이터 : " + user_image);

                } else {
                    Log.d(TAG, "onResponse: 리스폰스 실패");
                }

            }   // onResponse

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: 에러!!! " + t.getMessage());
            }

        });

    }  // updateImage()




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
    }

    public void setView() {

        drawer_Btn = v.findViewById(R.id.myProfile_drawerBtn);
        logout_Btn = v.findViewById(R.id.logout_Btn);

        profile_image = v.findViewById(R.id.myProfile_image);

        profile_nickname = v.findViewById(R.id.myProfile_nickname);
        profile_memo = v.findViewById(R.id.myProfile_memo);
        profile_Edit = v.findViewById(R.id.myProfile_Edit);

        photo_Btn = v.findViewById(R.id.myProfilePhoto_Btn);
        photo_Block = v.findViewById(R.id.myProfilePhoto_Block);
        map_Btn = v.findViewById(R.id.myProfileMap_Btn);
        map_Block = v.findViewById(R.id.myProfileMap_Block);
        profile_editBtn = v.findViewById(R.id.myProfile_editBtn);

//        user_nickname = this.getArguments().getString("nickname");
//        user_image = this.getArguments().getString("image");
//        user_memo = this.getArguments().getString("memo");

        sharedPreferences = this.getActivity().getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences_Kakao = this.getActivity().getSharedPreferences("a5636c0dc6cb43c4ea8f52134f0f1337", MODE_PRIVATE);
        editor_Kakao = sharedPreferences_Kakao.edit();

        sharedInfo = sharedPreferences.getString("로그인","");

        drawerLayout = v.findViewById(R.id.myProfile_drawerLayout);
        drawerView = v.findViewById(R.id.myProfile_drawer);

    }

}