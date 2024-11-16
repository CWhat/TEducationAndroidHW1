package com.cwhat.teducationandroidhw1.data

import kotlinx.coroutines.delay

object SimpleJokesRepository : JokesRepository {

    private val data = mutableListOf(
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

    override suspend fun getJokeById(id: Int): Joke {
        return data.find { it.id == id } ?: error("Element with id $id not found")
    }

    override suspend fun getJokes(): List<Joke> {
        delay(3000)
        return data
    }

    override suspend fun addJoke(joke: Joke) {
        val lastId = data.maxOfOrNull { it.id } ?: -1
        data.add(joke.copy(id = lastId + 1))
    }

}

fun provideJokesRepository(): JokesRepository = SimpleJokesRepository