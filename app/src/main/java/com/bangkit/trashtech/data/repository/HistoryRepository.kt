package com.bangkit.trashtech.data.repository

import androidx.lifecycle.LiveData
import com.bangkit.trashtech.data.database.History
import com.bangkit.trashtech.data.database.HistoryDao

class HistoryRepository(private val historyDao: HistoryDao) {

    val allHistory: LiveData<List<History>> = historyDao.allHistory()

    suspend fun insertHistory(history: History){
        historyDao.insertHistory(history)
    }

    fun getHistoryById(id: Int): LiveData<List<History>> = historyDao.getNewsById(id)

    suspend fun deleteHistoryById(id: Int) = historyDao.deleteHistoryById(id)

    suspend fun deleteAll() = historyDao.deleteAll()
}