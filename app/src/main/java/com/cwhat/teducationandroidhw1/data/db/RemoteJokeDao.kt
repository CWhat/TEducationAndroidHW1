package com.cwhat.teducationandroidhw1.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteJokeDao {

    @Query("SELECT * FROM $REMOTE_JOKES_TABLE ORDER BY $TIMESTAMP_COLUMN ASC")
    fun getAllJokes(): Flow<List<DbRemoteJoke>>

    @Insert
    suspend fun insertJoke(joke: DbRemoteJoke)

    @Insert
    suspend fun insertJokes(jokes: List<DbRemoteJoke>)

    @Query("DELETE FROM $REMOTE_JOKES_TABLE WHERE $TIMESTAMP_COLUMN < :timestamp")
    suspend fun deleteOldJokes(timestamp: Long)

}