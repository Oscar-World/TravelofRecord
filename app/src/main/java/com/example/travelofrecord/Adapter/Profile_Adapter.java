package com.example.travelofrecord.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

import java.util.ArrayList;

public class Profile_Adapter extends RecyclerView.Adapter<Profile_Adapter.ViewHolder>{

    String TAG = "프로필 어댑터";
    Context context;
    ArrayList<PostData> postData;
    SharedPreferences sharedPreferences;
    String currentNickname;


    @Override
    public Profile_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_profile, parent, false);
        Profile_Adapter.ViewHolder viewHolder = new Profile_Adapter.ViewHolder(view);

        return viewHolder;

    } // onCreateViewHolder()


    @Override
    public void onBindViewHolder(@NonNull Profile_Adapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(postData.get(holder.getAdapterPosition()));
    } // onBindViewHolder()


    public void setItemProfile(ArrayList<PostData> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.postData = list;
        notifyDataSetChanged();

    } // setItemProfile()


    @Override
    public int getItemCount() {
        return postData.size();
    } // getItemCount()


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView proflie_PostImage;
        int networkStatus;

        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            proflie_PostImage = itemView.findViewById(R.id.profile_PostImage);
            sharedPreferences = context.getSharedPreferences("로그인 정보", Context.MODE_PRIVATE);
            currentNickname = sharedPreferences.getString("nickname","");
            networkStatus = NetworkStatus.getConnectivityStatus(context);

        }

        void onBind(PostData item) {

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

                    if(networkStatus == NetworkStatus.TYPE_MOBILE || networkStatus == NetworkStatus.TYPE_WIFI) {

                        Intent i = new Intent(context, Post.class);
                        i.putExtra("num", item.getNum());

                        context.startActivity(i);

                    }else {
                        Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } // onBind()

    } // ViewHolder

}
