package com.cwhat.jokes.domain.entity

data class Joke(
    val category: String,
    val question: String,
    val answer: String,
    val type: JokeType,
    val id: Int = UNDEFINED_ID,
) {

    companion object {
        const val UNDEFINED_ID = -1
    }

}
