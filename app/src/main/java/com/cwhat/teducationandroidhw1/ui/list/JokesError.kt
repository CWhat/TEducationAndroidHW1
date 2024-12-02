package com.cwhat.teducationandroidhw1.ui.list

import androidx.annotation.StringRes

sealed class JokesError {

    class ErrorWithString(val message: String) : JokesError()

    class ErrorWithId(@StringRes val resId: Int) : JokesError()

}