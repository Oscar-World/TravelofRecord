package com.example.travelofrecord.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.travelofrecord.Activity.DirectMessage;
import com.example.travelofrecord.Data.Chat;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.R;

import java.util.ArrayList;

public class ChatRoom_Adapter extends RecyclerView.Adapter<ChatRoom_Adapter.ViewHolder> {

    String TAG = "채팅방 어댑터";
    ArrayList<Chat> arrayList;
    Context context;
    GetTime getTime;

    @Override
    public ChatRoom_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom, parent, false);
        ChatRoom_Adapter.ViewHolder viewHolder = new ChatRoom_Adapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoom_Adapter.ViewHolder holder,int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(arrayList.get(holder.getAdapterPosition()));
    }

    public void setItemChatRoom(ArrayList<Chat> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.arrayList = list;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount() 호출됨");
//        Log.d(TAG, "리스트 사이즈 : " + postData.size());

        return arrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout chatRoomLayout;
        ImageView profileImage;
        TextView nicknameText;
        TextView messageText;
        TextView dateText;
        TextView notReadText;

        boolean userStatus = false;

        Long currentTime = System.currentTimeMillis();
        String currentFormatTime = "";
        String parseFormatTime = "";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatRoomLayout = itemView.findViewById(R.id.chatRoom_Layout);
            profileImage = itemView.findViewById(R.id.chatRoom_Image);
            nicknameText = itemView.findViewById(R.id.chatRoomNickname_Text);
            messageText = itemView.findViewById(R.id.chatRoomMessage_Text);
            dateText = itemView.findViewById(R.id.chatRoomDate_Text);
            notReadText = itemView.findViewById(R.id.chatRoomNotRead_Text);

            getTime = new GetTime();

        }

        void onBind(Chat item) {
            Log.d(TAG, "onBind() 호출됨");
            String dateMessage = item.getLastDate();
            Log.d(TAG, "onBind: " + dateMessage);
            long parseTime = Long.parseLong(dateMessage);
            String time = getTime.getFormatTime1(parseTime);

            if (item.getSenderImage().equals("") | item.getSenderImage() == null) {

                Glide.with(context)
                        .load(R.drawable.userfull)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(profileImage);

                nicknameText.setText(item.getRoomName());
                messageText.setText("(탈퇴한 사용자)");
                dateText.setText("");

                userStatus = false;

            } else {

                Glide.with(context)
                        .load(ApiClient.serverProfileImagePath + item.getSenderImage())
//                        .transition(withCrossFade(factory))
//                        .placeholder(R.drawable.loading2)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(profileImage);

                nicknameText.setText(item.getRoomName());
                messageText.setText(item.getLastMessage());

                currentFormatTime = getTime.getFormatTime3(currentTime);
                parseFormatTime = getTime.getFormatTime3(parseTime);

                if (!currentFormatTime.equals(parseFormatTime)) {

                    if (!currentFormatTime.substring(0,2).equals(parseFormatTime.substring(0,2))) {

                        time = getTime.getFormatTime5(parseTime);

                    } else {

                        time = getTime.getFormatTime4(parseTime);

                    }

                }

                dateText.setText(time);

                userStatus = true;

            }

            if (item.getNotReadMessage() == 0) {
                notReadText.setVisibility(View.GONE);
            } else if (item.getNotReadMessage() > 0){
                notReadText.setVisibility(View.VISIBLE);
                notReadText.setText(String.valueOf(item.getNotReadMessage()));
            } else if (item.getNotReadMessage() > 10) {
                notReadText.setVisibility(View.VISIBLE);
                notReadText.setText("10+");
            }

            chatRoomLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d(TAG, "onClick : " + item.getRoomName());


                    Intent i = new Intent(context, DirectMessage.class);
                    i.putExtra("postNickname", item.getRoomName());
                    i.putExtra("userStatus", userStatus);
                    context.startActivity(i);

                }
            });

        } // onBind

//

    } // ViewHolder

}
