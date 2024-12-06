package com.cwhat.teducationandroidhw1.domain.use_cases

import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.data.JokesRepository
import kotlinx.coroutines.flow.Flow

class ShowListUseCase(
    private val jokesRepository: JokesRepository,
) {

    fun getJokes(): Flow<List<Joke>> = jokesRepository.getJokes()

    suspend fun loadNextPage() = jokesRepository.loadNextPage()

}