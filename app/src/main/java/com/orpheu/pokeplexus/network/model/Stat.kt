package com.orpheu.pokeplexus.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stat (
	val name : String,
	val url : String
)