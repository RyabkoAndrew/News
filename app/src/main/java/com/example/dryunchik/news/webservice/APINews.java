package com.example.dryunchik.news.webservice;

import com.example.dryunchik.news.model.JSONResponse;
import com.example.dryunchik.news.model.NewsData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dryunchik on 19.04.2018.
 */

public interface APINews {
    @GET("everything?apiKey=7d43b154e45a4b87ba10d300caa9c5cd")
    Call<JSONResponse> getData(@Query("q") String searchQuery);

    @GET("/v2/top-headlines?apiKey=7d43b154e45a4b87ba10d300caa9c5cd")
    Call<JSONResponse>getTopData(@Query("country") String searchQuery);


}
