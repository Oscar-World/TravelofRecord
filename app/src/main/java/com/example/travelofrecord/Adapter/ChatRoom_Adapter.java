package com.example.travelofrecord.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelofrecord.Activity.DirectMessage;
import com.example.travelofrecord.Activity.Profile;
import com.example.travelofrecord.Data.Chat;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Function.GetAdress;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        TextView nicknameText;
        TextView messageText;
        TextView dateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatRoomLayout = itemView.findViewById(R.id.chatRoom_Layout);
            nicknameText = itemView.findViewById(R.id.chatRoomNickname_Text);
            messageText = itemView.findViewById(R.id.chatRoomMessage_Text);
            dateText = itemView.findViewById(R.id.chatRoomDate_Text);

            getTime = new GetTime();

        }

        void onBind(Chat item) {
            Log.d(TAG, "onBind() 호출됨");
            String dateMessage = item.getDateMessage();
            Log.d(TAG, "onBind: " + dateMessage);
            long parseTime = Long.parseLong(dateMessage);
            String time = getTime.getFormatTime(parseTime);


            nicknameText.setText(item.getRoomNum() + " 채팅방");
            messageText.setText(item.getMessage());
            dateText.setText(time);

            chatRoomLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, DirectMessage.class);
                    i.putExtra("postNickname", item.getRoomNum());
                    context.startActivity(i);

                }
            });

        } // onBind



    } // ViewHolder

}
