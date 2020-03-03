package com.tail_island.jetbus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.databinding.FragmentSplashBinding

class SplashFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentSplashBinding.inflate(inflater, container, false).apply {
            bookmarksButton.setOnClickListener {
                findNavController().navigate(SplashFragmentDirections.splashFragmentToBookmarksFragment())
            }
        }.root
    }
}
