package com.cwhat.teducationandroidhw1.data.repository

import com.cwhat.teducationandroidhw1.data.db.LocalJokeDao
import com.cwhat.teducationandroidhw1.data.db.toJoke
import com.cwhat.teducationandroidhw1.data.db.toJokes
import com.cwhat.teducationandroidhw1.domain.entity.Joke
import com.cwhat.teducationandroidhw1.domain.entity.JokeType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.time.Duration.Companion.seconds

class LocalJokesRepository(
    private val localJokeDao: LocalJokeDao,
) {

    private val delayValue = 3.seconds

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
        .onStart { delay(delayValue) }

}