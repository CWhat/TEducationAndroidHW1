package com.cwhat.teducationandroidhw1.data

import android.icu.util.Calendar
import com.cwhat.teducationandroidhw1.data.db.DB_UNDEFINED_ID
import com.cwhat.teducationandroidhw1.data.db.DbLocalJoke
import com.cwhat.teducationandroidhw1.data.db.DbRemoteJoke
import com.cwhat.teducationandroidhw1.data.db.LocalJokeDao
import com.cwhat.teducationandroidhw1.data.db.MILLISECONDS_IN_DAY
import com.cwhat.teducationandroidhw1.data.db.RemoteJokeDao
import com.cwhat.teducationandroidhw1.data.db.toJokes
import com.cwhat.teducationandroidhw1.data.remote.RemoteApi
import com.cwhat.teducationandroidhw1.data.remote.toJoke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

class WithNetworkAndDbJokesRepository(
    private val api: RemoteApi,
    private val localJokeDao: LocalJokeDao,
    private val remoteJokeDao: RemoteJokeDao,
) : JokesRepository {

    private val initRemoteData = emptyList<Joke>()

    private val flowLocalData = MutableStateFlow<List<Joke>?>(null)

    private val flowRemoteData = MutableStateFlow<List<Joke>?>(null)

    private val context: CoroutineContext = Dispatchers.IO

    private val coroutineScope = CoroutineScope(context)

    private val delayValue = 3.seconds

    private var isLoading = false

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

    private suspend fun loadRemoteJokes() {
        withContext(context) {
            if (isLoading)
                return@withContext

            isLoading = true
            try {
                remoteJokeDao.deleteOldJokes(getCurrentTime() - MILLISECONDS_IN_DAY)

                val remoteJokes = api.getJokes().jokes.map { it.toJoke() }
                addJokes(remoteJokes)
            } catch (t: Throwable) {
                loadRemoteJokesFromCache()
                throw t
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadLocalJokes() {
        coroutineScope.launch {
            localJokeDao.getAllJokes()
                .map { dbJokes -> dbJokes.toJokes() }
                .collect { flowLocalData.value = it }
        }
    }

    private suspend fun loadInitData() {
        withContext(context) {
            if (flowLocalData.value == null) loadLocalJokes()
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
        delay(delayValue)
        loadInitData()
    }
        .combine(flowRemoteData) { localList, remoteList ->
            if (localList == null || remoteList == null)
                null
            else
                localList + remoteList
        }
        .filterNotNull()

    override suspend fun loadNextPage() {
        loadRemoteJokes()
    }

    private fun getCurrentTime(): Long = Calendar.getInstance().timeInMillis

    private fun Joke.checkUndefinedId(): Int = if (id == Joke.UNDEFINED_ID) DB_UNDEFINED_ID else id

    private fun Joke.toDbLocalJoke(): DbLocalJoke {
        require(type == JokeType.Local) { "joke must be local" }
        return DbLocalJoke(
            category = category,
            question = question,
            answer = answer,
            timestamp = getCurrentTime(),
            id = checkUndefinedId(),
        )
    }

    private fun Joke.toDbRemoteJoke(): DbRemoteJoke {
        require(type == JokeType.Remote) { "joke must be remote" }
        return DbRemoteJoke(
            category = category,
            question = question,
            answer = answer,
            timestamp = getCurrentTime(),
            id = checkUndefinedId(),
        )
    }

    private suspend fun addLocalJoke(joke: Joke) {
        require(joke.type == JokeType.Local) { "joke must be local" }
        localJokeDao.insertJoke(joke.toDbLocalJoke())
    }

    private suspend fun addLocalJokes(jokes: List<Joke>) {
        if (jokes.isEmpty())
            return

        require(jokes.all { it.type == JokeType.Local }) { "jokes must be local" }
        localJokeDao.insertJokes(
            jokes.map { it.toDbLocalJoke() }
        )
    }

    private suspend fun addRemoteJoke(joke: Joke) {
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

    private suspend fun addRemoteJokes(jokes: List<Joke>) {
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