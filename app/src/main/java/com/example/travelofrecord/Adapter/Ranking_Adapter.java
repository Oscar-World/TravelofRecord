package com.example.travelofrecord.Adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.travelofrecord.Activity.Post;
import com.example.travelofrecord.Activity.Profile;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.Network.ApiClient;
import com.example.travelofrecord.Network.NetworkStatus;
import com.example.travelofrecord.R;

import java.util.ArrayList;

public class Ranking_Adapter extends RecyclerView.Adapter<Ranking_Adapter.ViewHolder>{

    String TAG = "프로필 어댑터";
    Context context;
    ArrayList<PostData> postData;

    @Override
    public Ranking_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.rank_item, parent, false);
        Ranking_Adapter.ViewHolder viewHolder = new Ranking_Adapter.ViewHolder(view);

        return viewHolder;

    } // onCreateViewHolder()


    @Override
    public void onBindViewHolder(@NonNull Ranking_Adapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() 호출됨");
        holder.onBind(postData.get(holder.getAdapterPosition()));
    } // onBindViewHolder()


    public void setRankItem(ArrayList<PostData> list) {
        Log.d(TAG, "setGameList() 호출됨");

        this.postData = list;
        notifyDataSetChanged();

    } // setRankItem()

    @Override
    public int getItemCount() {
        return postData.size();
    } // getItemCount()


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView rankText;
        TextView nickname;
        TextView heartNum;
        ImageView image;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rankText = itemView.findViewById(R.id.rankItem_UserRankText);
            nickname = itemView.findViewById(R.id.rankItem_UserNicknameText);
            heartNum = itemView.findViewById(R.id.rankItem_HeartNumText);
            image = itemView.findViewById(R.id.rankItem_UserImage);
            layout = itemView.findViewById(R.id.rankItem_Layout);

        }

        void onBind(PostData item) {

            rankText.setText(String.valueOf(item.getRank()));
            nickname.setText(item.getPostNickname());
            heartNum.setText(String.valueOf(item.getHeartNum()));

            Glide.with(context)
                    .load(ApiClient.serverProfileImagePath + item.getProfileImage())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(image);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, Profile.class);
                    i.putExtra("nickname", item.getPostNickname());
                    context.startActivity(i);

                }
            });

        } // onBind()

    } // ViewHolder

}

