package com.dboy.rickandmortyapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dboy.rickandmortyapp.databinding.CharacterLoadStateFooterBinding

class CharacterLoadStateAdapter(private val retry: () -> Unit): LoadStateAdapter<CharacterLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(val binding: CharacterLoadStateFooterBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.btnRetry.setOnClickListener {
                retry()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = CharacterLoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.binding.apply {
            pgFooter.isVisible = loadState is LoadState.Loading
            tvNotLoaded.isVisible = loadState is LoadState.Error
            btnRetry.isVisible = loadState is LoadState.Error
        }
    }
}