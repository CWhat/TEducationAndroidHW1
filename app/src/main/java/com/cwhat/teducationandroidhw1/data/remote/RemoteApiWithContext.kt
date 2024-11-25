package com.cwhat.teducationandroidhw1.data.remote

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RemoteApiWithContext(private val api: RemoteApi, private val context: CoroutineContext) :
    RemoteApi {
    override suspend fun getJokes(
        amount: Int,
        blacklist: List<String>,
        type: String
    ): RemoteResult = withContext(context) { api.getJokes(amount, blacklist, type) }
}