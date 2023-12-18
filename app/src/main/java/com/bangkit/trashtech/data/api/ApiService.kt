package com.bangkit.trashtech.data.api

import com.bangkit.trashtech.data.response.MapsResponse
import com.bangkit.trashtech.data.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

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

    @GET
    fun getNearbyPlaces(
        @Url url: String
    ): Call<MapsResponse>

}