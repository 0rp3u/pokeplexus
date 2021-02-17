package com.orpheu.pokeplexus.ui.pokemon

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale
import com.orpheu.pokeplexus.R
import com.orpheu.pokeplexus.databinding.PokemonsFragmentBinding
import com.orpheu.pokeplexus.domain.model.Pokemon
import com.orpheu.pokeplexus.extension.launchWhenStartedIn
import com.orpheu.pokeplexus.extension.toVisibility
import com.orpheu.pokeplexus.ui.base.NavDestinationFragment
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonsFragment : NavDestinationFragment(R.id.pokemonsFragment) {


    private val pokemonsAdapter by lazy { PokemonAdapter(viewModel::pokemonClicked) }

    private val viewModel: PokemonViewModel by viewModel()

    private var _binding: PokemonsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialElevationScale(false).apply {
            duration = 200
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 200
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PokemonsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewModel.error
            .onEach { Snackbar.make(view, it.message, Snackbar.LENGTH_LONG).show() }
            .launchWhenStartedIn(viewLifecycleOwner.lifecycleScope)

        viewModel.navigation
            .onEach {
                when (it) {
                    is PokemonContract.ViewInstructions.NavigateToPokemonDetails -> navigateToPokemonDetails(
                        it.pokemon
                    )
                }
            }
            .launchWhenStartedIn(viewLifecycleOwner.lifecycleScope)


        setupToolBar()
        setupPokemonList()

    }

    private fun setupToolBar() {
        viewModel.isFilteringFavorite
            .onEach { isFilteringFavorite ->

                binding.btnFilterFavorite.setImageResource(
                    if (isFilteringFavorite)
                        R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite
                )
            }
            .launchWhenStartedIn(viewLifecycleOwner.lifecycleScope)

        binding.btnFilterFavorite.setOnClickListener {
            viewModel.toggleFavoritePokemonFilter()
        }
    }

    private fun setupPokemonList() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPokemons.layoutManager = layoutManager
        binding.rvPokemons.adapter = pokemonsAdapter
            .withLoadStateFooter(LoaderStateAdapter(pokemonsAdapter::retry))


        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =
                if (position == pokemonsAdapter.itemCount) 2 else 1
        }

        viewModel.pokemons
            .onEach { pokemonsAdapter.submitData(viewLifecycleOwner.lifecycle, it) }
            .launchWhenStartedIn(viewLifecycleOwner.lifecycleScope)

        viewModel.setPokemonListFlow(pokemonsAdapter.loadStateFlow)


        viewModel.pokemonListState
            .onEach { state ->
                TransitionManager.beginDelayedTransition(
                    binding.root,
                    AutoTransition().excludeTarget(binding.rvPokemons, true)
                )

                binding.clErrorState.toVisibility =
                    state is PokemonContract.PokemonListState.LoadingError
                binding.tvErrorMessage.text =
                    (state as? PokemonContract.PokemonListState.LoadingError)?.message

                binding.ivLoading.toVisibility = state is PokemonContract.PokemonListState.Loading
            }
            .launchWhenStartedIn(viewLifecycleOwner.lifecycleScope)

        binding.btnRetry.setOnClickListener {
            pokemonsAdapter.refresh()
        }


    }


    private fun navigateToPokemonDetails(pokemon: Pokemon) {
        val extras = try {
            val containerTransitionName = "container${pokemon.name}"

            val container = binding.rvPokemons.findViewWithTag<View>(containerTransitionName)
            ViewCompat.setTransitionName(container, containerTransitionName)

            FragmentNavigatorExtras(
                container to containerTransitionName,
            )
        } catch (e: Exception) {
            Log.e("navigateToPokemonDetails", "${e.message}")
            FragmentNavigatorExtras()
        }

        navController.navigate(
            PokemonsFragmentDirections.actionFirstFragmentToSecondFragment(pokemon),
            extras
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        TransitionManager.endTransitions(binding.root)
        _binding = null
    }


}