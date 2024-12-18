package com.cwhat.jokes.ui.add_joke

import androidx.annotation.StringRes

sealed class AddJokeEvent {

    data object NavigateToBack : AddJokeEvent()

    data class ShowMessage(@StringRes val resId: Int) : AddJokeEvent()

}