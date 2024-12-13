package com.cwhat.teducationandroidhw1.ui.full_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwhat.teducationandroidhw1.domain.entity.Joke
import com.cwhat.teducationandroidhw1.domain.entity.JokeType
import com.cwhat.teducationandroidhw1.domain.use_cases.GetJokeByIdUseCase
import com.cwhat.teducationandroidhw1.ui.mutableEventFlow
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FullJokeViewModel @Inject constructor(private val getJokeByIdUseCase: GetJokeByIdUseCase) :
    ViewModel() {

    private val _joke =
        MutableSharedFlow<Joke>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val joke: SharedFlow<Joke> = _joke

    private val _error = mutableEventFlow<String>()
    val error: SharedFlow<String> = _error

    fun loadJoke(id: Int, type: JokeType) {
        viewModelScope.launch {
            try {
                val loadedJoke = getJokeByIdUseCase(id, type)
                _joke.emit(loadedJoke)
            } catch (t: Throwable) {
                _error.emit(t.message ?: "Loading error occurred")
            }
        }
    }

}