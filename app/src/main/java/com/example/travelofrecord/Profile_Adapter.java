package com.example.travelofrecord;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Profile_Adapter extends RecyclerView.Adapter<Profile_Adapter.ViewHolder> {

    String TAG = "프로필 어댑터";

    ArrayList<Post> post;
    Context context;
    Bundle bundle;
    Home home;

    // 레이아웃을 실체화 해줌 - inflate
    @Override
    public Profile_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        home = (Home) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_profile, parent, false);
        Profile_Adapter.ViewHolder viewHolder = new Profile_Adapter.ViewHolder(view);

        return viewHolder;
    }


    // 받아온 데이터를 바인딩해줌
    @Override
    public void onBindViewHolder(@NonNull Profile_Adapter.ViewHolder holder,int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(post.get(holder.getAdapterPosition()));
    }

    // 뷰와 데이터를 연결해줌
    public void setItemHeart(ArrayList<Post> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.post = list;
        Log.d(TAG, "어댑터 리스트 : " + post);

        notifyDataSetChanged();
    }

//    public void setBundle(Bundle bundle) {
//        this.bundle = bundle;
//        notifyDataSetChanged();
//    }

    // 리사이클러뷰 리스트 사이즈를 불러옴
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() 호출됨");
        Log.d(TAG, "리스트 사이즈 : " + post.size());

        return post.size();

    }


    // 뷰홀더 생성
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView proflie_PostImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            proflie_PostImage = itemView.findViewById(R.id.profile_PostImage);
            bundle = new Bundle();

        }

        void onBind(Post item) {
            Log.d(TAG, "onBind() 호출됨");

            Glide.with(context)
                    .load(item.getPostImage())
                    .into(proflie_PostImage);

            proflie_PostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bundle.putInt("num", item.getNum());
                    bundle.putString("nickname", item.getNickname());
                    bundle.putString("profileImage", item.getProfileImage());
                    bundle.putInt("heart", item.getHeart());
                    bundle.putString("location", item.getLocation());
                    bundle.putString("postImage", item.getPostImage());
                    bundle.putString("writing", item.getWriting());
                    bundle.putString("dateCreated", item.getDateCreated());
                    bundle.putInt("backPosition", 3);

                    home.goPostFragment(bundle);

                }
            });


        } // onBind



    } // ViewHolder


}
