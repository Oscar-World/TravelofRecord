package com.example.travelofrecord;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


// 레트로핏 클라이언트
public class ApiClient {

    private static final String BASE_URL = "http://3.34.246.77/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {

            Log.d("레트로핏", "getApiClient: 레트로핏 재생성");

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;

    }

}
