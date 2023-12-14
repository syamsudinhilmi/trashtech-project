package com.bangkit.trashtech.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface GoogleApiServices {
    @GET
    fun getNearbyPlacesUrl(@Url url: String): Call<String>
}