package com.example.pokedex

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.api.data.PokemonList
import com.example.pokedex.api.repository.PokemonRepositoryData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class PokemonViewModel(
    private val pokemonRepositoryData: PokemonRepositoryData
): ViewModel() {

    private lateinit var mPokemonList: MutableLiveData<PokemonList>
    var pokemonList = mPokemonList

    suspend fun getPokemonList() {
        return coroutineScope {
            withContext(IO) {
                mPokemonList.value = pokemonRepositoryData.list().body()
            }
        }
    }

}