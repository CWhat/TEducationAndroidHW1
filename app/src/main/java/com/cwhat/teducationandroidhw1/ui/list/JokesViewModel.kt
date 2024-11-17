package com.cwhat.teducationandroidhw1.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwhat.teducationandroidhw1.data.JokesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JokesViewModel(private val repository: JokesRepository) : ViewModel() {

    private val _state = MutableStateFlow<JokesState>(JokesState.Loading)
    val state: StateFlow<JokesState> = _state

    init {
        observableJokes()
    }

    private fun observableJokes() {
        viewModelScope.launch {
            repository.getJokes().collect { jokes ->
                _state.value =
                    if (jokes.isEmpty()) JokesState.EmptyList
                    else JokesState.ShowJokes(jokes)
            }
        }
    }

}