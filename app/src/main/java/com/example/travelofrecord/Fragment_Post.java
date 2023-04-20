package com.example.travelofrecord;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class Fragment_Post extends Fragment {

    String TAG = "게시물 프래그먼트";

    View v;

    ImageButton back_Btn;
    TextView post_Nickname;
    ImageView post_ProfileImage;
    TextView post_Location;
    ImageView post_PostImage;
    TextView post_DateCreated;
    TextView post_Writing;
    ImageView post_Heart;
    ImageView post_HeartFull;
    TextView post_HeartNum;
    TextView post_CommentNum;

    int backPosition;
    int num;
    String nickname;
    String profileImage;
    int heart;
    String location;
    String postImage;
    String writing;
    String dateCreated;

    Home home;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_post, container, false);

        setView();

        return v;
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

        back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.fragmentChange(backPosition);

            }
        });

        post_Nickname.setText(nickname);
        post_Location.setText(location);
        post_HeartNum.setText(String.valueOf(heart));
        post_DateCreated.setText(dateCreated);
        post_Writing.setText(writing);

        Glide.with(getActivity())
                .load(profileImage)
                .into(post_ProfileImage);

        Glide.with(getActivity())
                .load(postImage)
                .into(post_PostImage);

    }

    public void setView() {

        back_Btn = v.findViewById(R.id.postBack_Btn);
        post_Nickname = v.findViewById(R.id.post_nickname);
        post_ProfileImage = v.findViewById(R.id.post_profileImage);
        post_PostImage = v.findViewById(R.id.post_postImage);
        post_Writing = v.findViewById(R.id.post_writing);
        post_HeartNum = v.findViewById(R.id.post_heartNumber);
        post_CommentNum = v.findViewById(R.id.post_commentNumber);
        post_DateCreated = v.findViewById(R.id.post_dateCreated);
        post_Heart = v.findViewById(R.id.post_heart);
        post_HeartFull = v.findViewById(R.id.post_heartFull);
        post_Location = v.findViewById(R.id.post_location);

        backPosition = getArguments().getInt("backPosition");
        num = getArguments().getInt("num");
        nickname = getArguments().getString("nickname");
        profileImage = getArguments().getString("profileImage");
        heart = getArguments().getInt("heart");
        location = getArguments().getString("location");
        postImage = getArguments().getString("postImage");
        writing = getArguments().getString("writing");
        dateCreated = getArguments().getString("dateCreated");

        Log.d(TAG, "getArguments : " + getArguments());
        Log.d(TAG, "backPosition : " + backPosition + "\nnum : " + num + "\nnickname : " + nickname + "\nprofileImage : " + profileImage + "\nheart : " + heart
        + "\nlocation : " + location + "\npostImage : " + postImage + "\nwriting : " + writing + "\ndateCreated : " + dateCreated);

        home = (Home) getActivity();

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
    }

}