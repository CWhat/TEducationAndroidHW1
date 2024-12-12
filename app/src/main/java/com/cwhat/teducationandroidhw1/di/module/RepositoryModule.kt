package com.cwhat.teducationandroidhw1.di.module

import com.cwhat.teducationandroidhw1.data.repository.WithNetworkAndDbJokesRepository
import com.cwhat.teducationandroidhw1.domain.repository.JokesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindJokeRepository(repository: WithNetworkAndDbJokesRepository): JokesRepository

}