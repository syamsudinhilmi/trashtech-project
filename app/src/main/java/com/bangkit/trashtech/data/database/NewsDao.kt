package com.bangkit.trashtech.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNews(news: News)

    @Query("SELECT * FROM news_table")
    fun allData(): LiveData<List<News>>

    @Query("SELECT * FROM news_table WHERE title = :title")
    fun getNewsByTitle(title: String): LiveData<List<News>>

    @Query("DELETE FROM news_table WHERE title = :title")
    suspend fun deleteNewsByTitle(title: String)

}