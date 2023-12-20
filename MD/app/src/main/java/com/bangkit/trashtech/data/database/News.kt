package com.bangkit.trashtech.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class News (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val urlImage: String,
    val urlNews: String,
    val source: String,
    val author: String,
    val publishedAt: String,
    val content: String,
    val isBookmark: Boolean
)