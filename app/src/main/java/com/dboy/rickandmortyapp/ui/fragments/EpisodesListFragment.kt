package com.dboy.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dboy.rickandmortyapp.databinding.FragmentEpisodesListBinding
import com.dboy.rickandmortyapp.ui.RmViewModel
import com.dboy.rickandmortyapp.ui.adapters.PagingLoadStateAdapter
import com.dboy.rickandmortyapp.ui.adapters.EpisodesAdapterPagination

class EpisodesListFragment: Fragment() {
    private var binding: FragmentEpisodesListBinding? = null
    private lateinit var episodesAdapter: EpisodesAdapterPagination
    private val rmViewModel: RmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodesListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAndAdapter()
        rmViewModel.episodesWithPagination.observe(viewLifecycleOwner){
            episodesAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun setRecyclerViewAndAdapter(){
        episodesAdapter = EpisodesAdapterPagination()
        binding?.rvEpisodes?.apply {
            adapter = episodesAdapter.withLoadStateFooter(PagingLoadStateAdapter{
                episodesAdapter.retry()
            })
            layoutManager = LinearLayoutManager(requireContext())
        }

        episodesAdapter.addLoadStateListener {
            binding?.apply {
                val loadState = it.source.refresh
                pgEpisodes.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
                rvEpisodes.isVisible = loadState is LoadState.NotLoading
                tvError.isVisible = loadState is LoadState.Error
            }
        }

        binding?.btnRetry?.setOnClickListener {
            episodesAdapter.retry()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}