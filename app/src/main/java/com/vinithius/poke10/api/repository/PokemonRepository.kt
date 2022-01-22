package com.vinithius.poke10.api.repository

import com.vinithius.poke10.api.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonRepository {

    @GET("pokemon/")
    suspend fun getPokemonList(@Query("limit") limit: Int): Response<PokemonListResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): Response<Pokemon>

    @GET("evolution-chain/{id}")
    suspend fun getPokemonEvolution(@Path("id") id: Int): Response<EvolutionChain>

    @GET("pokemon/{id}/encounters")
    suspend fun getPokemonEncounters(@Path("id") id: Int): Response<List<Location>>

    @GET("characteristic/{id}")
    suspend fun getPokemonCharacteristic(@Path("id") id: Int): Response<Characteristic>

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): Response<Specie>

    @GET("type/{type}")
    suspend fun getPokemonDamageRelations(@Path("type") type: String): Response<Damage>

}