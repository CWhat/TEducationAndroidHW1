package com.cwhat.teducationandroidhw1.data.di

import com.cwhat.teducationandroidhw1.data.JokesRepository
import com.cwhat.teducationandroidhw1.data.WithNetworkJokesRepository
import com.cwhat.teducationandroidhw1.data.remote.RemoteApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


object DI {
    private val remoteApi: RemoteApi by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(RemoteApi.BASE_URL)
            .client(client)
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .build()

        retrofit.create(RemoteApi::class.java)
    }

    private val repository: JokesRepository by lazy { WithNetworkJokesRepository(remoteApi) }

    fun provideRemoteApi(): RemoteApi = remoteApi

    fun provideJokesRepository(): JokesRepository = repository

}
