package com.cwhat.jokes.di

import android.content.Context
import com.cwhat.jokes.di.module.DbModule
import com.cwhat.jokes.di.module.NetworkModule
import com.cwhat.jokes.di.module.PresentationModule
import com.cwhat.jokes.di.module.RepositoryModule
import com.cwhat.jokes.ui.add_joke.AddJokeFragment
import com.cwhat.jokes.ui.full_view.FullJokeFragment
import com.cwhat.jokes.ui.list.JokesListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DbModule::class,
        RepositoryModule::class,
        PresentationModule::class,
    ]
)
interface AppComponent {

    fun inject(fragment: JokesListFragment)

    fun inject(fragment: FullJokeFragment)

    fun inject(fragment: AddJokeFragment)

    @Component.Factory
    interface AppComponentFactory {

        fun create(@BindsInstance context: Context): AppComponent

    }

}