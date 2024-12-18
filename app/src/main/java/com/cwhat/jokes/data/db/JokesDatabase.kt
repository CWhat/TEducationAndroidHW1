package com.cwhat.jokes.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DbLocalJoke::class, DbRemoteJoke::class], version = 1)
abstract class JokesDatabase : RoomDatabase() {

    abstract fun localJokeDao(): LocalJokeDao

    abstract fun remoteJokeDao(): RemoteJokeDao

}