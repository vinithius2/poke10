package com.example.pokedex.api.data

import com.google.gson.annotations.SerializedName

data class DamageRelations(
    @SerializedName("double_damage_from")
    val effective_damage_from: List<Default>, // Esses tipos são super eficazes contra Pokémon Grass (DEFESA) (APANHA)
    @SerializedName("double_damage_to")
    val effective_damage_to: List<Default>, // Movimentos de grama são super eficazes contra: (ATAQUE) (METE A PEIA)
    @SerializedName("half_damage_from")
    val ineffective_damage_from: List<Default>, // Esses tipos não são muito eficazes contra Pokémon Grass : (DEFESA) (DA CONTA)
    @SerializedName("half_damage_to")
    val ineffective_damage_to: List<Default>, // Movimentos de grama não são muito eficazes contra: (ATAQUE) (NÃO DA CONTA)
    val no_damage_from: List<Default>,
    val no_damage_to: List<Default>
)