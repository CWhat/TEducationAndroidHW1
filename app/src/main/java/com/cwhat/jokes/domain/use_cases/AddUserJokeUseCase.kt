package com.cwhat.jokes.domain.use_cases

import com.cwhat.jokes.domain.entity.Joke
import com.cwhat.jokes.domain.entity.JokeType
import com.cwhat.jokes.domain.repository.JokesRepository
import javax.inject.Inject

class AddUserJokeUseCase @Inject constructor(
    private val jokesRepository: JokesRepository,
) {

    suspend operator fun invoke(category: String, question: String, answer: String) {
        val joke = Joke(category, question, answer, JokeType.Local)
        jokesRepository.addJoke(joke)
    }

}