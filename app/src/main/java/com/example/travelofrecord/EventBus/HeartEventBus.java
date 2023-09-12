package com.example.travelofrecord.EventBus;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HeartEventBus {

    String TAG = "onEventt";

    TextView textView;
    ImageView heartIv;
    ImageView heartFullIv;
    int post_Num;

    public HeartEventBus(TextView heartNumText, ImageView heartIv, ImageView heartFullIv, int post_Num) {

        this.textView = heartNumText;
        this.heartIv = heartIv;
        this.heartFullIv = heartFullIv;
        this.post_Num = post_Num;

    }

    @Subscribe
    public void onHeartEvent(String[] array) {

        Log.d(TAG, "onEvent1 들어옴");
        String heartNum = array[0];
        boolean heartStatus = Boolean.parseBoolean(array[1]);
        int num = Integer.parseInt(array[2]);

        Log.d(TAG, "num : " + num + " / post_Num : " + post_Num);

        if (post_Num == num) {
            Log.d(TAG, "num == post_Num");
            textView.setText(heartNum);

            if (heartStatus) {
                heartIv.setVisibility(View.GONE);
                heartFullIv.setVisibility(View.VISIBLE);
            } else {
                heartFullIv.setVisibility(View.GONE);
                heartIv.setVisibility(View.VISIBLE);
            }

        }

    }

}
