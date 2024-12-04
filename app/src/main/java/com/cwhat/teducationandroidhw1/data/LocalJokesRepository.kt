package com.cwhat.teducationandroidhw1.data

import com.cwhat.teducationandroidhw1.data.db.LocalJokeDao
import com.cwhat.teducationandroidhw1.data.db.toJoke
import com.cwhat.teducationandroidhw1.data.db.toJokes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

class LocalJokesRepository(
    private val localJokeDao: LocalJokeDao,
) {

    private val flowLocalData = MutableStateFlow<List<Joke>?>(null)

    private val context: CoroutineContext = Dispatchers.IO

    private val coroutineScope = CoroutineScope(context)

    private val delayValue = 3.seconds

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
        }
    }

    suspend fun getJokeById(id: Int): Joke = localJokeDao.getJokeById(id).toJoke()

    suspend fun addLocalJoke(joke: Joke) {
        require(joke.type == JokeType.Local) { "joke must be local" }
        localJokeDao.insertJoke(joke.toDbLocalJoke())
    }

    suspend fun addLocalJokes(jokes: List<Joke>) {
        if (jokes.isEmpty())
            return

        require(jokes.all { it.type == JokeType.Local }) { "jokes must be local" }
        localJokeDao.insertJokes(
            jokes.map { it.toDbLocalJoke() }
        )
    }

    fun getJokes(): Flow<List<Joke>> = flowLocalData
        .onSubscription {
            delay(delayValue)
            loadInitData()
        }
        .filterNotNull()

}