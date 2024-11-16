package com.cwhat.teducationandroidhw1.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.data.JokesRepository
import kotlinx.coroutines.launch

class JokesViewModel(private val repository: JokesRepository) : ViewModel() {

    private val _jokesList = MutableLiveData<List<Joke>>()
    val jokesList: LiveData<List<Joke>> = _jokesList

    fun loadJokes() {
        viewModelScope.launch {
            val jokes = repository.getJokes()
            _jokesList.postValue(jokes)
        }
    }

}