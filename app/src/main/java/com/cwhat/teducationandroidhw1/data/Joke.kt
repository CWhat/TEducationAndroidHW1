package com.cwhat.teducationandroidhw1.data

data class Joke(
    val category: String,
    val question: String,
    val answer: String,
    val id: Int = UNDEFINED_ID,
) {

    companion object {
        const val UNDEFINED_ID = -1
    }

}
