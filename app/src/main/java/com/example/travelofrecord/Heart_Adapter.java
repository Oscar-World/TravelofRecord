package com.example.travelofrecord;

import android.content.Context;
import android.text.TextUtils;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Heart_Adapter extends RecyclerView.Adapter<Heart_Adapter.ViewHolder> {

    String TAG = "하트 어댑터";

    ArrayList<Post> post;
    Context context;


    // 레이아웃을 실체화 해줌 - inflate
    @Override
    public Heart_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_heart, parent, false);
        Heart_Adapter.ViewHolder viewHolder = new Heart_Adapter.ViewHolder(view);

        return viewHolder;
    }


    // 받아온 데이터를 바인딩해줌
    @Override
    public void onBindViewHolder(@NonNull Heart_Adapter.ViewHolder holder,int position) {
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

    // 리사이클러뷰 리스트 사이즈를 불러옴
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() 호출됨");
        Log.d(TAG, "리스트 사이즈 : " + post.size());

        return post.size();

    }


    // 뷰홀더 생성
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView heart_Location;
        ImageView heart_PostImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            heart_Location = itemView.findViewById(R.id.heart_LocationText);
            heart_PostImage = itemView.findViewById(R.id.heart_PostImage);

        }

        void onBind(Post item) {
            Log.d(TAG, "onBind() 호출됨");

            heart_Location.setText(item.getLocation());

            Glide.with(context)
                    .load(item.getPostImage())
                    .into(heart_PostImage);


        } // onBind



    } // ViewHolder



} // Heart_Adapter
