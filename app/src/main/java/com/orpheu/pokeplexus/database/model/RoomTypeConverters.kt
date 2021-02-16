package com.orpheu.pokeplexus.database.model

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object RoomTypeConverters {

    private val moshi by lazy { Moshi.Builder().build() }

    @TypeConverter
    fun fromTagPokemonTypeInfo(type: String): PokemonTypeInfo = PokemonTypeInfo.values().first { it.tag == type }


    @TypeConverter
    fun toTagPokemonTypeInfo(pokemonTypeInfo: PokemonTypeInfo) = pokemonTypeInfo.tag

    @TypeConverter
    @JvmStatic
    fun fromListPokemonTypeInfoToJson(types: List<PokemonTypeInfo>?): String? {
        if (types == null) return null
        val type = Types.newParameterizedType(List::class.java, PokemonTypeInfo::class.java)
        val adapter: JsonAdapter<List<PokemonTypeInfo>> = moshi.adapter(type)
        return adapter.toJson(types)
    }

    @TypeConverter
    @JvmStatic
    fun fromJsonToListOfPokemonTypeInfo(json: String?): List<PokemonTypeInfo>? {
        if (json == null) return null
        val type = Types.newParameterizedType(List::class.java, PokemonTypeInfo::class.java)
        val adapter: JsonAdapter<List<PokemonTypeInfo>> = moshi.adapter(type)
        return adapter.fromJson(json) ?: emptyList()
    }

}