package com.orpheu.pokeplexus

import MainCoroutineRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.orpheu.pokeplexus.database.dao.FavoritePokemonDao
import com.orpheu.pokeplexus.data.PokemonRepository
import com.orpheu.pokeplexus.data.model.Pokemon
import com.orpheu.pokeplexus.network.PokeService
import com.orpheu.pokeplexus.ui.pokemon.PokemonContract
import com.orpheu.pokeplexus.ui.pokemon.PokemonViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class PokemonViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var pokeService: PokeService
    private lateinit var favoritePokemonDao: FavoritePokemonDao
    private lateinit var pokemonRepository: PokemonRepository
    private lateinit var pokemonViewModel: PokemonViewModel

    @Before
    fun `set up`() {
        pokeService = mockk(relaxed = true)
        favoritePokemonDao = mockk(relaxed = true)

        pokemonRepository = PokemonRepository(pokeService, favoritePokemonDao)
        pokemonViewModel = PokemonViewModel(pokemonRepository)
    }

    @Test
    fun `pokemon navigation ok`() = coroutineRule.runBlockingTest {

        val testnavigationFlow = pokemonViewModel.navigation.shareIn(coroutineRule, SharingStarted.Eagerly, replay = 1)
        val testPokemon = mockk<Pokemon>(relaxed = true)
        pokemonViewModel.pokemonClicked(testPokemon)

        with(testnavigationFlow.replayCache.first()) {
            assertTrue(this is PokemonContract.ViewInstructions.NavigateToPokemonDetails)
            assertTrue((this as PokemonContract.ViewInstructions.NavigateToPokemonDetails).pokemon == testPokemon)
        }

    }
}