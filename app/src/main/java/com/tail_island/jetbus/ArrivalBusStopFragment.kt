package com.tail_island.jetbus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.databinding.FragmentArrivalBusStopBinding

class ArrivalBusStopFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentArrivalBusStopBinding.inflate(inflater, container, false).apply {
            busApproachesButton.setOnClickListener {
                findNavController().navigate(ArrivalBusStopFragmentDirections.arrivalBusStopFragmentToBusApproachesFragment("日本ユニシス株式会社", "深川第八中学校前"))
            }
        }.root
    }
}
