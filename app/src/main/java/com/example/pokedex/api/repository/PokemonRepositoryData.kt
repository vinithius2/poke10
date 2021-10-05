package com.example.pokedex.api.repository

import com.example.pokedex.api.data.PokemonList
import retrofit2.Response

class PokemonRepositoryData(
    private val repository: PokemonRepository
) {

    suspend fun list(): Response<PokemonList> {
        val pokemons = repository.getPokemonList()
        return pokemons
    }

}

