package com.example.travelofrecord.Adapter;

import android.content.Context;
import android.util.Log;
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

public class Chat_Adapter extends RecyclerView.Adapter<Chat_Adapter.ViewHolder> {

    String TAG = "채팅 어댑터";
    Context context;
    ArrayList<PostData> arrayList;

    @Override
    public Chat_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_box, parent, false);
        Chat_Adapter.ViewHolder viewHolder = new Chat_Adapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_Adapter.ViewHolder holder,int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(arrayList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setItemChat(ArrayList<PostData> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView chatProfileImage;
        TextView chatNicknameText;
        TextView chatMessageText;
        TextView chatDateText;

        public ViewHolder(View view) {
            super(view);

            chatProfileImage = view.findViewById(R.id.chatProfile_Image);
            chatNicknameText = view.findViewById(R.id.chatNickname_Text);
            chatMessageText = view.findViewById(R.id.chatMessage_Text);
            chatDateText = view.findViewById(R.id.chatDate_Text);

        }

        void onBind(PostData item) {

//            chatNicknameText.setText();
//            chatMessageText.setText();
//            chatDateText.setText();
//            Glide.with(context)
//                    .load()
//                    .into(chatProfileImage);

        }

    }

}
