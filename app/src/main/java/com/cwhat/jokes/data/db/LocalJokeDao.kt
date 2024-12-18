package com.cwhat.jokes.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalJokeDao {

    @Query("SELECT * FROM $LOCAL_JOKES_TABLE ORDER BY $TIMESTAMP_COLUMN DESC")
    fun getAllJokes(): Flow<List<DbLocalJoke>>

    @Insert
    suspend fun insertJoke(joke: DbLocalJoke)

    @Insert
    suspend fun insertJokes(jokes: List<DbLocalJoke>)

    @Query("SELECT * FROM $LOCAL_JOKES_TABLE WHERE id=:id")
    suspend fun getJokeById(id: Int): DbLocalJoke

}