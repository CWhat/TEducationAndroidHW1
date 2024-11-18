package com.cwhat.teducationandroidhw1.data

import kotlinx.coroutines.flow.Flow

interface JokesRepository {

    suspend fun getJokeById(id: Int): Joke

    suspend fun getJokes(): Flow<List<Joke>>

    suspend fun addJoke(joke: Joke)

}