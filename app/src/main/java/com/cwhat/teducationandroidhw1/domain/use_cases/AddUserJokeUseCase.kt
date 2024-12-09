package com.cwhat.teducationandroidhw1.domain.use_cases

import com.cwhat.teducationandroidhw1.domain.entity.Joke
import com.cwhat.teducationandroidhw1.domain.entity.JokeType
import com.cwhat.teducationandroidhw1.domain.repository.JokesRepository

class AddUserJokeUseCase(
    private val jokesRepository: JokesRepository,
) {

    suspend operator fun invoke(category: String, question: String, answer: String) {
        val joke = Joke(category, question, answer, JokeType.Local)
        jokesRepository.addJoke(joke)
    }

}