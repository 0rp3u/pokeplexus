package com.orpheu.pokeplexus.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Stats (
	val base_stat : Int,
	val effort : Int,
	val stat : Stat
)