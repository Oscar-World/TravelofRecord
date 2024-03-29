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
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelofrecord.Activity.Profile;
import com.example.travelofrecord.EventBus.CommentDeleteEventBus;
import com.example.travelofrecord.EventBus.CommentNumDeleteEventBus;
import com.example.travelofrecord.Function.GetTime;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHolder> {

    String TAG = "댓글 어댑터";

    ArrayList<PostData> postData;
    Context context;
    TextView commentNumText;
    EventBus eventBusCommentDelete, eventBusCommentNumDelete;


    @Override
    public Comment_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_post, parent, false);
        Comment_Adapter.ViewHolder viewHolder = new Comment_Adapter.ViewHolder(view);

        return viewHolder;

    } // onCreateViewHolder()


    @Override
    public void onBindViewHolder(@NonNull Comment_Adapter.ViewHolder holder,int position) {

        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(postData.get(holder.getAdapterPosition()));

    } // onBindViewHolder()


    public void setItemComment(ArrayList<PostData> list, TextView commentNumText) {

        this.postData = list;
        this.commentNumText = commentNumText;
        notifyDataSetChanged();

    } // setItemComment()


    @Override
    public int getItemCount() {
        return postData.size();
    } // getItemCount()


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView comment_ProfileImage;
        TextView comment_NicknameText, comment_DateCreatedText, comment_CommentText;
        ImageButton comment_MenuBtn;

        ApiInterface apiInterface;
        SharedPreferences sharedPreferences = context.getSharedPreferences("로그인 정보", Context.MODE_PRIVATE);
        String currentNickname = sharedPreferences.getString("nickname","");
        GetTime getTime = new GetTime();
        CommentDeleteEventBus commentDeleteEventBus;
        CommentNumDeleteEventBus commentNumDeleteEventBus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_ProfileImage = itemView.findViewById(R.id.comment_profileImage);
            comment_NicknameText = itemView.findViewById(R.id.comment_nicknameText);
            comment_DateCreatedText = itemView.findViewById(R.id.comment_dateCreatedText);
            comment_CommentText = itemView.findViewById(R.id.commnet_commentText);
            comment_MenuBtn = itemView.findViewById(R.id.comment_menuBtn);

            eventBusCommentDelete = EventBus.getDefault();
            eventBusCommentNumDelete = EventBus.getDefault();

        }

        void onBind(PostData item) {

            commentDeleteEventBus = new CommentDeleteEventBus(postData, Comment_Adapter.this, item.getPostNum(), item.getCommentNum(), item.getCommentNumber());

            if (!eventBusCommentDelete.isRegistered(commentDeleteEventBus)) {
                eventBusCommentDelete.register(commentDeleteEventBus);
            }


            String commentTime = getTime.lastTime(item.getDateComment());

            comment_NicknameText.setText(item.getWhoComment());
            comment_DateCreatedText.setText(commentTime);
            comment_CommentText.setText(item.getComment());

            Glide.with(context)
                    .load(ApiClient.serverProfileImagePath + item.getCommentProfileImage())
                    .into(comment_ProfileImage);

            if (currentNickname.equals(item.getWhoComment())) {
                comment_MenuBtn.setVisibility(View.VISIBLE);
            } else {
                comment_MenuBtn.setVisibility(View.GONE);
            }

            comment_ProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!currentNickname.equals(item.getWhoComment())) {
                        Intent intent = new Intent(context, Profile.class);
                        intent.putExtra("nickname",item.getWhoComment());
                        context.startActivity(intent);
                    }

                }
            });

            comment_NicknameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!currentNickname.equals(item.getWhoComment())) {
                        Intent intent = new Intent(context, Profile.class);
                        intent.putExtra("nickname",item.getWhoComment());
                        context.startActivity(intent);
                    }

                }
            });

            comment_MenuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(context, view);
                    popup.getMenuInflater().inflate(R.menu.comment_delete, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            if (menuItem.getItemId() == R.id.menu_CommentDelete) {

                                item.commentNum = postData.size() - 1;

                                commentNumDeleteEventBus = new CommentNumDeleteEventBus(commentNumText, item.postNum);

                                if (!eventBusCommentNumDelete.isRegistered(commentNumDeleteEventBus)) {
                                    eventBusCommentNumDelete.register(commentNumDeleteEventBus);
                                }

                                deleteComment(item.whoComment, item.dateComment, item.commentNum, item.postNum, item.commentNumber);

                            }

                            return false;
                        }
                    });
                    popup.show();

                    Log.d(TAG, "who : " + item.whoComment + " / date : " + item.dateComment + " / postNum : " + item.postNum + " / commentNum : " + item.commentNum + " / 포지션 : " + getAdapterPosition());

                }
            });


        } // onBind

        public void deleteComment(String whoComment, String dateComment, int commentNum, int postNum, int commentNumber) {

            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<String> call = apiInterface.deleteComment(whoComment, dateComment, commentNum, postNum);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "deleteComment onResponse");
                    if (response.isSuccessful()) {

                        String rpCode = response.body();
                        Log.d(TAG, "onResponse: " + rpCode + " / position : " + getAdapterPosition());

                        Log.d(TAG, "commentNum2 : " + commentNum);
                        int[] array = new int[4];
                        array[0] = postNum;
                        array[1] = getAdapterPosition();
                        array[2] = commentNum;
                        array[3] = commentNumber;

                        eventBusCommentDelete.post(array);

                        String[] commentNumArray = new String[2];
                        commentNumArray[0] = String.valueOf(commentNum);
                        commentNumArray[1] = String.valueOf(postNum);

                        eventBusCommentNumDelete.post(commentNumArray);


                    }
                    Log.d(TAG, "responseFail");
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "deleteComent onFailure : " + t);
                }
            });

        } // deleteComment()

    } // ViewHolder


}