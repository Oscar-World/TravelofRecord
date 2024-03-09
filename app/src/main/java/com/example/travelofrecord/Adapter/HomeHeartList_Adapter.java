package com.example.travelofrecord.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.travelofrecord.Activity.Profile;
import com.example.travelofrecord.Data.User;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.R;

import java.util.ArrayList;

public class HomeHeartList_Adapter extends RecyclerView.Adapter<HomeHeartList_Adapter.ViewHolder>{

    ArrayList<User> data;
    Context context;


    @Override
    public HomeHeartList_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.heartlist, parent, false);
        HomeHeartList_Adapter.ViewHolder viewHolder = new HomeHeartList_Adapter.ViewHolder(view);

        return viewHolder;

    } // onCreateViewHolder()


    @Override
    public void onBindViewHolder(@NonNull HomeHeartList_Adapter.ViewHolder holder, int position) {
        holder.onBind(data.get(holder.getAdapterPosition()));
    } // onBindViewHolder()


    @Override
    public int getItemCount() {
        return data.size();
    } // getItemCount()


    public void setItem(ArrayList<User> data) {
        this.data = data;
        notifyDataSetChanged();
    } // setItem()


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.heartList_Image);
            textView = itemView.findViewById(R.id.heartList_Text);
            layout = itemView.findViewById(R.id.heartList_Layout);

        }

        void onBind(User item) {

                textView.setText(item.getNickname());

                Glide.with(context)
                        .load(ApiClient.serverProfileImagePath + item.getImage())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(context, Profile.class);
                        i.putExtra("nickname", item.getNickname());
                        context.startActivity(i);

                    }
                });

        } // onBind()

    } // ViewHolder

}
