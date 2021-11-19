package com.example.pokedex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.api.data.Pokemon
import com.example.pokedex.api.repository.PokemonRepositoryData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val pokemonRepositoryData: PokemonRepositoryData
): ViewModel() {

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>>
        get() = _pokemonList

    private val _pokemonDetail = MutableLiveData<Pokemon>()
    val pokemonDetail: LiveData<Pokemon>
        get() = _pokemonDetail

    private val _pokemonListLoading = MutableLiveData<Boolean>()
    val pokemonListLoading: LiveData<Boolean>
        get() = _pokemonListLoading

    fun getPokemonList(limit: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _pokemonListLoading.postValue(true)
            _pokemonList.postValue(pokemonRepositoryData.pokemonList(limit)?.results)
            _pokemonListLoading.postValue(false)
        }
    }

    fun getPokemonDetail(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _pokemonListLoading.postValue(true)
            _pokemonDetail.postValue(pokemonRepositoryData.pokemonDetail(id))
            _pokemonListLoading.postValue(false)
        }
    }

}