package com.dboy.rickandmortyapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dboy.rickandmortyapp.R
import com.dboy.rickandmortyapp.api.response.character.Character
import com.dboy.rickandmortyapp.databinding.CharacterItemBinding

class CharacterAdapterPagination :
    PagingDataAdapter<Character, CharacterAdapterPagination.RmViewHolder>(
        differCallback
    ) {

    private var onItemClickListener: ((character: Character) -> Unit)? = null

    fun setOnItemClickListener(listener: (character: Character) -> Unit) {
        onItemClickListener = listener
    }

    inner class RmViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.apply {
                tvName.text = character.name
                ivCharacter.load(character.image)
                tvSituation.text = character.status
                tvDotSituation.apply {
                    setTextColor(
                        when (tvSituation.text) {
                            "Alive" -> {
                                ContextCompat.getColor(context, R.color.alive)
                            }
                            "Dead" -> {
                                ContextCompat.getColor(context, R.color.dead)
                            }
                            else -> ContextCompat.getColor(context, R.color.unknown)
                        }
                    )
                }

                tvType.text = character.species
                tvLastLocation.text = character.location.name
                tvOrigin.text = character.origin.name
            }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(character)
                }
            }
        }
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RmViewHolder {
        val binding =
            CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RmViewHolder, position: Int) {
        val character = getItem(position)
        character?.let { holder.bind(it) }
    }
}