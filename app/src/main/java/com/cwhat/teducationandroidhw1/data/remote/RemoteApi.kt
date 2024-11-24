package com.cwhat.teducationandroidhw1.data.remote

import retrofit2.http.GET
import retrofit2.http.Query


interface RemoteApi {

    companion object {
        const val BASE_URL = "https://v2.jokeapi.dev/"

        const val DEFAULT_AMOUNT = 10

        val DEFAULT_BLACKLIST =
            listOf("nsfw", "religious", "political", "racist", "sexist", "explicit")
    }

    @GET("joke/Any")
    suspend fun getJokes(
        @Query("amount") amount: Int = DEFAULT_AMOUNT,
        @Query("blacklistFlags") blacklist: List<String> = DEFAULT_BLACKLIST,
    ): RemoteResult
}