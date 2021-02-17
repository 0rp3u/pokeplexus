package com.orpheu.pokeplexus.ui.pokemon

import androidx.paging.PagingData
import com.orpheu.pokeplexus.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PokemonContract {

    interface ViewModel : ViewState, ViewActions

    interface ViewState {

        val error: SharedFlow<Error>

        val navigation: SharedFlow<ViewInstructions>

        val isFilteringFavorite: Flow<Boolean>

        val pokemons: Flow<PagingData<Pokemon>>

        val pokemonListState: Flow<PokemonListState>

        //when having multiple errors with different view behaviours we should migrate to a sealed class
        // with the different errors
        data class Error(val message: String)

    }


    sealed class PokemonListState {
        object Loaded : PokemonListState()
        object Loading : PokemonListState()
        data class LoadingError(val message: String) : PokemonListState()
    }


    interface ViewActions {
        fun toggleFavoritePokemonFilter()
        fun pokemonClicked(pokemon: Pokemon)
    }

    sealed class ViewInstructions {
        data class NavigateToPokemonDetails(val pokemon: Pokemon) : ViewInstructions()
    }
}