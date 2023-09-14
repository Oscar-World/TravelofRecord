package com.example.travelofrecord.EventBus;

import android.util.Log;
import android.widget.TextView;

import com.example.travelofrecord.Adapter.Comment_Adapter;
import com.example.travelofrecord.Data.PostData;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class CommentDeleteEventBus {

    String TAG = "CommentDeleteEventBus";

    ArrayList<PostData> arrayList;
    Comment_Adapter adapter;
    int post_Num;
    int comment_Num;
    int comment_Number;

    public CommentDeleteEventBus(ArrayList<PostData> arrayList, Comment_Adapter adapter, int post_Num, int comment_Num, int comment_Number) {

        this.arrayList = arrayList;
        this.adapter = adapter;
        this.post_Num = post_Num;
        this.comment_Num = comment_Num;
        this.comment_Number = comment_Number;

    }

    @Subscribe
    public void onCommentDeleteEvent(int[] array) {

        int postNum = array[0];
        int position = array[1];
        int commentNum = array[2];
        int commentNumber = array[3];

        Log.d(TAG, "onCommentDeleteEvent1 - postNum : " + post_Num + "," + postNum );
        Log.d(TAG, "onCommentDeleteEvent1 - commentNum : " + commentNum + "," + commentNum );
        Log.d(TAG, "onCommentDeleteEvent1 - position : " + position + "," + position );
        Log.d(TAG, "onCommentDeleteEvent1 - commentNumber : " + commentNumber + "," + commentNumber );

        if (post_Num == postNum & comment_Number == commentNumber) {
            Log.d(TAG, "onCommentDeleteEvent 들어옴");
            arrayList.remove(position);
            adapter.notifyDataSetChanged();

        }



    }

}
