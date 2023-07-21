package com.example.travelofrecord.Function;

import android.app.Activity;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class BackBtn {
    private Activity activity;
    private Toast toast;
    private long backTouchTime = 0;
    String text = "\'뒤로\' 버튼을 한번 더 누르면 종료됩니다.";

    public BackBtn(Activity activity) {
        this.activity = activity;
    }

    public void onBackTouched() {

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

    public void onBackTouched(String msg) {

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

    public void onBackTouched(RecyclerView recyclerView) {

        if (System.currentTimeMillis() > backTouchTime + 2000) {
            backTouchTime = System.currentTimeMillis();
            recyclerView.smoothScrollToPosition(0);
            showToast();
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
