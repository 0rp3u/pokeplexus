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


    /*
    Since this is a different endpoint in a real scenario we would have a new okHTTPClient Instance
    and a new retrofit Service for it, but for the sake of simplicity we force the full URL on this service
      */
    @POST("https://webhook.site/bcc14f53-54ca-49e6-914c-3ffcd10b12df/favofite")
    suspend fun favoritePokemonDetails(
        @Body pokemon: PokemonDetailsRequest
    ): Unit


    @POST("https://webhook.site/bcc14f53-54ca-49e6-914c-3ffcd10b12df/unFavofite")
    suspend fun unFavoritePokemonDetails(
        @Body pokemon: PokemonDetailsRequest
    ): Unit


}