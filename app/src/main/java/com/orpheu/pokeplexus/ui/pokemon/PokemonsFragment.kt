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
import com.orpheu.pokeplexus.data.model.Pokemon
import com.orpheu.pokeplexus.extension.launchWhenStartedIn
import com.orpheu.pokeplexus.extension.toVisibility
import com.orpheu.pokeplexus.ui.base.NavDestinationFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonsFragment : NavDestinationFragment(R.id.pokemonsFragment) {


    private val pokemonsAdapter by lazy { PokemonAdapter(viewModel::pokemonClicked) }

    private val viewModel: PokemonViewModel by viewModel()

    //needs a backing field to allow to null the viewBinding instance when the fragment view is destroyed,
    // thus preventing memory leak with the view Instance
    private var _binding: PokemonsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animDuration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        exitTransition = MaterialElevationScale(false).apply {
            duration = animDuration
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = animDuration
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

        setupToolBar()
        setupPokemonList()


        viewModel.error
            .onEach { Snackbar.make(view, it.message, Snackbar.LENGTH_LONG).show() }
            .launchWhenStartedIn(viewLifecycleOwner.lifecycleScope)

        viewModel.navigation
            .onEach {
                when (it) {
                    is PokemonsContract.ViewInstructions.NavigateToPokemonDetails -> navigateToPokemonDetails(
                        it.pokemon
                    )
                }
            }
            .launchWhenStartedIn(viewLifecycleOwner.lifecycleScope)



        viewModel.viewState.onEach { (isFilteringFavorite, pokemons, pokemonListState) ->
            binding.btnFilterFavorite.setImageResource(
                if (isFilteringFavorite)
                    R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite
            )

            pokemonsAdapter.submitData(viewLifecycleOwner.lifecycle, pokemons)

            TransitionManager.beginDelayedTransition(
                binding.root,
                AutoTransition().excludeTarget(binding.rvPokemons, true)
            )

            binding.clErrorState.toVisibility =
                pokemonListState is PokemonsContract.PokemonListState.LoadingError
            binding.tvErrorMessage.text =
                (pokemonListState as? PokemonsContract.PokemonListState.LoadingError)?.message

            binding.ivLoading.toVisibility =
                pokemonListState is PokemonsContract.PokemonListState.Loading

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupToolBar() {
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

        viewModel.setPokemonListFlow(pokemonsAdapter.loadStateFlow)

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