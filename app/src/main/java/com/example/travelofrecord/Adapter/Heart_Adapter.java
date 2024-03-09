package com.example.travelofrecord.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.travelofrecord.Activity.Post;
import com.example.travelofrecord.Function.GetAddress;
import com.example.travelofrecord.Activity.Home;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

import java.util.ArrayList;

public class Heart_Adapter extends RecyclerView.Adapter<Heart_Adapter.ViewHolder> {

    String TAG = "하트 어댑터";
    GetAddress getAddress = new GetAddress();

    int networkStatus;
    ArrayList<PostData> postData;
    Context context;

    Home home;
    Bundle bundle;


    @Override
    public Heart_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        home = (Home) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_heart, parent, false);
        Heart_Adapter.ViewHolder viewHolder = new Heart_Adapter.ViewHolder(view);

        return viewHolder;

    } // onCreateViewHolder()


    @Override
    public void onBindViewHolder(@NonNull Heart_Adapter.ViewHolder holder,int position) {

        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(postData.get(holder.getAdapterPosition()));

    } // onBindViewHolder()


    public void setItemHeart(ArrayList<PostData> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.postData = list;
        notifyDataSetChanged();

    } // setItemHeart()


    @Override
    public int getItemCount() {
        return postData.size();
    } // getItemCount()


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView heart_Location;
        ImageView heart_PostImage;

        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            heart_Location = itemView.findViewById(R.id.heart_LocationText);
            heart_PostImage = itemView.findViewById(R.id.heart_PostImage);
            bundle = new Bundle();
            networkStatus = NetworkStatus.getConnectivityStatus(context);

        }

        void onBind(PostData item) {
            Log.d(TAG, "onBind() 호출됨");


            heart_Location.setText(getAddress.editAddress13(item.getLocation()));

            Glide.with(context)
                    .load(ApiClient.serverPostImagePath + item.getPostImage())
                    .transition(withCrossFade(factory))
                    .placeholder(R.drawable.loading2)
                    .into(heart_PostImage);

            heart_PostImage.setOnClickListener(new View.OnClickListener() {
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


        } // onBind

    } // ViewHolder

} // Heart_Adapter
