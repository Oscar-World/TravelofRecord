package com.example.travelofrecord.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.travelofrecord.Activity.Post;
import com.example.travelofrecord.Activity.Profile;
import com.example.travelofrecord.EventBus.CommentNumAddEventBus;
import com.example.travelofrecord.EventBus.CommentNumDeleteEventBus;
import com.example.travelofrecord.EventBus.HeartEventBus;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.ApiInterface;
import com.example.travelofrecord.Activity.Home;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "홈 어댑터";

    ArrayList<PostData> postData;
    Context context;
    Bundle bundle;
    Home home;

    EventBus eventBusHeart;
    EventBus eventBusCommentNum;
    EventBus eventBusCommentNumDelete;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        home = (Home) context;

        View view;

        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.feed_post, parent, false);
            return new MainViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.ad, parent, false);
            return new AdViewHolder(view);
        }

    } // onCreateViewHolder()


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {

        if (holder instanceof Home_Adapter.MainViewHolder) {
            ((Home_Adapter.MainViewHolder) holder).onBind(postData.get(holder.getAdapterPosition()));
        } else if (holder instanceof Home_Adapter.AdViewHolder) {
            ((Home_Adapter.AdViewHolder) holder).onBind(postData.get(holder.getAdapterPosition()));
        }

    } // onBindViewHolder()


    public void setItemPost(ArrayList<PostData> list) {
        this.postData = list;
        notifyDataSetChanged();
    } // setItemPost()


    @Override
    public int getItemCount() {
        return postData.size();
    } // getItemCount()


    @Override
    public int getItemViewType(int position) {
        return postData.get(position).getViewType();
    } // getItemViewType()


    private OnItemLongClickListener itemLongClickListener;
    public interface OnItemLongClickListener {
        void onItemLongClick(int postNum);
    }


    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }


    public class MainViewHolder extends RecyclerView.ViewHolder {

        TextView post_Nickname, post_Location, post_DateCreated, post_Writing, post_SeeMore, post_Summary, post_HeartNum, post_CommentNum;
        ImageView post_ProfileImage, post_PostImage, post_Heart, post_HeartFull;
        LinearLayout linearLayout;

        SharedPreferences sharedPreferences;
        String nickname;
        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        HeartEventBus heartEventBus;
        CommentNumAddEventBus commentNumAddEventBus;
        CommentNumDeleteEventBus commentNumDeleteEventBus;


        public MainViewHolder(@NonNull View itemView) {
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

            eventBusHeart = EventBus.getDefault();
            eventBusCommentNum = EventBus.getDefault();
            eventBusCommentNumDelete = EventBus.getDefault();

        }

        void onBind(PostData item) {

            heartEventBus = new HeartEventBus(post_HeartNum, post_Heart, post_HeartFull, postData.get(getAdapterPosition()).getNum());
            commentNumAddEventBus = new CommentNumAddEventBus(post_CommentNum, postData.get(getAdapterPosition()).getNum());
            commentNumDeleteEventBus = new CommentNumDeleteEventBus(post_CommentNum, item.getPostNum());

            if (!eventBusHeart.isRegistered(heartEventBus)) {
                eventBusHeart.register(heartEventBus);
            }
            if (!eventBusCommentNum.isRegistered(commentNumAddEventBus)) {
                eventBusCommentNum.register(commentNumAddEventBus);
            }
            if (!eventBusCommentNumDelete.isRegistered(commentNumDeleteEventBus)) {
                eventBusCommentNumDelete.register(commentNumDeleteEventBus);
            }

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
                                .load(ApiClient.serverProfileImagePath + item.getProfileImage())
                                .into(post_ProfileImage);

                        Glide.with(context)
                                .load(ApiClient.serverPostImagePath + item.getPostImage())
                                .transition(withCrossFade(factory))
                                .placeholder(R.drawable.loading2)
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

                        insertWhoLike(item.getNum(),nickname, item.getHeart(), String.valueOf(System.currentTimeMillis()));

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

                    Intent i = new Intent(context, Profile.class);
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
                        i.putExtra("position", getAdapterPosition());

                        context.startActivity(i);

                    }else {
                        Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            post_Heart.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        if (itemLongClickListener != null) {

                            itemLongClickListener.onItemLongClick(item.getNum());

                        }

                    }

                    return true;
                }
            });

            post_HeartFull.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        if (itemLongClickListener != null) {

                            itemLongClickListener.onItemLongClick(item.getNum());

                        }

                    }

                    return true;
                }
            });


        } // onBind


        public void insertWhoLike(int postNum, String whoLike, int heart, String time) {

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<String> call = apiInterface.insertWhoLike(postNum, whoLike, heart, time);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "insertWhoLike_Response 성공");

                        String rpCode = response.body();

                        if (rpCode.equals("noData")) {

//                            noDataDlg();

                        } else {
                            Log.d(TAG, "onResponse : 좋아요 추가 완료");
                        }

                    } else {
                        Log.d(TAG, "insertWhoLike_Response 실패");
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "onFailure: 실패 " + t);
                }
            });

        } // insertWhoLike


        public void deleteWhoLike(int postNum, String whoLike, int heart) {

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<String> call = apiInterface.deleteWhoLike(postNum, whoLike, heart);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    if (response.isSuccessful()) {
                        Log.d(TAG, "insertWhoLike_Response 성공");

                        String rpCode = response.body();

                        if (rpCode.equals("noData")) {

//                            noDataDlg();

                        } else {
                            Log.d(TAG, "onResponse : 좋아요 삭제 완료");
                        }

                    } else {
                        Log.d(TAG, "insertWhoLike_Response 실패");
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "onFailure: 실패 " + t);
                }
            });

        } // deleteWhoLike()


    } // MainViewHolder


    public class AdViewHolder extends RecyclerView.ViewHolder {

        TextView ad_NicknameText;
        TextView ad_WriteText;
        ImageView ad_ProfileImage;
        ImageView ad_PostImage;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            ad_NicknameText = itemView.findViewById(R.id.ad_nickname);
            ad_WriteText = itemView.findViewById(R.id.ad_writing);
            ad_ProfileImage = itemView.findViewById(R.id.ad_profileImage);
            ad_PostImage = itemView.findViewById(R.id.ad_postImage);

        }

        void onBind(PostData item) {
            Log.d(TAG, "AdViewHolder : " + item.getProfileImage() + " / " + item.getPostImage());

            ad_NicknameText.setText(item.getPostNickname());
            ad_WriteText.setText(item.getWriting());

            Glide.with(context)
                    .load(Integer.parseInt(item.getProfileImage()))
                    .into(ad_ProfileImage);

            Glide.with(context)
                    .load(Integer.parseInt(item.getPostImage()))
                    .into(ad_PostImage);

        } // onBind()

    } // AdViewHolder


} // Home_Adapter
