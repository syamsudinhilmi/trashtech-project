package com.bangkit.trashtech.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.trashtech.data.database.News
import com.bangkit.trashtech.data.database.AppDatabase
import com.bangkit.trashtech.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application): AndroidViewModel(application) {
    val allData: LiveData<List<News>>
    private val repository: NewsRepository

    init {
        val newsDao = AppDatabase.getDatabase(application).newsDao()
        repository = NewsRepository(newsDao)
        allData = repository.allData
    }

    fun addNews(news: News){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNews(news)
        }
    }

    fun getNewsByTitle(title: String) = repository.getNewsByTitle(title)

    fun deleteNewsByTitle(title: String) {
      viewModelScope.launch(Dispatchers.IO) {
          repository.deleteNewsByTitle(title)
      }
    }
}