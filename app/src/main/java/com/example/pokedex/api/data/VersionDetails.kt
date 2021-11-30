package com.example.pokedex.api.data

data class VersionDetails (
    val encounter_details: List<EncounterDetails>,
    val max_chance: Int,
    val version: Default
)
