package com.cwhat.teducationandroidhw1.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RemoteResult(
    val error: Boolean,
    val amount: Int,
    val jokes: List<RemoteJoke>,
)
