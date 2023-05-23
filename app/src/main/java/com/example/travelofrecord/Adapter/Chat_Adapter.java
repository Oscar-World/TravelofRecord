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
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

import java.util.ArrayList;

public class Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "채팅 어댑터";
    Context context;
    ArrayList<PostData> arrayList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_left, parent, false);
            return new LeftViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_right, parent, false);
            return new RightViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {

        if (holder instanceof LeftViewHolder) {
            ((LeftViewHolder) holder).onBind(arrayList.get(holder.getAdapterPosition()));
        } else {
            ((RightViewHolder) holder).onBind(arrayList.get(holder.getAdapterPosition()));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position).getViewType();
    }

    public void setItemChat(ArrayList<PostData> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }



    public class LeftViewHolder extends RecyclerView.ViewHolder {

        ImageView leftChatProfileImage;
        TextView leftChatNicknameText;
        TextView leftChatMessageText;
        TextView leftChatDateText;

        public LeftViewHolder(View view) {
            super(view);
            leftChatProfileImage = view.findViewById(R.id.leftChatProfile_Image);
            leftChatNicknameText = view.findViewById(R.id.leftChatNickname_Text);
            leftChatMessageText = view.findViewById(R.id.leftChatMessage_Text);
            leftChatDateText = view.findViewById(R.id.leftChatDate_Text);
        }

        void onBind(PostData item) {

            leftChatNicknameText.setText(item.getNickname());
            leftChatMessageText.setText(item.getMessage());
            leftChatDateText.setText(item.getDateMessage());
            Glide.with(context)
                    .load(item.getProfileImage())
                    .into(leftChatProfileImage);

        }

    }

    public class RightViewHolder extends RecyclerView.ViewHolder {

        TextView rightChatMessageText;
        TextView rightChatDateText;

        public RightViewHolder(View view) {
            super(view);
            rightChatMessageText = view.findViewById(R.id.rightChatMessage_Text);
            rightChatDateText = view.findViewById(R.id.rightChatDate_Text);
        }

        void onBind(PostData item) {

            rightChatMessageText.setText(item.getMessage());
            rightChatDateText.setText(item.getDateMessage());

        }

    }

}
