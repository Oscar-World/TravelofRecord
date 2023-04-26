package com.example.travelofrecord;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHolder> {

    String TAG = "댓글 어댑터";

    ArrayList<PostData> postData;
    Context context;

    @Override
    public Comment_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_post, parent, false);
        Comment_Adapter.ViewHolder viewHolder = new Comment_Adapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Comment_Adapter.ViewHolder holder,int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(postData.get(holder.getAdapterPosition()));
    }

    public void setItemComment(ArrayList<PostData> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.postData = list;
        Log.d(TAG, "어댑터 리스트 : " + postData);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() 호출됨");
        Log.d(TAG, "리스트 사이즈 : " + postData.size());

        return postData.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView comment_ProfileImage;
        TextView comment_NicknameText;
        TextView comment_DateCreatedText;
        TextView comment_CommentText;
        ImageButton comment_MenuBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_ProfileImage = itemView.findViewById(R.id.comment_profileImage);
            comment_NicknameText = itemView.findViewById(R.id.comment_nicknameText);
            comment_DateCreatedText = itemView.findViewById(R.id.comment_dateCreatedText);
            comment_CommentText = itemView.findViewById(R.id.commnet_commentText);
            comment_MenuBtn = itemView.findViewById(R.id.comment_menuBtn);


        }

        void onBind(PostData item) {
            Log.d(TAG, "onBind() 호출됨");

            comment_NicknameText.setText(item.getWhoComment());
            comment_DateCreatedText.setText(item.getDateComment());
            comment_CommentText.setText(item.getComment());

            Glide.with(context)
                    .load(item.getCommentProfileImage())
                    .into(comment_ProfileImage);

            comment_MenuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                }
            });


        } // onBind



    } // ViewHolder


}