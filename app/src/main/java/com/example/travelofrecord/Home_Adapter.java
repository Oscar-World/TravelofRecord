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

    // 뷰와 데이터를 연결해줌
    public void setItemPost(ArrayList<Item_Post> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.itemPost = list;
        Log.d(TAG, "어댑터 리스트 : " + itemPost);

        notifyDataSetChanged();
    }

    // 리사이클러뷰 리스트 사이즈를 불러옴
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() 호출됨");
        Log.d(TAG, "리스트 사이즈 : " + itemPost.size());

        return itemPost.size();

    }


    // 뷰홀더 생성
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView post_Nickname;
        ImageView post_ProfileImage;
        TextView post_Location;
        ImageView post_PostImage;
        TextView post_DateCreated;
        TextView post_Writing;
        TextView post_SeeMore;
        TextView post_Summary;
        ImageView post_Heart;
        ImageView post_HeartFull;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            post_Nickname = itemView.findViewById(R.id.item_nickname);
            post_ProfileImage = itemView.findViewById(R.id.item_profileImage);
            post_Location = itemView.findViewById(R.id.item_location);
            post_PostImage = itemView.findViewById(R.id.item_postImage);
            post_Writing = itemView.findViewById(R.id.item_writing);
            post_DateCreated = itemView.findViewById(R.id.item_dateCreated);
            post_SeeMore = itemView.findViewById(R.id.item_seeMore);
            post_Summary = itemView.findViewById(R.id.item_summary);
            post_Heart = itemView.findViewById(R.id.item_heart);
            post_HeartFull = itemView.findViewById(R.id.item_heartFull);

        }

        void onBind(Item_Post item) {
            Log.d(TAG, "onBind() 호출됨");

            post_Nickname.setText(item.getNickname());

            post_ProfileImage.setClipToOutline(true);

            if (!itemView.isLaidOut()) {

                final TextView postTextView = post_Writing;
                post_Writing.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "linecount : " + postTextView.getLineCount());

                        int line = post_Writing.getLineCount();
                        Log.d(TAG, "lineCount : " + line);
                        if (line > 3) {
                            post_Writing.setMaxLines(3);
                            post_Writing.setEllipsize(TextUtils.TruncateAt.END);
                            post_SeeMore.setVisibility(View.VISIBLE);
                        }


                        Glide.with(context)
                                .load(item.getProfileImage())
                                .into(post_ProfileImage);

                        Glide.with(context)
                                .load(item.getPostImage())
                                .into(post_PostImage);

                    }
                });

            }

            post_SeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    post_SeeMore.setVisibility(View.GONE);
                    post_Summary.setVisibility(View.VISIBLE);
                    post_Writing.setMaxLines(100);
                }
            });

            post_Summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    post_SeeMore.setVisibility(View.VISIBLE);
                    post_Summary.setVisibility(View.GONE);
                    post_Writing.setMaxLines(3);
                    post_Writing.setEllipsize(TextUtils.TruncateAt.END);
                }
            });

            post_Heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    post_Heart.setVisibility(View.GONE);
                    post_HeartFull.setVisibility(View.VISIBLE);
                }
            });

            post_HeartFull.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    post_HeartFull.setVisibility(View.GONE);
                    post_Heart.setVisibility(View.VISIBLE);
                }
            });

            post_Location.setText(item.getLocation());

            post_Writing.setText(item.getWriting());

            post_DateCreated.setText(item.getDateCreated());


        }


    }



}
