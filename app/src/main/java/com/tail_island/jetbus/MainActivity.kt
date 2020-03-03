package com.tail_island.jetbus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tail_island.jetbus.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).also {
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
