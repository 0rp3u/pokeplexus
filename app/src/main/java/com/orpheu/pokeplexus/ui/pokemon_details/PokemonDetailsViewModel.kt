package com.orpheu.pokeplexus.ui.pokemon_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orpheu.pokeplexus.core.Resource
import com.orpheu.pokeplexus.data.PokemonRepository
import com.orpheu.pokeplexus.data.model.PokemonDetails
import com.orpheu.pokeplexus.extension.filterResultFailure
import com.orpheu.pokeplexus.extension.toResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailsViewModel(
    private val pokemonId: Int,
    private val pokemonRepository: PokemonRepository
) : ViewModel(), PokemonDetailsContract.ViewModel {


    private val _error = MutableSharedFlow<PokemonDetailsContract.ViewState.Error>()
    override val error: SharedFlow<PokemonDetailsContract.ViewState.Error> = _error

    private val _navigation = MutableSharedFlow<PokemonDetailsContract.ViewInstructions>()
    override val navigation: SharedFlow<PokemonDetailsContract.ViewInstructions> = _navigation


    private val pokemonRequest = MutableSharedFlow<Int>(replay = 1).also { it.tryEmit(pokemonId) }

    private val pokemonResource: Flow<Resource<PokemonDetails>> = pokemonRequest.flatMapLatest {
        pokemonRepository.getPokemonDetails(pokemonId)
    }.shareIn(viewModelScope, SharingStarted.Lazily, 1)


    private val pokemon: Flow<PokemonDetails> = pokemonResource.mapNotNull { it.data }

    private val isPokemonDetailsLoading: Flow<Boolean> =
        pokemonResource.map { it.data == null && it is Resource.Loading }


    private val favoritePokemonAction = MutableSharedFlow<Unit>()
    private val favoritePokemonResult: SharedFlow<Resource<Unit>> = favoritePokemonAction
        .transformLatest {
            emit(Resource.Loading(null))
            emit(pokemonRepository.addToFavorites(pokemonId).toResource())
        }.shareIn(viewModelScope, SharingStarted.Lazily, 1)

    private val removeFavoritePokemonAction = MutableSharedFlow<Unit>()
    private val removePokemonResult: SharedFlow<Resource<Unit>> = removeFavoritePokemonAction
        .transformLatest {
            emit(Resource.Loading(null))
            emit(pokemonRepository.removeFromFavorites(pokemonId).toResource())
        }.shareIn(viewModelScope, SharingStarted.Lazily, 1)

    private val isAddOrRemoveFavoriteLoading: Flow<Boolean> =
        merge(favoritePokemonResult, removePokemonResult)
            .map { it is Resource.Loading }
            .onStart { emit(false) }


    override val viewState: SharedFlow<PokemonDetailsContract.ViewState.PokemonDetailsViewState> =
        combine(
            pokemon,
            isPokemonDetailsLoading,
            isAddOrRemoveFavoriteLoading
        ) { pokemon, isPokemonDetailsLoading, isAddOrRemoveFavoriteLoading ->
            PokemonDetailsContract.ViewState.PokemonDetailsViewState(
                pokemon,
                isPokemonDetailsLoading,
                isAddOrRemoveFavoriteLoading
            )
        }.shareIn(viewModelScope, replay = 1, started = SharingStarted.Lazily)


    init {
        merge(favoritePokemonResult, removePokemonResult)
            .filterResultFailure()
            .onEach {
                _error.emit(
                    PokemonDetailsContract.ViewState.Error.RequestError(
                        it.error.message ?: "Something went wrong"
                    )
                )
            }
            .launchIn(viewModelScope)

        pokemonResource
            .filterResultFailure()
            .onEach {
                _error.emit(
                    PokemonDetailsContract.ViewState.Error.PokemonDetailsError(
                        it.error.message ?: "Something went wrong"
                    )
                )
            }
            .launchIn(viewModelScope)
    }

    override fun backClicked() {
        viewModelScope.launch {
            _navigation.emit(PokemonDetailsContract.ViewInstructions.NavigateBack)
        }
    }


    override fun removeFavoritePokemonClicked() {
        viewModelScope.launch {
            removeFavoritePokemonAction.emit(Unit)
        }
    }

    override fun retryFetchPokemonDetails() {
        viewModelScope.launch {
            pokemonRequest.emit(pokemonId)
        }
    }

    override fun addFavoritePokemonClicked() {
        viewModelScope.launch {
            favoritePokemonAction.emit(Unit)
        }
    }
}