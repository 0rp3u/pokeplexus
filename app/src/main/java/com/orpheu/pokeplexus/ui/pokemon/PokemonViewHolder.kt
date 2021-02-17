package com.orpheu.pokeplexus.ui.pokemon

import android.graphics.drawable.Animatable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.orpheu.pokeplexus.R
import com.orpheu.pokeplexus.databinding.PokemonItemBinding
import com.orpheu.pokeplexus.data.model.Pokemon

class PokemonViewHolder(val binding: PokemonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val loadingDrawable = ContextCompat.getDrawable(
            binding.root.context,
            R.drawable.loading_anim
        )

        fun bind(pokemon: Pokemon?, onItemClicked: (Pokemon) -> Unit) {

            if (!(loadingDrawable as Animatable).isRunning)
                loadingDrawable.start()

            pokemon?.let {
                addtransitionInfo(pokemon)
                binding.tvPokemonName.text = pokemon.name
                binding.root.setOnClickListener { onItemClicked(pokemon) }

                Glide
                    .with(binding.root)
                    .load(pokemon.imageUrl)
                    .centerCrop()
                    .error(R.drawable.ic_pokeball)
                    .placeholder(loadingDrawable)
                    .listener(
                        GlidePalette.with(pokemon.imageUrl)
                            .use(BitmapPalette.Profile.VIBRANT_LIGHT)
                            .intoCallBack { palette ->
                                binding.clParent.background = customPokemonBackground(
                                    pokemon.favorite,
                                palette?.vibrantSwatch
                                    ?: palette?.dominantSwatch,
                                    palette?.dominantSwatch
                                )

                            }.crossfade(true, 1000)
                    )
                    .into(binding.ivPokemon)
            }
        }

        private fun customPokemonBackground(
            isFavorite: Boolean,
            vibrant: Palette.Swatch?,
            dominant: Palette.Swatch?
        ) = if (vibrant != null && dominant != null) {
            GradientDrawable(
                GradientDrawable.Orientation.BL_TR,
                intArrayOf(vibrant.rgb, dominant.rgb)
            ).apply {
                cornerRadius = binding.root.resources.getDimensionPixelSize(
                    R.dimen.pokemon_cards_corners
                ).toFloat()
                setStroke(
                    6,
                    ContextCompat.getColor(
                        binding.root.context,
                        if (isFavorite)
                            R.color.primary
                        else
                            android.R.color.transparent
                    )
                )
            }
        } else ContextCompat.getDrawable(
            binding.root.context,
            if (isFavorite)
                R.drawable.favorite_pokemon_wrapper_bg
            else
                R.drawable.pokemon_bg
        )
        

        private fun addtransitionInfo(pokemon: Pokemon) {
            val containerTransitionName = "container${pokemon.name}"
            binding.root.tag = containerTransitionName
            ViewCompat.setTransitionName(binding.root, containerTransitionName)
        }

        companion object {
            fun create(parent: ViewGroup): PokemonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PokemonItemBinding.inflate(layoutInflater, parent, false)
                return PokemonViewHolder(binding)
            }
        }
    }
