package com.orpheu.pokeplexus.data.model

data class PokemonDetails(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val baseStats: BaseStats,
    val favorite: Boolean
)