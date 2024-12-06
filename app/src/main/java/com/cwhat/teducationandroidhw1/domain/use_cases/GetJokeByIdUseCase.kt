package com.cwhat.teducationandroidhw1.domain.use_cases

import com.cwhat.teducationandroidhw1.domain.entity.Joke
import com.cwhat.teducationandroidhw1.domain.entity.JokeType
import com.cwhat.teducationandroidhw1.domain.repository.JokesRepository

class GetJokeByIdUseCase(
    private val jokesRepository: JokesRepository,
) {

    suspend operator fun invoke(id: Int, type: JokeType): Joke =
        jokesRepository.getJokeById(id, type)

}