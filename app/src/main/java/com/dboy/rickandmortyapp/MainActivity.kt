package com.dboy.rickandmortyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dboy.rickandmortyapp.databinding.ActivityMainBinding
import com.dboy.rickandmortyapp.ui.RmViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var binding: ActivityMainBinding? = null
    private var query = ""
    private var jobQuery: Job? = null
    private val rmViewModel: RmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.charactersListFragment,
                R.id.locationsListFragment
            )
        )  //the top level fragments
//        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavigationView.setupWithNavController(navController) //this is necessary in order to navigate
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.search_function, menu)
//        val search = menu?.findItem(R.id.action_search)
//        val searchView = search?.actionView as SearchView
//        searchView.isSubmitButtonEnabled = true
//        searchView.setOnQueryTextListener(this)
//        val query = rmViewModel.query.value
//        if (query != null && query.isNotEmpty()) searchView.setQuery(query, false)
//
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onQueryTextSubmit(p0: String?): Boolean {  //this method is called when the search buttom is pressed
//        jobQuery?.cancel()
//        jobQuery = MainScope().launch {
////            delay(1000L)
//            p0?.let {
//                rmViewModel.makeCharacterQuery(it)
//            }
//        }
//        return true
//    }
//
//    override fun onQueryTextChange(p0: String?): Boolean { //this method is called when a new character is typed in the query
//
//        return true
//    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}