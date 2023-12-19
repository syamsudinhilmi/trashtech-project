package com.bangkit.trashtech.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.trashtech.data.database.History
import com.bangkit.trashtech.data.database.AppDatabase
import com.bangkit.trashtech.data.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application): AndroidViewModel(application) {
    val allHistory: LiveData<List<History>>
    private val repository: HistoryRepository

    init {
        val historyDao = AppDatabase.getDatabase(application).historyDao()
        repository = HistoryRepository(historyDao)
        allHistory = repository.allHistory
    }

    fun insertHistory(history: History){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertHistory(history)
        }
    }

    fun getHistoryById(id: Int) = repository.getHistoryById(id)

    fun deleteHistoryById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHistoryById(id)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}