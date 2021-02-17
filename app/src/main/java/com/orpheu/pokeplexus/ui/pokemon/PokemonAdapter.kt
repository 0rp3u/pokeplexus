package com.orpheu.pokeplexus.ui.pokemon

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.orpheu.pokeplexus.data.model.Pokemon


class PokemonAdapter(private val onPokemonClicked: (Pokemon) -> Unit) :
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
