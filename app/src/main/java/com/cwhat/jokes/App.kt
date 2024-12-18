package com.cwhat.jokes

import android.app.Application
import com.cwhat.jokes.di.AppComponent
import com.cwhat.jokes.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

}