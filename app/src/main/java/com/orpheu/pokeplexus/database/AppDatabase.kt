package com.orpheu.pokeplexus.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.orpheu.pokeplexus.database.dao.FavoritePokemonDao
import com.orpheu.pokeplexus.database.model.PokemonDetailsEntity
import com.orpheu.pokeplexus.database.model.RoomTypeConverters


@Database(
    entities = [PokemonDetailsEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): FavoritePokemonDao
}