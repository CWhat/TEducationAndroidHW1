package com.cwhat.jokes.di.module

import android.content.Context
import androidx.room.Room
import com.cwhat.jokes.data.db.JOKES_DB
import com.cwhat.jokes.data.db.JokesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideJokesDatabase(context: Context): JokesDatabase =
        Room.databaseBuilder(context, JokesDatabase::class.java, JOKES_DB).build()

    @Provides
    @Singleton
    fun provideLocalJokeDao(db: JokesDatabase) = db.localJokeDao()

    @Provides
    @Singleton
    fun provideRemoteJokeDao(db: JokesDatabase) = db.remoteJokeDao()

}