package com.cwhat.jokes.data.repository

import android.icu.util.Calendar
import com.cwhat.jokes.data.db.DB_UNDEFINED_ID
import com.cwhat.jokes.data.db.DbLocalJoke
import com.cwhat.jokes.data.db.DbRemoteJoke
import com.cwhat.jokes.domain.entity.Joke
import com.cwhat.jokes.domain.entity.JokeType

fun getCurrentTime(): Long = Calendar.getInstance().timeInMillis

fun Joke.checkUndefinedId(): Int = if (id == Joke.UNDEFINED_ID) DB_UNDEFINED_ID else id

fun Joke.toDbLocalJoke(): DbLocalJoke {
    require(type == JokeType.Local) { "joke must be local" }
    return DbLocalJoke(
        category = category,
        question = question,
        answer = answer,
        timestamp = getCurrentTime(),
        id = checkUndefinedId(),
    )
}

fun Joke.toDbRemoteJoke(): DbRemoteJoke {
    require(type == JokeType.Remote) { "joke must be remote" }
    return DbRemoteJoke(
        category = category,
        question = question,
        answer = answer,
        timestamp = getCurrentTime(),
        id = checkUndefinedId(),
    )
}