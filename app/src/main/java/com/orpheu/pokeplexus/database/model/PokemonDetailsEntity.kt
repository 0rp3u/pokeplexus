package com.orpheu.pokeplexus.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
data class PokemonDetailsEntity(
    @PrimaryKey
    @ColumnInfo(name = "pokemon_id")
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int,
    val weight: Int,
    @field:TypeConverters(RoomTypeConverters::class)
    val types: List<PokemonTypeInfo>,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val specialAttack: Int,
    val specialDefense: Int
)