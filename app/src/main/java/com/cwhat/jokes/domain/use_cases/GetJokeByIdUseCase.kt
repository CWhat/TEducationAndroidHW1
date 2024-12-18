package com.cwhat.jokes.domain.use_cases

import com.cwhat.jokes.domain.entity.Joke
import com.cwhat.jokes.domain.entity.JokeType
import com.cwhat.jokes.domain.repository.JokesRepository
import javax.inject.Inject

class GetJokeByIdUseCase @Inject constructor(
    private val jokesRepository: JokesRepository,
) {

    suspend operator fun invoke(id: Int, type: JokeType): Joke =
        jokesRepository.getJokeById(id, type)

}