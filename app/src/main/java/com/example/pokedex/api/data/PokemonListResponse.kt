package com.example.pokedex.api.data

data class PokemonListResponse (
    var count: Int,
    var next: String?,
    var previous: String?,
    var results: List<Pokemon>
)
