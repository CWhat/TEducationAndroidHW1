package com.cwhat.teducationandroidhw1.di.module

import com.cwhat.teducationandroidhw1.data.remote.RemoteApi
import com.cwhat.teducationandroidhw1.data.remote.RemoteApiWithContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return logging
    }

    @Provides
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

    @Provides
    fun provideNetworkDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideRemoteApi(client: OkHttpClient, dispatcher: CoroutineDispatcher): RemoteApi {
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

        return RemoteApiWithContext(baseApi, dispatcher)
    }

}