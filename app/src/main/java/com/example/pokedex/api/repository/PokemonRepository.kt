package com.example.pokedex.api.repository

import com.example.pokedex.api.data.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonRepository {

    @GET("pokemon/")
    suspend fun getPokemonList(@Query("limit") Int limit) : Response<PokemonResponse>

}