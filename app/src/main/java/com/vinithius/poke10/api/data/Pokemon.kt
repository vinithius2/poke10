package com.vinithius.poke10.api.data

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
    var sprites: Sprites?,
    var encounters: List<Location>,
    var evolution: EvolutionChain?,
    var characteristic: Characteristic?,
    var specie: Specie?,
    var damage: List<Damage>
)
