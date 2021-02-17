package com.orpheu.pokeplexus.database.model.mappers

import com.orpheu.pokeplexus.database.model.PokemonDetailsEntity
import com.orpheu.pokeplexus.database.model.PokemonTypeInfo
import com.orpheu.pokeplexus.domain.model.BaseStats
import com.orpheu.pokeplexus.domain.model.Pokemon
import com.orpheu.pokeplexus.domain.model.PokemonDetails
import com.orpheu.pokeplexus.domain.model.Type

fun PokemonDetailsEntity.mapToPokemon(): Pokemon {
    return Pokemon(
        id,
        name,
        "https://pokeres.bastionbot.org/images/pokemon/$id.png",
        true
    )
}

fun PokemonDetailsEntity.mapToPokemonDetails(): PokemonDetails {
    return PokemonDetails(
        id,
        name,
        "https://pokeres.bastionbot.org/images/pokemon/$id.png",
        height,
        weight,
        types.map { it.mapToDomain() },
        BaseStats(
            hp,
            attack,
            defense,
            speed,
            specialAttack,
            specialDefense
        ),
        true
    )
}


fun PokemonTypeInfo.mapToDomain() = when (this) {

    PokemonTypeInfo.WATER -> Type.water
    PokemonTypeInfo.STEEL -> Type.steel
    PokemonTypeInfo.ROCK -> Type.rock
    PokemonTypeInfo.PSYCHIC -> Type.psychic
    PokemonTypeInfo.POISON -> Type.poison
    PokemonTypeInfo.NORMAL -> Type.normal
    PokemonTypeInfo.ICE -> Type.ice
    PokemonTypeInfo.GROUND -> Type.ground
    PokemonTypeInfo.GRASS -> Type.grass
    PokemonTypeInfo.GHOST -> Type.ghost
    PokemonTypeInfo.FLYING -> Type.flying
    PokemonTypeInfo.FIRE -> Type.fire
    PokemonTypeInfo.FIGHTING -> Type.fighting
    PokemonTypeInfo.FAIRY -> Type.fairy
    PokemonTypeInfo.ELECTRIC -> Type.electric
    PokemonTypeInfo.DRAGON -> Type.dragon
    PokemonTypeInfo.DARK -> Type.dark
    PokemonTypeInfo.BUG -> Type.bug
}