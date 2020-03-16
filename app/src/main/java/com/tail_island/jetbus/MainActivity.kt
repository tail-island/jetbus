package com.tail_island.jetbus

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tail_island.jetbus.databinding.ActivityMainBinding
import com.tail_island.jetbus.view_model.MainViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity: AppCompatActivity() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<MainViewModel> { viewModelProviderFactory }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).component.inject(this)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.clearDatabase -> run {
                        lifecycleScope.launch {
                            viewModel.clearDatabase()
                            findNavController(R.id.navHostFragment).navigate(R.id.splashFragment)
                        }

                        true
                    }
                    else -> false

                }.also {
                    if (!it) {
                        return@also
                    }

                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }

        }.also {
            findNavController(R.id.navHostFragment).apply {
                NavigationUI.setupWithNavController(it.toolbar, this, AppBarConfiguration(setOf(R.id.bookmarksFragment), it.drawerLayout))

                addOnDestinationChangedListener { _, destination, _ ->
                    it.appBarLayout.visibility = if (destination.id == R.id.splashFragment) View.GONE else View.VISIBLE
                }
            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }

        if (findNavController(R.id.navHostFragment).currentDestination?.id == R.id.bookmarksFragment) {
            finish()
            return
        }

        super.onBackPressed()
    }
}
