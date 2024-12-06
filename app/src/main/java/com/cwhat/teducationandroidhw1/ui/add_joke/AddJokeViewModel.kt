package com.cwhat.teducationandroidhw1.ui.add_joke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.domain.use_cases.AddUserJokeUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AddJokeViewModel(private val addUserJokeUseCase: AddUserJokeUseCase) : ViewModel() {

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

        viewModelScope.launch {
            addUserJokeUseCase(category = category, question = question, answer = answer)
            _events.emit(AddJokeEvent.NavigateToBack)
        }
    }

}