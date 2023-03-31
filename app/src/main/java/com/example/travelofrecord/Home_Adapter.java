package com.example.travelofrecord;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.ViewHolder> {

    String TAG = "홈 어댑터";

    ArrayList<Item_Post> itemPost;
    Context context;


    // 레이아웃을 실체화 해줌 - inflate
    @Override
    public Home_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_post, parent, false);
        Home_Adapter.ViewHolder viewHolder = new Home_Adapter.ViewHolder(view);

        return viewHolder;
    }


    // 받아온 데이터를 바인딩해줌
    @Override
    public void onBindViewHolder(@NonNull Home_Adapter.ViewHolder holder,int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(itemPost.get(holder.getAdapterPosition()));
    }


    // 리사이클러뷰 리스트 사이즈를 불러옴
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() 호출됨");
        Log.d(TAG, "리스트 사이즈 : " + itemPost.size());

        return itemPost.size();

    }


    // 뷰와 데이터를 연결해줌
    public void setItemPost(ArrayList<Item_Post> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.itemPost = list;
        Log.d(TAG, "어댑터 리스트 : " + itemPost);

        notifyDataSetChanged();
    }




    // 뷰홀더 생성
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView post_ProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            post_ProfileImage = (ImageView) itemView.findViewById(R.id.item_profileImage);



        }

        void onBind(Item_Post item) {
            Log.d(TAG, "onBind() 호출됨");

            Glide.with(context)
                    .load(item.getProfileImage())
                    .into(post_ProfileImage);

        }


    }



}
