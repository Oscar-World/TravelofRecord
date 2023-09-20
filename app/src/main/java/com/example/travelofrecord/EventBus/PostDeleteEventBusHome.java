package com.example.travelofrecord.EventBus;

import android.util.Log;
import android.widget.TextView;

import com.example.travelofrecord.Data.PostData;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class PostDeleteEventBusHome {

    String TAG = "PostDeleteEventBusHome";

    ArrayList<PostData> arrayList;
    TextView textView;

    public PostDeleteEventBusHome(ArrayList<PostData> arrayList, TextView textView) {

        this.arrayList = arrayList;
        this.textView = textView;

    }

    @Subscribe
    public void onPostDeleteEvent(int[] array) {
        Log.d(TAG, "onPostDeleteEventHome 들어옴1");

        if (arrayList != null) {
            Log.d(TAG, "onPostDeleteEventHome 들어옴2");
            int postNum = array[0];
            int i;

            for (i = 0; i < arrayList.size(); i++) {

                if (arrayList.get(i).getNum() == postNum) {

                    textView.setText(String.valueOf(i));
                    Log.d(TAG, "textView : " + textView.getText().toString());
                    break;
                }

            }

        }

    }

}
