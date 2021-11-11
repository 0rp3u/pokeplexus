package com.orpheu.pokeplexus.ui.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.orpheu.pokeplexus.data.PokemonRepository
import com.orpheu.pokeplexus.data.model.Pokemon
import com.orpheu.pokeplexus.extension.initialCombinedLoadStates
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PokemonViewModel(
    pokemonRepository: PokemonRepository
) : ViewModel(), PokemonsContract.ViewModel {


    private val pokemonListLoadStates = MutableStateFlow(initialCombinedLoadStates)

    private val _error = MutableSharedFlow<PokemonsContract.ViewState.Error>()
    override val error: SharedFlow<PokemonsContract.ViewState.Error> = _error

    private val _navigation = MutableSharedFlow<PokemonsContract.ViewInstructions>()
    override val navigation: SharedFlow<PokemonsContract.ViewInstructions> = _navigation

    private val _isFilteringFavorite = MutableStateFlow(false)

    private val favoritePokemon = pokemonRepository.getFavoritePokemon()
        .shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)


    //combined with the favorite information
    private val pagedPokemon = pokemonRepository.getPokemonPaged()
        .cachedIn(viewModelScope)
        .combine(favoritePokemon) { pokemonList, favoritePokemon ->
            pokemonList.map { pokemonCollectionItem ->
                favoritePokemon.firstOrNull { it.name == pokemonCollectionItem.name }
                    ?: pokemonCollectionItem
            }
        }

    private val pokemons: Flow<PagingData<Pokemon>> =
        _isFilteringFavorite.flatMapLatest { isFilteringFavorite ->
            if (isFilteringFavorite) {
                favoritePokemon.map { PagingData.from(it) }
            } else pagedPokemon
        }


    private val pokemonListState: Flow<PokemonsContract.PokemonListState> =
        pokemonListLoadStates.map {
            when (val loadState = it.refresh) {
                is LoadState.NotLoading -> PokemonsContract.PokemonListState.Loaded
                is LoadState.Loading -> PokemonsContract.PokemonListState.Loading
                //On a real application we would use content from string resources
                is LoadState.Error -> PokemonsContract.PokemonListState.LoadingError(
                    loadState.error.localizedMessage ?: "something went wrong"
                )
            }
        }


    override val viewState: Flow<PokemonsContract.ViewState.PokemonsViewState> = combine(
        _isFilteringFavorite,
        pokemons,
        pokemonListState
    ) { isFilteringFavorite, pokemons, pokemonListState ->
        PokemonsContract.ViewState.PokemonsViewState(
            isFilteringFavorite,
            pokemons,
            pokemonListState
        )
    }


    override fun toggleFavoritePokemonFilter() {
        _isFilteringFavorite.value = !_isFilteringFavorite.value
    }


    override fun pokemonClicked(pokemon: Pokemon) {
        viewModelScope.launch {
            _navigation.emit(PokemonsContract.ViewInstructions.NavigateToPokemonDetails(pokemon))
        }
    }

    fun setPokemonListFlow(loadStates: Flow<CombinedLoadStates>) {
        viewModelScope.launch {
            pokemonListLoadStates.emitAll(loadStates)
        }
    }
}