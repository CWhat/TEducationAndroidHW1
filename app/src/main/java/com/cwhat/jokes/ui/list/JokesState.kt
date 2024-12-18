package com.cwhat.jokes.ui.list

import com.cwhat.jokes.domain.entity.Joke

sealed class JokesState {

    data object Loading : JokesState()

    data object EmptyList : JokesState()

    data class ShowJokes(val jokes: List<Joke>) : JokesState()

}