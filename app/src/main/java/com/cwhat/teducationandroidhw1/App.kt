package com.cwhat.teducationandroidhw1

import android.app.Application
import androidx.room.Room
import com.cwhat.teducationandroidhw1.data.db.JOKES_DB
import com.cwhat.teducationandroidhw1.data.db.JokesDatabase
import com.cwhat.teducationandroidhw1.di.DI

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DI.db = Room.databaseBuilder(this, JokesDatabase::class.java, JOKES_DB).build()
    }

}