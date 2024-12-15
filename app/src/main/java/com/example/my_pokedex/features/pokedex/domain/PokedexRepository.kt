package com.example.my_pokedex.features.pokedex.domain

import com.example.my_pokedex.features.PokemonDetails.domain.PokemonDetailsModel
import com.example.my_pokedex.utils.Resource

interface PokedexRepository {
    suspend fun getPokemon(limit: Int,offset: Int): Resource<PokemonModel>
    suspend fun getPokemonByName(name:String): Resource<PokemonDetailsModel>
}