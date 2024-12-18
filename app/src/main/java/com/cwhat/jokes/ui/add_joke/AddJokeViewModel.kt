package com.cwhat.jokes.ui.add_joke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwhat.jokes.R
import com.cwhat.jokes.domain.use_cases.AddUserJokeUseCase
import com.cwhat.jokes.ui.mutableEventFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddJokeViewModel @Inject constructor(private val addUserJokeUseCase: AddUserJokeUseCase) :
    ViewModel() {

    private val _events = mutableEventFlow<AddJokeEvent>()
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