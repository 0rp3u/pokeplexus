package com.orpheu.pokeplexus.ui.pokemon

import android.graphics.drawable.Animatable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.orpheu.pokeplexus.R
import com.orpheu.pokeplexus.databinding.PokemonItemBinding
import com.orpheu.pokeplexus.domain.model.Pokemon


class PokemonAdapter(val onPokemonClicked: (Pokemon) -> Unit) :
    PagingDataAdapter<Pokemon, PokemonViewHolder>(
        PokemonDiffer
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonViewHolder {
        return PokemonViewHolder.create(parent)
    }


    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onPokemonClicked)
    }


    companion object {
        private val PokemonDiffer = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem
        }
    }

}
