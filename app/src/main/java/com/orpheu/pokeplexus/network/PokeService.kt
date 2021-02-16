package com.orpheu.pokeplexus.network

import com.orpheu.pokeplexus.network.model.PaginatedResponse
import com.orpheu.pokeplexus.network.model.PokemonCollectionItem
import com.orpheu.pokeplexus.network.model.PokemonDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {

    @GET("pokemon")
    suspend fun getPokemonsPaged(
        @Query("offset") page: Int,
        @Query("limit") pageSize: Int?
    ): PaginatedResponse<PokemonCollectionItem>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(
        @Path("id") id: Int
    ): PokemonDetailsResponse

}