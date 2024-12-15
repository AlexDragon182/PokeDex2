package com.example.my_pokedex.features.PokemonDetails.domain

import com.example.my_pokedex.features.PokemonDetails.data.Sprites


data class PokemonDetailsModel(
    var id: Int = 0,
    var name: String? = null,
    var height: Int? = null,
    var order: Int? = null,
    var weight: Int? = null,
    var sprites: Sprites? = Sprites(),
    var abilities: List<AbilitiesModel> = listOf(),
    var stats: List<StatsModel> = listOf(),
    var types: List<TypesModel> = listOf(),
)

data class AbilitiesModel(
    var isHidden: Boolean? = null,
    var slot: Int? = null,
    var ability: AbilityModel? = AbilityModel()
)

data class AbilityModel(
    var name: String? = null,
    var url: String? = null
)

data class StatsModel(
    var baseStat: Int? = null,
    var effort: Int? = null,
    var stat: StatModel? = StatModel()
)

data class StatModel(
    var name: String? = null,
    var url: String? = null
)

data class TypesModel(
    var slot: Int? = null,
    var type: TypeModel? = TypeModel()
)

data class TypeModel(
    var name: String? = null,
    var url: String? = null
)