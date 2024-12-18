package com.cwhat.jokes.domain.use_cases

import com.cwhat.jokes.domain.entity.Joke
import com.cwhat.jokes.domain.repository.JokesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShowListUseCase @Inject constructor(
    private val jokesRepository: JokesRepository,
) {

    fun getJokes(): Flow<List<Joke>> = jokesRepository.getJokes()

    suspend fun loadNextPage() = jokesRepository.loadNextPage()

}