package com.cwhat.teducationandroidhw1.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwhat.teducationandroidhw1.data.JokesRepository
import com.cwhat.teducationandroidhw1.data.remote.RemoteApi
import com.cwhat.teducationandroidhw1.data.remote.toJoke
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JokesViewModel(private val repository: JokesRepository, private val api: RemoteApi) :
    ViewModel() {

    private val _state = MutableStateFlow<JokesState>(JokesState.Loading)
    val state: StateFlow<JokesState> = _state

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        observableJokes()
    }

    fun loadJokes() {
        viewModelScope.launch {
            if (_isLoading.value)
                return@launch

            _isLoading.value = true
            val remoteJokes = api.getJokes().jokes.map { it.toJoke() }

            repository.addJokes(remoteJokes)
            _isLoading.value = false
        }
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