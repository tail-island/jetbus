package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.adapter.BusStopAdapter
import com.tail_island.jetbus.adapter.IndexAdapter
import com.tail_island.jetbus.databinding.FragmentDepartureBusStopBinding
import com.tail_island.jetbus.view_model.DepartureBusStopViewModel
import javax.inject.Inject

class DepartureBusStopFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<DepartureBusStopViewModel> { viewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        return FragmentDepartureBusStopBinding.inflate(inflater, container, false).apply {
            recyclerView.adapter = BusStopAdapter().apply {
                viewModel.departureBusStops.observe(viewLifecycleOwner, Observer {
                    submitList(it)
                })

                onBusStopClick = {
                    findNavController().navigate(DepartureBusStopFragmentDirections.departureBusStopFragmentToArrivalBusStopFragment(it.name))
                }
            }

            indexRecyclerView.adapter = IndexAdapter().apply {
                viewModel.departureBusStopIndexes.observe(viewLifecycleOwner, Observer {
                    submitList(it)
                })

                onIndexClick = {
                    recyclerView.layoutManager!!.startSmoothScroll(
                        AcceleratedSmoothScroller(requireContext()).apply {
                            targetPosition = getBusStopPosition(viewModel.departureBusStops.value!!, it)
                        }
                    )
                }
            }
        }.root
    }
}
