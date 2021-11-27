package com.example.pokedex.api.data

data class Pokemon(
    var id: Int?,
    var name: String,
    var url: String?,
    var height: Int?,
    var weight: Int?,
    var base_experience: Int?,
    var stats: List<Stat>?,
    var types: List<Type>?,
    var abilities: List<Abilities>?,
    var sprites: Sprites
)
