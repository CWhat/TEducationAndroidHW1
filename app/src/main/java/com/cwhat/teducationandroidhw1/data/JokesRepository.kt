package com.cwhat.teducationandroidhw1.data

interface JokesRepository {

    suspend fun getJokeById(id: Int): Joke

    suspend fun getJokes(): List<Joke>

    suspend fun addJoke(joke: Joke)

}