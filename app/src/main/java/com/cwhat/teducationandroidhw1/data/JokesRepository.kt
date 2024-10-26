package com.cwhat.teducationandroidhw1.data

class JokesRepository {

    fun getJokes(): List<Joke> {
        // TODO fill category
        return listOf(
            Joke(
                0,
                "category",
                "What does Santa suffer from if he gets stuck in a chimney?",
                "Claustrophobia!",
            ),
            Joke(
                1,
                "",
                "When do you go in through one hole and come out through two holes at the same time?",
                "When you're putting on your trousers.",
            ),
            Joke(
                2,
                "",
                "How do you throw an egg on the floor without cracking it?",
                "Unless you have very bad floors, throw the egg any which way. The floor shouldn’t crack from a simple egg throw.",
            ),
            Joke(
                3,
                "",
                "What disappears the second you start talking about it?",
                "Silence.",
            ),
            Joke(
                4,
                "",
                "What can you see twice in a week or once in every year – but not once in million months?",
                "The letter E.",
            ),
            Joke(
                5,
                "",
                "You had 20 men build your house in two months. How long would it take 10 men to build the very same house?",
                "Zero seconds. The house was already built by the 20 men.",
            ),
            Joke(
                6,
                "",
                "What goes up when the water comes down?",
                "An umbrella.",
            ),
        )
    }

}