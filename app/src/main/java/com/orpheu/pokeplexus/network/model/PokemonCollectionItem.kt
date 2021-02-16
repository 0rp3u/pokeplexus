package com.orpheu.pokeplexus.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonCollectionItem(
    val name: String,
    val url: String
)
