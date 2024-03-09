package com.example.travelofrecord.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
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


    @Override
    public MyProfile_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        home = (Home) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_myprofile, parent, false);
        MyProfile_Adapter.ViewHolder viewHolder = new MyProfile_Adapter.ViewHolder(view);

        return viewHolder;

    } // onCreateViewHolder()


    @Override
    public void onBindViewHolder(@NonNull MyProfile_Adapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(postData.get(holder.getAdapterPosition()));
    } // onBindViewHolder()


    public void setItemMyProfile(ArrayList<PostData> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.postData = list;
        notifyDataSetChanged();

    } // setItemMyProfile()


    @Override
    public int getItemCount() {
        return postData.size();
    } // getItemCount()


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView proflie_PostImage;

        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            proflie_PostImage = itemView.findViewById(R.id.myProfile_PostImage);

        }

        void onBind(PostData item) {
            Log.d(TAG, "onBind() 호출됨");

            Glide.with(context)
                    .load(ApiClient.serverPostImagePath + item.getPostImage())
                    .transition(withCrossFade(factory))
                    .placeholder(R.drawable.loading2)
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
