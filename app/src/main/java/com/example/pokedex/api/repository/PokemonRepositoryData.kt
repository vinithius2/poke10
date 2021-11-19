package com.example.pokedex.api.repository

import com.example.pokedex.api.data.Pokemon
import com.example.pokedex.api.data.PokemonListResponse

class PokemonRepositoryData(
    private val repository: PokemonRepository
) {

    suspend fun pokemonList(limit: Int): PokemonListResponse? {
        return repository.getPokemonList(limit).body()
    }

    suspend fun pokemonDetail(id: Int): Pokemon? {
        return repository.getPokemonDetail(id).body()
    }

}

