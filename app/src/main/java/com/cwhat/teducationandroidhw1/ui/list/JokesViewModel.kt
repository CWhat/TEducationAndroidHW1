package com.cwhat.teducationandroidhw1.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.domain.entity.EmptyCacheException
import com.cwhat.teducationandroidhw1.domain.use_cases.ShowListUseCase
import com.cwhat.teducationandroidhw1.ui.mutableEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch

class JokesViewModel(private val showListUseCase: ShowListUseCase) :
    ViewModel() {

    private val _errors = mutableEventFlow<JokesError>()
    val errors: SharedFlow<JokesError> = _errors

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
                showListUseCase.loadNextPage()
            } catch (t: Throwable) {
                catchException(t)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun catchException(t: Throwable) {
        errorOccurred = true

        _errors.emit(
            JokesError.ErrorWithId(
                if (t is EmptyCacheException) R.string.empty_cache_message
                else R.string.jokes_loaded_from_cache_message
            )
        )
    }

    private fun observableJokes() {
        viewModelScope.launch {
            showListUseCase.getJokes()
                .catch { cause ->
                    catchException(cause)
                    if (_state.value == JokesState.Loading) throw cause
                }
                // If there is a network error on a first load, we need to read a value again to
                // load local jokes
                .retry()
                .collect { jokes ->
                    _state.value =
                        if (jokes.isEmpty()) JokesState.EmptyList
                        else JokesState.ShowJokes(jokes)
                }
        }
    }

}