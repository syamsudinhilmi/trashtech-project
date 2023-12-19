package com.bangkit.trashtech.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(history: History)

    @Query("SELECT * FROM history_table ORDER BY id DESC")
    fun allHistory(): LiveData<List<History>>

    @Query("SELECT * FROM history_table WHERE id = :id")
    fun getNewsById(id: Int): LiveData<List<History>>

    @Query("DELETE FROM history_table WHERE id = :id")
    suspend fun deleteHistoryById(id: Int)

    @Query("DELETE FROM history_table")
    suspend fun deleteAll()

}