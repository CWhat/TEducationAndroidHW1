package com.cwhat.teducationandroidhw1.ui

import android.content.Context
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.data.JokeType

fun Context.typeToString(type: JokeType): String =
    getString(
        when (type) {
            JokeType.Local -> R.string.joke_type_local
            JokeType.Remote -> R.string.joke_type_remote
        }
    )