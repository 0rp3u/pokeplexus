package com.orpheu.pokeplexus.network

import com.orpheu.pokeplexus.network.model.PaginatedResponse
import com.orpheu.pokeplexus.network.model.PokemonCollectionItem
import com.orpheu.pokeplexus.network.model.PokemonDetailsRequest
import com.orpheu.pokeplexus.network.model.PokemonDetailsResponse
import retrofit2.http.*

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



    /*Since this is a different endpoint in a real scenario we would have a new okHTTPClient Instance
     and a new retrofit Service using it */
    @POST("https://webhook.site/c09b20f2-4007-4b6d-89db-220c46fee90f/favofite")
    suspend fun favoritePokemonDetails(
        @Body pokemon: PokemonDetailsRequest
    ): Unit


    @POST("https://webhook.site/c09b20f2-4007-4b6d-89db-220c46fee90f/unFavofite")
    suspend fun unFavoritePokemonDetails(
        @Body pokemon: PokemonDetailsRequest
    ) : Unit



}