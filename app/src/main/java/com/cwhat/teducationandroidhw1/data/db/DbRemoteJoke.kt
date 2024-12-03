package com.cwhat.teducationandroidhw1.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.data.JokeType

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

fun List<DbRemoteJoke>.toJokes(): List<Joke> = this.map { joke ->
    with(joke) {
        Joke(
            category = category,
            question = question,
            answer = answer,
            type = JokeType.Remote,
            id = id
        )
    }
}
