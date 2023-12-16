package com.bangkit.trashtech.data.api

import com.bangkit.trashtech.data.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/everything")
    fun getNews(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

    @GET("v2/everything")
    fun detailNews(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

}