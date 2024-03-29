package com.example.travelofrecord.Function;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.travelofrecord.Activity.Home;

public class BackBtn {

    String TAG = "뒤로가기";
    private Activity activity;
    private Toast toast;
    private long backTouchTime = 0;
    String text = "\'뒤로\' 버튼을 한번 더 누르면 종료됩니다.";

    private Home.OnBackPressedListener mBackListener;

    public BackBtn(Activity activity) {
        this.activity = activity;
    }
    public BackBtn(Activity activity, Home.OnBackPressedListener listener) {
        this.activity = activity;
        this.mBackListener = listener;
    }

    public void onBackPressedAtDm() {

        Intent i = new Intent(activity, Home.class);
        i.putExtra("fromDm", "fromDm");
        activity.startActivity(i);
        activity.finish();

    }

    public void onBackPressed() {

        if (System.currentTimeMillis() > backTouchTime + 2000) {
            backTouchTime = System.currentTimeMillis();
            showToast();
            return;
        }

        if (System.currentTimeMillis() <= backTouchTime + 2000) {
            activity.finish();
            toast.cancel();
        }

    }

    public void onBackPressed(String msg) {

        if (System.currentTimeMillis() > backTouchTime + 2000) {
            backTouchTime = System.currentTimeMillis();
            showToast(msg);
            return;
        }

        if (System.currentTimeMillis() <= backTouchTime + 2000) {
            activity.finish();
            toast.cancel();
        }

    }


    private void showToast() {

        toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        toast.show();

    }

    private void showToast(String msg) {

        toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.show();

    }


}
