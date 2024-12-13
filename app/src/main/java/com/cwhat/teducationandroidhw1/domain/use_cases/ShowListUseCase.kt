package com.cwhat.teducationandroidhw1.domain.use_cases

import com.cwhat.teducationandroidhw1.domain.entity.Joke
import com.cwhat.teducationandroidhw1.domain.repository.JokesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShowListUseCase @Inject constructor(
    private val jokesRepository: JokesRepository,
) {

    fun getJokes(): Flow<List<Joke>> = jokesRepository.getJokes()

    suspend fun loadNextPage() = jokesRepository.loadNextPage()

}