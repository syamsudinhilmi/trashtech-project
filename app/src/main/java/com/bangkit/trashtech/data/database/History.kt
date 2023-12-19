package com.bangkit.trashtech.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val urlImage: String,
    val label: String,
    val date: String,
    var recommendation: String = ""
)