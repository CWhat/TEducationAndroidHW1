package com.cwhat.teducationandroidhw1.ui.full_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.data.JokeType
import com.cwhat.teducationandroidhw1.data.JokesRepository
import kotlinx.coroutines.launch

class FullJokeViewModel(private val repository: JokesRepository) : ViewModel() {

    private val _joke = MutableLiveData<Joke>()
    val joke: LiveData<Joke> = _joke

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadJoke(id: Int, type: JokeType) {
        viewModelScope.launch {
            try {
                val loadedJoke = repository.getJokeById(id, type)
                _joke.postValue(loadedJoke)
            } catch (t: Throwable) {
                _error.postValue(t.message ?: "Loading error occurred")
            }
        }
    }

}