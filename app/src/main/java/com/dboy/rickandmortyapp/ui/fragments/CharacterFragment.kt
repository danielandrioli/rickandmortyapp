package com.dboy.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.dboy.rickandmortyapp.MainActivity
import com.dboy.rickandmortyapp.R
import com.dboy.rickandmortyapp.api.response.character.Character
import com.dboy.rickandmortyapp.databinding.FragmentCharacterBinding
import com.dboy.rickandmortyapp.ui.RmViewModel
import com.dboy.rickandmortyapp.util.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView

class CharacterFragment : Fragment() {
    private var binding: FragmentCharacterBinding? = null
    private val args: CharacterFragmentArgs by navArgs()
    private val characterId: Int by lazy { args.characterId }
    private val rmViewModel: RmViewModel by activityViewModels()
    private val bottomNavigationView: BottomNavigationView by lazy {
        (activity as MainActivity).findViewById<BottomNavigationView>(
            R.id.bottomNavigationView
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomNavigationView.isVisible = false
        rmViewModel.getSingleCharacter(characterId)
        rmViewModel.character.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> bindFieldsSuccess(it.data!!)
                is Resource.Error -> bindFieldsError(true)
                is Resource.Loading -> bindFieldsLoading()
            }
        }
        binding?.apply {
            btnRetry.setOnClickListener {
                rmViewModel.getSingleCharacter(characterId)
            }
        }
    }

    private fun bindFieldsLoading() {
        binding?.apply {
            pgCharacter.isVisible = true
            constraintCharacter.isVisible = false
            linearLayoutCharacter.isVisible = false
        }
    }

    private fun bindFieldsError(isError: Boolean) {
        binding?.apply {
            pgCharacter.isVisible = false
            linearLayoutCharacter.isVisible = isError
            constraintCharacter.isVisible = !isError
        }
    }

    private fun bindFieldsSuccess(character: Character) {
        binding?.apply {
            ivCharacter.load(character.image)
            tvName.text = character.name
            tvGender.text = character.gender
            tvStatus.text = character.status
            tvSpecies.text = character.species
            tvLastLocation.text = character.location.name
            pgCharacter.isVisible = false
            tvNumberOfEpisodes.text = character.episode.size.toString()
        }
        bindFieldsError(false)
        (activity as MainActivity).supportActionBar?.title = character.name
    }

    override fun onDestroyView() {
        rmViewModel.setValueCharacterNull() //It's necessary to set the value of this liveData to null, otherwise
        binding =
            null                  //the old data will flicker on the screen before the new data appears.
        bottomNavigationView.isVisible = true
        super.onDestroyView()
    }


}