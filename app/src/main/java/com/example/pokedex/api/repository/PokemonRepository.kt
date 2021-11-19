package com.example.pokedex.api.repository


import com.example.pokedex.api.data.Pokemon
import com.example.pokedex.api.data.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonRepository {

    @GET("pokemon/")
    suspend fun getPokemonList(@Query("limit") limit: Int) : Response<PokemonListResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int) : Response<Pokemon>

}