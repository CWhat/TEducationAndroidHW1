package com.cwhat.teducationandroidhw1.di

import com.cwhat.teducationandroidhw1.data.JokesRepository
import com.cwhat.teducationandroidhw1.data.WithNetworkAndDbJokesRepository
import com.cwhat.teducationandroidhw1.data.db.JokesDatabase
import com.cwhat.teducationandroidhw1.data.db.LocalJokeDao
import com.cwhat.teducationandroidhw1.data.db.RemoteJokeDao
import com.cwhat.teducationandroidhw1.data.remote.RemoteApi
import com.cwhat.teducationandroidhw1.data.remote.RemoteApiWithContext
import kotlinx.coroutines.Dispatchers
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

        val baseApi = retrofit.create(RemoteApi::class.java)

        RemoteApiWithContext(baseApi, Dispatchers.IO)
    }

    private val repository: JokesRepository by lazy {
        WithNetworkAndDbJokesRepository(remoteApi, localJokeDao, remoteJokeDao)
    }

    lateinit var db: JokesDatabase

    private val localJokeDao: LocalJokeDao by lazy { db.localJokeDao() }

    private val remoteJokeDao: RemoteJokeDao by lazy { db.remoteJokeDao() }

    fun provideJokesRepository(): JokesRepository = repository

}
