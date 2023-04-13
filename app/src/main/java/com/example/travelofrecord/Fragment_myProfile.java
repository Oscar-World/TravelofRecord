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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    String edit_memo;

    String user_id;
    String user_nickname;
    String user_memo;
    String user_image;

    DrawerLayout drawerLayout;
    View drawerView;

    Uri uri;

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

        Log.d(TAG, "onCreateView()");

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
                profile_Text.setText("프로필 수정");

                profile_memo.setVisibility(View.GONE);
                profile_Edit.setVisibility(View.VISIBLE);
                profile_Edit.setText(user_memo);
                drawer_Btn.setVisibility(View.GONE);
                editProfileSubmit_Btn.setVisibility(View.VISIBLE);
                profile_Image.setVisibility(View.GONE);
                editProfile_Image.setVisibility(View.VISIBLE);
                profileSelect_Layout.setVisibility(View.GONE);
                touchImage_Image.setVisibility(View.VISIBLE);

                drawerLayout.closeDrawer(drawerView);

            }
        });

        // 프로필 수정 완료 버튼
        editProfileSubmit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profile_Text.setText("프로필");
                edit_memo = profile_Edit.getText().toString();
                Log.d(TAG, "수정된 메시지 : " + edit_memo);

                drawer_Btn.setVisibility(View.VISIBLE);
                editProfileSubmit_Btn.setVisibility(View.GONE);
                profile_Image.setVisibility(View.VISIBLE);
                editProfile_Image.setVisibility(View.GONE);
                profileSelect_Layout.setVisibility(View.VISIBLE);
                touchImage_Image.setVisibility(View.GONE);

                updateProfile(user_nickname,edit_memo, user_image);

            }
        });

        // 로그아웃 버튼
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

        // 회원 탈퇴 버튼
        userQuit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteUser(user_id);

            }
        });


        // 피드 버튼
        photo_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                photo_Btn.setVisibility(View.GONE);
                photo_Block.setVisibility(View.VISIBLE);
                map_Btn.setVisibility(View.VISIBLE);
                map_Block.setVisibility(View.GONE);

            }
        });


        // 지도 버튼
        map_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                map_Btn.setVisibility(View.GONE);
                map_Block.setVisibility(View.VISIBLE);
                photo_Btn.setVisibility(View.VISIBLE);
                photo_Block.setVisibility(View.GONE);

            }
        });

        // 프로필 사진 수정 버튼
        editProfile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_PICK);
                launcher.launch(i);

                Log.d(TAG, "DB로 전송할 데이터 : " + user_nickname + " " + user_image);


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


    // 상태 메시지, 프로필 사진 변경
    public void updateProfile(String nickname, String memo, String image) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.updateMemo(nickname, memo, image);
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

        profileSelect_Layout = v.findViewById(R.id.myProfileSelect_Layout);
        drawer_Btn = v.findViewById(R.id.myProfile_drawerBtn);
        logout_Btn = v.findViewById(R.id.logout_Btn);
        userQuit_Btn = v.findViewById(R.id.userQuit_Btn);
        editProfile_Btn = v.findViewById(R.id.editProfile_Btn);
        editProfileSubmit_Btn = v.findViewById(R.id.editProfileSubmit_Btn);

        profile_Image = v.findViewById(R.id.myProfile_image);
        editProfile_Image = v.findViewById(R.id.myProfileEdit_image);
        touchImage_Image = v.findViewById(R.id.myProfileTouchImage_Image);

        profile_Text = v.findViewById(R.id.profileText);
        profile_nickname = v.findViewById(R.id.myProfile_nickname);
        profile_memo = v.findViewById(R.id.myProfile_memo);
        profile_Edit = v.findViewById(R.id.myProfile_Edit);

        photo_Btn = v.findViewById(R.id.myProfilePhoto_Btn);
        photo_Block = v.findViewById(R.id.myProfilePhoto_Block);
        map_Btn = v.findViewById(R.id.myProfileMap_Btn);
        map_Block = v.findViewById(R.id.myProfileMap_Block);

//        user_nickname = this.getArguments().getString("nickname");
//        user_image = this.getArguments().getString("image");
//        user_memo = this.getArguments().getString("memo");

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


    }

}