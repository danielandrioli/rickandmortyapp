package com.dboy.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dboy.rickandmortyapp.databinding.FragmentCharactersListBinding
import com.dboy.rickandmortyapp.ui.RmViewModel
import com.dboy.rickandmortyapp.ui.adapters.CharacterAdapterPagination
import com.dboy.rickandmortyapp.ui.adapters.PagingLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersListFragment : Fragment() {
    private var binding: FragmentCharactersListBinding? = null
    private lateinit var characterAdapter: CharacterAdapterPagination
    private val rmViewModel: RmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerAndAdapter()
        setSearchInput()
        rmViewModel.charactersWithPagination.observe(viewLifecycleOwner) {
            characterAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        characterAdapter.setOnItemClickListener {
            val action =
                CharactersListFragmentDirections.actionCharactersListFragmentToCharacterFragment(it.id)
            findNavController().navigate(action)
        }

        binding?.apply {
            btnReset.setOnClickListener {
                rmViewModel.clearFilter()
                if(tieSearch.text?.isNotEmpty() == true){ // == true just to avoid creating a new if to null-check
                    tieSearch.text?.clear() //this is enough to clear the name query and make a new search with all characters
                } else {
                    rmViewModel.nameQuery.value = "" //this will automatically generate a new request
                }
            }

            btnFilter.setOnClickListener {
                val action =
                    CharactersListFragmentDirections.actionCharactersListFragmentToFilterFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun setSearchInput() {
        var job: Job? = null
        //I'm using this method doOnTextChanged because I can compare the current text to the query in the viewModel. I need to do this
        //because these text listeners are called whenever I return to this fragment, so they do unnecessary network calls.
        binding?.tieSearch?.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != rmViewModel.nameQuery.value) {
                job?.cancel()
                job = MainScope().launch {
                    if (text != null) {
                        if (text.isNotEmpty())  //I want to delay just when text is being written
                            delay(1000L)
                    }
                    text?.let {
                        rmViewModel.makeCharacterQuery(it.toString())
                    }
                }
            }
        }
    }

    private fun setRecyclerAndAdapter() {
        characterAdapter = CharacterAdapterPagination()
        binding?.rvCharacterList?.apply {
            adapter = characterAdapter.withLoadStateFooter(PagingLoadStateAdapter {
                characterAdapter.retry()
            })
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }

        characterAdapter.addLoadStateListener {
            binding?.apply {
                val loadState = it.source.refresh
                pgCharacters.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
                rvCharacterList.isVisible = loadState is LoadState.NotLoading
                tvError.isVisible = loadState is LoadState.Error
                tvError.text =
                    if (loadState is LoadState.Error && loadState.error.message == "404") "Could not find the character!"
                    else "Could not load data!"
            }
        }

        binding?.btnRetry?.setOnClickListener {
            characterAdapter.retry()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}