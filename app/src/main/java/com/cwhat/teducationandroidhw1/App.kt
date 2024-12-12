package com.cwhat.teducationandroidhw1

import android.app.Application
import androidx.room.Room
import com.cwhat.teducationandroidhw1.data.db.JOKES_DB
import com.cwhat.teducationandroidhw1.data.db.JokesDatabase
import com.cwhat.teducationandroidhw1.di.AppComponent
import com.cwhat.teducationandroidhw1.di.DI
import com.cwhat.teducationandroidhw1.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        DI.db = Room.databaseBuilder(this, JokesDatabase::class.java, JOKES_DB).build()
    }

}