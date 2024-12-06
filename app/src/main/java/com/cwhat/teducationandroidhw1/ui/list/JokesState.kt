package com.cwhat.teducationandroidhw1.ui.list

import com.cwhat.teducationandroidhw1.domain.entity.Joke

sealed class JokesState {

    data object Loading : JokesState()

    data object EmptyList : JokesState()

    data class ShowJokes(val jokes: List<Joke>) : JokesState()

}