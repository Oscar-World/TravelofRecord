package com.example.travelofrecord.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.travelofrecord.Activity.Post;
import com.example.travelofrecord.Activity.Home;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

import java.util.ArrayList;

public class MyProfile_Adapter extends RecyclerView.Adapter<MyProfile_Adapter.ViewHolder> {

    String TAG = "프로필 어댑터";

    ArrayList<PostData> postData;
    Context context;
    Home home;

    // 레이아웃을 실체화 해줌 - inflate
    @Override
    public MyProfile_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        home = (Home) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_myprofile, parent, false);
        MyProfile_Adapter.ViewHolder viewHolder = new MyProfile_Adapter.ViewHolder(view);

        return viewHolder;
    }


    // 받아온 데이터를 바인딩해줌
    @Override
    public void onBindViewHolder(@NonNull MyProfile_Adapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(postData.get(holder.getAdapterPosition()));
    }

    // 뷰와 데이터를 연결해줌
    public void setItemMyProfile(ArrayList<PostData> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.postData = list;
        Log.d(TAG, "어댑터 리스트 : " + postData);

        notifyDataSetChanged();
    }

    // 리사이클러뷰 리스트 사이즈를 불러옴
    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount() 호출됨");
//        Log.d(TAG, "리스트 사이즈 : " + postData.size());

        return postData.size();

    }


    // 뷰홀더 생성
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView proflie_PostImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            proflie_PostImage = itemView.findViewById(R.id.myProfile_PostImage);

        }

        void onBind(PostData item) {
            Log.d(TAG, "onBind() 호출됨");

            Glide.with(context)
                    .load(ApiClient.serverPostImagePath + item.getPostImage())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(proflie_PostImage);

            proflie_PostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int status = NetworkStatus.getConnectivityStatus(context);
                    Log.d(TAG, "NetworkStatus : " + status);
                    if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                        Intent i = new Intent(context, Post.class);
                        i.putExtra("num", item.getNum());
                        i.putExtra("nickname", item.getPostNickname());
                        i.putExtra("profileImage", item.getProfileImage());
                        i.putExtra("heart", item.getHeart());
                        i.putExtra("commentNum", item.getCommentNum());
                        i.putExtra("location", item.getLocation());
                        i.putExtra("postImage", item.getPostImage());
                        i.putExtra("writing", item.getWriting());
                        i.putExtra("dateCreated", item.getDateCreated());
                        i.putExtra("whoLike", item.getWhoLike());
                        i.putExtra("heartStatus", item.getHeartStatus());

                        context.startActivity(i);

                    }else {
                        Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        } // onBind



    } // ViewHolder


}
