package com.orpheu.pokeplexus.ui.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.orpheu.pokeplexus.data.PokemonRepository
import com.orpheu.pokeplexus.data.model.Pokemon
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel(), PokemonContract.ViewModel {


    private val pokemonListLoadStates = MutableSharedFlow<CombinedLoadStates>()

    private val _error = MutableSharedFlow<PokemonContract.ViewState.Error>()
    override val error: SharedFlow<PokemonContract.ViewState.Error> = _error

    private val _navigation = MutableSharedFlow<PokemonContract.ViewInstructions>()
    override val navigation: SharedFlow<PokemonContract.ViewInstructions> = _navigation

    private val _isFilteringFavorite = MutableStateFlow(false)
    override val isFilteringFavorite: Flow<Boolean> = _isFilteringFavorite

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

    override val pokemons: Flow<PagingData<Pokemon>> =
        _isFilteringFavorite.flatMapLatest { isFilteringFavorite ->
            if (isFilteringFavorite){ favoritePokemon.map { PagingData.from(it) } }
            else pagedPokemon
        }


    override val pokemonListState: Flow<PokemonContract.PokemonListState> =
        pokemonListLoadStates.map {
            when (val loadState = it.refresh) {
                is LoadState.NotLoading -> PokemonContract.PokemonListState.Loaded
                is LoadState.Loading -> PokemonContract.PokemonListState.Loading
                //On a real application we would use content from string resources
                is LoadState.Error -> PokemonContract.PokemonListState.LoadingError(
                    loadState.error.localizedMessage ?: "something went wrong"
                )
            }
        }

    override fun toggleFavoritePokemonFilter() {
            _isFilteringFavorite.value = !_isFilteringFavorite.value

    }


    override fun pokemonClicked(pokemon: Pokemon) {
        viewModelScope.launch {
            _navigation.emit(PokemonContract.ViewInstructions.NavigateToPokemonDetails(pokemon))
        }
    }

    fun setPokemonListFlow(loadStates: Flow<CombinedLoadStates>) {
        viewModelScope.launch {
            pokemonListLoadStates.emitAll(loadStates)
        }
    }
}