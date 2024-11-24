package com.cwhat.teducationandroidhw1.ui.add_joke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.data.JokeType
import com.cwhat.teducationandroidhw1.data.JokesRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AddJokeViewModel(private val jokesRepository: JokesRepository) : ViewModel() {

    private val _events = MutableSharedFlow<AddJokeEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )
    val events: SharedFlow<AddJokeEvent> = _events

    fun addJoke(category: String, question: String, answer: String) {
        if (category.isEmpty() || question.isEmpty() || answer.isEmpty()) {
            viewModelScope.launch {
                _events.emit(AddJokeEvent.ShowMessage(R.string.empty_fields))
            }
            return
        }

        val joke = Joke(category, question, answer, JokeType.Local)
        viewModelScope.launch {
            jokesRepository.addJoke(joke)
            _events.emit(AddJokeEvent.NavigateToBack)
        }
    }

}