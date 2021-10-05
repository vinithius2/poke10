package com.example.pokedex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.api.data.Pokemon
import com.example.pokedex.api.repository.PokemonRepositoryData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.List

class PokemonViewModel(
    private val pokemonRepositoryData: PokemonRepositoryData
): ViewModel() {

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>>
        get() = _pokemonList

    fun getPokemonList() {
        CoroutineScope(Dispatchers.IO).launch {
            _pokemonList.postValue(pokemonRepositoryData.pokemonList()?.results)
        }
    }


}