package com.cwhat.teducationandroidhw1.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cwhat.teducationandroidhw1.di.ViewModelKey
import com.cwhat.teducationandroidhw1.ui.ViewModelFactory
import com.cwhat.teducationandroidhw1.ui.add_joke.AddJokeViewModel
import com.cwhat.teducationandroidhw1.ui.full_view.FullJokeViewModel
import com.cwhat.teducationandroidhw1.ui.list.JokesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PresentationModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(JokesViewModel::class)
    fun bindJokesViewModel(viewModel: JokesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FullJokeViewModel::class)
    fun bindFullJokeViewModel(viewModel: FullJokeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddJokeViewModel::class)
    fun bindAddJokeViewModel(viewModel: AddJokeViewModel): ViewModel

}