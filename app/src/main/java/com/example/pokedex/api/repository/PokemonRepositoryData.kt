package com.example.pokedex.api.repository

import com.example.pokedex.api.data.PokemonResponse

class PokemonRepositoryData(
    private val repository: PokemonRepository
) {

    suspend fun pokemonList(limit: Int): PokemonResponse? {
        return repository.getPokemonList(limit).body()
    }

}

