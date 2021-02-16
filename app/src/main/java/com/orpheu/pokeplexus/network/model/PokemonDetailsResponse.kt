package com.orpheu.pokeplexus.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailsResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val stats: List<Stats>,
    val types: List<Types>
)