package com.example.travelofrecord.Other;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {

    private static GlobalApplication instance;

    String native_Key;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        native_Key = "a5636c0dc6cb43c4ea8f52134f0f1337";

        // 네이티브 앱 키로 초기화
        KakaoSdk.init(this,native_Key);
    }

}
