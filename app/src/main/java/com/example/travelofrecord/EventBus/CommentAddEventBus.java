package com.example.travelofrecord.EventBus;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelofrecord.Activity.Post;
import com.example.travelofrecord.Adapter.Comment_Adapter;
import com.example.travelofrecord.Data.PostData;
import com.example.travelofrecord.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentAddEventBus {

    String TAG = "CommentAddEventBus";

    ArrayList<PostData> arrayList;
    Comment_Adapter adapter;
    RecyclerView recyclerView;
    int post_Num;

    public CommentAddEventBus(ArrayList<PostData> arrayList, Comment_Adapter adapter, RecyclerView recyclerView, int post_Num) {

        this.arrayList = arrayList;
        this.adapter = adapter;
        this.recyclerView = recyclerView;
        this.post_Num = post_Num;

    }

    @Subscribe
    public void onCommentAddEvent(PostData postData) {

        Log.d(TAG, "onCommentAddEvent 들어옴");

        if (post_Num == postData.getPostNum()) {

            arrayList.add(postData);
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(arrayList.size()-1);

        }

    }

}
