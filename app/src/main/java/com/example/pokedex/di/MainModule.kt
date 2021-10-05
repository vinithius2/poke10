package com.example.pokedex.di


import com.example.pokedex.PokemonViewModel
import com.example.pokedex.api.repository.PokemonRepository
import com.example.pokedex.api.repository.PokemonRepositoryData
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//val repositoryInterface = module {
//    single { get<Retrofit>().create(PokemonRepository::class.java) }
//}

val repositoryModule = module {
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