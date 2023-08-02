package com.example.travelofrecord.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.travelofrecord.Data.Chat;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.R;

import java.util.ArrayList;

public class Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "채팅 어댑터";
    Context context;
    ArrayList<Chat> arrayList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_left, parent, false);
            return new LeftViewHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_right, parent, false);
            return new RightViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_center, parent, false);
            return new CenterViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {

        if (holder instanceof LeftViewHolder) {
            ((LeftViewHolder) holder).onBind(arrayList.get(holder.getAdapterPosition()));
        } else if (holder instanceof  RightViewHolder) {
            ((RightViewHolder) holder).onBind(arrayList.get(holder.getAdapterPosition()));
        } else {
            ((CenterViewHolder) holder).onBind(arrayList.get(holder.getAdapterPosition()));
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

    public void setItemChat(ArrayList<Chat> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    public class CenterViewHolder extends RecyclerView.ViewHolder {

        TextView centerChatDayText;

        public CenterViewHolder(View view) {
            super(view);
            centerChatDayText = view.findViewById(R.id.centerChat_Text);
        }

        void onBind(Chat item) {

            centerChatDayText.setText(item.getDate());

        }

    }



    public class LeftViewHolder extends RecyclerView.ViewHolder {

        ImageView leftChatProfileImage;
        TextView leftChatNicknameText;
        TextView leftChatMessageText;
        TextView leftChatDateText;


        public LeftViewHolder(View view) {
            super(view);
            leftChatProfileImage = view.findViewById(R.id.leftChat_Image);
            leftChatNicknameText = view.findViewById(R.id.leftChatNickname_Text);
            leftChatMessageText = view.findViewById(R.id.leftChatMessage_Text);
            leftChatDateText = view.findViewById(R.id.leftChatDate_Text);
        }

        void onBind(Chat item) {

            if (item.getSenderImage().equals("") | item.getSenderImage() == null) {

                leftChatNicknameText.setText(item.getSender());
                leftChatMessageText.setText(item.getMessage());
                leftChatDateText.setText(item.getDateMessage());


                Glide.with(context)
                        .load(R.drawable.userfull)
                        .into(leftChatProfileImage);

            } else {

                if (item.getDateMessage().equals("")) {
                    leftChatDateText.setVisibility(View.GONE);
                } else {
                    leftChatDateText.setVisibility(View.VISIBLE);
                    leftChatDateText.setText(item.getDateMessage());
                }
                leftChatNicknameText.setText(item.getSender());
                leftChatMessageText.setText(item.getMessage());

                Glide.with(context)
                        .load(ApiClient.serverProfileImagePath + item.getSenderImage())
                        .into(leftChatProfileImage);

            }

        }

    }

    public class RightViewHolder extends RecyclerView.ViewHolder {

        TextView rightChatMessageText;
        TextView rightChatDateText;
        TextView rightChatStatusText;

        public RightViewHolder(View view) {
            super(view);
            rightChatMessageText = view.findViewById(R.id.rightChatMessage_Text);
            rightChatDateText = view.findViewById(R.id.rightChatDate_Text);
            rightChatStatusText = view.findViewById(R.id.rightChatStatus_Text);
        }

        void onBind(Chat item) {

            if (item.getMessageStatus().equals("true")) {
                rightChatStatusText.setVisibility(View.GONE);

            } else if (item.getMessageStatus().equals("false")) {
                rightChatStatusText.setVisibility(View.VISIBLE);
            }

            if (item.getDateMessage().equals("")) {
                rightChatDateText.setVisibility(View.GONE);
            } else {
                rightChatDateText.setVisibility(View.VISIBLE);
                rightChatDateText.setText(item.getDateMessage());
            }

            rightChatMessageText.setText(item.getMessage());

        }

    }

}
