package com.dboy.rickandmortyapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dboy.rickandmortyapp.api.response.episode.Episode
import com.dboy.rickandmortyapp.databinding.EpisodeItemBinding

class EpisodesAdapterPagination :
    PagingDataAdapter<Episode, EpisodesAdapterPagination.EpisodesViewHolder>(differCallback) {

    inner class EpisodesViewHolder(private val binding: EpisodeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episode) {
            binding.apply {
                tvEpisodeName.text = episode.name
                tvEpisodeNumber.text = episode.episode
                tvNumberCharacters.text = "${episode.characters.size} chars"
                tvAirDate.text = episode.air_date
            }
        }
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val binding = EpisodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val episode = getItem(position)
        episode?.let {
            holder.bind(it)
        }
    }
}