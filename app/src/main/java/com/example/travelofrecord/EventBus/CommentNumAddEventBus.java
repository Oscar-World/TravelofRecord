package com.example.travelofrecord.EventBus;

import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

public class CommentNumAddEventBus {

    String TAG = "onCommentNumAddEvent";

    TextView commentNumText;
    int post_Num;

    public CommentNumAddEventBus(TextView commentNumText, int post_Num) {

        this.commentNumText = commentNumText;
        this.post_Num = post_Num;

    }

    @Subscribe
    public void onCommentNumAddEvent(String[] array) {

        Log.d(TAG, "onCommentNumAddEvent 들어옴");

        String commentNum = array[0];
        int num = Integer.parseInt(array[1]);

        if (num == post_Num) {

            commentNumText.setText(commentNum);

        }

    }

}
