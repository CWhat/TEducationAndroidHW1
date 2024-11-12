package com.cwhat.teducationandroidhw1.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cwhat.teducationandroidhw1.data.JokesRepository

inline fun <reified VM : ViewModel> Fragment.jokesViewModels(
    crossinline viewModelCreator: (JokesRepository) -> VM,
) = viewModels<VM> {
    viewModelFactory {
        initializer {
            val repository = JokesRepository()
            viewModelCreator(repository)
        }
    }
}