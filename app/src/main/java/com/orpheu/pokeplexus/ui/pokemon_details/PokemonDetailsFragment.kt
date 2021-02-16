package com.orpheu.pokeplexus.ui.pokemon_details

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.orpheu.pokeplexus.R
import com.orpheu.pokeplexus.databinding.PokemonsDetailsFragmentBinding
import com.orpheu.pokeplexus.domain.model.BaseStats
import com.orpheu.pokeplexus.domain.model.Type
import com.orpheu.pokeplexus.extension.changeStatusBarColor
import com.orpheu.pokeplexus.extension.launchWhenStartedIn
import com.orpheu.pokeplexus.extension.toVisibility
import com.orpheu.pokeplexus.ui.base.NavDestinationFragment
import com.orpheu.pokeplexus.ui.extension.getColorResource
import com.orpheu.pokeplexus.ui.extension.getImageResource
import com.orpheu.pokeplexus.ui.extension.getNameStringResource
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PokemonDetailsFragment : NavDestinationFragment(R.id.pokemonDetailsFragment) {

    private val args: PokemonDetailsFragmentArgs by navArgs()

    private var _binding: PokemonsDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonDetailsViewModel by viewModel { parametersOf(args.pokemon.id) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PokemonsDetailsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        TransitionManager.endTransitions(binding.root)
        _binding = null

        activity?.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = resources.getColor(R.color.primary_contrast, requireActivity().theme)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareTransition()
        bingPokemonBaseInfo(
            args.pokemon.name,
            args.pokemon.id,
            args.pokemon.imageUrl,
            args.pokemon.favorite
        )


        var firstEvent = true
        viewModel.pokemon
            .debounce { if (firstEvent) 500 else 0 } //so it does nto interfere with shared transition
            .onEach { pokemon ->
                firstEvent = false
                TransitionManager.beginDelayedTransition(binding.root)

                bingPokemonBaseInfo(pokemon.name, pokemon.id, pokemon.imageUrl, pokemon.favorite)



                binding.fabFavorite.setOnClickListener {
                    if (pokemon.favorite)
                        viewModel.removeFavoritePokemonClicked()
                    else
                        viewModel.addFavoritePokemonClicked()
                }

                binding.tvPokemonWight.text =
                    getString(R.string.weight_number, pokemon.weight.div(10f).toString())
                binding.tvPokemonHieght.text =
                    getString(R.string.height_number, pokemon.height.div(10f).toString())

                bindPokemonTypes(pokemon.types)

                bindBaseStats(pokemon.baseStats)

            }.launchWhenStartedIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isPokemonDetailsLoading
            .onEach { isLoading ->
                if (isLoading) binding.clErrorState.visibility = View.GONE
                binding.ivLoadingPokemon.toVisibility = isLoading
            }
            .launchWhenStartedIn(viewLifecycleOwner.lifecycleScope)


        viewModel.error
            .onEach {
                when (it) {
                    is PokemonDetailsContract.ViewState.Error.PokemonDetailsError -> {
                        binding.clErrorState.visibility = View.VISIBLE
                        binding.tvErrorMessage.text = it.message
                    }
                    is PokemonDetailsContract.ViewState.Error.RequestError ->
                        Snackbar.make(view, it.message, Snackbar.LENGTH_LONG).show()
                }


            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.navigation
            .onEach {
                when (it) {
                    PokemonDetailsContract.ViewInstructions.NavigateBack -> navController.navigateUp()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)


        viewModel.isAddOrRemoveFavoriteLoading
            .onEach { isLoading ->
                binding.ivLoadingFavorite.toVisibility = isLoading
                binding.fabFavorite.toVisibility = !isLoading
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.ablToolbar.addOnOffsetChangedListener(
            OnOffsetChangedListener { appBarLayout, verticalOffset ->
                binding.tvPokemonNameToolbar.alpha =
                    -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            })

        binding.btnBack.setOnClickListener {
            viewModel.backClicked()
        }
        binding.btnRetry.setOnClickListener {
            viewModel.retryFetchPokemonDetails()
        }

    }

    private fun prepareTransition() {
        binding.ablToolbar.transitionName = "container${args.pokemon.name}"
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            scrimColor = Color.TRANSPARENT
            duration = 400
        }
    }

    private fun bingPokemonBaseInfo(
        name: String,
        number: Int,
        imageUrl: String,
        isFavorite: Boolean
    ) {
        binding.tvPokemonName.text = name
        binding.tvPokemonNameToolbar.text = name
        binding.tvPokemonNumber.text = getString(R.string.pokemon_number, number)

        binding.fabFavorite.setImageResource(
            if (isFavorite)
                R.drawable.ic_baseline_remove_circle_outline_24
            else
                R.drawable.ic_baseline_add_circle_outline_24
        )


        binding.fabFavorite.setOnClickListener {
            if (isFavorite)
                viewModel.removeFavoritePokemonClicked()
            else
                viewModel.addFavoritePokemonClicked()
        }

        Glide
            .with(binding.root)
            .load(imageUrl)
            .centerCrop()
            .error(R.drawable.ic_pokeball)
            .listener(
                GlidePalette.with(imageUrl)
                    .use(BitmapPalette.Profile.VIBRANT_LIGHT)
                    .intoCallBack { palette ->

                        val vibrant = palette?.vibrantSwatch ?: palette?.dominantSwatch

                        val dominant = palette?.dominantSwatch
                        binding.clTop.background =
                            customPokemonBackground(vibrant, dominant)

                        val topcolor = dominant?.rgb ?: ContextCompat.getColor(
                            binding.root.context,
                            R.color.teal_700
                        )
                        activity?.window?.changeStatusBarColor(topcolor)
                        binding.cablToolbar.setContentScrimColor(topcolor)
                        binding.cablToolbar.setStatusBarScrimColor(topcolor)

                    }.crossfade(true, 1000)
            )
            .into(binding.ivPokemon)
    }

    private fun bindPokemonTypes(types: List<Type>) {

        binding.flFirstType.toVisibility = types.isNotEmpty()
        types.getOrNull(0)?.let { type ->
            binding.ivFirstTypeIcon.setImageResource(type.getImageResource())
            binding.ivFirstTypeIcon.background.setTint(
                ContextCompat.getColor(
                    requireContext(),
                    type.getColorResource()
                )
            )
            binding.tvFirstTypeName.setText(type.getNameStringResource())
        }

        binding.flSecondType.toVisibility = types.size == 2
        types.getOrNull(1)?.let { type ->
            binding.ivSecondTypeIcon.setImageResource(type.getImageResource())
            binding.ivSecondTypeIcon.background.setTint(
                ContextCompat.getColor(
                    requireContext(),
                    type.getColorResource()
                )
            )
            binding.tvSecondTypeName.setText(type.getNameStringResource())
        }
    }

    private fun bindBaseStats(baseStats: BaseStats) {

        ObjectAnimator.ofInt(binding.pbBaseHp, "progress", baseStats.hp)
            .apply { interpolator = DecelerateInterpolator() }
            .apply { startDelay = 500 }
            .setDuration(800)
            .start()

        binding.tvHpValue.text = getString(R.string.stat_number, baseStats.hp)

        ObjectAnimator.ofInt(binding.pbBaseAttack, "progress", baseStats.attack)
            .apply { interpolator = DecelerateInterpolator() }
            .apply { startDelay = 500 }
            .setDuration(800)
            .start()

        binding.tvAttackValue.text = getString(R.string.stat_number, baseStats.attack)

        ObjectAnimator.ofInt(
            binding.pbBaseSpecialAttack,
            "progress",
            baseStats.specialAttack
        )
            .apply { interpolator = DecelerateInterpolator() }
            .apply { startDelay = 500 }
            .setDuration(800)
            .start()

        binding.tvSpecialAttackValue.text = getString(R.string.stat_number, baseStats.specialAttack)

        ObjectAnimator.ofInt(binding.pbBaseDefense, "progress", baseStats.defense)
            .apply { interpolator = DecelerateInterpolator() }
            .apply { startDelay = 500 }
            .setDuration(800)
            .start()

        binding.tvDefenseValue.text = getString(R.string.stat_number, baseStats.defense)

        ObjectAnimator.ofInt(
            binding.pbBaseSpecialDefense,
            "progress",
            baseStats.specialDefense
        )
            .apply { interpolator = DecelerateInterpolator() }
            .apply { startDelay = 500 }
            .setDuration(800)
            .start()

        binding.tvSpecialDefenseValue.text =
            getString(R.string.stat_number, baseStats.specialDefense)

        ObjectAnimator.ofInt(binding.pbBaseSpeed, "progress", baseStats.speed)
            .apply { interpolator = DecelerateInterpolator() }
            .apply { startDelay = 500 }
            .setDuration(800)
            .start()

        binding.tvSpeedValue.text = getString(R.string.stat_number, baseStats.speed)

    }

    private fun customPokemonBackground(
        vibrant: Palette.Swatch?,
        dominant: Palette.Swatch?
    ) = if (vibrant != null && dominant != null) {

        GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP,
            intArrayOf(vibrant.rgb, dominant.rgb)
        ).apply {
            val cornerRadius =
                binding.root.resources.getDimensionPixelSize(R.dimen.pokemon_cards_corners)
                    .toFloat()
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(
                0f, 0f, 0f, 0f,
                cornerRadius, cornerRadius, cornerRadius, cornerRadius
            )
        }

    } else ContextCompat.getDrawable(binding.root.context, R.drawable.pokemon_details_bg)


}