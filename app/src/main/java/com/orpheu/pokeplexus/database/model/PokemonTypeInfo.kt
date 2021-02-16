package com.orpheu.pokeplexus.database.model;

import com.squareup.moshi.Json

enum class PokemonTypeInfo(
    val tag: String
) {

    @Json(name = UUID_WATER)
    WATER(PokemonTypeInfo.UUID_WATER),

    @Json(name = UUID_STEEL)
    STEEL(PokemonTypeInfo.UUID_STEEL),

    @Json(name = UUID_ROCK)
    ROCK(PokemonTypeInfo.UUID_ROCK),

    @Json(name = UUID_PSYCHIC)
    PSYCHIC(PokemonTypeInfo.UUID_PSYCHIC),

    @Json(name = UUID_POISON)
    POISON(PokemonTypeInfo.UUID_POISON),

    @Json(name = UUID_NORMAL)
    NORMAL(PokemonTypeInfo.UUID_NORMAL),

    @Json(name = UUID_ICE)
    ICE(PokemonTypeInfo.UUID_ICE),

    @Json(name = UUID_GROUND)
    GROUND(PokemonTypeInfo.UUID_GROUND),

    @Json(name = UUID_GRASS)
    GRASS(PokemonTypeInfo.UUID_GRASS),

    @Json(name = UUID_GHOST)
    GHOST(PokemonTypeInfo.UUID_GHOST),

    @Json(name = UUID_FLYING)
    FLYING(PokemonTypeInfo.UUID_FLYING),

    @Json(name = UUID_FIRE)
    FIRE(PokemonTypeInfo.UUID_FIRE),

    @Json(name = UUID_FIGHTING)
    FIGHTING(PokemonTypeInfo.UUID_FIGHTING),

    @Json(name = UUID_FAIRY)
    FAIRY(PokemonTypeInfo.UUID_FAIRY),

    @Json(name = UUID_ELECTRIC)
    ELECTRIC(PokemonTypeInfo.UUID_ELECTRIC),

    @Json(name = UUID_DRAGON)
    DRAGON(PokemonTypeInfo.UUID_DRAGON),

    @Json(name = UUID_DARK)
    DARK(PokemonTypeInfo.UUID_DARK),

    @Json(name = UUID_BUG)
    BUG(PokemonTypeInfo.UUID_BUG);


    // UUID used because of persistance together with Moshi and RoomTypeConverters
    companion object {

        const val UUID_WATER = "water"
        const val UUID_STEEL = "steel"
        const val UUID_ROCK = "rock"
        const val UUID_PSYCHIC = "psychic"
        const val UUID_POISON = "poison"
        const val UUID_NORMAL = "normal"
        const val UUID_ICE = "ice"
        const val UUID_GROUND = "ground"
        const val UUID_GRASS = "grass"
        const val UUID_GHOST = "ghost"
        const val UUID_FLYING = "flying"
        const val UUID_FIRE = "fire"
        const val UUID_FIGHTING = "fighting"
        const val UUID_FAIRY = "fairy"
        const val UUID_ELECTRIC = "electric"
        const val UUID_DRAGON = "dragon"
        const val UUID_DARK = "dark"
        const val UUID_BUG = "bug"
    }
}