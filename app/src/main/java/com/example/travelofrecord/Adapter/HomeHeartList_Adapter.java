package com.example.travelofrecord.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

import java.util.ArrayList;

public class HomeHeartList_Adapter extends RecyclerView.Adapter<HomeHeartList_Adapter.ViewHolder>{

    ArrayList<PostData> data;
    Context context;


    @Override
    public HomeHeartList_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.heartlist, parent, false);
        HomeHeartList_Adapter.ViewHolder viewHolder = new HomeHeartList_Adapter.ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull HomeHeartList_Adapter.ViewHolder holder, int position) {
        holder.onBind(data.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setItem(ArrayList<PostData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.heartList_Image);
            textView = itemView.findViewById(R.id.heartList_Text);

        }

        void onBind(PostData item) {

            textView.setText(item.getNickname());

            Glide.with(context)
                    .load(item.getProfileImage())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);

        }

    }

}
