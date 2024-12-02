package com.cwhat.teducationandroidhw1.data

import kotlinx.coroutines.flow.Flow

interface JokesRepository {

    suspend fun getJokeById(id: Int, type: JokeType): Joke

    suspend fun getJokes(): Flow<List<Joke>>

    suspend fun loadNextPage()

    suspend fun addJoke(joke: Joke)

    suspend fun addJokes(jokes: List<Joke>) {
        for (joke in jokes)
            addJoke(joke)
    }

}