package com.cwhat.jokes.data.remote

import com.cwhat.jokes.domain.entity.Joke
import com.cwhat.jokes.domain.entity.JokeType
import kotlinx.serialization.Serializable

// For two-part jokes
@Serializable
data class RemoteJoke(
    val category: String,
    val type: String,
    val setup: String,
    val delivery: String,
    val flags: Map<String, Boolean>,
    val id: Int,
    val safe: Boolean,
    val lang: String,
)

fun RemoteJoke.toJoke(): Joke = Joke(
    category = category,
    question = setup,
    answer = delivery,
    id = id,
    type = JokeType.Remote,
)
