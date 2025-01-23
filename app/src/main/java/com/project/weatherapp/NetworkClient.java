package com.project.weatherapp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    public static Retrofit retrofit;
    private final static String BASE_URL="http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/";

    public static Retrofit getRetrofitClient(Context context) {
        if(retrofit == null) {
            // TODO : 데이터 통신의 로그를 Logcat에서 확인할 수 있다.
            HttpLoggingInterceptor Interceptor = new HttpLoggingInterceptor();
            Interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .addInterceptor(Interceptor)
                    .build();

            Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson)) // 데이터를 주고 받을 때 모델에 만든 클래스로 사용
                    .build();
        }
        return retrofit;
    }
}
