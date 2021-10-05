package com.example.pokedex.api.repository

import com.example.pokedex.api.data.PokemonList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface PokemonRepository {

    @GET("pokemon/")
    suspend fun getPokemonList() : Response<PokemonList>

}