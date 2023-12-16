package com.bangkit.trashtech.data.repository

import androidx.lifecycle.LiveData
import com.bangkit.trashtech.data.database.News
import com.bangkit.trashtech.data.database.NewsDao

class NewsRepository(private val newsDao: NewsDao) {

    val allData: LiveData<List<News>> = newsDao.allData()

    suspend fun addNews(news: News){
        newsDao.addNews(news)
    }

    fun getNewsByTitle(title: String): LiveData<List<News>> = newsDao.getNewsByTitle(title)

    suspend fun deleteNewsByTitle(title: String) = newsDao.deleteNewsByTitle(title)
}