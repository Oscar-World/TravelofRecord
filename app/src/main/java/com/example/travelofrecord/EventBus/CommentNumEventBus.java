package com.example.travelofrecord.EventBus;

import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

public class CommentNumEventBus {

    String TAG = "onCommentNumEvent";

    TextView commentNumText;
    int post_Num;

    public CommentNumEventBus(TextView commentNumText, int post_Num) {

        this.commentNumText = commentNumText;
        this.post_Num = post_Num;

    }

    @Subscribe
    public void onCommentNumEvent(String[] array) {

        Log.d(TAG, "onCommentNumEvent 들어옴");

        String commentNum = array[0];
        int num = Integer.parseInt(array[1]);

        if (num == post_Num) {

            commentNumText.setText(commentNum);

        }

    }

}
