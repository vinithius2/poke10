package com.vinithius.poke10.di


import com.vinithius.poke10.PokemonViewModel
import com.vinithius.poke10.api.repository.PokemonRepository
import com.vinithius.poke10.api.repository.PokemonRepositoryData
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val repositoryModule = module {
    single { get<Retrofit>().create(PokemonRepository::class.java) }
}

val repositoryDataModule = module {
    single { PokemonRepositoryData(get()) }
}

val viewModelModule = module {
    viewModel { PokemonViewModel(get()) }
}

/******************** REMOTE ********************/

val remoteDataModule = module {
    single { retrofit() }
}

private fun retrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}