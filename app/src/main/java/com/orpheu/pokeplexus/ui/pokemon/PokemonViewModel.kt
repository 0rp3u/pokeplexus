package com.orpheu.pokeplexus.ui.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.orpheu.pokeplexus.domain.PokemonRepository
import com.orpheu.pokeplexus.domain.model.Pokemon
import com.orpheu.pokeplexus.network.model.mappers.mapTopPokemon
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel(), PokemonContract.ViewModel {


    private val PokemonListLoadStates = MutableSharedFlow<CombinedLoadStates>()

    private val _error = MutableSharedFlow<PokemonContract.ViewState.Error>()
    override val error: SharedFlow<PokemonContract.ViewState.Error> = _error

    private val _navigation = MutableSharedFlow<PokemonContract.ViewInstructions>()
    override val navigation: SharedFlow<PokemonContract.ViewInstructions> = _navigation

    override val pokemons: Flow<PagingData<Pokemon>> = pokemonRepository.getPokemonsPaged()
        .cachedIn(viewModelScope)
        .combine(
        pokemonRepository.getPFavoritePokemons()
    ) { pokemonList, favoritePokemon ->
        pokemonList.map { pokemonCollectionItem ->
            favoritePokemon.firstOrNull { it.name == pokemonCollectionItem.name } ?: pokemonCollectionItem
        }
    }

    override val pokemonListState: Flow<PokemonContract.PokemonListState> = PokemonListLoadStates.map {
        when(val loadState = it.refresh) {
            is LoadState.NotLoading -> PokemonContract.PokemonListState.Loaded
            is LoadState.Loading -> PokemonContract.PokemonListState.Loading
            //On a real application we would use content from string resources
            is LoadState.Error -> PokemonContract.PokemonListState.LoadingError(loadState.error.localizedMessage ?: "something went wrong" )
        }
    }


    override fun pokemonClicked(pokemon: Pokemon) {
        viewModelScope.launch {
            _navigation.emit(PokemonContract.ViewInstructions.NavigateToPokemonDetails(pokemon))
        }
    }

    fun setPokemonListFlow(loadStates: Flow<CombinedLoadStates>){
        viewModelScope.launch {
            PokemonListLoadStates.emitAll(loadStates)
        }
    }
}