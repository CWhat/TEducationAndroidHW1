package com.cwhat.teducationandroidhw1.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WithNetworkAndDbJokesRepository(
    private val remoteRepository: RemoteJokesRepository,
    private val localRepository: LocalJokesRepository,
) : JokesRepository {

    private val context: CoroutineContext = Dispatchers.IO

    override suspend fun getJokeById(id: Int, type: JokeType): Joke {
        return when (type) {
            JokeType.Remote -> remoteRepository.getJokeById(id)
            JokeType.Local -> localRepository.getJokeById(id)
        }
    }

    override suspend fun getJokes(): Flow<List<Joke>> =
        combine(localRepository.getJokes(), remoteRepository.getJokes()) { localList, remoteList ->
            localList + remoteList
        }

    override suspend fun loadNextPage() {
        remoteRepository.loadRemoteJokes()
    }

    override suspend fun addJoke(joke: Joke) {
        withContext(context) {
            when (joke.type) {
                JokeType.Local -> localRepository.addLocalJoke(joke)
                JokeType.Remote -> remoteRepository.addRemoteJoke(joke)
            }
        }
    }

    override suspend fun addJokes(jokes: List<Joke>) {
        withContext(context) {
            if (jokes.isEmpty())
                return@withContext

            if (jokes.all { it.type == JokeType.Remote })
                remoteRepository.addRemoteJokes(jokes)
            else if (jokes.all { it.type == JokeType.Local })
                localRepository.addLocalJokes(jokes)
            else
                super.addJokes(jokes)
        }
    }

}