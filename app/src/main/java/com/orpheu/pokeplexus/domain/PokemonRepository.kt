package com.orpheu.pokeplexus.domain

import android.net.Uri
import androidx.paging.*
import com.orpheu.pokeplexus.core.Resource
import com.orpheu.pokeplexus.database.dao.FavoritePokemonDao
import com.orpheu.pokeplexus.database.model.mappers.mapToPokemon
import com.orpheu.pokeplexus.database.model.mappers.mapToPokemonDetails
import com.orpheu.pokeplexus.domain.model.Pokemon
import com.orpheu.pokeplexus.domain.model.PokemonDetails
import com.orpheu.pokeplexus.network.PokeService
import com.orpheu.pokeplexus.network.model.PokemonCollectionItem
import com.orpheu.pokeplexus.network.model.mappers.mapTopDomain
import com.orpheu.pokeplexus.network.model.mappers.mapTopEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class PokemonRepository(
    private val pokeService: PokeService,
    private val favoritePokemonDao: FavoritePokemonDao
) {

    /*
        NOTE:
        this could be done differently, using the page number as the paging Source Key,
        but since the api already gives us good pagination metadata I choose to go with that but
        extract the page number before the http request.
        could also work with the full URL using retrofit @Url annotation but would make it cumbersome
        to fetch specific pages if needed.
     */
    class PokemonPagingSource(
        val pokeService: PokeService,
    ) : PagingSource<String, PokemonCollectionItem>() {
        override suspend fun load(
            params: LoadParams<String>
        ): LoadResult<String, PokemonCollectionItem> {
            return try {
                // Start refresh at page 0 if param key undefined.
                val page = try {
                    Uri.parse(params.key).getQueryParameter("offset")?.toInt() ?: 0
                } catch (e: Exception) {
                    0
                }

                val response = pokeService.getPokemonsPaged(page, params.loadSize)

                return LoadResult.Page(
                    data = response.results,
                    prevKey = response.previous,
                    nextKey = response.next
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        // returns null because we already deal with Params.key being null on the load function
        override fun getRefreshKey(state: PagingState<String, PokemonCollectionItem>): String? =
            null
    }

    fun getPokemonPaged(): Flow<PagingData<Pokemon>> {
        return Pager(
            PagingConfig(
                pageSize = 20,
                prefetchDistance = 40,
                enablePlaceholders = false
            )
        ) {
            PokemonPagingSource(pokeService)
        }.flow.map { it.map { it.mapTopDomain() } }
    }

    fun getFavoritePokemon(): Flow<List<Pokemon>> =
        favoritePokemonDao.getFavoritedPokemon()
            .map { it.map { dbPokemons -> dbPokemons.mapToPokemon() } }


    /*
    Since Pokemon data does not change that much we only fetch if we don't have local data.
    If the pokemon data was constantly changing and we wanted to always show fresh data, but still
    be local first and reactive, as in local data event -> remote call and update local storage with
    new data -> local event. We would need to implement a NetworkBoundResource using Flow
     */
    fun getPokemonDetails(pokemonId: Int): Flow<Resource<PokemonDetails>> = flow {
        val local =
            favoritePokemonDao.getFavoritePokemonById(pokemonId).first()?.mapToPokemonDetails()
        emit(Resource.Loading(local))

        emitAll(
            favoritePokemonDao.getFavoritePokemonById(pokemonId)
                .map {
                    Resource.Success(
                        it?.mapToPokemonDetails()
                            ?: pokeService.getPokemonDetails(pokemonId).mapTopDomain()
                    )
                }

        )
    }.catch { emit(Resource.Failure(null, it)) }


    suspend fun addToFavorites(pokemonId: Int): Result<Unit> {
        return try {
            val pokemonDetails = pokeService.getPokemonDetails(pokemonId).mapTopEntity()
            delay(5000)
            favoritePokemonDao.insertOrIgnore(pokemonDetails)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFromFavorites(pokemonId: Int): Result<Unit> {
        return try {
            delay(5000)
            favoritePokemonDao.deleteById(pokemonId)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}