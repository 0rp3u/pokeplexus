package com.orpheu.pokeplexus.ui.pokemon_details

import androidx.paging.PagingData
import com.orpheu.pokeplexus.data.model.Pokemon
import com.orpheu.pokeplexus.data.model.PokemonDetails
import com.orpheu.pokeplexus.ui.pokemon.PokemonsContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface PokemonDetailsContract {

    interface ViewModel : ViewState, ViewActions

    interface ViewState {
        val error: SharedFlow<Error>

        val navigation: SharedFlow<ViewInstructions>

        val viewState: SharedFlow<PokemonDetailsViewState>

        data class PokemonDetailsViewState(
            val pokemon: PokemonDetails,
            val isPokemonDetailsLoading: Boolean,
            val isAddOrRemoveFavoriteLoading: Boolean,
        )



        sealed class Error(open val message: String){

            data class PokemonDetailsError(override val message: String): Error(message)
            data class RequestError(override val message: String): Error(message)
        }
    }

    interface ViewActions {
        fun backClicked()
        fun addFavoritePokemonClicked()
        fun removeFavoritePokemonClicked()
        fun retryFetchPokemonDetails()
    }

    sealed class ViewInstructions {
        object NavigateBack : ViewInstructions()
    }
}