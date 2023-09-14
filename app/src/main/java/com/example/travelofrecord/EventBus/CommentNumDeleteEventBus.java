package com.example.travelofrecord.EventBus;

import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

public class CommentNumDeleteEventBus {

    String TAG = "onCommentNumDeleteEvent";

    TextView commentNumText;
    int post_Num;

    public CommentNumDeleteEventBus(TextView commentNumText, int post_Num) {

        this.commentNumText = commentNumText;
        this.post_Num = post_Num;

    }

    @Subscribe
    public void onCommentNumDeleteEvent(String[] array) {

        Log.d(TAG, "onCommentNumDeleteEvent 들어옴");

        String commentNum = array[0];
        int num = Integer.parseInt(array[1]);
        Log.d(TAG, "commentNum3 : " + commentNum);

        if (num == post_Num) {

            Log.d(TAG, "commentNumText3 : " + commentNumText);
            commentNumText.setText(commentNum);

        }

    }

}
