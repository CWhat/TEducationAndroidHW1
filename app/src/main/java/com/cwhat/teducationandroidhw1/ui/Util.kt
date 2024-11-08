package com.cwhat.teducationandroidhw1.ui

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cwhat.teducationandroidhw1.data.JokesRepository

inline fun <reified VM : ViewModel> ComponentActivity.jokesViewModels(
    crossinline viewModelCreator: (JokesRepository) -> VM,
) = viewModels<VM> {
    viewModelFactory {
        initializer {
            val repository = JokesRepository()
            viewModelCreator(repository)
        }
    }
}