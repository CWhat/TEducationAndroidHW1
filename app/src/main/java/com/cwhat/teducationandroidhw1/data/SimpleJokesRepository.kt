package com.cwhat.teducationandroidhw1.data

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

object SimpleJokesRepository : JokesRepository {

    private val initData = emptyList<Joke>()

    private val flowData = MutableStateFlow<List<Joke>?>(null)

    private val context: CoroutineContext = Dispatchers.IO

    private val delayValue = 3.seconds

    private suspend fun loadInitData() {
        withContext(context) {
            if (flowData.value == null) flowData.value = initData
        }
    }

    override suspend fun getJokeById(id: Int): Joke {
        return flowData.filterNotNull().first().find { it.id == id }
            ?: error("Element with id $id not found")
    }

    override suspend fun getJokes(): Flow<List<Joke>> = flowData.onSubscription {
        loadInitData()
        delay(delayValue)
    }.filterNotNull()

    override suspend fun addJoke(joke: Joke) {
        withContext(context) {
            val lastData = flowData.filterNotNull().first()
            val newId = if (lastData.isEmpty()) 0 else (lastData.maxOf { it.id } + 1)
            flowData.value = lastData.toMutableList().apply { add(joke.copy(id = newId)) }
        }
    }

}

fun provideJokesRepository(): JokesRepository = SimpleJokesRepository