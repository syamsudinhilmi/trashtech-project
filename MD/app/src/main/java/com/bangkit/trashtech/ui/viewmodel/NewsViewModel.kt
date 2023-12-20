package com.bangkit.trashtech.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.trashtech.data.api.ApiConfig
import com.bangkit.trashtech.data.response.Article
import com.bangkit.trashtech.data.response.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    val listNews = MutableLiveData<ArrayList<Article>>()
    val detailNews = MutableLiveData<ArrayList<Article>>()

    fun setNews(query:String, apiKey:String){
        val client = ApiConfig.getApiService().getNews(query, apiKey)
        client.enqueue(object: Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if(response.isSuccessful){
                    listNews.postValue(response.body()?.articles)
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
            }

        })
    }

    fun setDetailNews(query:String, apiKey:String){
        val client = ApiConfig.getApiService().detailNews(query, apiKey)
        client.enqueue(object: Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if(response.isSuccessful){
                    detailNews.value = response.body()?.articles
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
            }

        })
    }


}
