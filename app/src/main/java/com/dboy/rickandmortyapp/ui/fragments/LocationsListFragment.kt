package com.dboy.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.dboy.rickandmortyapp.MainActivity
import com.dboy.rickandmortyapp.R
import com.dboy.rickandmortyapp.databinding.FragmentLocationsListBinding

class LocationsListFragment: Fragment() {
    private var binding: FragmentLocationsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationsListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}