package com.cwhat.jokes.di.module

import com.cwhat.jokes.data.repository.WithNetworkAndDbJokesRepository
import com.cwhat.jokes.domain.repository.JokesRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindJokeRepository(repository: WithNetworkAndDbJokesRepository): JokesRepository

}