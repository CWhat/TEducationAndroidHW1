package com.cwhat.teducationandroidhw1.data.repository

import com.cwhat.teducationandroidhw1.domain.entity.Joke
import com.cwhat.teducationandroidhw1.domain.entity.JokeType
import com.cwhat.teducationandroidhw1.domain.repository.JokesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class WithNetworkAndDbJokesRepository @Inject constructor(
    private val remoteRepository: RemoteJokesRepository,
    private val localRepository: LocalJokesRepository,
) : JokesRepository {

    override suspend fun getJokeById(id: Int, type: JokeType): Joke {
        return when (type) {
            JokeType.Remote -> remoteRepository.getJokeById(id)
            JokeType.Local -> localRepository.getJokeById(id)
        }
    }

    override fun getJokes(): Flow<List<Joke>> =
        combine(localRepository.getJokes(), remoteRepository.getJokes()) { localList, remoteList ->
            localList + remoteList
        }

    override suspend fun loadNextPage() {
        remoteRepository.loadRemoteJokes()
    }

    override suspend fun addJoke(joke: Joke) {
        require(joke.type == JokeType.Local) { "Only local joke can be added" }
        localRepository.addLocalJoke(joke)
    }

    override suspend fun addJokes(jokes: List<Joke>) {
        if (jokes.isEmpty())
            return

        require(jokes.all { it.type == JokeType.Local }) { "Only local jokes can be added" }
        localRepository.addLocalJokes(jokes)
    }

}