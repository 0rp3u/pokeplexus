package com.orpheu.pokeplexus.data

import android.net.Uri
import com.orpheu.pokeplexus.data.model.BaseStats
import com.orpheu.pokeplexus.data.model.Pokemon
import com.orpheu.pokeplexus.data.model.PokemonDetails
import com.orpheu.pokeplexus.data.model.Type
import com.orpheu.pokeplexus.database.model.PokemonDetailsEntity
import com.orpheu.pokeplexus.database.model.PokemonTypeInfo
import com.orpheu.pokeplexus.database.model.RoomTypeConverters
import com.orpheu.pokeplexus.network.model.PokemonCollectionItem
import com.orpheu.pokeplexus.network.model.PokemonDetailsRequest
import com.orpheu.pokeplexus.network.model.PokemonDetailsResponse
import com.orpheu.pokeplexus.network.model.Types

fun PokemonCollectionItem.mapTopDomain(): Pokemon {
    val id = Uri.parse(url).lastPathSegment?.toInt()
        ?: throw Exception("Could not parse id from Pokemon url")

    return Pokemon(
        id,
        name,
        "https://pokeres.bastionbot.org/images/pokemon/$id.png",
        false
    )
}


fun Types.mapTopDomain() = when (type.name) {
    "water" -> Type.water
    "steel" -> Type.steel
    "rock" -> Type.rock
    "psychic" -> Type.psychic
    "poison" -> Type.poison
    "normal" -> Type.normal
    "ice" -> Type.ice
    "ground" -> Type.ground
    "grass" -> Type.grass
    "ghost" -> Type.ghost
    "flying" -> Type.flying
    "fire" -> Type.fire
    "fighting" -> Type.fighting
    "fairy" -> Type.fairy
    "electric" -> Type.electric
    "dragon" -> Type.dragon
    "dark" -> Type.dark
    "bug" -> Type.bug
    else -> throw  Exception("cound not parse pokemon type ${type.name} ")
}

fun PokemonDetailsResponse.mapTopDomain(): PokemonDetails {
    return PokemonDetails(
        id,
        name,
        "https://pokeres.bastionbot.org/images/pokemon/$id.png",
        height,
        weight,
        this.types.map { it.mapTopDomain() },
        BaseStats(
            stats.find { it.stat.name == "hp" }?.base_stat ?: 0,
            stats.find { it.stat.name == "attack" }?.base_stat ?: 0,
            stats.find { it.stat.name == "defense" }?.base_stat ?: 0,
            stats.find { it.stat.name == "speed" }?.base_stat ?: 0,
            stats.find { it.stat.name == "special-attack" }?.base_stat ?: 0,
            stats.find { it.stat.name == "special-defense" }?.base_stat ?: 0,
        ),
        false
    )
}

fun PokemonDetailsResponse.mapTopEntity(): PokemonDetailsEntity {
    return PokemonDetailsEntity(
        id,
        name,
        "https://pokeres.bastionbot.org/images/pokemon/$id.png",
        height,
        weight,
        types.map { RoomTypeConverters.fromTagPokemonTypeInfo(it.type.name) },
        stats.find { it.stat.name == "hp" }?.base_stat ?: 0,
        stats.find { it.stat.name == "attack" }?.base_stat ?: 0,
        stats.find { it.stat.name == "defense" }?.base_stat ?: 0,
        stats.find { it.stat.name == "speed" }?.base_stat ?: 0,
        stats.find { it.stat.name == "special-attack" }?.base_stat ?: 0,
        stats.find { it.stat.name == "special-defense" }?.base_stat ?: 0,
    )
}

fun PokemonDetailsEntity.mapToPokemonDetailsRequest(): PokemonDetailsRequest {
    return PokemonDetailsRequest(
        id,
        name,
        imageUrl,
        height,
        weight,
        types,
        hp,
        attack,
        defense,
        speed,
        specialAttack,
        specialDefense
    )
}


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

