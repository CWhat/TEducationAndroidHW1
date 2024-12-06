package com.cwhat.teducationandroidhw1.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cwhat.teducationandroidhw1.data.JokesRepository
import com.cwhat.teducationandroidhw1.data.LocalJokesRepository
import com.cwhat.teducationandroidhw1.data.RemoteJokesRepository
import com.cwhat.teducationandroidhw1.data.WithNetworkAndDbJokesRepository
import com.cwhat.teducationandroidhw1.data.db.JokesDatabase
import com.cwhat.teducationandroidhw1.data.db.LocalJokeDao
import com.cwhat.teducationandroidhw1.data.db.RemoteJokeDao
import com.cwhat.teducationandroidhw1.data.remote.RemoteApi
import com.cwhat.teducationandroidhw1.data.remote.RemoteApiWithContext
import com.cwhat.teducationandroidhw1.domain.use_cases.AddUserJokeUseCase
import com.cwhat.teducationandroidhw1.domain.use_cases.GetJokeByIdUseCase
import com.cwhat.teducationandroidhw1.domain.use_cases.ShowListUseCase
import com.cwhat.teducationandroidhw1.ui.add_joke.AddJokeViewModel
import com.cwhat.teducationandroidhw1.ui.full_view.FullJokeViewModel
import com.cwhat.teducationandroidhw1.ui.list.JokesViewModel
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

    private val remoteJokesRepository: RemoteJokesRepository by lazy {
        RemoteJokesRepository(remoteApi, remoteJokeDao)
    }

    private val localJokesRepository: LocalJokesRepository by lazy {
        LocalJokesRepository(localJokeDao)
    }

    private val repository: JokesRepository by lazy {
        WithNetworkAndDbJokesRepository(remoteJokesRepository, localJokesRepository)
    }

    private val addUserJokeUseCase: AddUserJokeUseCase by lazy {
        AddUserJokeUseCase(repository)
    }

    private val getJokeByIdUseCase: GetJokeByIdUseCase by lazy {
        GetJokeByIdUseCase(repository)
    }

    private val showListUseCase: ShowListUseCase by lazy {
        ShowListUseCase(repository)
    }

    lateinit var db: JokesDatabase

    private val localJokeDao: LocalJokeDao by lazy { db.localJokeDao() }

    private val remoteJokeDao: RemoteJokeDao by lazy { db.remoteJokeDao() }

    fun provideAddUserJokeUseCase() = addUserJokeUseCase

    fun provideGetJokeByIdUseCase() = getJokeByIdUseCase

    fun provideShowListUseCase() = showListUseCase

}

private inline fun <reified VM : ViewModel> Fragment.customViewModels(
    crossinline viewModelCreator: () -> VM,
) = viewModels<VM> {
    viewModelFactory {
        initializer {
            viewModelCreator()
        }
    }
}

fun Fragment.addJokeViewModels(): Lazy<AddJokeViewModel> = customViewModels {
    val useCase = DI.provideAddUserJokeUseCase()
    AddJokeViewModel(useCase)
}

fun Fragment.fullJokeViewModels(): Lazy<FullJokeViewModel> = customViewModels {
    val useCase = DI.provideGetJokeByIdUseCase()
    FullJokeViewModel(useCase)
}

fun Fragment.jokesViewModels(): Lazy<JokesViewModel> = customViewModels {
    val useCase = DI.provideShowListUseCase()
    JokesViewModel(useCase)
}
