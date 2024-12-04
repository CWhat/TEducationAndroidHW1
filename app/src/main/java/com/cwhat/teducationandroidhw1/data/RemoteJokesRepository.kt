package com.cwhat.teducationandroidhw1.data

import com.cwhat.teducationandroidhw1.data.db.MILLISECONDS_IN_DAY
import com.cwhat.teducationandroidhw1.data.db.RemoteJokeDao
import com.cwhat.teducationandroidhw1.data.db.toJokes
import com.cwhat.teducationandroidhw1.data.remote.RemoteApi
import com.cwhat.teducationandroidhw1.data.remote.toJoke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

class RemoteJokesRepository(
    private val api: RemoteApi,
    private val remoteJokeDao: RemoteJokeDao,
) {

    private val initRemoteData = emptyList<Joke>()

    private val flowRemoteData = MutableStateFlow<List<Joke>?>(null)

    private val context: CoroutineContext = Dispatchers.IO

    private val delayValue = 3.seconds

    private var isLoading = false

    suspend fun getJokeById(id: Int): Joke {
        val data = flowRemoteData.filterNotNull().first()
        return data.find { it.id == id }
            ?: error("Element with id $id not found")
    }

    private suspend fun loadInitData() {
        withContext(context) {
            if (flowRemoteData.value == null) {
                flowRemoteData.value = initRemoteData
                loadRemoteJokes()
            }
        }
    }

    private suspend fun loadRemoteJokesFromCache() {
        withContext(context) {
            val jokesFromCache = remoteJokeDao.getAllJokes().toJokes()
            val lastData = flowRemoteData.filterNotNull().first()
            val ids = lastData.map { it.id }.toSet()
            val filteredJokes = jokesFromCache.filter { it.id !in ids }
            if (filteredJokes.isEmpty())
                throw EmptyCacheException()
            else
                flowRemoteData.value = lastData.toMutableList().apply { addAll(filteredJokes) }
        }
    }


    suspend fun loadRemoteJokes() {
        withContext(context) {
            if (isLoading)
                return@withContext

            isLoading = true
            try {
                remoteJokeDao.deleteOldJokes(getCurrentTime() - MILLISECONDS_IN_DAY)

                val remoteJokes = api.getJokes().jokes.map { it.toJoke() }
                addRemoteJokes(remoteJokes)
            } catch (t: Throwable) {
                loadRemoteJokesFromCache()
                throw t
            } finally {
                isLoading = false
            }
        }
    }

    suspend fun addRemoteJoke(joke: Joke) {
        withContext(context) {
            require(joke.type == JokeType.Remote) { "joke must be remote" }
            val lastData = flowRemoteData.filterNotNull().first()
            val ids = lastData.map { it.id }.toSet()
            if (joke.id !in ids) {
                flowRemoteData.value = lastData.toMutableList().apply { add(joke) }
                remoteJokeDao.insertJoke(joke.toDbRemoteJoke())
            }
        }
    }

    suspend fun addRemoteJokes(jokes: List<Joke>) {
        if (jokes.isEmpty())
            return

        withContext(context) {
            require(jokes.all { it.type == JokeType.Remote }) { "jokes must be remote" }
            val lastData = flowRemoteData.filterNotNull().first()
            val ids = lastData.map { it.id }.toSet()
            val filteredJokes = jokes.filter { it.id !in ids }
            flowRemoteData.value =
                lastData.toMutableList().apply { addAll(filteredJokes) }
            remoteJokeDao.insertJokes(filteredJokes.map { it.toDbRemoteJoke() })
        }
    }

    fun getJokes(): Flow<List<Joke>> = flowRemoteData
        .onSubscription {
            delay(delayValue)
            loadInitData()
        }
        .filterNotNull()

}