package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearSmoothScroller
import com.tail_island.jetbus.adapter.BusStopAdapter
import com.tail_island.jetbus.adapter.IndexAdapter
import com.tail_island.jetbus.databinding.FragmentArrivalBusStopBinding
import com.tail_island.jetbus.view_model.ArrivalBusStopViewModel
import javax.inject.Inject

class ArrivalBusStopFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<ArrivalBusStopViewModel> { viewModelProviderFactory }

    private val args by navArgs<ArrivalBusStopFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        viewModel.departureBusStopName.value = args.departureBusStopName

        return FragmentArrivalBusStopBinding.inflate(inflater, container, false).apply {
            recyclerView.adapter = BusStopAdapter().apply {
                viewModel.arrivalBusStops.observe(viewLifecycleOwner, Observer {
                    submitList(it)
                })

                onBusStopClick = {
                    findNavController().navigate(ArrivalBusStopFragmentDirections.arrivalBusStopFragmentToBusApproachesFragment(viewModel.departureBusStopName.value!!, it.name))
                }
            }

            indexRecyclerView.adapter = IndexAdapter().apply {
                viewModel.arrivalBusStopIndexes.observe(viewLifecycleOwner, Observer {
                    submitList(it)
                })

                onIndexClick = {
                    recyclerView.layoutManager!!.startSmoothScroll(
                        AcceleratedSmoothScroller(requireContext()).apply {
                            targetPosition = getBusStopPosition(viewModel.arrivalBusStops.value!!, it)
                        }
                    )
                }
            }
        }.root
    }
}
