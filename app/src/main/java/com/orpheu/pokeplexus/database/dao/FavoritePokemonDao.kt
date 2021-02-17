package com.orpheu.pokeplexus.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.orpheu.pokeplexus.database.model.PokemonDetailsEntity
import com.orpheu.pokeplexus.database.model.RoomTypeConverters
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FavoritePokemonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertOrIgnore(pokemon: PokemonDetailsEntity): Long

    @Query("DELETE FROM pokemondetailsentity WHERE pokemon_id = :pokemon_id")
    abstract suspend fun deleteById(pokemon_id: Int)


    @Query("SELECT * FROM pokemondetailsentity")
    abstract fun getFavoritedPokemon(): Flow<List<PokemonDetailsEntity>>


    @Query("SELECT * FROM pokemondetailsentity WHERE pokemon_id = :pokemonId")
    abstract fun getFavoritePokemonById(pokemonId: Int): Flow<PokemonDetailsEntity?>

    @Query("SELECT COUNT(*) FROM pokemondetailsentity WHERE pokemon_id = :pokemonId")
    abstract fun isPokemonFavorited(pokemonId: Int): Flow<Boolean>



}