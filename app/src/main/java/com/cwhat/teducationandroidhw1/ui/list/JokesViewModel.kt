package com.cwhat.teducationandroidhw1.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwhat.teducationandroidhw1.data.JokesRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class JokesViewModel(private val repository: JokesRepository) :
    ViewModel() {

    private val _errors = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )
    val errors: SharedFlow<String> = _errors

    // So that in case of a network error there is no situation with constant attempts to download
    // jokes
    private var errorOccurred = false

    private val _state = MutableStateFlow<JokesState>(JokesState.Loading)
    val state: StateFlow<JokesState> = _state

    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        observableJokes()
    }

    fun loadJokes() {
        if (errorOccurred) return

        viewModelScope.launch {
            if (_isLoading.value || errorOccurred) return@launch

            _isLoading.value = true
            try {
                repository.loadNextPage()
            } catch (t: Throwable) {
                errorOccurred = true
                _errors.emit(t.message ?: "Load error")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun observableJokes() {
        viewModelScope.launch {
            repository.getJokes().catch { cause ->
                errorOccurred = true
                _errors.emit(cause.message ?: "Load error")
            }.collect { jokes ->
                _state.value =
                    if (jokes.isEmpty()) JokesState.EmptyList
                    else JokesState.ShowJokes(jokes)
            }
        }
    }

}