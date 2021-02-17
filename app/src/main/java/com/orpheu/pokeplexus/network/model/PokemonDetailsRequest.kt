package com.orpheu.pokeplexus.network.model


import com.orpheu.pokeplexus.database.model.PokemonTypeInfo
import com.orpheu.pokeplexus.database.model.RoomTypeConverters
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailsRequest(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val specialAttack: Int,
    val specialDefense: Int
)