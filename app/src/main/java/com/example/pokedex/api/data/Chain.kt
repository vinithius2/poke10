package com.example.pokedex.api.data

data class Chain (
    val is_baby: Boolean,
    val species: Default?,
    val evolution_details: List<EvolutionDetails>?,
    val evolves_to: List<Chain>?
)