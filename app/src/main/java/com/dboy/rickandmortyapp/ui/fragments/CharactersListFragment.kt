package com.dboy.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dboy.rickandmortyapp.MainActivity
import com.dboy.rickandmortyapp.R
import com.dboy.rickandmortyapp.databinding.FragmentCharactersListBinding
import com.dboy.rickandmortyapp.ui.RmViewModel
import com.dboy.rickandmortyapp.ui.adapters.CharacterAdapterPagination
import com.dboy.rickandmortyapp.ui.adapters.CharacterLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersListFragment : Fragment(), SearchView.OnQueryTextListener {
    private var binding: FragmentCharactersListBinding? = null
    private lateinit var characterAdapter: CharacterAdapterPagination
    private val rmViewModel: RmViewModel by activityViewModels()
    private var job: Job? = null
    private var query = ""

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
        setHasOptionsMenu(true) //I need to set this to true in order to the search menu appear
        rmViewModel.charactersWithPagination.observe(viewLifecycleOwner) {
            characterAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        characterAdapter.setOnItemClickListener {
            val action = CharactersListFragmentDirections.actionCharactersListFragmentToCharacterFragment(it.id)
            findNavController().navigate(action)
        }
        setupToolBar()
    }


    /* I'm setting the action bar in this fragment so I can implement the search function just in this fragment and not in others.*/
    private fun setupToolBar(){
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.charactersListFragment,
                R.id.locationsListFragment
            )
        )
        NavigationUI.setupActionBarWithNavController(
            activity as MainActivity,
            findNavController(),
            appBarConfiguration
        )
    }

    private fun setRecyclerAndAdapter() {
        characterAdapter = CharacterAdapterPagination()
        binding?.rvCharacterList?.apply {
            adapter = characterAdapter.withLoadStateFooter(CharacterLoadStateAdapter {
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
                tvError.isVisible = loadState is LoadState.Error
                rvCharacterList.isVisible = loadState is LoadState.NotLoading
            }
        }

        binding?.btnRetry?.setOnClickListener {
            characterAdapter.retry()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_function, menu)

        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        rmViewModel.query.value?.let {
            if (rmViewModel.query.value != ""){
                search.expandActionView()
                searchView.setQuery(it, false)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean { //this method is called when the search buttom is pressed and the query is not empty
        query?.let {
            this.query = it
            rmViewModel.makeCharacterQuery(it)
        }
        Log.i("CharacterListFrag", "onQueryTextSubmit ${query.toString()}")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean { //this method is called when a new character is typed in the query. It's also called when you first open the searchview.
        newText?.let {
            if (it.isEmpty()){
                Log.i("CharacterListFrag", "onQueryTextChange $it")

            }
//            Log.i("CharacterListFrag", "onQueryTextChange: $newText")
//            job?.cancel()
//            job = lifecycleScope.launch {
//                delay(1000)
//                rmViewModel.makeCharacterQuery(it)
//            }
        }
        return true
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}