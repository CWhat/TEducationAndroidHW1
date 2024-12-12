package com.cwhat.teducationandroidhw1

import android.app.Application
import com.cwhat.teducationandroidhw1.di.AppComponent
import com.cwhat.teducationandroidhw1.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }

}