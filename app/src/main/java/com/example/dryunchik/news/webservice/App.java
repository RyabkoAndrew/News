package com.example.dryunchik.news.webservice;

import android.app.Application;

import com.example.dryunchik.news.model.JSONResponse;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
     private static APINews mAPINews;
     private Retrofit mRetrofit;



    @Override
    public void onCreate() {
        super.onCreate();

        final String BASE_URL = "https://newsapi.org/v2/";

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15,TimeUnit.SECONDS)
                .writeTimeout(15,TimeUnit.SECONDS)
                .build();


        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        mAPINews = mRetrofit.create(APINews.class);



    }
    public static APINews getApi(){
        return mAPINews;
    }

}
