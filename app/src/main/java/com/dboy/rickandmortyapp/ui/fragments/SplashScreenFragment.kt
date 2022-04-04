package com.dboy.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dboy.rickandmortyapp.MainActivity
import com.dboy.rickandmortyapp.R
import com.dboy.rickandmortyapp.databinding.FragmentSplashScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment: Fragment() {
    var binding: FragmentSplashScreenBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val supportActionBar = (activity as MainActivity).supportActionBar
        val bottomNavigationView = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        lifecycleScope.launch {
            supportActionBar?.hide()
            bottomNavigationView.isVisible = false
            delay(2000)
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToCharactersListFragment())
            supportActionBar?.show()
            bottomNavigationView.isVisible = true
        }
    }
}