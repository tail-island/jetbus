package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.tail_island.jetbus.adapter.BusApproachAdapter
import com.tail_island.jetbus.databinding.FragmentBusApproachesBinding
import com.tail_island.jetbus.view_model.BusApproachesViewModel
import javax.inject.Inject

class BusApproachesFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<BusApproachesViewModel> { viewModelProviderFactory }

    private val args by navArgs<BusApproachesFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        viewModel.departureBusStopName.value = args.departureBusStopName
        viewModel.arrivalBusStopName.value   = args.arrivalBusStopName

        return FragmentBusApproachesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel      = this@BusApproachesFragment.viewModel

            bookmarkImageView.setOnClickListener {
                this@BusApproachesFragment.viewModel.toggleBookmark()
            }

            recyclerView.adapter = BusApproachAdapter().apply {
                this@BusApproachesFragment.viewModel.busApproaches.observe(viewLifecycleOwner, Observer {
                    noBusApproachesTextView.visibility = if (it.isEmpty()) { View.VISIBLE } else { View.GONE }

                    submitList(it)
                })
            }
        }.root
    }
}
