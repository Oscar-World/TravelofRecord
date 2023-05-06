package com.example.travelofrecord;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.ViewHolder> {

    String TAG = "홈 어댑터";

    ArrayList<PostData> postData;
    Context context;
    Bundle bundle;
    Home home;

    // 레이아웃을 실체화 해줌 - inflate
    @Override
    public Home_Adapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        home = (Home) context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.feed_post, parent, false);
        Home_Adapter.ViewHolder viewHolder = new Home_Adapter.ViewHolder(view);

        return viewHolder;
    }


    // 받아온 데이터를 바인딩해줌
    @Override
    public void onBindViewHolder(@NonNull Home_Adapter.ViewHolder holder,int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(postData.get(holder.getAdapterPosition()));
    }

    // 뷰와 데이터를 연결해줌
    public void setItemPost(ArrayList<PostData> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.postData = list;
        Log.d(TAG, "어댑터 리스트 : " + postData);

        notifyDataSetChanged();
    }

    // 리사이클러뷰 리스트 사이즈를 불러옴
    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount() 호출됨");
//        Log.d(TAG, "리스트 사이즈 : " + postData.size());

        return postData.size();

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
        TextView post_HeartNum;
        TextView post_CommentNum;

        SharedPreferences sharedPreferences;
        String nickname;

        LinearLayout linearLayout;

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
            post_HeartNum = itemView.findViewById(R.id.item_heartNumber);
            post_CommentNum = itemView.findViewById(R.id.item_commentNumber);

            sharedPreferences = context.getSharedPreferences("로그인 정보", Context.MODE_PRIVATE);
            nickname = sharedPreferences.getString("nickname","");
            bundle = new Bundle();

            linearLayout = itemView.findViewById(R.id.post_LinearLayout);


        }

        void onBind(PostData item) {
            Log.d(TAG, "onBind() 호출됨");
            Log.d(TAG, "item.heartStatus : " + item.heartStatus);
            if (item.heartStatus) {
                post_HeartFull.setVisibility(View.VISIBLE);
                post_Heart.setVisibility(View.GONE);
            } else {
                post_HeartFull.setVisibility(View.GONE);
                post_Heart.setVisibility(View.VISIBLE);
            }


            post_Nickname.setText(item.getPostNickname());
            post_HeartNum.setText(String.valueOf(item.getHeart()));
            post_CommentNum.setText(String.valueOf(item.getCommentNum()));
            post_Location.setText(item.getLocation());
            post_Writing.setText(item.getWriting());
            post_DateCreated.setText(item.getDateCreated());

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

                    int status = NetworkStatus.getConnectivityStatus(context);
                    Log.d(TAG, "NetworkStatus : " + status);
                    if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                        post_Heart.setVisibility(View.GONE);
                        post_HeartFull.setVisibility(View.VISIBLE);
                        item.heartStatus = true;

                        Log.d(TAG, "누르기전 item.getHeart() : " + item.getHeart());
                        item.heart += 1;
                        post_HeartNum.setText(String.valueOf(item.getHeart()));

                        Log.d(TAG, "누른 후 item.getHeart() : " + item.getHeart());

                        insertWhoLike(item.getNum(),nickname, item.getHeart());

                    }else {
                        Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            post_HeartFull.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int status = NetworkStatus.getConnectivityStatus(context);
                    Log.d(TAG, "NetworkStatus : " + status);
                    if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                        post_HeartFull.setVisibility(View.GONE);
                        post_Heart.setVisibility(View.VISIBLE);
                        item.heartStatus = false;

                        Log.d(TAG, "누르기전 item.getHeart() : " + item.getHeart());
                        item.heart -= 1;
                        post_HeartNum.setText(String.valueOf(item.getHeart()));

                        Log.d(TAG, "누른 후 item.getHeart() : " + item.getHeart());

                        deleteWhoLike(item.getNum(), nickname, item.getHeart());

                    }else {
                        Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            post_ProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context,Profile.class);
                    i.putExtra("nickname", item.getPostNickname());
                    context.startActivity(i);

                }
            });

            post_Nickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context,Profile.class);
                    i.putExtra("nickname", item.getPostNickname());
                    context.startActivity(i);

                }
            });

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int status = NetworkStatus.getConnectivityStatus(context);
                    Log.d(TAG, "NetworkStatus : " + status);
                    if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                        Intent i = new Intent(context, Post.class);
                        i.putExtra("num", item.getNum());
                        i.putExtra("nickname", item.getPostNickname());
                        i.putExtra("profileImage", item.getProfileImage());
                        i.putExtra("heart", item.getHeart());
                        i.putExtra("commentNum", item.getCommentNum());
                        i.putExtra("location", item.getLocation());
                        i.putExtra("postImage", item.getPostImage());
                        i.putExtra("writing", item.getWriting());
                        i.putExtra("dateCreated", item.getDateCreated());
                        i.putExtra("whoLike", item.getWhoLike());
                        i.putExtra("heartStatus", item.getHeartStatus());

                        context.startActivity(i);

                    }else {
                        Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }
            });



        } // onBind


        public void insertWhoLike(int postNum, String whoLike, int heart) {

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<PostData> call = apiInterface.insertWhoLike(postNum, whoLike, heart);
            call.enqueue(new Callback<PostData>() {
                @Override
                public void onResponse(Call<PostData> call, Response<PostData> response) {

                    if (response.isSuccessful()) {
                        Log.d(TAG, "insertWhoLike_Response 성공");

                        int rp_postNum = response.body().getPostNum();
                        String rp_whoLike = response.body().getWhoLike();

                        Log.d(TAG, "저장된 데이터 -\nrp_num : " + rp_postNum + "\nrp_heart : " + rp_whoLike);

                    } else {
                        Log.d(TAG, "insertWhoLike_Response 실패");
                    }

                }

                @Override
                public void onFailure(Call<PostData> call, Throwable t) {
                    Log.d(TAG, "onFailure: 실패 " + t);
                }
            });

        } // insertWhoLike


        public void deleteWhoLike(int postNum, String whoLike, int heart) {

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<PostData> call = apiInterface.deleteWhoLike(postNum, whoLike, heart);
            call.enqueue(new Callback<PostData>() {
                @Override
                public void onResponse(Call<PostData> call, Response<PostData> response) {

                    if (response.isSuccessful()) {
                        Log.d(TAG, "insertWhoLike_Response 성공");
                    } else {
                        Log.d(TAG, "insertWhoLike_Response 실패");
                    }

                }

                @Override
                public void onFailure(Call<PostData> call, Throwable t) {
                    Log.d(TAG, "onFailure: 실패 " + t);
                }
            });

        } // deleteWhoLike()


    } // ViewHolder



} // Home_Adapter
