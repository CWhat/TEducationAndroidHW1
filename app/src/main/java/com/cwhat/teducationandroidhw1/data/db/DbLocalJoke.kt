package com.cwhat.teducationandroidhw1.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = LOCAL_JOKES_TABLE)
data class DbLocalJoke(
    @ColumnInfo(name = CATEGORY_COLUMN)
    val category: String,
    @ColumnInfo(name = QUESTION_COLUMN)
    val question: String,
    @ColumnInfo(name = ANSWER_COLUMN)
    val answer: String,
    @ColumnInfo(name = TIMESTAMP_COLUMN)
    val timestamp: Long,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COLUMN)
    val id: Int = 0,
)
