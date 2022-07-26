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

    ArrayList<Home_PhotoItem> home_photoItems;
    Context context;


    // 레이아웃을 실체화 해줌 - inflate
    @Override
    public Home_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.home_photo, parent, false);
        Home_Adapter.ViewHolder viewHolder = new Home_Adapter.ViewHolder(view);

        return viewHolder;
    }


    // 받아온 데이터를 바인딩해줌
    @Override
    public void onBindViewHolder(@NonNull Home_Adapter.ViewHolder holder,int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(home_photoItems.get(holder.getAdapterPosition()));
    }


    // 리사이클러뷰 리스트 사이즈를 불러옴
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() 호출됨");
        Log.d(TAG, "리스트 사이즈 : " + home_photoItems.size());

        return home_photoItems.size();

    }


    // 뷰와 데이터를 연결해줌
    public void setHome_photoItems(ArrayList<Home_PhotoItem> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.home_photoItems = list;
        Log.d(TAG, "어댑터 리스트 : " + home_photoItems);

        notifyDataSetChanged();
    }




    // 뷰홀더 생성
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView home_RecyclerViewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            home_RecyclerViewImage = (ImageView) itemView.findViewById(R.id.home_RecyclerViewImage);



        }

        void onBind(Home_PhotoItem item) {
            Log.d(TAG, "onBind() 호출됨");

            Glide.with(context)
                    .load(item.getHomeImage())
                    .into(home_RecyclerViewImage);

        }


    }



}
