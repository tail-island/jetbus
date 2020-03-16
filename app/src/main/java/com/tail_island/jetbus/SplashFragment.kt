package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.databinding.FragmentSplashBinding
import com.tail_island.jetbus.view_model.SplashViewModel
import javax.inject.Inject

class SplashFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<SplashViewModel> { viewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        viewModel.isSyncDatabaseFinished.observe(viewLifecycleOwner, Observer {
            if (!it) {
                requireActivity().finish()
                return@Observer
            }

            findNavController().navigate(SplashFragmentDirections.splashFragmentToBookmarksFragment())
        })

        return FragmentSplashBinding.inflate(inflater, container, false).root
    }
}
