package com.cwhat.jokes.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cwhat.jokes.domain.entity.Joke
import com.cwhat.jokes.domain.entity.JokeType

@Entity(tableName = REMOTE_JOKES_TABLE)
data class DbRemoteJoke(
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

fun DbRemoteJoke.toJoke() = Joke(
    category = category,
    question = question,
    answer = answer,
    type = JokeType.Remote,
    id = id
)

fun List<DbRemoteJoke>.toJokes(): List<Joke> = this.map { it.toJoke() }
