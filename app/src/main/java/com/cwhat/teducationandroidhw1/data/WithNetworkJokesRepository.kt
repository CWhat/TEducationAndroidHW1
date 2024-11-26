package com.cwhat.teducationandroidhw1.data

import com.cwhat.teducationandroidhw1.data.remote.RemoteApi
import com.cwhat.teducationandroidhw1.data.remote.toJoke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

class WithNetworkJokesRepository(private val api: RemoteApi) : JokesRepository {

    private val initLocalData = emptyList<Joke>()

    private val initRemoteData = emptyList<Joke>()

    private val flowLocalData = MutableStateFlow<List<Joke>?>(null)

    private val flowRemoteData = MutableStateFlow<List<Joke>?>(null)

    private val context: CoroutineContext = Dispatchers.IO

    private val delayValue = 3.seconds

    private var isLoading = false

    private suspend fun loadRemoteJokes() {
        withContext(context) {
            if (isLoading)
                return@withContext

            isLoading = true
            val remoteJokes = api.getJokes().jokes.map { it.toJoke() }
            addJokes(remoteJokes)
            isLoading = false
        }
    }

    private suspend fun loadInitData() {
        withContext(context) {
            if (flowLocalData.value == null) flowLocalData.value = initLocalData
            if (flowRemoteData.value == null) {
                flowRemoteData.value = initRemoteData
                loadRemoteJokes()
            }
        }
    }

    override suspend fun getJokeById(id: Int, type: JokeType): Joke {
        val data = when (type) {
            JokeType.Local -> flowLocalData
            JokeType.Remote -> flowRemoteData
        }
        return data.filterNotNull().first().find { it.id == id }
            ?: error("Element with id $id not found")
    }

    override suspend fun getJokes(): Flow<List<Joke>> = flowLocalData.onSubscription {
        loadInitData()
        delay(delayValue)
    }
        .combine(flowRemoteData) { localList, remoteList ->
            if (localList == null || remoteList == null)
                null
            else
                localList + remoteList
        }
        .filterNotNull()

    private fun generateNewId(jokes: List<Joke>) =
        if (jokes.isEmpty()) 0 else (jokes.maxOf { it.id } + 1)

    private suspend fun addLocalJoke(joke: Joke) {
        withContext(context) {
            require(joke.type == JokeType.Local) { "joke must be local" }
            val lastData = flowLocalData.filterNotNull().first()
            val newId = generateNewId(lastData)
            flowLocalData.value = lastData.toMutableList().apply { add(joke.copy(id = newId)) }
        }
    }

    private fun List<Joke>.mapWithId(startId: Int): List<Joke> =
        mapIndexed { i, joke -> joke.copy(id = startId + i) }

    private suspend fun addLocalJokes(jokes: List<Joke>) {
        if (jokes.isEmpty())
            return

        withContext(context) {
            require(jokes.all { it.type == JokeType.Local }) { "jokes must be local" }
            val lastData = flowLocalData.filterNotNull().first()
            val newStartId = generateNewId(lastData)
            flowLocalData.value =
                lastData.toMutableList().apply { addAll(jokes.mapWithId(newStartId)) }
        }
    }

    private suspend fun addRemoteJoke(joke: Joke) {
        withContext(context) {
            require(joke.type == JokeType.Remote) { "joke must be remote" }
            val lastData = flowRemoteData.filterNotNull().first()
            flowRemoteData.value = lastData.toMutableList().apply { add(joke) }
        }
    }

    private suspend fun addRemoteJokes(jokes: List<Joke>) {
        if (jokes.isEmpty())
            return

        withContext(context) {
            require(jokes.all { it.type == JokeType.Remote }) { "jokes must be remote" }
            val lastData = flowRemoteData.filterNotNull().first()
            flowRemoteData.value =
                lastData.toMutableList().apply { addAll(jokes) }
        }
    }

    override suspend fun addJoke(joke: Joke) {
        withContext(context) {
            when (joke.type) {
                JokeType.Local -> addLocalJoke(joke)
                JokeType.Remote -> addRemoteJoke(joke)
            }
        }
    }

    override suspend fun addJokes(jokes: List<Joke>) {
        withContext(context) {
            if (jokes.isEmpty())
                return@withContext

            if (jokes.all { it.type == JokeType.Remote })
                addRemoteJokes(jokes)
            else if (jokes.all { it.type == JokeType.Local })
                addLocalJokes(jokes)
            else
                super.addJokes(jokes)
        }
    }

}