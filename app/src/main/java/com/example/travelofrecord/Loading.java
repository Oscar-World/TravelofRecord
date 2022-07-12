package com.example.travelofrecord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Loading extends AppCompatActivity {

    String TAG = "로딩 액티비티";

    TextView Title_text;

    Thread inthread;
    Thread outthread;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding);
        Log.d(TAG, "onCreate() 호출");

        setView();

//        Log.d(TAG, "키해시 : " + getKeyHash());

    }

//    // 키해시 얻기
//    public String getKeyHash(){
//        try{
//            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),PackageManager.GET_SIGNATURES);
//            if(packageInfo == null) return null;
//            for(Signature signature: packageInfo.signatures){
//                try{
//                    MessageDigest md = MessageDigest.getInstance("SHA");
//                    md.update(signature.toByteArray());
//                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
//                }catch (NoSuchAlgorithmException e){
//                    Log.w("getKeyHash", "Unable to get MessageDigest. signature="+signature, e);
//                }
//            }
//        }catch(PackageManager.NameNotFoundException e){
//            Log.w("getPackageInfo", "Unable to getPackageInfo");
//        }
//        return null;
//    } // getKeyHash

    public class InThread extends Thread {
        public void run() {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(Loading.this,Home.class);
//            Intent i = new Intent(Loading.this,Start.class);
            startActivity(i);

            finish();
        }
    }

    public class OutThread extends Thread {
        public void run() {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(Loading.this,Start.class);
            startActivity(i);

            finish();
        }
    }

    public void setView() {

        Title_text = findViewById(R.id.Title_text);

        inthread = new InThread();
        outthread = new OutThread();

        sharedPreferences = getSharedPreferences("로그인 정보", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        shared = sharedPreferences.getString("로그인","");

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() 호출");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() 호출됨");

        if (!shared.equals("")) {
            inthread.start();
        } else {
            outthread.start();
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() 호출됨");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() 호출됨");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(TAG, "onRestart() 호출됨");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() 호출됨");
    }


}