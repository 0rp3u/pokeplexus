package com.orpheu.pokeplexus.ui.pokemon

import android.graphics.drawable.Animatable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.orpheu.pokeplexus.databinding.PokemonItemBinding
import com.orpheu.pokeplexus.databinding.PokemonsLoadstateItemBinding
import com.orpheu.pokeplexus.extension.toVisibility

class LoaderStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        return LoaderViewHolder.create(parent, retry)
    }

    class LoaderViewHolder(val binding: PokemonsLoadstateItemBinding, val retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {

        companion object {

            fun create(parent: ViewGroup, retry: () -> Unit): LoaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PokemonsLoadstateItemBinding.inflate(layoutInflater, parent, false)
                return LoaderViewHolder(binding, retry)
            }
        }


        fun bind(loadState: LoadState) {
            binding.errorGroup.toVisibility = loadState is LoadState.Error
            binding.tvErrorMessage.text = (loadState as? LoadState.Error)?.error?.localizedMessage
            binding.btnRetry.setOnClickListener { retry.invoke() }
            binding.ivLoading.toVisibility = loadState is LoadState.Loading

        }
    }
}