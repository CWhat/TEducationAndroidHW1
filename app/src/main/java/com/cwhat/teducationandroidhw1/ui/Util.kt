package com.cwhat.teducationandroidhw1.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.data.JokeType
import com.cwhat.teducationandroidhw1.data.JokesRepository
import com.cwhat.teducationandroidhw1.di.DI

inline fun <reified VM : ViewModel> Fragment.jokesViewModels(
    crossinline viewModelCreator: (JokesRepository) -> VM,
) = viewModels<VM> {
    viewModelFactory {
        initializer {
            val repository = DI.provideJokesRepository()
            viewModelCreator(repository)
        }
    }
}

fun Context.typeToString(type: JokeType): String =
    getString(
        when (type) {
            JokeType.Local -> R.string.joke_type_local
            JokeType.Remote -> R.string.joke_type_remote
        }
    )