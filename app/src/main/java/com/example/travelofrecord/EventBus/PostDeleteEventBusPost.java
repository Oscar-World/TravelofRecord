package com.example.travelofrecord.EventBus;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.greenrobot.eventbus.Subscribe;

public class PostDeleteEventBusPost {

    String TAG = "PostDeleteEventBusPost";
    int post_Num;
    ImageButton button;

    public PostDeleteEventBusPost(int post_Num, ImageButton button) {
        this.post_Num = post_Num;
        this.button = button;
    }

    @Subscribe
    public void onPostDeleteEvent(int[] array) {

        int postNum = array[0];

        Log.d(TAG, "onPostDeleteEvent 들어옴");
        if (post_Num == postNum) {
            Log.d(TAG, "게시글삭제 확인됨1-1 : " + button.getVisibility());
            button.setVisibility(View.GONE);
            Log.d(TAG, "게시글삭제 확인됨1-2 : " + button.getVisibility());
        }

    }

}
