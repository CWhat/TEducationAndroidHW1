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

object SimpleJokesRepository : JokesRepository {

    private val initData = listOf(
        Joke(
            0,
            "logical answer",
            "What does Santa suffer from if he gets stuck in a chimney?",
            "Claustrophobia!",
        ),
        Joke(
            1,
            "life experience",
            "When do you go in through one hole and come out through two holes at the same time?",
            "When you're putting on your trousers.",
        ),
        Joke(
            2,
            "ambiguity",
            "How do you throw an egg on the floor without cracking it?",
            "Unless you have very bad floors, throw the egg any which way. The floor shouldn’t crack from a simple egg throw.",
        ),
        Joke(
            3,
            "logical answer",
            "What disappears the second you start talking about it?",
            "Silence.",
        ),
        Joke(
            4,
            "answer is in the question",
            "What can you see twice in a week or once in every year – but not once in million months?",
            "The letter E.",
        ),
        Joke(
            5,
            "question for attentiveness",
            "You had 20 men build your house in two months. How long would it take 10 men to build the very same house?",
            "Zero seconds. The house was already built by the 20 men.",
        ),
        Joke(
            6,
            "life experience",
            "What goes up when the water comes down?",
            "An umbrella.",
        ),
    )

    private val flowData = MutableStateFlow<List<Joke>?>(null)

    private val context: CoroutineContext = Dispatchers.IO

    private suspend fun loadInitData() {
        withContext(context) {
            if (flowData.value == null) flowData.value = initData
        }
    }

    override suspend fun getJokeById(id: Int): Joke {
        return initData.find { it.id == id } ?: error("Element with id $id not found")
    }

    override suspend fun getJokes(): Flow<List<Joke>> = flowData.onSubscription {
        loadInitData()
        delay(3000)
    }.filterNotNull()

    override suspend fun addJoke(joke: Joke) {
        withContext(context) {
            val lastData = flowData.filterNotNull().first()
            val lastId = lastData.maxOfOrNull { it.id } ?: -1
            flowData.value = lastData.toMutableList().apply { add(joke.copy(id = lastId + 1)) }
        }
    }

}

fun provideJokesRepository(): JokesRepository = SimpleJokesRepository