package com.cwhat.jokes.ui

import android.content.Context
import com.cwhat.jokes.R
import com.cwhat.jokes.domain.entity.JokeType
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

fun Context.typeToString(type: JokeType): String =
    getString(
        when (type) {
            JokeType.Local -> R.string.joke_type_local
            JokeType.Remote -> R.string.joke_type_remote
        }
    )

fun <T> mutableEventFlow() = MutableSharedFlow<T>(
    replay = 0,
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
)