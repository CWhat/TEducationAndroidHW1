package com.cwhat.jokes.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteJokeDao {

    @Query("SELECT * FROM $REMOTE_JOKES_TABLE ORDER BY $TIMESTAMP_COLUMN ASC")
    suspend fun getAllJokes(): List<DbRemoteJoke>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertJoke(joke: DbRemoteJoke)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertJokes(jokes: List<DbRemoteJoke>)

    @Query("DELETE FROM $REMOTE_JOKES_TABLE WHERE $TIMESTAMP_COLUMN < :timestamp")
    suspend fun deleteOldJokes(timestamp: Long)

}