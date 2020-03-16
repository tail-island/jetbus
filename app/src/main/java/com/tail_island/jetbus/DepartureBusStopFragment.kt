package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.databinding.FragmentDepartureBusStopBinding

class DepartureBusStopFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentDepartureBusStopBinding.inflate(inflater, container, false).apply {
            arrivalBusStopButton.setOnClickListener {
                findNavController().navigate(DepartureBusStopFragmentDirections.departureBusStopFragmentToArrivalBusStopFragment("日本ユニシス本社前"))
            }
        }.root
    }
}
