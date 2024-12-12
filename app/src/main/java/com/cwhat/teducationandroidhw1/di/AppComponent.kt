package com.cwhat.teducationandroidhw1.di

import android.content.Context
import com.cwhat.teducationandroidhw1.di.module.DbModule
import com.cwhat.teducationandroidhw1.di.module.NetworkModule
import com.cwhat.teducationandroidhw1.di.module.RepositoryModule
import com.cwhat.teducationandroidhw1.ui.add_joke.AddJokeFragment
import com.cwhat.teducationandroidhw1.ui.full_view.FullJokeFragment
import com.cwhat.teducationandroidhw1.ui.list.JokesListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DbModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(fragment: JokesListFragment)

    fun inject(fragment: FullJokeFragment)

    fun inject(fragment: AddJokeFragment)

    @Component.Factory
    interface AppComponentFactory {

        fun create(@BindsInstance context: Context): AppComponent

    }

}