package com.cwhat.jokes.data.repository

import com.cwhat.jokes.data.db.LocalJokeDao
import com.cwhat.jokes.data.db.toJoke
import com.cwhat.jokes.data.db.toJokes
import com.cwhat.jokes.domain.entity.Joke
import com.cwhat.jokes.domain.entity.JokeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalJokesRepository @Inject constructor(
    private val localJokeDao: LocalJokeDao,
) {

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

    fun getJokes(): Flow<List<Joke>> = localJokeDao.getAllJokes()
        .map { dbJokes -> dbJokes.toJokes() }

}