package com.cwhat.teducationandroidhw1.data

class JokesRepository {

    fun getJokes(): List<Joke> {
        return listOf(
            Joke(
                0,
                "What does Santa suffer from if he gets stuck in a chimney?",
                "Claustrophobia!",
            ),
            // TODO add more
        )
    }

}